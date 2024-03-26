package evaluation;

import utils.FileIO;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.Properties;
import java.util.Scanner;
import static evaluation.DSType.*;

public class Main {
	private String pathList;

	public Main(String pathList) {
		this.pathList = pathList;
	}
	
	public void generateParallelCorpus(final String outPath, final boolean recursive, final boolean testing, Properties config) throws Exception {
		String content = FileIO.readStringFromFile(pathList);
		Scanner sc = new Scanner(content);
		while (sc.hasNextLine()) {
			final String line = sc.nextLine();
			int index = line.indexOf(' '); // <name> <path>
			final String name = line.substring(0, index), path = line.substring(index + 1);
			if (name.startsWith("#")) {
				continue;
			}

			if (name.equals("neg")) {
				DSSplitParser dsSplitParser = new DSSplitParser(name, path, testing, NEG, false, config);
				dsSplitParser.evalCorpus(outPath + "/" + name, recursive);
			} else {
				DSSplitParser dsSplitParser = new DSSplitParser(name, path, testing, POS, false, config);
				dsSplitParser.evalCorpus(outPath + "/" + name, recursive);
			}

		}
		sc.close();
	}
	
	public static void main(String[] args) throws Exception{
		Properties config = new Properties();
		config.load(Files.newInputStream(Path.of("./src/evaluation/config.properties")));

		Instant start = Instant.now();
		Main parser = new Main(config.getProperty("list_eval"));
		parser.generateParallelCorpus(config.getProperty("output"), true, true, config);
		Instant finish = Instant.now();
		long timeElapsed = Duration.between(start, finish).toMinutes();
		System.out.println(String.format("elapsed time: %d", timeElapsed));
	}
}
