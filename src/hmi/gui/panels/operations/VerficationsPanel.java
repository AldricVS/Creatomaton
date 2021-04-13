package hmi.gui.panels.operations;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import hmi.gui.reusable_elements.listeners.ActionSearchFile;
import process.file.DataFilePaths;

public class VerficationsPanel extends JPanel {

	private JPanel filePanel = new JPanel();

	private JCheckBox checkWordCheckBox = new JCheckBox("Vérifier un mot");
	JCheckBox equivalentCheckBox = new JCheckBox("Équivalent ?");

	JTextField wordTextField = new JTextField();
	JTextField filenameTextField = new JTextField();

	public VerficationsPanel() {
		initLayout();
		setComponentEnabled(wordTextField, false);
		setComponentEnabled(filePanel, false);
	}
	
	private void initLayout() {
		setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;

		JPanel checkboxesPanel = createCheckboxesPanel();
		add(checkboxesPanel, gridBagConstraints);

		gridBagConstraints.gridx = 1;
		gridBagConstraints.weightx = 2;
		JPanel textFieldsPanel = createTextFieldsPanel();
		add(textFieldsPanel, gridBagConstraints);
	}

	private JPanel createCheckboxesPanel() {
		JPanel checkboxesPanel = new JPanel();
		checkboxesPanel.setLayout(new GridLayout(0, 1));
		checkWordCheckBox.addActionListener(new CheckboxListener(wordTextField));
		checkboxesPanel.add(checkWordCheckBox);
		checkboxesPanel.add(Box.createVerticalGlue());
		equivalentCheckBox.addActionListener(new CheckboxListener(filePanel));
		checkboxesPanel.add(equivalentCheckBox);
		return checkboxesPanel;
	}

	private JPanel createTextFieldsPanel() {
		JPanel textFieldsPanel = new JPanel();
		textFieldsPanel.setLayout(new GridLayout(0, 1));
		textFieldsPanel.add(wordTextField);
		textFieldsPanel.add(Box.createVerticalGlue());
		filePanel.setLayout(new BorderLayout());
		filePanel.add(filenameTextField, BorderLayout.CENTER);
		filenameTextField.setEditable(false);
		JButton searchButton = new JButton("Parcourir");
		searchButton.addActionListener(new ActionSearchFile(this, filenameTextField, new File(DataFilePaths.INPUT_PATH)));
		filePanel.add(searchButton, BorderLayout.EAST);
		textFieldsPanel.add(filePanel);
		return textFieldsPanel;
	}

	class CheckboxListener implements ActionListener {
		private Component associatedComponent;

		public CheckboxListener(Component associatedComponent) {
			this.associatedComponent = associatedComponent;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JCheckBox checkBox = (JCheckBox) e.getSource();
			setComponentEnabled(associatedComponent, checkBox.isSelected());
		}
	}

	private void setComponentEnabled(Component component, boolean isEnabled) {
		component.setEnabled(isEnabled);
		if (component instanceof Container) {
			Container container = (Container) component;
			for(Component comp : container.getComponents()) {
				setComponentEnabled(comp, isEnabled);
			}
		}
	}
}
