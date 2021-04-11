package hmi.gui.panels;

import javax.swing.JPanel;

import hmi.gui.MainWindow;
import hmi.gui.management.TitledPanel;

public class ExportPanel extends TitledPanel {
	
	public ExportPanel(MainWindow mainWindow) {
		super(mainWindow);
		initPanel("Export", new JPanel());
	}
}
