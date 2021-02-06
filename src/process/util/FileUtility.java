package process.util;

import java.io.File;
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
	 * @param filename the file (with path) of the file
	 * @return if the file actually exists or not
	 */
	public static boolean fileExists(String filename) {
		File file = new File(filename);
		return file.exists();
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
	 * @param filename the name of the file 
	 * @return the extension <b>without the '.'</b> if extsts, an empty String else 
	 */
	public static String getFilenameExtension(String filename) {
		int dotIndex = filename.lastIndexOf('.');
		if(dotIndex == -1 || dotIndex == filename.length() - 1) {
			return "";
		}else{
			return filename.substring(dotIndex + 1, filename.length());
		}
	}
	
	/**
	 * Set the filename so that the extension is the one desired.<p>
	 * ex :
	 * <ul>
	 * <li>changeFilenameExtension("file.txt", "jpg") == "file.jpg"</li>
	 * <li>changeFilenameExtension("file", "jpg") == "file.jpg"</li>
	 * </ul>
	 */
	public static String getRightFilenameExtension(String filename, String extension) {
		String fileExtension = getFilenameExtension(filename);
		//if no extension on the file
		if(fileExtension.isEmpty()) {
			//remove last dot if exists
			int dotIndex = filename.lastIndexOf('.');
			if(dotIndex == filename.length() - 1) {
				filename = filename.substring(0, filename.length() - 1);
			}
			//add the file extension
			filename += "." + fileExtension;
			return filename;
		}
		
		if(fileExtension.equals(extension)) {
			return filename;
		}else {
			int dotIndex = filename.lastIndexOf('.');
			filename = filename.substring(dotIndex + 1, filename.length());
			filename += extension;
			return filename;
		}
	}
}
