package hmi.gui.panels.operations;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import hmi.gui.MainWindow;
import hmi.gui.management.TitledPanel;
import hmi.gui.panels.init.InitModes;

public class OperationsPanel extends TitledPanel {
	
	
	JPanel specificPanel = new JPanel();
	
	public OperationsPanel(MainWindow mainWindow) {
		super(mainWindow);
		initPanel("Opérations", specificPanel);
	}
}
