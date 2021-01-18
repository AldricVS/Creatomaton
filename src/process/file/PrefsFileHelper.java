package process.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Used to search the ini file in the appdata folder on the user computer and
 * deals with it
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class PrefsFileHelper {
	public static final String GRAPHVIZ_PATH_KEY = "dot_path";
	public static final String DEFALUT_OUTPUT_FOLDER_KEY = "def_out";
	public static final String DEFALUT_INPUT_FOLDER_KEY = "def_in";

	private static final int NUMBER_OF_PREFERENCES = 3;

	private static final String APP_FOLDER_NAME = "Creatomaton";
	private static final String DATA_FOLDER_NAME = "data";
	private static final String INI_FILE_NAME = "settings.ini";

	private static final String DEFAULT_GRAPHVIZ_PATH = "C:\\Program Files (x86)\\Graphviz\\bin\\dot.exe";
	private static final String DEFAULT_OUTPUT_FOLDER = "C:\\Creatomaton\\output";
	private static final String DEFAULT_INPUT_FOLDER = "C:\\Creatomaton\\input";

	private String iniFilePath;
	File iniFile;

	private HashMap<String, String> preferences = new HashMap<String, String>();

	/**
	 * Tries to open the ini file in the right location and create a new one else,
	 * then parse it in order to put it in the prefereces hashmap
	 * 
	 * @throws IOException
	 */
	public PrefsFileHelper() throws IOException {
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
		} else {
			parseIniFile();
		}
	}

	private void parseIniFile() throws IOException {
		FileReader in = new FileReader(iniFile);
		BufferedReader bufferedReader = new BufferedReader(in);

		String line;
		while ((line = bufferedReader.readLine()) != null) {
			// check if line is not a comment
			if (line.startsWith("#")) {
				continue;
			}

			int beginIndex = line.indexOf('=') + 1;
			// if we didn't found any '=' sign, stop immediately for this line
			if (beginIndex == -1) {
				continue;
			}
			String substr = line.substring(beginIndex, line.length());
			if (line.startsWith(GRAPHVIZ_PATH_KEY)) {
				preferences.put(GRAPHVIZ_PATH_KEY, substr);

			} else if (line.startsWith(DEFALUT_OUTPUT_FOLDER_KEY)) {
				preferences.put(DEFALUT_OUTPUT_FOLDER_KEY, substr);

			} else if (line.startsWith(DEFALUT_INPUT_FOLDER_KEY)) {
				preferences.put(DEFALUT_INPUT_FOLDER_KEY, substr);
			}
			// else, don't read the line
		}

		bufferedReader.close();

		// we must have all preferences filled, else, we reset all data
		if (preferences.size() != NUMBER_OF_PREFERENCES) {
			preferences.clear();
			resetIniContent();
		}
	}

	private void createNewIniFile() throws IOException {
		iniFile.createNewFile();
		resetIniContent();
	}

	private void resetIniContent() throws IOException {
		FileWriter out = new FileWriter(iniFile, false);
		BufferedWriter bw = new BufferedWriter(out);

		// only 3 things to add for now
		bw.write(GRAPHVIZ_PATH_KEY + "=" + DEFAULT_GRAPHVIZ_PATH);
		bw.newLine();
		bw.write(DEFALUT_OUTPUT_FOLDER_KEY + "=" + DEFAULT_OUTPUT_FOLDER);
		bw.close();
		bw.write(DEFALUT_INPUT_FOLDER_KEY + "=" + DEFAULT_INPUT_FOLDER);
		bw.close();

		// no need to parse, we already know preferences
		preferences.put(GRAPHVIZ_PATH_KEY, DEFAULT_GRAPHVIZ_PATH);
		preferences.put(DEFALUT_OUTPUT_FOLDER_KEY, DEFAULT_OUTPUT_FOLDER);
		preferences.put(DEFALUT_INPUT_FOLDER_KEY, DEFAULT_INPUT_FOLDER);
	}

	/**
	 * Retrieve the entire hashmap of preferences parsed in the ini file
	 */
	public HashMap<String, String> getPreferencesList() {
		return preferences;
	}

	/**
	 * Retrieve a specific preference by its key. A key is a String defined in a
	 * constant finishing by "_KEY".
	 * 
	 * @param key the key of the preference wanted
	 * @return the String associated with this preference or {@code null} if no one
	 *         is found
	 */
	public String getPreference(String key) {
		return preferences.get(key);
	}

	/**
	 * If needed, create the default input and output folder
	 */
	public void createFolders() {
		String outputPath = getPreference(PrefsFileHelper.DEFALUT_OUTPUT_FOLDER_KEY);
		String inputPath = getPreference(PrefsFileHelper.DEFALUT_INPUT_FOLDER_KEY);

		File output = new File(outputPath);
		if (!output.exists()) {
			output.mkdirs();
		}

		File input = new File(inputPath);
		if (!input.exists()) {
			input.mkdirs();
		}
	}

	/**
	 * Change a preference associated with a specific key. All changed need to be
	 * saved with {@link #saveInFile()} in order to be permanent
	 * 
	 * @param preferenceKey the key of the preference to change
	 * @param newValue      the new value of the key
	 * @throws NoSuchElementException if the key provided doesn't exists
	 */
	public void changePreference(String preferenceKey, String newValue) throws NoSuchElementException {
		if (!preferences.containsKey(preferenceKey)) {
			throw new NoSuchElementException("Preference " + preferenceKey + " does not exists");
		}
		preferences.replace(preferenceKey, newValue);
	}

	/**
	 * Save all preferences in the ini file.
	 * @throws IOException
	 */
	public void saveInFile() throws IOException {
		FileWriter out = new FileWriter(iniFile, false);
		BufferedWriter bw = new BufferedWriter(out);

		for (Map.Entry<String, String> map : preferences.entrySet()) {
			bw.write(map.getKey() + "=" + map.getValue());
			bw.newLine();
		}

		bw.close();
	}
}
