package hmi.gui.management.gui_scanner;

import java.io.IOException;

import data.Automaton;
import exceptions.FileFormatException;
import hmi.AutomatonContainer;
import hmi.gui.management.VerificationResult;
import hmi.gui.panels.operations.ModificationsPanel;
import hmi.gui.panels.operations.OperationsPanel;
import hmi.gui.panels.operations.VerficationsPanel;
import process.AutomatonManager;
import process.file.AutomatonFileHelper;

public class OperationGuiScanner extends PartGuiScanner {

	private OperationsPanel operationsPanel;
	private VerificationResult verificationResult = new VerificationResult();
	
	public OperationGuiScanner(GuiScanner guiScanner, OperationsPanel operationsPanel) {
		super(guiScanner);
		this.operationsPanel = operationsPanel;
	}

	public VerificationResult getVerificationResult() {
		return verificationResult;
	}

	@Override
	public void scanPart() {
		AutomatonContainer automatonContainer = getGuiScanner().getAutomatonContainer();
		handleOperations(automatonContainer);
		VerficationsPanel vPanel = operationsPanel.getVerificationsPanel();
		handleCheckWord(automatonContainer, vPanel);
		handleEquivalentAutomaton(automatonContainer, vPanel);
	}

	/**
	 * @param automatonContainer
	 */
	private void handleOperations(AutomatonContainer automatonContainer) {
		ModificationsPanel mP = operationsPanel.getModificationsPanel();
		if(mP.isDeterministicCheckBoxChecked()) {
			automatonContainer.appendDeterministicAutomaton();
		}
		if(mP.isSynchronizedCheckBoxChecked()) {
			automatonContainer.appendSynchronizedAutomaton();
		}
		if(mP.isMirrorCheckBoxChecked()) {
			automatonContainer.appendMirrorAutomaton();
		}
		if(mP.isMinimalCheckBoxChecked()) {
			automatonContainer.appendMinimalAutomaton();
		}
	}

	/**
	 * @param automatonContainer
	 * @param vPanel
	 */
	private void handleCheckWord(AutomatonContainer automatonContainer, VerficationsPanel vPanel) {
		if(vPanel.isCheckWordBoxChecked()) {
			String word = vPanel.getWordText();
			AutomatonManager automatonManager = new AutomatonManager(automatonContainer.getBaseAutomaton());
			boolean isWordValid = automatonManager.validateAutomatonByDeterminism(word);
			if(isWordValid) {
				verificationResult.setResultWord(String.format("Le mot \"%s\" est reconnu par l'automate.", word));
			}else {
				verificationResult.setResultWord(String.format("Le mot \"%s\" n'est pas reconnu par l'automate.", word));
			}
		}
	}

	/**
	 * @param automatonContainer
	 * @param vPanel
	 */
	private void handleEquivalentAutomaton(AutomatonContainer automatonContainer, VerficationsPanel vPanel) {
		if(vPanel.isEquivalentBoxChecked()) {
			String filename = vPanel.getFilenameText();
			AutomatonFileHelper automatonFileHelper = new AutomatonFileHelper();
			try {
				Automaton secondAutomaton = automatonFileHelper.loadAutomaton(filename);
				AutomatonManager automatonManager = new AutomatonManager(automatonContainer.getBaseAutomaton());
				boolean isEquals = automatonManager.isEqualsByMinimalism(secondAutomaton);
				if(isEquals) {
					verificationResult.setResultEquals("Les deux automates sont équivalents.");
				}else {
					verificationResult.setResultEquals("Les deux automates ne sont pas équivalents.");
				}
			} catch (IllegalArgumentException | IOException exception) {
				verificationResult.setResultEquals("Le fichier n'existe pas ou n'est pas de la bonne extension.");
			} catch (FileFormatException exception) {
				verificationResult.setResultEquals("Le fichier n'est pas correctement écrit, impossible de charger l'automate");
			}
		}
	}
}
