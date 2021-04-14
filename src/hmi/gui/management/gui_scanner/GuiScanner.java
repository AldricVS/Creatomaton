package hmi.gui.management.gui_scanner;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import data.Automaton;
import exceptions.FileFormatException;
import hmi.AutomatonContainer;
import hmi.gui.management.VerificationResult;
import hmi.gui.panels.export.ExportPanel;
import hmi.gui.panels.init.FileInitPanel;
import hmi.gui.panels.init.InitModes;
import hmi.gui.panels.init.InitPanel;
import hmi.gui.panels.init.RandomInitPanel;
import hmi.gui.panels.init.ThompsonInitPanel;
import hmi.gui.panels.operations.ModificationsPanel;
import hmi.gui.panels.operations.OperationsPanel;
import hmi.gui.panels.operations.VerficationsPanel;
import process.builders.RandomAutomatonBuilder;
import process.builders.ThompsonAutomatonBuilder;
import process.file.AutomatonFileHelper;
import process.util.FileUtility;

/**
 * Scan through each gui element and do the appropriate operation depending on
 * it.
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class GuiScanner {

	private AutomatonContainer automatonContainer;

	public AutomatonContainer getAutomatonContainer() {
		return automatonContainer;
	}
	
	public void setAutomatonContainer(AutomatonContainer automatonContainer) {
		this.automatonContainer = automatonContainer;
	}

	public void scanInitPanel(InitPanel initPanel) throws IllegalArgumentException {
		InitGuiScanner guiScanner = new InitGuiScanner(this, initPanel);
		guiScanner.scanPart();
	}
	
	public VerificationResult scanOperationsPanel(OperationsPanel operationsPanel) throws IllegalArgumentException {
		OperationGuiScanner guiScanner = new OperationGuiScanner(this, operationsPanel);
		guiScanner.scanPart();
		return guiScanner.getVerificationResult();
	}

	public void scanExportPanel(ExportPanel exportPanel) throws IllegalArgumentException {
		ExportGuiScanner guiScanner = new ExportGuiScanner(this, exportPanel);
		guiScanner.scanPart();
	}
}
