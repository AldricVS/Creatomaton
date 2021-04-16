package hmi.gui.panels.export;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.File;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import process.file.DataFilePaths;

public class ExportFieldsPanel extends JPanel {
	
	private JTextField nameTextField = new JTextField();
	private JTextField outputFolderTextField = new JTextField();
	
	private JPanel filePanel = new JPanel();
	File selectedFolder = new File(DataFilePaths.OUTPUT_PATH);
	
	public ExportFieldsPanel() {
		updateTextField();
		initLayout();
	}
	
	public File getSelectedFolder() {
		return selectedFolder;
	}
	
	public String getNameText() {
		return nameTextField.getText();
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
		checkboxesPanel.add(new JLabel("Nom racine du fichier : "));
		checkboxesPanel.add(Box.createVerticalGlue());
		checkboxesPanel.add(new JLabel("Dossier de destination : "));
		return checkboxesPanel;
	}

	private JPanel createTextFieldsPanel() {
		JPanel textFieldsPanel = new JPanel();
		textFieldsPanel.setLayout(new GridLayout(0, 1));
		textFieldsPanel.add(nameTextField);
		textFieldsPanel.add(Box.createVerticalGlue());
		filePanel.setLayout(new BorderLayout());
		filePanel.add(outputFolderTextField, BorderLayout.CENTER);
		outputFolderTextField.setEditable(false);
		JButton searchButton = new JButton("Parcourir");
		searchButton.addActionListener((actionEvent)-> {
			JFileChooser fileChooser = new JFileChooser();
			if(selectedFolder.exists()) {
				fileChooser.setSelectedFile(selectedFolder);
			}
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int answer = fileChooser.showOpenDialog(ExportFieldsPanel.this);
			if (answer == JFileChooser.APPROVE_OPTION) {
				selectedFolder = fileChooser.getSelectedFile();
				updateTextField();
			}
		});
		filePanel.add(searchButton, BorderLayout.EAST);
		textFieldsPanel.add(filePanel);
		return textFieldsPanel;
	}
	
	private void updateTextField() {
		if(selectedFolder.exists()) {
			outputFolderTextField.setText(selectedFolder.getAbsolutePath());
		}else {
			outputFolderTextField.setText("");
		}
	}
}
