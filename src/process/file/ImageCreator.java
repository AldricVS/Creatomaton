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
		graphvizHelper = new GraphvizHelper();
		dotBuilder = new DotBuilder(automaton);
	}

	/**
	 * Generates the image file (as a jpeg file) with al the parameters gathered. It
	 * will be created in the output folder defined in the settings.ini file
	 * preferences
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
		// retreive the file input path from the PrefsFileHelper
		String inputFolderPath = prefsFileHelper.getPreference(PrefsFileHelper.DEFAULT_INPUT_FOLDER_KEY);
		File inputFile = new File(inputFolderPath);
		if (!inputFile.exists()) {
			inputFile.mkdirs();
		}

		// create the fot file
		String name = filename;
		if (!filename.endsWith(".dot")) {
			name += ".dot";
		}
		File dotFile = new File(name);
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
		GraphvizHelper graphvizHelper = new GraphvizHelper(dotFile.getAbsolutePath(), prefsFileHelper);
		String name = filename;
		if (!filename.endsWith(".jpg")) {
			name += ".jpg";
		}
		graphvizHelper.setFileOutputName(name);
		graphvizHelper.runCommand();
		//check if the file exists
		File imageFile = new File(prefsFileHelper.getPreference(PrefsFileHelper.DEFAULT_OUTPUT_FOLDER_KEY) + "/" + name);
		return imageFile;
	}

	public Automaton getAutomaton() {
		return automaton;
	}

	public boolean isMirror() {
		return isMirror;
	}

	public void setAutomaton(Automaton automaton) {
		this.automaton = automaton;
	}

	public void setIsMirror(boolean isMirror) {
		this.isMirror = isMirror;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public boolean isDoesTryToGetNames() {
		return doesTryToGetNames;
	}

	public void setDoesTryToGetNames(boolean doesTryToGetNames) {
		this.doesTryToGetNames = doesTryToGetNames;
	}

	public boolean isInLandscapeMode() {
		return isInLandscapeMode;
	}

	public void setInLandscapeMode(boolean isInLandscapeMode) {
		this.isInLandscapeMode = isInLandscapeMode;
	}
}
