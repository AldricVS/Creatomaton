package test.apps;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GraphvizLocationManager extends JFrame {
	
	private JLabel graphvizLocationLabel = new JLabel("Aucun dossier selectionné");
	private JButton updateGraphvizButton = new JButton("Parcourir");
	
	private JLabel defaultResultLocationLabel = new JLabel();
	private JButton updateDefaultResultButton = new JButton("Parcourir");
	
	public GraphvizLocationManager() {
		super("Graphviz location manager");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		initLabels();
		tryLoadIni();
		initButtons();
		
		
		getContentPane().setLayout(new BorderLayout());
		JPanel pan1 = new JPanel();
		JPanel pan2 = new JPanel();
		
		pan1.add(new JLabel("Emplacement de l'application Graphviz : "));
//		pan1.add(graphvizLocationChooser);
		pan1.add(graphvizLocationLabel);
		pan1.add(updateGraphvizButton);
		
		pan2.add(new JLabel("Dossier destination par défaut : "));
//		pan2.add(defaultResultLocationChooser);
		pan2.add(defaultResultLocationLabel);
		pan2.add(updateDefaultResultButton);
		
		getContentPane().add(pan1, BorderLayout.NORTH);
		getContentPane().add(pan2, BorderLayout.SOUTH);
		
		
		setVisible(true);
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	

	private void initButtons() {
		updateGraphvizButton.addActionListener(new ActionUpdateGraphviz());
	}


	private void initLabels() {
		
	}


	private void tryLoadIni() {
		
	}
	
	class ActionUpdateGraphviz implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Emplacement de l'application Graphviz :");
			chooser.setFileFilter(new FileNameExtensionFilter("éxecutables", "exe"));
			chooser.setAcceptAllFileFilterUsed(false);
			
			int answer = chooser.showOpenDialog(getContentPane());
			if(answer == JFileChooser.APPROVE_OPTION) {
				
			}
		}
		
	}


	public static void main(String[] args) {
		new GraphvizLocationManager();
	}

}
