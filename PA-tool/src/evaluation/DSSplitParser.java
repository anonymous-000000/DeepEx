package evaluation;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import utils.GetSourcePaths;
import evaluation.json.ArgToCalls;
import evaluation.json.ObjectSlices;
import evaluation.json.Response;
import evaluation.json.Slices;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;
import parsing.SpecMethod;

import static evaluation.DSType.POS;
import static xml.MatchingType.*;

import xml.MatchingType;
import xml.XMLQuery;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;


public class DSSplitParser {

	private String inPath;
	private String projName;
	private PrintStream stLog;
	private PrintStream stLog2;
	private PrintStream stRQ2Log;
	private boolean testing = false;
	private GetSourcePaths GetRootPaths = new GetSourcePaths();
	private Map<String, SpecMethod> methods = new HashMap<>();
	private APICallSpec apiCallSpec;
	private DSType dsType;
	private List<String> badFiles = new LinkedList<>();
	@SuppressWarnings("rawtypes")
	private Map options = JavaCore.getOptions();
	private ASTParser parser = ASTParser.newParser(AST.JLS8); // Java Language Specification
	private static final int  N = 1938;
	//	private static final int  N = 10;
	private JsonReader jsonReader;
	private Gson gson = new Gson();
	private Response response;
	private String outputPath;
	Properties config;


	{
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);

		parser.setCompilerOptions(options);
		parser.setEnvironment(null, new String[]{}, new String[]{}, true);
		parser.setResolveBindings(false);
		parser.setBindingsRecovery(false);
	}

	public DSSplitParser(String name, String inPath) {
		this.inPath = inPath;
		this.projName = name;
	}

	public DSSplitParser(String name, String inPath, boolean testing, DSType dsType, boolean filterByTrainMethodNames, Properties config) throws FileNotFoundException {
		this(name, inPath);
		this.testing = testing;
		this.dsType = dsType;
		this.jsonReader = new JsonReader(new FileReader(config.getProperty("slice_path")));
		this.response = gson.fromJson(jsonReader, Response.class);
		this.config = config;
	}

	public void evalCorpus(String outPath, boolean recursive) throws Exception {
		//ArrayList<String> rootPaths = GetRootPaths.getRootPaths(inPath); // contains only one path that was passed to the constructor
		XMLQuery xmlQuery = new XMLQuery(config);

		this.outputPath = outPath;

		if (!Files.exists(Path.of(outPath))) {
			Files.createDirectory(Path.of(outPath));
		}

		if (testing)
			System.out.println("Start parsing");

		String[] sourcePaths = GetRootPaths.getSourcePaths(inPath, new String[]{".java"}, recursive);

		final HashMap<String, CompilationUnit> cus = new HashMap<>(); // cus stands for compilation unit suite
		final FileASTRequestor r = new FileASTRequestor() {
			@Override
			public void acceptAST(String sourceFilePath, CompilationUnit ast) {
				if (ast.getPackage() == null)
					return;
				cus.put(sourceFilePath, ast); // only care about java files within some packages.
			}
		};

		try {
			parser.createASTs(sourcePaths, null, new String[0], r, null);
		} catch (Throwable t) {
			t.printStackTrace(stLog);
			if (testing) {
				System.err.println(t.getMessage());
				t.printStackTrace();
				System.exit(-1);
			}
		}

		if (dsType == POS) {
			System.out.println("Eval RQ3");
			evalRQ3(cus, xmlQuery);
		}

	}

	private void evalRQ1(final HashMap<String, CompilationUnit> cus, XMLQuery xmlQuery) throws Exception{
		try {
			if (Files.exists(Path.of(outputPath + "/log.txt"))) {
				Files.delete(Path.of(outputPath + "/log.txt"));
				Files.createFile(Path.of(outputPath + "/log.txt"));
			}

			if (Files.exists(Path.of(outputPath + "/instances.txt"))) {
				Files.delete(Path.of(outputPath + "/instances.txt"));
				Files.createFile(Path.of(outputPath + "/instances.txt"));
			}
			stLog = new PrintStream(new FileOutputStream(outputPath + "/log.txt"));
			stLog2 = new PrintStream(new FileOutputStream(outputPath + "/instances.txt"));
		} catch (FileNotFoundException e) {
			if (testing)
				System.err.println(e.getMessage());
		}

		List<String> fileList = new ArrayList<>(cus.keySet());
		Collections.sort(fileList);

		Collections.sort(fileList, new Comparator<String>() {
			@Override
			public int compare(String s, String t1) {
				Path p1 = Path.of(s);
				String f1 = p1.getFileName().toString();

				Path p2 = Path.of(t1);
				String f2 = p2.getFileName().toString();

				int h = Integer.parseInt(f1.substring(4, f1.length() - 5));
				int k = Integer.parseInt(f2.substring(4, f2.length() - 5));
				return h - k;
			}
		});

		Path outPath = Path.of(String.format(config.getProperty("rq1_eval")+"/%s_pred.txt", dsType));

		Files.deleteIfExists(outPath);
		Files.createFile(outPath);

		Path RQ1_TP = Path.of(config.getProperty("rq2_eval")+"/RQ1_TP.txt");
		if (dsType == POS) {
			Files.deleteIfExists(RQ1_TP);
			Files.createFile(RQ1_TP);
		}

//		Map<String, Integer> decidingMethodNamesCounter = new HashMap<>();

		int noAPICall = 0;
		int nProcessed = 0;
		boolean error = false;
		for (int j = 0; j < fileList.size(); j++) {
			if (nProcessed == N) {
				break;
			}
			System.out.println(j);
			String sourceFilePath = fileList.get(j);
			CompilationUnit ast = cus.get(sourceFilePath);
//			stLog.println(sourceFilePath);

			if (ast.types().size() == 0) {
				throw new IOException("No method");
			}

			enum MatchStatus {INIT, NoApiCall, NoMatch, MatchFound}
			MatchStatus matchStat = MatchStatus.INIT;
			boolean processedFlag = false;
			types_loop:
			for (int  i = 0; i < ast.types().size(); i++) {
				assert(ast.types().size() == 1); // must be 1 otherwise the "nProcess" will be added multiple times
				// Either ClassDeclaration or InterfaceDeclaration.
				// In our case it is a ClassDeclaration.
				if (ast.types().get(i) instanceof TypeDeclaration) {
					TypeDeclaration td = (TypeDeclaration) ast.types().get(i);

					MethodDeclaration[] methodArr = td.getMethods();
					if (methodArr.length == 0) {
						badFiles.add(Path.of(sourceFilePath).getFileName().toString());
//						Files.delete(Path.of(sourceFilePath));
						error = true;
						break;
					}

					// By construction, we know there is only one element in the methodArr
					assert (methodArr.length == 1);

					MethodDeclaration method = methodArr[0];
					Block body = method.getBody();

					APICallVisitor callv = new APICallVisitor(ast);
					if (body != null) {
						body.accept(callv);
					} else {
						badFiles.add(Path.of(sourceFilePath).getFileName().toString());
						error = true;
						break;
					}

					if (processedFlag == false)
						nProcessed += 1;
					processedFlag = true;
					if (callv.apiCallSpecList.size() == 0) {
						matchStat = MatchStatus.NoApiCall;
						break types_loop;
					}

					matchStat = MatchStatus.NoMatch;

					for (APICallSpec apiCallSpec : callv.apiCallSpecList) {
						MatchingType matchType = xmlQuery.process(apiCallSpec);
						if (matchType == NO_MATCH) {
							continue;
						}

//						decidingMethodNamesCounter.merge(apiCallSpec.methodName, 1, Integer::sum);

						matchStat = MatchStatus.MatchFound;
						break types_loop;
					}
				}
			}

			// Log statistics before going to the next file
			// If the error is false, the source file is bad
			if (error == false) {
				stLog2.println(sourceFilePath);
				if (matchStat == MatchStatus.INIT) {
					throw new IOException(String.format("match equals to %d", matchStat));
				}

				if (matchStat == MatchStatus.MatchFound) {
					Files.writeString(outPath, "1 ", Charset.defaultCharset(), StandardOpenOption.APPEND);
				}

				if (matchStat == MatchStatus.MatchFound && dsType == POS) // only recording for correct cases in the positive dataset
					Files.writeString(RQ1_TP, sourceFilePath + "\n", Charset.defaultCharset(), StandardOpenOption.APPEND);

				if (matchStat == MatchStatus.NoMatch)
					Files.writeString(outPath, "0 ", Charset.defaultCharset(), StandardOpenOption.APPEND);

				if (matchStat == MatchStatus.NoApiCall) {
					noAPICall += 1;

					stLog.println("Files without API calls:");
					stLog.println(sourceFilePath);
					Files.writeString(outPath, "0 ", Charset.defaultCharset(), StandardOpenOption.APPEND);
				}
			}

			error = false;
		}

		stLog.println();
		stLog.println("=====================");
		stLog.println(String.format("The number of instances without any api calls is %d", noAPICall));
		stLog.println();
		stLog.println("=====================");
		stLog.println(String.format("nProcessed is %d", nProcessed));
		stLog.println();
		stLog.println("=====================");
		stLog.println("Bad Files: ");
		for (String f : badFiles) {
			stLog.println(f);
		}
	}

	private void evalRQ2(final HashMap<String, CompilationUnit> cus, XMLQuery xmlQuery) throws Exception{
		try {
			if (Files.exists(Path.of(outputPath + "/rq2.log"))) {
				Files.delete(Path.of(outputPath + "/rq2.log"));
				Files.createFile(Path.of(outputPath + "/rq2.log"));
			}
			stRQ2Log = new PrintStream(new FileOutputStream(outputPath + "/rq2.log"));
		} catch (FileNotFoundException e) {
			if (testing)
				System.err.println(e.getMessage());
		}

		// positive instances which the tool also predicted to be positive
		List<String> files = Files.readAllLines(
				Path.of(config.getProperty("RQ1_TP"))
		);

		Path stRQ2Result = Path.of(config.getProperty("stRQ2Result"));
		Files.deleteIfExists(stRQ2Result);
		Files.createFile(stRQ2Result);

		int totalIn = 0;
		int correctIn = 0;
		int totalOut = 0;
		int correctOut = 0;

		int n_iter = 1;
		for (String sourceFilePath : files) {
//			if (n_iter == 10) {
//				break;
//			}
			System.out.println(n_iter);
			n_iter++;
			Path fileNamePath = Path.of(sourceFilePath).getFileName();
			String fileName = fileNamePath.toString();
			String fileNameWithoutSuffix = fileName.substring(0, fileName.length()-5);
			Path meta = Path.of(String.format(config.getProperty("meta"), fileNameWithoutSuffix));

			Path metaCallPath = Path.of(String.format("../../dataset/pos_test_meta_api_call/%s_meta.txt",
					fileNameWithoutSuffix));
			Files.deleteIfExists(metaCallPath);
			Files.createFile(metaCallPath);

			Set<Integer> try_stmt_lines = new HashSet<>();
			CompilationUnit ast = cus.get(sourceFilePath);
			APICallVisitor callv = null;

			for (int  i = 0; i < ast.types().size(); i++) {
				assert(ast.types().size() == 1);
				// Either ClassDeclaration or InterfaceDeclaration.
				// In our case it is a ClassDeclaration.
				if (ast.types().get(i) instanceof TypeDeclaration) {
					TypeDeclaration td = (TypeDeclaration) ast.types().get(i);

					MethodDeclaration[] methodArr = td.getMethods();

					// By construction, we know there is only one element in the methodArr
					MethodDeclaration method = methodArr[0];
					Block body = method.getBody();

					callv = new APICallVisitor(ast);
					if (body != null) {
						body.accept(callv);
					}

					for (APICallSpec apiCallSpec : callv.apiCallSpecList) {
						MatchingType matchType = xmlQuery.process(apiCallSpec);
						if (matchType == MATCH) {
							Files.writeString(metaCallPath, String.format("%s\t%s\t%s\t%s\n", apiCallSpec.methodName, apiCallSpec.startLineNum, apiCallSpec.endLineNum, apiCallSpec.arity), Charset.defaultCharset(), StandardOpenOption.APPEND);

//							// query data slices produced by joern
							for (ObjectSlices objectSlice : response.getObjectSlices()) {
								String xFileName = objectSlice.getFileName();
								if (xFileName.equals(fileName)) {
									for (Slices slice : objectSlice.getSliceArr()) {
										int targetLineNumber = slice.getTargetObj().getLineNumber();
										for (ArgToCalls argToCall : slice.getArgToCalls()) {
											if (argToCall.getCallName().equals(apiCallSpec.methodName) &&
													(argToCall.getLineNumber() == apiCallSpec.startLineNum ||
															argToCall.getLineNumber() == apiCallSpec.endLineNum)) {
												if (targetLineNumber < apiCallSpec.startLineNum) {
													apiCallSpec.startLineNum = targetLineNumber;
													System.out.print("*");
												}
											}
										}
									}
								}
							}

							try_stmt_lines.add(apiCallSpec.startLineNum);
							try_stmt_lines.add(apiCallSpec.endLineNum);
						}
					}
				}
			}

			try {
				int minPred = Collections.min(try_stmt_lines);
				int maxPred = Collections.max(try_stmt_lines);
//			registerAPICallMeta(metaCallPath, callv);

				List<String> metaLines = Files.readAllLines(meta);
				String[] minMaxArr = metaLines.get(1).strip().split(" ");
				int minTrue = Integer.valueOf(minMaxArr[0]);
				int maxTrue = Integer.valueOf(minMaxArr[1]);

				totalIn += maxTrue - minTrue + 1;
				correctIn += getCorrectInLineNum(minPred, maxPred, minTrue, maxTrue);

				String[] stmtLineRange = metaLines.get(2).strip().split(" ");
				int minStmt = Integer.valueOf(stmtLineRange[0]);
				int maxStmt = Integer.valueOf(stmtLineRange[1]);
				totalOut += (maxStmt - minStmt + 1) - (maxTrue - minTrue + 1);
				correctOut += getCorrectOutLineNum(minPred, maxPred, minTrue, maxTrue, minStmt, maxStmt);

				if (totalOut < correctOut) throw new IOException();

			} catch (NoSuchElementException e) {
				stRQ2Log.println(fileNamePath);
			}
		}

		Files.writeString(stRQ2Result, "======== Accuracy for STMTs inside the Try-Catch block\n", Charset.defaultCharset(), StandardOpenOption.APPEND);
		Files.writeString(stRQ2Result, String.format("correct: %d, total: %d\n", correctIn, totalIn), Charset.defaultCharset(), StandardOpenOption.APPEND);
		Files.writeString(stRQ2Result, String.valueOf(correctIn/(double)totalIn), Charset.defaultCharset(), StandardOpenOption.APPEND);
		System.out.println(correctIn/(double)totalIn);

		Files.writeString(stRQ2Result, "======== Accuracy for STMTs outside the Try-Catch block\n", Charset.defaultCharset(), StandardOpenOption.APPEND);
		Files.writeString(stRQ2Result, String.format("correct: %d, total: %d\n", correctOut, totalOut), Charset.defaultCharset(), StandardOpenOption.APPEND);
		Files.writeString(stRQ2Result, String.valueOf(correctOut/(double)totalOut), Charset.defaultCharset(), StandardOpenOption.APPEND);
		System.out.println(correctOut/(double)totalOut);
	}

	private void evalRQ3(final HashMap<String, CompilationUnit> cus, XMLQuery xmlQuery) throws Exception {
		Path stRQ3Result = Path.of(config.getProperty("stRQ3Result"));
		Files.deleteIfExists(stRQ3Result);
		Files.createFile(stRQ3Result);

		// positive instances which the tool also predicted to be positive
		List<String> files = Files.readAllLines(
				Path.of(config.getProperty("RQ1_TP"))
		);

		int n_iter = 0;
		for (String sourceFilePath : files) {
			System.out.println(n_iter);
//			if (n_iter > 10) {
//				break;
//			}
			n_iter++;
			Path fileNamePath = Path.of(sourceFilePath).getFileName();
			String fileName = fileNamePath.toString();
			String fileNameWithoutSuffix = fileName.substring(0, fileName.length() - 5);
			Path stRQ3Logger = Path.of(String.format(config.getProperty("stRQ3Logger"), fileNameWithoutSuffix));
			Files.deleteIfExists(stRQ3Logger);
			Files.createFile(stRQ3Logger);

			Path meta = Path.of(String.format(config.getProperty("meta"), fileNameWithoutSuffix));
			List<String> metaLines = Files.readAllLines(meta);
			String[] exceptions = metaLines.get(0).split(" ");
			for (int i = 0; i < exceptions.length; i++) {
				if (exceptions[i].contains(".")) {
					exceptions[i] = exceptions[i].trim().split("\\.")[exceptions[i].split("\\.").length - 1];
				}
			}

			Set<String> trueExceptions = new HashSet<>(Arrays.asList(exceptions));
			int numE = trueExceptions.size();


			List<String> callMetaLines = Files.readAllLines(Path.of(String.format(config.getProperty("metaApiCall"), fileNameWithoutSuffix)));
			List<String> callNames = new ArrayList<>();
			List<String> arities = new ArrayList<>();

			for (String line : callMetaLines) {
				String[] l = line.split("\t");
				callNames.add(l[0].strip());
				arities.add(l[3].strip());
			}

			Set<String> predExceptions = new HashSet<>();
			Random rand = new Random(42);
			for (int i = 0; i < callNames.size(); i++) {
				List<Set<String>> PSetCandidates = xmlQuery.predExceptions(callNames.get(i), arities.get(i));
				Set<String> PSet = PSetCandidates.get(rand.nextInt(PSetCandidates.size()));
				predExceptions.addAll(PSet);
			}

			int numP = predExceptions.size();

			int numOverlap = 0;
			for (String e : trueExceptions) {
				if (predExceptions.contains(e)) {
					numOverlap++;
				}
			}

			Files.writeString(stRQ3Logger, String.format("numE=%d\n", numE), Charset.defaultCharset(), StandardOpenOption.APPEND);
			Files.writeString(stRQ3Logger, String.format("numP=%d\n", numP), Charset.defaultCharset(), StandardOpenOption.APPEND);
			Files.writeString(stRQ3Logger, String.format("numOverlap=%d\n", numOverlap), Charset.defaultCharset(), StandardOpenOption.APPEND);

			for (String e : trueExceptions) {
				Files.writeString(stRQ3Logger, String.format(" # %s", e), Charset.defaultCharset(), StandardOpenOption.APPEND);
			}

			Files.writeString(stRQ3Logger, String.format("\n"), Charset.defaultCharset(), StandardOpenOption.APPEND);

			for (String e : predExceptions) {
				Files.writeString(stRQ3Logger, String.format(" # %s", e), Charset.defaultCharset(), StandardOpenOption.APPEND);
			}
		}
	}

	private void registerAPICallMeta(Path metaCallPath, APICallVisitor callv) throws IOException{
		assert (Objects.nonNull(callv));
		Files.deleteIfExists(metaCallPath);
		Files.createFile(metaCallPath);

		for (APICallSpec apiCallSpec : callv.apiCallSpecList) {
			Files.writeString(metaCallPath, String.format("%s\t%s\t%s\n", apiCallSpec.methodName, apiCallSpec.startLineNum, apiCallSpec.endLineNum), Charset.defaultCharset(), StandardOpenOption.APPEND);
		}
	}

	private int getCorrectInLineNum(int minPred, int maxPred, int minTrue, int maxTrue) {
		Set<Integer> trueSet = new HashSet<>();
		for (int i = minTrue; i <= maxTrue; i++) {
			trueSet.add(i);
		}

		Set<Integer> predSet = new HashSet<>();
		for (int i = minPred; i <= maxPred; i++) {
			predSet.add(i);
		}

		int correct = 0;
		for (int item : trueSet) {
			if (predSet.contains(item)) {
				correct++;
			}
		}

		return correct;
	}

	private int getCorrectOutLineNum(int minPred, int maxPred, int minTrue, int maxTrue, int minStmt, int maxStmt) {
		Set<Integer> stmtIdxSet = new HashSet<>();
		for (int i = minStmt; i <= maxStmt; i++) {
			stmtIdxSet.add(i);
		}

		Set<Integer> trueSet = new HashSet<>();
		for (int i = minTrue; i <= maxTrue; i++) {
			trueSet.add(i);
		}

		stmtIdxSet.removeAll(trueSet);
		Set<Integer> outSet = new HashSet<>(stmtIdxSet); // indices of stmts that are outside try-catch

		Set<Integer> predSet = new HashSet<>();
		for (int i = minPred; i <= maxPred; i++) {
			predSet.add(i);
		}

		int correct = 0;
		for (int item : outSet) {
			if (!predSet.contains(item)) {
				correct++;
			}
		}

		return correct;
	}
}