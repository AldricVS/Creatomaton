package process.helpers;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import process.file.PrefsFileHelper;
import process.util.FileUtility;

/**
 * Helper class used to handle Graphviz easily. Only jpg exports are supported
 * for the moment
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class GraphvizHelper {
	private static final String DEFAULT_OUTPUT_FILENAME = "graph_res.jpg";

	private String graphvizPath = null;
	private String fileOutputPath = null;
	private String fileInputPath = null;

	private String fileOutputName = null;

	public GraphvizHelper(String graphvizPath, String fileInputPath, String fileOutputPath) {
		this.graphvizPath = graphvizPath;
		this.fileOutputPath = fileOutputPath;
		this.fileInputPath = fileInputPath;
	}

	public GraphvizHelper(String fileInputPath, String fileOutputPath) throws IOException {
		this.fileOutputPath = fileOutputPath;
		this.fileInputPath = fileInputPath;
		retrieveDefaultPreferences();
	}

	public GraphvizHelper(String fileInputPath) throws IOException {
		this.fileInputPath = fileInputPath;
		retrieveDefaultPreferences();

	}

	private void retrieveDefaultPreferences() throws IOException {
		PrefsFileHelper prefsFileHelper = new PrefsFileHelper();
		if (graphvizPath == null) {
			graphvizPath = prefsFileHelper.getPreference(PrefsFileHelper.GRAPHVIZ_PATH_KEY);
		}
		if (fileOutputPath == null) {
			// we only have directory in the preferences
			fileOutputPath = prefsFileHelper.getPreference(PrefsFileHelper.DEFALUT_OUTPUT_FOLDER_KEY);
		}
	}

	public void setFileOutputName(String name) {
		fileOutputName = name;
	}

	/**
	 * Run the command and wait for the end of it
	 * 
	 * @return the exit code of the graphviz process
	 */
	public int runCommand() {
		// check if fileOutputPath is the entire path
		String separator;
		if(fileOutputPath.endsWith("/") || fileOutputPath.endsWith("\\")) {
			separator = "";
		}else {
			separator = "/";
		}
		
		if (fileOutputName != null) {
			fileOutputPath += separator + fileOutputName;
		} else {
			if (!fileOutputPath.endsWith(".jpg") && !fileOutputPath.endsWith(".jpg\"")) {
				fileOutputPath += separator + DEFAULT_OUTPUT_FILENAME;
			}
		}

		fileOutputPath = FileUtility.searchFileOutputName(fileOutputPath);

		// in order to allow spaces in file path
		fileOutputPath = encapsulateFilePath(fileOutputPath);
		fileInputPath = encapsulateFilePath(fileInputPath);
		graphvizPath = encapsulateFilePath(graphvizPath);

		String command = graphvizPath + " -Tjpg -o " + fileOutputPath + " " + fileInputPath;

		ProcessHelper processHelper = new ProcessHelper(command);
		return processHelper.runCommandAndWait();
	}

	/**
	 * Returns a new String inside '"' characters, if it is not already the case
	 */
	private String encapsulateFilePath(String filePath) {
		String res = filePath;
		if (!filePath.startsWith("\"")) {
			res = "\"" + res;
		}
		if (!filePath.endsWith("\"")) {
			res = res + "\"";
		}
		return res;
	}

	

	
}
