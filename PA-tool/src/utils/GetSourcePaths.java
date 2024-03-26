package utils;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.io.*;
import java.util.*;

public class GetSourcePaths {
    private static final boolean PARSE_INDIVIDUAL_SRC = false, SCAN_FILES_FRIST = false, REMOVE_COMMON_WORDS = true;
    private Set<String> badFiles = new HashSet<>();

    /*
    * Return paths of all files that end in some suffixes within a folder
    *
    * Example Usage:
    *   String[] sourcePaths = getSourcePaths(rootPath, new String[]{".java"}, true);
    * */
    public String[] getSourcePaths(String path, String[] extensions, boolean recursive) {
        HashSet<String> exts = new HashSet<>();
        for (String e : extensions)
            exts.add(e);
        HashSet<String> paths = new HashSet<>();
        getSourcePaths(new File(path), paths, exts, recursive);
//        paths.removeAll(badFiles);
        // dump the collection into a newly allocated array of String
        return (String[]) paths.toArray(new String[0]);
    }

    private void getSourcePaths(File file, HashSet<String> paths, HashSet<String> exts, boolean recursive) {
        if (file.isDirectory()) {
            if (paths.isEmpty() || recursive)
                for (File sub : file.listFiles())
                    getSourcePaths(sub, paths, exts, recursive);
        } else if (exts.contains(getExtension(file.getName())))
            paths.add(file.getAbsolutePath());
    }

    private static Object getExtension(String name) {
        int index = name.lastIndexOf('.');
        if (index < 0)
            index = 0;
        return name.substring(index);
    }

    public ArrayList<String> getRootPaths(String inPath) {
        ArrayList<String> rootPaths = new ArrayList<>();
        if (PARSE_INDIVIDUAL_SRC)
            getRootPaths(new File(inPath), rootPaths);
        else {
            if (SCAN_FILES_FRIST)
                getRootPaths(new File(inPath), rootPaths);
            rootPaths = new ArrayList<>();
            rootPaths.add(inPath);
        }
        return rootPaths;
    }

    private void getRootPaths(File file, ArrayList<String> rootPaths) {
        if (file.isDirectory()) {
//			System.out.println(rootPaths);
            for (File sub : file.listFiles())
                getRootPaths(sub, rootPaths);
        } else if (file.getName().endsWith(".java")) {
            @SuppressWarnings("rawtypes")
            Map options = JavaCore.getOptions();
            options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
            options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
            options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
            ASTParser parser = ASTParser.newParser(AST.JLS8);
            parser.setCompilerOptions(options);
            parser.setSource(FileIO.readStringFromFile(file.getAbsolutePath()).toCharArray());
            try {
                CompilationUnit ast = (CompilationUnit) parser.createAST(null);
                if (ast.getPackage() != null && !ast.types().isEmpty() && ast.types().get(0) instanceof TypeDeclaration) {
                    String name = ast.getPackage().getName().getFullyQualifiedName();
                    name = name.replace('.', '\\');
                    String p = file.getParentFile().getAbsolutePath();
                    if (p.endsWith(name))
                        add(p.substring(0, p.length() - name.length() - 1), rootPaths);
                } /*else
					badFiles.add(file.getAbsolutePath());*/
            } catch (Throwable t) {
                badFiles.add(file.getAbsolutePath());
            }
        }
    }

    private void add(String path, ArrayList<String> rootPaths) {
        int index = Collections.binarySearch(rootPaths, path);
        if (index < 0) {
            index = - index - 1;
            int i = rootPaths.size() - 1;
            while (i > index) {
                if (rootPaths.get(i).startsWith(path))
                    rootPaths.remove(i);
                i--;
            }
            i = index - 1;
            while (i >= 0) {
                if (path.startsWith(rootPaths.get(i)))
                    return;
                i--;
            }
            rootPaths.add(index, path);
        }
    }

}