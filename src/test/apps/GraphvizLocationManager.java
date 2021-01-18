package test.apps;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.BoxLayout;
import javax.swing.JTextField;
import java.awt.Component;
import javax.swing.Box;

public class GraphvizLocationManager extends JFrame {
	private final JPanel graphvizPanel = new JPanel();
	private JTextField txtAucunFichierSelectionn;
	private JTextField txtAucunFichierSlectionn;

	public GraphvizLocationManager() {
		super("Graphviz location manager");
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		getContentPane().add(graphvizPanel);
		graphvizPanel.setLayout(new BoxLayout(graphvizPanel, BoxLayout.Y_AXIS));

		JPanel panel_2 = new JPanel();
		graphvizPanel.add(panel_2);

		JLabel lblNewLabel = new JLabel("Emplacement par défaut de Graphviz (dot.exe) : ");
		panel_2.add(lblNewLabel);

		JButton graphvizButton = new JButton("Parcourir");
		panel_2.add(graphvizButton);

		txtAucunFichierSelectionn = new JTextField();
		txtAucunFichierSelectionn.setText("Aucun fichier selectionné");
		graphvizPanel.add(txtAucunFichierSelectionn);
		txtAucunFichierSelectionn.setColumns(10);

		JPanel panel = new JPanel();
		getContentPane().add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JPanel panel_4 = new JPanel();
		panel.add(panel_4);

		JLabel lblNewLabel_1 = new JLabel("Dossier résultat par défaut : ");
		panel_4.add(lblNewLabel_1);

		JButton defautOutputButton = new JButton("Parcourir");
		panel_4.add(defautOutputButton);

		txtAucunFichierSlectionn = new JTextField();
		txtAucunFichierSlectionn.setText("Aucun fichier sélectionné");
		panel.add(txtAucunFichierSlectionn);
		txtAucunFichierSlectionn.setColumns(10);

		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));

		Component horizontalGlue_2 = Box.createHorizontalGlue();
		panel_1.add(horizontalGlue_2);

		JButton saveButton = new JButton("Sauvegarder");
		panel_1.add(saveButton);

		Component horizontalGlue = Box.createHorizontalGlue();
		panel_1.add(horizontalGlue);

		JButton quitButton = new JButton("Quitter sans sauvegarder");
		panel_1.add(quitButton);

		Component horizontalGlue_1 = Box.createHorizontalGlue();
		panel_1.add(horizontalGlue_1);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		initLabels();
		tryLoadIni();
		initButtons();
	}

	private void initButtons() {
	}

	private void initLabels() {

	}

	private void tryLoadIni() {

	}

	public static void main(String[] args) {
		new GraphvizLocationManager();
	}

}
