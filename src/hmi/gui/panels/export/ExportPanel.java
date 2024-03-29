package hmi.gui.panels.export;

import java.awt.GridLayout;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import hmi.gui.MainWindow;
import hmi.gui.reusable_elements.TitledPanel;

public class ExportPanel extends TitledPanel {
	
	private JPanel specificPanel = new JPanel();
	
	private ButtonGroup radioButtonGroup = new ButtonGroup();
	private JRadioButton noExportRadioButton = new JRadioButton("Pas d'export");
	private JRadioButton imageRadioButton = new JRadioButton("Image (.jpg)");
	private JRadioButton fileRadioButton = new JRadioButton("Fichier (.crea)");
	
	private ExportFieldsPanel exportFieldsPanel = new ExportFieldsPanel();
	
	public ExportPanel(MainWindow mainWindow) {
		super(mainWindow);
		initButtonGroup();
		initSpecificPanel();
		initPanel("Export", specificPanel);
	}

	public ExportFieldsPanel getExportFieldsPanel() {
		return exportFieldsPanel;
	}

	public boolean isImageButtonChecked() {
		return imageRadioButton.isSelected();
	}
	
	public boolean isFileButtonChecked() {
		return fileRadioButton.isSelected();
	}
	
	public boolean isNoExportButtonChecked() {
		return noExportRadioButton.isSelected();
	}
	
	public File getSelectedFolder() {
		return exportFieldsPanel.getSelectedFolder();
	}
	
	public String getNameText() {
		return exportFieldsPanel.getNameText();
	}
	
	private void initButtonGroup() {
		radioButtonGroup.add(imageRadioButton);
		radioButtonGroup.add(fileRadioButton);
		radioButtonGroup.add(noExportRadioButton);
		imageRadioButton.setSelected(true);
	}

	private void initSpecificPanel() {
		specificPanel.setLayout(new GridLayout(1, 1));
		JPanel radioPanel = createRadioButtonPanel();
		radioPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
		specificPanel.add(radioPanel);
		specificPanel.add(exportFieldsPanel);
	}
	
	private JPanel createRadioButtonPanel() {
		JPanel radioPanel = new JPanel();
		radioPanel.setLayout(new GridLayout(0, 1));
		radioPanel.add(imageRadioButton);
		radioPanel.add(fileRadioButton);
		radioPanel.add(noExportRadioButton);
		return radioPanel;
	}
}
