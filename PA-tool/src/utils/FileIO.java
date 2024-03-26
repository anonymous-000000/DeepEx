package utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class FileIO {

    public static String getSimpleClassName(String className) {
        String name = className.substring(className.lastIndexOf('.') + 1);
        return name;
    }
	
	public static String readStringFromFile(String inputFile) {
		try {
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(inputFile));
			byte[] bytes = new byte[(int) new File(inputFile).length()];
			in.read(bytes);
			in.close();
			return new String(bytes);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void writeStringToFile(String string, String outputFile) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
			writer.write(string);
			writer.flush();
			writer.close();
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	public static ArrayList<File> getPaths(File dir) {
		ArrayList<File> files = new ArrayList<>();
		if (dir.isDirectory())
			for (File sub : dir.listFiles())
				files.addAll(getPaths(sub));
		else if (dir.getName().endsWith(".java"))
			files.add(dir);
		return files;
	}
	
	public static ArrayList<String> getAllFilesInFolder(String folder) {
		ArrayList<String> allFiles = new ArrayList<String>();
		for (File file : new File(folder).listFiles()) {
			if (file.isFile())
			{
				System.out.println(file.getName() + ":" + file.length());
				allFiles.add(file.getPath());
			}
			else
				allFiles.addAll(getAllFilesInFolder(file.getPath()));
		}
		return allFiles;
	}
}
