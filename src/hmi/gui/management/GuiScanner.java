package hmi.gui.management;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import data.Automaton;
import exceptions.FileFormatException;
import hmi.AutomatonContainer;
import hmi.gui.panels.init.FileInitPanel;
import hmi.gui.panels.init.InitModes;
import hmi.gui.panels.init.InitPanel;
import hmi.gui.panels.init.RandomInitPanel;
import hmi.gui.panels.init.ThompsonInitPanel;
import hmi.gui.panels.operations.OperationsPanel;
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

	public GuiScanner() {

	}

	public void scanInitPanel(InitPanel initPanel) throws IllegalArgumentException {
		InitModes initMode = initPanel.getCurrentMode();
		switch (initMode) {
		case RANDOM:
			handleRandomInit(initPanel);
			break;
		case THOMPSON:
			handleThompsonInit(initPanel);
			break;
		case FILE:
			handleFileInit(initPanel);
			break;
		default:
			throw new IllegalArgumentException("Aucun type d'initialisation détecté, étrange...");
		}
	}
	
	public void scanOperationsPanel(OperationsPanel operationsPanel) throws IllegalArgumentException {
		// TODO : faire une op par case cochée, + vérifier automate si besoin
	}

	public void scanExportPanel(OperationsPanel operationsPanel) throws IllegalArgumentException {
		// TODO : trouver la méthode d'export et les args dépendant de cette méthode
	}

	private void handleRandomInit(InitPanel initPanel) {
		RandomInitPanel randomInitPanel = initPanel.getRandomInitPanel();
		if (!randomInitPanel.areFieldsValid()) {
			throw new IllegalArgumentException("L'automate aléatoire ne peut pas être chargé : tous les champs ne sont pas valides");
		}
		RandomAutomatonBuilder randomAutomatonBuilder = new RandomAutomatonBuilder();
		randomAutomatonBuilder.setNumberOfStates(randomInitPanel.getNumberOfStates());
		randomAutomatonBuilder.setNumberOfFinalStates(randomInitPanel.getNumberOfFinalStates());
		randomAutomatonBuilder.setNumberOfEpsilonTransitions(randomInitPanel.getNumberOfEpsilonTransitions());
		randomAutomatonBuilder.setAlphabet(randomInitPanel.getAlphabet());
		automatonContainer = new AutomatonContainer(randomAutomatonBuilder.build());
	}

	private void handleThompsonInit(InitPanel initPanel) {
		ThompsonInitPanel thompsonInitPanel = initPanel.getThompsonInitPanel();
		String expression = thompsonInitPanel.getExpression();
		ThompsonAutomatonBuilder thompsonAutomatonBuilder = new ThompsonAutomatonBuilder(expression);
		try {
			Automaton automaton = thompsonAutomatonBuilder.build();
			automatonContainer = new AutomatonContainer(automaton);
		} catch (ParseException exception) {
			throw new IllegalArgumentException("L'expression n'est pas valide, impossible de générer l'automate correspondant");
		}
	}

	private void handleFileInit(InitPanel initPanel) {
		FileInitPanel fileInitPanel = initPanel.getFileInitPanel();
		File selectedFile = fileInitPanel.getSelectedFile();
		String filename = selectedFile.getAbsolutePath();
		if (!selectedFile.isFile()) {
			throw new IllegalArgumentException("Aucun fichier selectionné");
		}
		if (!FileUtility.isFileWithGoodExtension(filename, "crea")) {
			throw new IllegalArgumentException("Le fichier n'a pas l'extension \".crea\"");
		}
		AutomatonFileHelper automatonFileHelper = new AutomatonFileHelper();
		try {
			Automaton automaton = automatonFileHelper.loadAutomaton(filename);
			automatonContainer = new AutomatonContainer(automaton);
		} catch (FileFormatException exception) {
			throw new IllegalArgumentException("Le fichier n'est pas correctement écrit, impossible de générer l'automate");
		} catch (IOException exception) {
			throw new IllegalArgumentException("Une erreur a eu lieu lors de la lecture du fichier");
		}
	}
}
