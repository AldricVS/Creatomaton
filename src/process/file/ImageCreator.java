package process.file;

import java.io.File;
import java.io.IOException;

import data.Automaton;

/**
 * Create an image from an automaton and some parameters, with the help of
 * {@code DotBuilder} and {@code GraphvizHelper}
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class ImageCreator {
	private Automaton automaton;

	/**
	 * Simply the name of the file without extension, no path with folders. 
	 */
	private String filename;

	/**
	 * If the automaton is a mirror of another automaton, set this varible to true
	 * in order to help graphviz to draw a viable image
	 */
	private boolean isMirror = false;

	public ImageCreator(Automaton automaton) {
		this.automaton = automaton;
	}

	/**
	 * Generates the image file (as a jpeg file) with al the parameters gathered. It will be created
	 * in the output folder defined in the settings.ini file preferences
	 * 
	 * @return the file where the image is stored
	 * @throws IOException if an IOError occurs (mostly a security or permission exception)
	 */
	public File createImageFile() throws IOException{
		return null;
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
}
