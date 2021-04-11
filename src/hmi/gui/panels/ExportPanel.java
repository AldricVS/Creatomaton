package hmi.gui.panels;

import java.awt.Color;

import javax.swing.JPanel;

import hmi.gui.MainWindow;

public class ExportPanel extends JPanel {
	
	private MainWindow context;

	public ExportPanel(MainWindow mainWindow) {
		this.context = mainWindow;
		setBackground(Color.BLUE);
	}
}
