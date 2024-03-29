package process.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Collection of methods used to manage files
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class FileUtility {
	private FileUtility() {};

	/**
	 * Checks if a file with the same name is on the computer
	 * 
	 * @param filename the file (with path) of the file
	 * @return if the file actually exists or not
	 */
	public static boolean fileExists(String filename) {
		File file = new File(filename);
		return file.exists();
	}

	/**
	 * Create data folder and all his sub-folders that the application need at the
	 * root of the application.
	 */
	public static void createDataFolders() {
		File tmpFolder = new File("data/tmp");
		if(!tmpFolder.exists() || !tmpFolder.isDirectory()) {
			tmpFolder.mkdirs();
		}
		
		File xmlFolder = new File("data/xml");
		if(!xmlFolder.exists() || !xmlFolder.isDirectory()) {
			xmlFolder.mkdirs();
		}
		
		File inputFolder = new File("data/input");
		if(!inputFolder.exists() || !inputFolder.isDirectory()) {
			inputFolder.mkdirs();
		}
		
		File outputFolder = new File("data/output");
		if(!outputFolder.exists() || !outputFolder.isDirectory()) {
			outputFolder.mkdirs();
		}
	}
	
	/**
	 * Clear all the content in a folder (such as files and sub-folders).
	 * If the folder the path provided doesn't exists or is not a directory, nothing will be done.
	 * @param folderName the name (with path) of the folder to clear content 
	 */
	public static void clearFolder(String folderName) {
		File directory = new File(folderName);
		if(directory.isDirectory()) {
			for (File file : directory.listFiles()) {
				//we must clear a folder before deleting it
				if(file.isDirectory()) {
					clearFolder(file.getAbsolutePath());
				}
				file.delete();
			}
		}
	}

	/**
	 * Create a new file name depending on the presence (or not) of another files
	 * that have the same name.
	 * 
	 * @param filename the path of the file to check
	 * @return the same String, or a string like "file(1).ext" if file like
	 *         "file.ext" already exists
	 */
	public static String searchFileOutputName(String filename) {
		final Pattern PATTERN = Pattern.compile("(.*?)(?:\\((\\d+)\\))?(\\.[^.]*)?");
		if (fileExists(filename)) {
			Matcher m = PATTERN.matcher(filename);
			if (m.matches()) {
				String prefix = m.group(1);
				String last = m.group(2);
				String suffix = m.group(3);
				if (suffix == null)
					suffix = "";

				int count = last != null ? Integer.parseInt(last) : 0;

				do {
					count++;
					filename = prefix + "(" + count + ")" + suffix;
				} while (fileExists(filename));
			}
		}
		return filename;
	}

	/**
	 * Get the extension from the filename provided
	 * 
	 * @param filename the name of the file
	 * @return the extension <b>without the '.'</b> if extsts, an empty String else
	 */
	public static String getFilenameExtension(String filename) {
		int dotIndex = filename.lastIndexOf('.');
		if (dotIndex == -1 || dotIndex == filename.length() - 1) {
			return "";
		} else {
			return filename.substring(dotIndex + 1, filename.length());
		}
	}

	/**
	 * Set the filename so that the extension is the one desired.
	 * <p>
	 * ex :
	 * <ul>
	 * <li>changeFilenameExtension("file.txt", "jpg") == "file.jpg"</li>
	 * <li>changeFilenameExtension("file", "jpg") == "file.jpg"</li>
	 * </ul>
	 */
	public static String getRightFilenameExtension(String filename, String extension) {
		String fileExtension = getFilenameExtension(filename);
		// if no extension on the file
		if (fileExtension.isEmpty()) {
			// remove last dot if exists
			int dotIndex = filename.lastIndexOf('.');
			if (dotIndex == filename.length() - 1) {
				filename = filename.substring(0, filename.length() - 1);
			}
			// add the file extension
			filename += "." + extension;
			return filename;
		}

		if (fileExtension.equals(extension)) {
			return filename;
		} else {
			int dotIndex = filename.lastIndexOf('.');
			filename = filename.substring(dotIndex + 1, filename.length());
			filename += "." + extension;
			return filename;
		}
	}

	public static boolean isFileWithGoodExtension(String filename, String extension) {
		int dotIndex = filename.lastIndexOf('.');
		if (dotIndex == -1) {
			return false;
		}

		String fileExtension = filename.substring(dotIndex + 1, filename.length());
		return fileExtension.equals(extension);
	}

	public static long getNumberOfLines(File file) {
		LineNumberReader numberReader;
		long numberOfLines;
		try {
			numberReader = new LineNumberReader(new FileReader(file));
			numberOfLines = numberReader.lines().count();
			numberReader.close();
			return numberOfLines;
		} catch (IOException e) {
			return 0;
		}
	}

	/**
	 * Check if two files have the exact same content
	 */
	public static boolean areFilesHaveSameContent(File file1, File file2) throws IOException {
		// check if they have the same number of lines
		long numberOfLines1 = getNumberOfLines(file1);
		long numberOfLines2 = getNumberOfLines(file2);
		if (numberOfLines1 != numberOfLines2) {
			return false;
		}

		BufferedReader reader1 = new BufferedReader(new FileReader(file1));
		BufferedReader reader2 = new BufferedReader(new FileReader(file2));

		String line1, line2;
		boolean haveSameContent = true;
		while (haveSameContent && (line1 = reader1.readLine()) != null) {
			line2 = reader2.readLine();
			if (!line1.equals(line2)) {
				haveSameContent = false;
			}
		}

		reader1.close();
		reader2.close();
		return haveSameContent;
	}
	
	/**
	 * Given a filename with his path, retreive the name of the parent folder (with his path).<p>
	 * For example, getParentFolderName("a/b/text.txt") will give "a/b";
	 * @param filename the name of the file to search the parent folder
	 * @return the name with path of the parent folder, or null if have no parent folder.
	 */
	public static String getParentFolderName(String filename) {
		Path path = Paths.get(filename);
		Path parentFolderPath = path.getParent();
		if(parentFolderPath == null) {
			return null;
		}else {
			return parentFolderPath.toString();
		}
	}
}
