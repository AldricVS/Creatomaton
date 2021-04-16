package hmi.gui.management;

import java.awt.Toolkit;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Document filter that permits to write only integers
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class IntegerDocumentFilter extends DocumentFilter {
	private static final int MAX_NUMBER_DIGITS_DEFAULT = 3;
	private static final String REGEX = "\\d";

	private int maxNumberDigits = MAX_NUMBER_DIGITS_DEFAULT;

	/**
	 * Init a document filter that allows only 3 digits or less integers.
	 */
	public IntegerDocumentFilter() {
		super();
	}

	public IntegerDocumentFilter(int maxNumberDigits) {
		super();
		this.maxNumberDigits = maxNumberDigits;
	}

	@Override
	public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
		// check if the total length of the new text is not too big and allow only
		// digits
		if ((fb.getDocument().getLength() + string.length()) <= maxNumberDigits && string.matches(REGEX)) {
			// replace in string to append only characters that are digits
			fb.insertString(offset, string, attr);
		} else {
			Toolkit.getDefaultToolkit().beep();
		}
	}

	@Override
	public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
		// check if the total length of the new text is not too big and allow only
		// digits
		if ((fb.getDocument().getLength() + text.length()) <= maxNumberDigits && text.matches(REGEX)) {
			// replace in string to append only characters that are digits
			fb.insertString(offset, text, attrs);
		} else {
			Toolkit.getDefaultToolkit().beep();
		}

	}

}