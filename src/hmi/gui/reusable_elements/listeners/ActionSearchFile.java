package hmi.gui.reusable_elements.listeners;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import hmi.gui.panels.init.FileInitPanel;
import process.file.PrefsFileHelper;

public class ActionSearchFile implements ActionListener {

	private JTextField textField;
	private File currentDirectory;
	private Component parent;
	
	public ActionSearchFile(Component parent, JTextField textField, File currentDirectory) {
		this.parent = parent;
		this.textField = textField;
		this.currentDirectory = currentDirectory;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser();
		if (currentDirectory != null) {
			fileChooser.setCurrentDirectory(currentDirectory);
		}
		FileFilter fileFilter = new FileNameExtensionFilter("fichier .crea", "crea");
		fileChooser.setFileFilter(fileFilter);
		int answer = fileChooser.showOpenDialog(parent);
		if (answer == JFileChooser.APPROVE_OPTION) {
			textField.setText(fileChooser.getSelectedFile().getAbsolutePath());
		}
	}

}
