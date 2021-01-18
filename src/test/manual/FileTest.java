package test.manual;

import java.io.IOException;

import process.file.PrefsFileHelper;
import process.helpers.ProcessHelper;

public class FileTest {

	public static void main(String[] args) {
//		System.out.println(System.getenv("LocalAppData")); // C:\Users\[username]\AppData\Local
//		System.out.println(System.getProperty("user.home")); // C:\Users\[username]
		
		try {
			PrefsFileHelper prefsFileHelper = new PrefsFileHelper();
			
			prefsFileHelper.changePreference(PrefsFileHelper.DEFALUT_OUTPUT_FOLDER_KEY, "C:/Creatomaton/output(2)");
			prefsFileHelper.saveInFile();

			// exec directly graphviz
			String graphvizPath = prefsFileHelper.getPreference(PrefsFileHelper.GRAPHVIZ_PATH_KEY);
			String outputPath = prefsFileHelper.getPreference(PrefsFileHelper.DEFALUT_OUTPUT_FOLDER_KEY);
			String inputPath = prefsFileHelper.getPreference(PrefsFileHelper.DEFALUT_INPUT_FOLDER_KEY);
			
			//create input & output folder
			prefsFileHelper.createFolders();
			
			String command = graphvizPath + " -Tjpg -o " + outputPath + "\\result_1.jpg " + inputPath + "\\test_1.dot\"";
			
			ProcessHelper processHelper = new ProcessHelper(command);
			processHelper.runCommandAndWait();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
