package hmi.gui.management;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import hmi.gui.MainWindow;

public class TitledPanel extends JPanel {

	private static final int PADDING = 3;
	private static final Font TITLE_FONT = new Font("Sans Serif", Font.PLAIN, 12);

	private MainWindow context;

	public TitledPanel(MainWindow context) {
		this.context = context;
	}

	public void initPanel(String title, JPanel panel) {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JPanel titlePanel = new JPanel(new BorderLayout());
		JLabel titleLabel = new JLabel(title, SwingUtilities.LEFT);
		titleLabel.setFont(TITLE_FONT);
		titlePanel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
		titlePanel.setPreferredSize(new Dimension(titlePanel.getWidth(), titleLabel.getFont().getSize() + PADDING << 1));
		titlePanel.add(titleLabel, BorderLayout.CENTER);
		add(titlePanel, BorderLayout.NORTH);
		add(panel, BorderLayout.CENTER);
	}

	public MainWindow getContext() {
		return context;
	}
}
