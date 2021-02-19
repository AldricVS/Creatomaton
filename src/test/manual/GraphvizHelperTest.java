package test.manual;

import java.io.IOException;

import process.file.PrefsFileHelper;
import process.helpers.GraphvizHelper;

public class GraphvizHelperTest {

	public static void main(String[] args) {
		try {
			PrefsFileHelper prefsFileHelper = new PrefsFileHelper();
			String fileInputPath = "\"C:/Program Files (x86)/Graphviz/tmp/test.dot\"";
			String fileOutputPath = "C:/Creatomaton";
			GraphvizHelper graphvizHelper = new GraphvizHelper(fileInputPath, fileOutputPath, prefsFileHelper);
			graphvizHelper.setFileOutputName("billy.jpg");
			graphvizHelper.runCommand();
//			
//			String fileInputPath = "\"C:/Program Files (x86)/Graphviz/tmp/test.dot\"";
//			GraphvizHelper graphvizHelper = new GraphvizHelper(fileInputPath);
//			graphvizHelper.setFileOutputName("joel billy.jpg");
//			graphvizHelper.runCommand();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
