package test.manual;

import java.io.IOException;

import process.helpers.GraphvizHelper;

public class GraphvizHelperTest {

	public static void main(String[] args) {
		try {
			String fileInputPath = "\"C:/Program Files (x86)/Graphviz/tmp/test.dot\"";
			String fileOutputPath = "C:\\Creatomaton\\output";
			GraphvizHelper graphvizHelper = new GraphvizHelper(fileInputPath, fileOutputPath);
			graphvizHelper.setFileOutputName("billy.jpg");
			graphvizHelper.runCommand();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
