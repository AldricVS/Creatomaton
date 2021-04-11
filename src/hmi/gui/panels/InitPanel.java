package hmi.gui.panels;

import java.awt.Color;

import javax.swing.JPanel;

import hmi.gui.MainWindow;
import hmi.gui.management.TitledPanel;

public class InitPanel extends TitledPanel {
	
	public InitPanel(MainWindow mainWindow) {
		super(mainWindow);
		JPanel panel = new JPanel();
		super.initPanel("Initialisation", panel);
	}
}
