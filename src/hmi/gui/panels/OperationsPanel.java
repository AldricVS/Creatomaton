package hmi.gui.panels;

import java.awt.Color;

import javax.swing.JPanel;

import hmi.gui.MainWindow;

public class OperationsPanel extends JPanel {
	
	private MainWindow context;

	public OperationsPanel(MainWindow mainWindow) {
		this.context = mainWindow;
		setBackground(Color.RED);
	}
}
