package process.file;

import java.io.File;
import java.io.IOException;

import data.Automaton;
import process.builders.DotBuilder;
import process.helpers.GraphvizHelper;

/**
 * Create an image from an automaton and some parameters, with the help of
 * {@code DotBuilder} and {@code GraphvizHelper}
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class ImageCreator {
	private GraphvizHelper graphvizHelper;
	private DotBuilder dotBuilder;
	private PrefsFileHelper prefsFileHelper;

	private Automaton automaton;

	/**
	 * Simply the name of the file without extension, no path with folders.
	 */
	private String filename;

	private String outputFolder = null;

	/**
	 * If the automaton is a mirror of another automaton, set this varible to true
	 * in order to help graphviz to draw a viable image
	 * 
	 */
	private boolean isMirror = false;

	private boolean doesTryToGetNames = true;

	private boolean isInLandscapeMode = true;

	/**
	 * Create an instance of ImageCreator, which also loads the GraphvizHelper and
	 * the DotBuilder
	 * 
	 * @param automaton the automaton to create the image for
	 * @throws IOException              most of the time if read/write permission is
	 *                                  denied
	 * @throws IllegalArgumentException If filename is an empty string
	 */
	public ImageCreator(Automaton automaton, String filename) throws IOException, IllegalArgumentException {
		if (filename.trim().isEmpty()) {
			throw new IllegalArgumentException("Empty filename (or with only spaces) is not allowed");
		}
		this.automaton = automaton;
		this.filename = filename;
		graphvizHelper = new GraphvizHelper();
		dotBuilder = new DotBuilder(automaton);
	}

	/**
	 * Generates the image file (as a jpeg file) with al the parameters gathered. If
	 * no output folder was defined before, the file will be created in the output
	 * folder defined in the settings.ini file preferences
	 * 
	 * @return the file where the image is stored
	 * @throws IOException if an IOError occurs (mostly a security or permission
	 *                     exception)
	 */
	public File createImageFile() throws IOException {
		prefsFileHelper = new PrefsFileHelper();
		File dotFile = createDotFile();
		File imageFile = createImage(dotFile);
		return imageFile;
	}

	/**
	 * Use DotBuilder in order to create the dot file
	 * 
	 * @return the file where the data is saved
	 * @throws IOException
	 */
	private File createDotFile() throws IOException {
		dotBuilder.setAutomaton(automaton);
		// retreive the file input path from the PrefsFileHelper
		String inputFolderPath = prefsFileHelper.getPreference(PrefsFileHelper.DEFAULT_INPUT_FOLDER_KEY);
		File inputFile = new File(inputFolderPath);
		if (!inputFile.exists()) {
			inputFile.mkdirs();
		}

		// create the fot file
		String name = filename;
		if (!name.endsWith(".dot")) {
			name += ".dot";
		}
		File dotFile = new File(inputFile.getAbsolutePath() + "/" + name);
		if (!dotFile.exists()) {
			dotFile.createNewFile();
		}
		// init dot builder preferences
		dotBuilder.setInReverseMode(isMirror);
		dotBuilder.setIsTriyingToGetStatesNames(doesTryToGetNames);
		dotBuilder.setInLandscapeMode(isInLandscapeMode);
		dotBuilder.buildDotFile(dotFile);
		return dotFile;
	}

	/**
	 * Create the image from the dot file provided and the help of the Graphviz
	 * appllication.
	 * 
	 * @param dotFile the dot file to create image with
	 * @return the image file
	 * @throws IOException
	 */
	private File createImage(File dotFile) throws IOException {
		GraphvizHelper graphvizHelper = new GraphvizHelper(dotFile.getAbsolutePath());
		String name = filename;
		if (!filename.endsWith(".jpg")) {
			name += ".jpg";
		}
		graphvizHelper.setFileOutputName(name);
		if(outputFolder != null) {
			graphvizHelper.setFileOutputPath(outputFolder);
		}
		graphvizHelper.runCommand();
		// check if the file exists
		File imageFile = new File(prefsFileHelper.getPreference(PrefsFileHelper.DEFAULT_OUTPUT_FOLDER_KEY) + "/" + name);
		return imageFile;
	}

	public GraphvizHelper getGraphvizHelper() {
		return graphvizHelper;
	}

	public DotBuilder getDotBuilder() {
		return dotBuilder;
	}

	public PrefsFileHelper getPrefsFileHelper() {
		return prefsFileHelper;
	}

	public Automaton getAutomaton() {
		return automaton;
	}

	public String getFilename() {
		return filename;
	}

	public String getOutputFolder() {
		return outputFolder;
	}

	public boolean isMirror() {
		return isMirror;
	}

	public boolean isDoesTryToGetNames() {
		return doesTryToGetNames;
	}

	public boolean isInLandscapeMode() {
		return isInLandscapeMode;
	}

	public void setGraphvizHelper(GraphvizHelper graphvizHelper) {
		this.graphvizHelper = graphvizHelper;
	}

	public void setDotBuilder(DotBuilder dotBuilder) {
		this.dotBuilder = dotBuilder;
	}

	public void setPrefsFileHelper(PrefsFileHelper prefsFileHelper) {
		this.prefsFileHelper = prefsFileHelper;
	}

	public void setAutomaton(Automaton automaton) {
		this.automaton = automaton;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setOutputFolder(String outputFolder) {
		this.outputFolder = outputFolder;
	}

	public void setMirror(boolean isMirror) {
		this.isMirror = isMirror;
	}

	public void setDoesTryToGetNames(boolean doesTryToGetNames) {
		this.doesTryToGetNames = doesTryToGetNames;
	}

	public void setInLandscapeMode(boolean isInLandscapeMode) {
		this.isInLandscapeMode = isInLandscapeMode;
	}
}
