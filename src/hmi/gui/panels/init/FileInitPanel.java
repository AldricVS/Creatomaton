package hmi.gui.panels.init;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import process.file.PrefsFileHelper;

public class FileInitPanel extends JPanel {

	private JTextField filenameField = new JTextField();
	private JButton seachButton = new JButton("Parcourir");
	private File selectedFile;
	private PrefsFileHelper prefsFileHelper;

	public FileInitPanel() {
		initFileHelper();
		initTextField();
		initButton();
		initLayout();
	}

	public File getSelectedFile() {
		return selectedFile;
	}

	public void setSelectedFile(File selectedFile) {
		this.selectedFile = selectedFile;
	}

	private void initLayout() {
		setLayout(new GridLayout(0, 1));
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		// Label
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 0;
		panel.add(new JLabel("Emplacement du fichier :"), gridBagConstraints);
		// Text field
		gridBagConstraints.gridx = 1;
		gridBagConstraints.weightx = 2;
		panel.add(filenameField, gridBagConstraints);
		// Button
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = GridBagConstraints.NONE;
		panel.add(seachButton, gridBagConstraints);
		add(panel);
	}

	private void initButton() {
		seachButton.addActionListener((actionEvent) -> {
			JFileChooser fileChooser = new JFileChooser();
			if (prefsFileHelper != null) {
				File folder = new File(prefsFileHelper.getPreference(PrefsFileHelper.DEFAULT_INPUT_FOLDER_KEY));
				fileChooser.setCurrentDirectory(folder);
			}
			FileFilter fileFilter = new FileNameExtensionFilter("fichier .crea", "crea");
			fileChooser.setFileFilter(fileFilter);
			int answer = fileChooser.showOpenDialog(FileInitPanel.this);
			if (answer == JFileChooser.APPROVE_OPTION) {
				selectedFile = fileChooser.getSelectedFile();
				updateTextField();
			}
		});
	}

	/**
	 * 
	 */
	private void initTextField() {
		filenameField.setEditable(false);
		filenameField.setFocusable(false);
		if (prefsFileHelper != null) {
			selectedFile = new File(prefsFileHelper.getPreference(PrefsFileHelper.DEFAULT_INPUT_FOLDER_KEY));
			updateTextField();
		}
	}

	private void updateTextField() {
		filenameField.setText(selectedFile.getAbsolutePath());
	}

	private void initFileHelper() {
		try {
			prefsFileHelper = new PrefsFileHelper();
			prefsFileHelper.createFolders();
		} catch (IOException exception) {
			System.err.println("Cannot create ini file.");
		}
	}
}
