package hmi.gui.panels.operations;

import javax.swing.JPanel;

import hmi.gui.MainWindow;
import hmi.gui.management.TitledPanel;

public class OperationsPanel extends TitledPanel {
	
	
	JPanel specificPanel = new JPanel();
	
	public OperationsPanel(MainWindow mainWindow) {
		super(mainWindow);
		initPanel("Opérations", specificPanel);
	}
}
