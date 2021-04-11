package hmi.gui.management;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import hmi.gui.MainWindow;

public class TitledPanel extends JPanel {
	
	private static final int PADDING = 5;

	private JPanel panel;
	
	private MainWindow context;
	
	public TitledPanel(MainWindow context) {
		this.context = context;
	}

	public void initPanel(String title, JPanel panel) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JPanel titlePanel = new JPanel(new BorderLayout());
		JLabel titleLabel = new JLabel(title, SwingUtilities.LEFT);
		titlePanel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
		titlePanel.add(titleLabel, BorderLayout.NORTH);
		add(titlePanel);
		this.panel = panel;
		add(panel);
	}

	public JPanel getPanel() {
		return panel;
	}

	public MainWindow getContext() {
		return context;
	}
}
