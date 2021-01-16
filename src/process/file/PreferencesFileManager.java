package process.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * Used to search the ini file in the appdata folder on the user computer and
 * deals with it
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class PreferencesFileManager {
	private static final String APP_FOLDER_NAME = "Creatomaton";
	private static final String DATA_FOLDER_NAME = "data";
	private static final String INI_FILE_NAME = "settings.ini";
	
	private static final String GRAPHVIZ_PATH_KEY = "dot_path";
	private static final String OUTPUT_FOLDER_KEY = "def_out";
	
	private static final String DEFAULT_GRAPHVIZ_PATH = "C:\\Program Files (x86)\\Graphviz\\bin\\dot.exe";
	private static final String DEFAULT_OUTPUT_FOLDER = "C:\\Creatomaton\\output";

	private String iniFilePath;
	File iniFile;

	private HashMap<String, String> preferences = new HashMap<String, String>();

	/**
	 * Tries to open the ini file in the right location and create a new one else,
	 * then parse it in order to put it in the prefereces hashmap
	 * @throws IOException 
	 */
	public PreferencesFileManager() throws IOException {
		handleIniFile();
	}

	private void handleIniFile() throws IOException {
		// try to get the user folder location
		iniFilePath = System.getProperty("user.home") + "/" + APP_FOLDER_NAME + "/" + DATA_FOLDER_NAME;

		// create folders if not exists and go in
		File appFolder = new File(iniFilePath);
		if (!appFolder.exists() || !appFolder.isDirectory()) {
			appFolder.mkdirs();
		}

		iniFile = new File(iniFilePath + "/" + INI_FILE_NAME);
		if (!iniFile.exists()) {
			createNewIniFile();
		}else {
			parseIniFile();
		}
	}

	private void parseIniFile() throws IOException{
		FileReader in = new FileReader(iniFile);
		BufferedReader bufferedReader = new BufferedReader(in);
		
		String line;
		while((line = bufferedReader.readLine()) != null) {
			int beginIndex = line.indexOf('=') + 1;
			if(beginIndex == 0)
			String substr = line.substring(beginIndex, line.length());
			if(line.startsWith(GRAPHVIZ_PATH_KEY)) {
				
			}else if(line.startsWith(OUTPUT_FOLDER_KEY)) {
				
			}
		}
	}

	private void createNewIniFile() throws IOException {
		iniFile.createNewFile();
		FileWriter out = new FileWriter(iniFile);
		BufferedWriter bw = new BufferedWriter(out);
		
		//only 2 things to add for now, default graphviz path and default output folder
		bw.write(GRAPHVIZ_PATH_KEY + "=" + DEFAULT_GRAPHVIZ_PATH);
		bw.newLine();
		bw.write(OUTPUT_FOLDER_KEY + "=" + DEFAULT_OUTPUT_FOLDER);
		bw.close();
		
		//no need to parse, we already know preferences
		preferences.put(GRAPHVIZ_PATH_KEY, DEFAULT_GRAPHVIZ_PATH);
		preferences.put(OUTPUT_FOLDER_KEY, DEFAULT_OUTPUT_FOLDER);
	}

}
