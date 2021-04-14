package hmi.gui.panels.operations;

import java.awt.GridLayout;

import javax.swing.JPanel;

import hmi.gui.MainWindow;
import hmi.gui.reusable_elements.TitledPanel;

public class OperationsPanel extends TitledPanel {
	JPanel specificPanel = new JPanel();
	
	ModificationsPanel modificationsPanel = new ModificationsPanel();
	VerficationsPanel verificationsPanel = new VerficationsPanel();
	
	public ModificationsPanel getModificationsPanel() {
		return modificationsPanel;
	}

	public VerficationsPanel getVerificationsPanel() {
		return verificationsPanel;
	}

	public OperationsPanel(MainWindow mainWindow) {
		super(mainWindow);
		initSpecificPanel();
		initPanel("Opérations", specificPanel);
	}
	
	public void initSpecificPanel() {
		specificPanel.setLayout(new GridLayout(1, 0));
		specificPanel.add(modificationsPanel);
		specificPanel.add(verificationsPanel);
	}
}
