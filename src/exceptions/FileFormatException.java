package exceptions;

/**
 * Exception thrown when the the file to store automaton is not formatted properly 
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class FileFormatException extends Exception {
	
	public FileFormatException() {
		super();
	}
	
	public FileFormatException(String message) {
		super(message);
	}
}
