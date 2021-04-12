package hmi.gui.panels.init;

import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import hmi.gui.management.TextFieldLabel;

public class RandomInitPanel extends JPanel {

	TextFieldLabel numberStatesTextField = new TextFieldLabel("Nombre d'états");
	TextFieldLabel numberFinalStatesTextField = new TextFieldLabel("Nombre d'états finaux");
	TextFieldLabel numberEpsilonTransTextField = new TextFieldLabel("Nombre d'epsilon transitions");
	TextFieldLabel alphabetTextField = new TextFieldLabel("Alphabet");
	
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
		// In order to have 
	}
}
