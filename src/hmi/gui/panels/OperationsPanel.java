package hmi.gui.panels;

import java.awt.Color;

import javax.swing.JPanel;

import hmi.gui.MainWindow;
import hmi.gui.management.TitledPanel;

public class OperationsPanel extends TitledPanel {

	public OperationsPanel(MainWindow mainWindow) {
		super(mainWindow);
		initPanel("Opérations", new JPanel());
	}
}
