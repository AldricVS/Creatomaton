package hmi.gui.panels.init;

import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import hmi.gui.MainWindow;
import hmi.gui.reusable_elements.TitledPanel;

public class InitPanel extends TitledPanel {
	
	private ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton randomRadioButton = new JRadioButton("Aléatoirement");
	private JRadioButton thompsonRadioButton = new JRadioButton("Via une expression régulière");
	private JRadioButton fileRadioButton = new JRadioButton("Via un fichier (\".crea\")");
	private JRadioButton buttonSelected;
	
	private JPanel specificPanel;
	
	private JPanel contentPanel;
	private InitModes currentMode = InitModes.RANDOM;
	private CardLayout cardLayout = new CardLayout();
	
	private RandomInitPanel randomInitPanel = new RandomInitPanel();
	private ThompsonInitPanel thompsonInitPanel = new ThompsonInitPanel();
	private FileInitPanel fileInitPanel = new FileInitPanel();

	public InitPanel(MainWindow mainWindow) {
		super(mainWindow);
		initSpecificPanel();
		super.initPanel("Initialisation", specificPanel);
	}
	
	public InitModes getCurrentMode() {
		return currentMode;
	}

	public RandomInitPanel getRandomInitPanel() {
		return randomInitPanel;
	}

	public ThompsonInitPanel getThompsonInitPanel() {
		return thompsonInitPanel;
	}

	public FileInitPanel getFileInitPanel() {
		return fileInitPanel;
	}

	private void initSpecificPanel() {
		specificPanel = new JPanel();
		specificPanel.setLayout(new GridLayout(1, 1));
		JPanel radioPanel = createRadioPanel();
		specificPanel.add(radioPanel);
		
		contentPanel = new JPanel();
		contentPanel.setLayout(cardLayout);
		contentPanel.add(randomInitPanel, InitModes.RANDOM.name());
		contentPanel.add(thompsonInitPanel, InitModes.THOMPSON.name());
		contentPanel.add(fileInitPanel, InitModes.FILE.name());
		changeContentPanel(currentMode);
		
		specificPanel.add(contentPanel);
	}

	private void changeContentPanel(InitModes mode) {
		cardLayout.show(contentPanel, mode.name());
		currentMode = mode;
	}

	private JPanel createRadioPanel() {
		JPanel radioPanel = new JPanel();
		RadioButtonListener radioButtonListener = new RadioButtonListener();
		radioPanel.setLayout(new GridLayout(0, 1));
		radioPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
		
		buttonGroup.add(randomRadioButton);
		radioPanel.add(randomRadioButton);
		randomRadioButton.addActionListener(radioButtonListener);
		
		buttonGroup.add(thompsonRadioButton);
		radioPanel.add(thompsonRadioButton);
		thompsonRadioButton.addActionListener(radioButtonListener);
		
		buttonGroup.add(fileRadioButton);
		radioPanel.add(fileRadioButton);
		fileRadioButton.addActionListener(radioButtonListener);
		
		buttonGroup.clearSelection();
		randomRadioButton.setSelected(true);
		buttonSelected = randomRadioButton;
		return radioPanel;
	}
	
	class RadioButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			JRadioButton radioButton = (JRadioButton) e.getSource();
			if(buttonSelected != radioButton) {
				buttonSelected = radioButton;
				if(radioButton == randomRadioButton) {
					changeContentPanel(InitModes.RANDOM);
				}else if(radioButton == thompsonRadioButton) {
					changeContentPanel(InitModes.THOMPSON);
				}else if(radioButton == fileRadioButton){
					changeContentPanel(InitModes.FILE);
				}
			}
		}
	}
}
