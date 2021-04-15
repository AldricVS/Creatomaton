package hmi.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Optional;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import hmi.gui.management.VerificationResult;
import hmi.gui.management.gui_scanner.GuiScanner;
import hmi.gui.panels.export.ExportPanel;
import hmi.gui.panels.init.InitModes;
import hmi.gui.panels.init.InitPanel;
import hmi.gui.panels.operations.OperationsPanel;

public class MainWindow extends JFrame {

	private static final Dimension WINDOW_MIN_DIMENSION = new Dimension(800, 700);
	private static final Dimension WINDOW_DIMENSION = new Dimension(1280, 720);

	private Dimension initPanelDimension = new Dimension();
	private Dimension operationsPanelDimension = new Dimension();
	private Dimension exportPanelDimension = new Dimension();
	private Dimension validatePanelDimension = new Dimension();

	private InitPanel initPanel = new InitPanel(this);
	private OperationsPanel operationsPanel = new OperationsPanel(this);
	private ExportPanel exportPanel = new ExportPanel(this);
	private JPanel validatePanel = new JPanel();

	public MainWindow() {
		super("Creatomaton");
		initLayout();
		changeDimensions(WINDOW_DIMENSION.width, WINDOW_DIMENSION.height);
		addComponentListener(new ResizeListener());
		pack();
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	private void changeDimensions(int windowWidth, int windowHeight) {
		initPanelDimension.setSize(windowWidth, 1 * windowHeight / 3);
		operationsPanelDimension.setSize(windowWidth, 1 * windowHeight / 3);
		exportPanelDimension.setSize(windowWidth, 1 * windowHeight / 6);
		validatePanelDimension.setSize(windowWidth, 1 * windowHeight / 6);

		initPanel.setPreferredSize(initPanelDimension);
		operationsPanel.setPreferredSize(operationsPanelDimension);
		exportPanel.setPreferredSize(exportPanelDimension);
		exportPanel.setPreferredSize(validatePanelDimension);

		initPanel.revalidate();
		operationsPanel.revalidate();
		exportPanel.revalidate();
		validatePanel.revalidate();
	}

	private void initLayout() {
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setPreferredSize(WINDOW_DIMENSION);
		setMinimumSize(WINDOW_MIN_DIMENSION);
		initValidatePanel();
		getContentPane().add(initPanel);
		getContentPane().add(operationsPanel);
		getContentPane().add(exportPanel);
		getContentPane().add(validatePanel);
	}

	private void initValidatePanel() {
		JButton validateButton = new JButton("Valider");
		validateButton.addActionListener(new ActionValidate());
		validatePanel.add(validateButton);
	}

	class ResizeListener extends ComponentAdapter {
		@Override
		public void componentResized(ComponentEvent componentEvent) {
			super.componentMoved(componentEvent);
			int frameWidth = getContentPane().getWidth();
			int frameHeight = getContentPane().getHeight();
			changeDimensions(frameWidth, frameHeight);
		}
	}

	class ActionValidate implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			GuiScanner guiScanner = new GuiScanner();
			boolean isInitCompleted = handleInitPart(guiScanner);

			if (isInitCompleted) {
				handleOperationsPart(guiScanner);
				handleExportPart(guiScanner);
			}

		}

		/**
		 * @param guiScanner
		 * @return
		 */
		private boolean handleInitPart(GuiScanner guiScanner) {
			try {
				guiScanner.scanInitPanel(initPanel);
				return true;
			} catch (IllegalArgumentException exception) {
				JOptionPane.showMessageDialog(MainWindow.this, exception.getMessage(), "Initialisation : erreur", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		/**
		 * @param guiScanner
		 */
		private void handleOperationsPart(GuiScanner guiScanner) {
			VerificationResult verifData = guiScanner.scanOperationsPanel(operationsPanel);
			Optional<String> resultEquals = verifData.getResultEquals();
			Optional<String> resultWord = verifData.getResultWord();
			if (resultEquals.isPresent() || resultWord.isPresent()) {
				String message = "";
				if (resultWord.isPresent()) {
					message += resultWord.get() + "\n";
				}
				if (resultEquals.isPresent()) {
					message += resultEquals.get() + "\n";
				}
				JOptionPane.showMessageDialog(MainWindow.this, message, "Résultat", JOptionPane.INFORMATION_MESSAGE);
			}
		}

		/**
		 * @param guiScanner
		 */
		private void handleExportPart(GuiScanner guiScanner) {
			if (!exportPanel.isNoExportButtonChecked()) {
				try {
					guiScanner.scanExportPanel(exportPanel);
					JOptionPane.showMessageDialog(MainWindow.this, "Exportation effectuée avec succès", "", JOptionPane.INFORMATION_MESSAGE);
				} catch (IllegalArgumentException exception) {
					JOptionPane.showMessageDialog(MainWindow.this, exception.getMessage(), "Exportation : erreur", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
}
