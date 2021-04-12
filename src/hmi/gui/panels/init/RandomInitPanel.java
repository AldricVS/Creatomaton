package hmi.gui.panels.init;

import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RandomInitPanel extends JPanel {

	JTextField numberStatesTextField = new JTextField();
	JTextField numberFinalStatesTextField = new JTextField();
	JTextField numberEpsilonTransTextField = new JTextField();
	JTextField alphabetTextField = new JTextField();
	
	public RandomInitPanel() {
		initTextFields();
		setLayout(new GridLayout(0, 1));
		add(numberStatesTextField);
		add(Box.createGlue());
		add(numberFinalStatesTextField);
		add(Box.createGlue());
		add(numberEpsilonTransTextField);
		add(Box.createGlue());
		add(alphabetTextField);
	}

	private void initTextFields() {
		numberEpsilonTransTextField.setInputVerifier(new NumberVerifier());
		numberStatesTextField.setInputVerifier(new NumberVerifier());
		numberFinalStatesTextField.setInputVerifier(new NumberVerifier());
	}

	/**
	 * An input is valid if it is a strictly positive integer.
	 */
	class NumberVerifier extends InputVerifier {
		@Override
		public boolean verify(JComponent input) {
			JTextField textField = (JTextField) input;
			String text = textField.getText().trim();
			try {
				int value = Integer.parseInt(text);
				return value > 0;
			} catch (NumberFormatException e) {
				return false;
			}
		}
	}
}
