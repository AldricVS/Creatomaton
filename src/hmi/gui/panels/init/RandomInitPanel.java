package hmi.gui.panels.init;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

import hmi.gui.management.IntegerDocumentFilter;
import hmi.gui.reusable_elements.TextFieldLabel;
import process.builders.RandomAutomatonBuilder;

public class RandomInitPanel extends JPanel {

	JTextField numberStatesTextField = new JTextField();
	JTextField numberFinalStatesTextField = new JTextField();
	JTextField numberEpsilonTransTextField = new JTextField();
	JTextField alphabetTextField = new JTextField();

	public RandomInitPanel() {
		setLayout(new GridBagLayout());
		initLayout();
		initTextFields();
	}
	
	public String getAlphabet() {
		return alphabetTextField.getText();
	}

	public int getNumberOfStates() {
		return Integer.parseInt(numberStatesTextField.getText().trim());
	}
	
	public int getNumberOfFinalStates() {
		return Integer.parseInt(numberFinalStatesTextField.getText().trim());
	}
	
	public int getNumberOfEpsilonTransitions() {
		return Integer.parseInt(numberEpsilonTransTextField.getText().trim());
	}
	
	private void initLayout() {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.fill = GridBagConstraints.BOTH;

		JPanel labelsPanel = createLabelsPanel();
		add(labelsPanel, gridBagConstraints);

		gridBagConstraints.gridx = 1;
		gridBagConstraints.weightx = 2;

		JPanel fieldsPanel = createFieldsPanel();
		add(fieldsPanel, gridBagConstraints);
	}

	private JPanel createLabelsPanel() {
		JPanel labelsPanel = new JPanel();
		labelsPanel.setLayout(new GridLayout(0, 1));
		labelsPanel.add(new JLabel("Nombre d'états :"));
		labelsPanel.add(createVerticalStrut());
		labelsPanel.add(new JLabel("Nombre d'états finaux :"));
		labelsPanel.add(createVerticalStrut());
		labelsPanel.add(new JLabel("Nombre d'epsilon-transitions :"));
		labelsPanel.add(createVerticalStrut());
		labelsPanel.add(new JLabel("Alphabet :"));
		return labelsPanel;
	}

	private JPanel createFieldsPanel() {
		JPanel fieldsPanel = new JPanel();
		fieldsPanel.setLayout(new GridLayout(0, 1));
		fieldsPanel.add(numberStatesTextField);
		fieldsPanel.add(createVerticalStrut());
		fieldsPanel.add(numberFinalStatesTextField);
		fieldsPanel.add(createVerticalStrut());
		fieldsPanel.add(numberEpsilonTransTextField);
		fieldsPanel.add(createVerticalStrut());
		fieldsPanel.add(alphabetTextField);
		return fieldsPanel;
	}

	private Component createVerticalStrut() {
		return Box.createVerticalStrut(0);
	}

	private void initTextFields() {
		addDocumentFilter(numberStatesTextField, new IntegerDocumentFilter());
		addDocumentFilter(numberFinalStatesTextField, new IntegerDocumentFilter());
		addDocumentFilter(numberEpsilonTransTextField, new IntegerDocumentFilter());
		
		// Retrieve values by default from the RandomAutomatonBuilder itself
		RandomAutomatonBuilder randomAutomatonBuilder = new RandomAutomatonBuilder();
		numberStatesTextField.setText(String.valueOf(randomAutomatonBuilder.getNumberOfInitialStates()));
		numberFinalStatesTextField.setText(String.valueOf(randomAutomatonBuilder.getNumberOfFinalStates()));
		numberEpsilonTransTextField.setText(String.valueOf(randomAutomatonBuilder.getNumberOfEpsilonTransitions()));
		alphabetTextField.setText(randomAutomatonBuilder.getAlphabet());
	}

	private void addDocumentFilter(JTextField textField, DocumentFilter documentFilter) {
		AbstractDocument abstractDocument = (AbstractDocument) textField.getDocument();
		abstractDocument.setDocumentFilter(documentFilter);
	}
}
