package test.apps;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import process.file.PrefsFileHelper;

/**
 * A simple Gui app that will allow user to set the path where the graphiz's
 * dot.exe file is located.
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class SettingsApp extends JFrame {
	private final JPanel graphvizPanel = new JPanel();
	private JFrame context = this;
	private JTextField graphvizTextField;
	private JTextField defaultOutputTextField;
	private PrefsFileHelper prefsFileHelper;

	private boolean isAllChangesSaved = true;

	public SettingsApp() {
		super("Graphviz location manager");
		tryLoadSystemLookAndFeel();

		initLayout();
		tryLoadIni();

		addWindowListener(new WindowCloseAdapter());
	}

	/* =======LAYOUT POSITIONNING======= */

	private void tryLoadSystemLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	private void initLayout() {
		Border border = new EmptyBorder(10, 10, 10, 10);
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		getContentPane().add(graphvizPanel);
		graphvizPanel.setLayout(new BoxLayout(graphvizPanel, BoxLayout.Y_AXIS));
		graphvizPanel.setBorder(border);

		JPanel panel_2 = new JPanel();
		graphvizPanel.add(panel_2);

		JLabel lblNewLabel = new JLabel("Emplacement par défaut de Graphviz (dot.exe) : ");
		panel_2.add(lblNewLabel);

		JButton graphvizButton = new JButton("Parcourir");
		graphvizButton.addActionListener(new ActionSearchGraphviz());
		panel_2.add(graphvizButton);

		graphvizTextField = new JTextField();
		graphvizTextField.setEditable(false);
		graphvizTextField.setText("Aucun fichier selectionné");
		graphvizPanel.add(graphvizTextField);
		graphvizTextField.setColumns(10);

//		JPanel outputPanel = new JPanel();
//		getContentPane().add(outputPanel);
//		outputPanel.setBorder(border);
//		outputPanel.setLayout(new BoxLayout(outputPanel, BoxLayout.Y_AXIS));
//
//		JPanel panel_4 = new JPanel();
//		outputPanel.add(panel_4);
//
//		JLabel lblNewLabel_1 = new JLabel("Dossier résultat par défaut : ");
//		panel_4.add(lblNewLabel_1);
//
//		JButton defaultOutputButton = new JButton("Parcourir");
//		defaultOutputButton.addActionListener(new ActionSearchDefaultOutput());
//		panel_4.add(defaultOutputButton);
//
//		defaultOutputTextField = new JTextField();
//		defaultOutputTextField.setEditable(false);
//		defaultOutputTextField.setText("Aucun fichier sélectionné");
//		outputPanel.add(defaultOutputTextField);
//		defaultOutputTextField.setColumns(10);
//
		JPanel buttonsPanel = new JPanel();
		getContentPane().add(buttonsPanel);
		buttonsPanel.setBorder(border);
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));

		Component horizontalGlue_2 = Box.createHorizontalGlue();
		buttonsPanel.add(horizontalGlue_2);
//
		JButton saveButton = new JButton("Sauvegarder");
		saveButton.addActionListener(new ActionSavePreferences());
		buttonsPanel.add(saveButton);
//
		Component horizontalGlue = Box.createHorizontalGlue();
		buttonsPanel.add(horizontalGlue);

		JButton quitButton = new JButton("Quitter sans sauvegarder");
		quitButton.addActionListener(new ActionQuit());
		buttonsPanel.add(quitButton);

		Component horizontalGlue_1 = Box.createHorizontalGlue();
		buttonsPanel.add(horizontalGlue_1);

		setVisible(true);
		pack();
		setLocationRelativeTo(null);
	}

	private void tryLoadIni() {
		try {
			prefsFileHelper = new PrefsFileHelper();
//			String defaultOutputPath = prefsFileHelper.getPreference(PrefsFileHelper.DEFAULT_OUTPUT_FOLDER_KEY);
			String graphvizPath = prefsFileHelper.getPreference(PrefsFileHelper.GRAPHVIZ_PATH_KEY);
			graphvizTextField.setText(graphvizPath);
//			defaultOutputTextField.setText(defaultOutputPath);
		} catch (IOException e) {
			e.printStackTrace();
			// cannot run the programm if file could not be created or parsed
			System.exit(1);
		}
	}

	/* =======ACTION LISTENERS======= */

	class ActionSearchGraphviz implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			FileFilter fileFilter = new FileNameExtensionFilter("executable", "exe");
			fileChooser.setFileFilter(fileFilter);

			int answer = fileChooser.showOpenDialog(getContentPane());
			if (answer == JFileChooser.APPROVE_OPTION) {
				// just change the JTextField
				File selectedFile = fileChooser.getSelectedFile();
				String absolutePath = selectedFile.getAbsolutePath();
				graphvizTextField.setText(absolutePath);
				isAllChangesSaved = false;
			}
		}

	}

	class ActionSearchDefaultOutput implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			int answer = fileChooser.showOpenDialog(getContentPane());
			if (answer == JFileChooser.APPROVE_OPTION) {
				// just change the JTextField
				File selectedFile = fileChooser.getSelectedFile();
				String absolutePath = selectedFile.getAbsolutePath();
				defaultOutputTextField.setText(absolutePath);
				isAllChangesSaved = false;
			}
		}
	}

	class ActionSavePreferences implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// Save each preferences
			String graphvizNewPath = graphvizTextField.getText();
			prefsFileHelper.changePreference(PrefsFileHelper.GRAPHVIZ_PATH_KEY, graphvizNewPath);

//			String defaultOutputNewPath = defaultOutputTextField.getText();
//			prefsFileHelper.changePreference(PrefsFileHelper.DEFAULT_OUTPUT_FOLDER_KEY, defaultOutputNewPath);

			try {
				prefsFileHelper.saveInFile();
				isAllChangesSaved = true;
				JOptionPane.showMessageDialog(SettingsApp.this, "Sauvegarde effectuée avec succès", "", JOptionPane.INFORMATION_MESSAGE);
			} catch (IOException exception) {
				JOptionPane.showMessageDialog(SettingsApp.this, "Erreur lors de la sauvegarde : " + exception.getMessage(), "", JOptionPane.ERROR_MESSAGE);
				System.err.println("Could not save the preferences : " + exception.getMessage());
			}
		}
	}

	class ActionQuit implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			closeWindow();
		}
	}

	class WindowCloseAdapter extends WindowAdapter {
		@Override
		public void windowClosing(WindowEvent e) {
			closeWindow();
		}
	}

	private void closeWindow() {
		if (isAllChangesSaved) {
			context.dispose();
			System.exit(0);
		}

		int answer = JOptionPane.showConfirmDialog(getContentPane(), "Voulez-vous vraiment quitter ? Les modifications non sauvegardés seront perdues.",
				"Quitter ?", JOptionPane.YES_NO_OPTION);

		if (answer == JOptionPane.YES_OPTION) {
			context.dispose();
			System.exit(0);
		}
	}

	/* =======MAIN======= */

	public static void main(String[] args) {
		new SettingsApp();
	}

}
