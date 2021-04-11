package hmi.gui.panels;

import java.awt.Color;

import javax.swing.JPanel;

import hmi.gui.MainWindow;

public class InitPanel extends JPanel {

	private MainWindow context;
	
	public InitPanel(MainWindow mainWindow) {
		this.context = mainWindow;
		setBackground(Color.BLACK);
	}
	
}
