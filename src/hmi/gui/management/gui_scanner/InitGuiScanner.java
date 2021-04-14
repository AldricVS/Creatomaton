package hmi.gui.management.gui_scanner;

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
import process.builders.RandomAutomatonBuilder;
import process.builders.ThompsonAutomatonBuilder;
import process.file.AutomatonFileHelper;
import process.util.FileUtility;

class InitGuiScanner extends PartGuiScanner {

	private InitPanel initPanel;

	public InitGuiScanner(GuiScanner guiScanner, InitPanel initPanel) {
		super(guiScanner);
		this.initPanel = initPanel;
	}

	public void scanPart() {
		InitModes initMode = initPanel.getCurrentMode();
		switch (initMode) {
		case RANDOM:
			handleRandomInit();
			break;
		case THOMPSON:
			handleThompsonInit();
			break;
		case FILE:
			handleFileInit();
			break;
		default:
			throw new IllegalArgumentException("Aucun type d'initialisation détecté, étrange...");
		}
	}

	private void handleRandomInit() {
		RandomInitPanel randomInitPanel = initPanel.getRandomInitPanel();
		if (!randomInitPanel.areFieldsValid()) {
			throw new IllegalArgumentException("L'automate aléatoire ne peut pas être chargé : tous les champs ne sont pas valides");
		}
		RandomAutomatonBuilder randomAutomatonBuilder = new RandomAutomatonBuilder();
		randomAutomatonBuilder.setNumberOfStates(randomInitPanel.getNumberOfStates());
		randomAutomatonBuilder.setNumberOfFinalStates(randomInitPanel.getNumberOfFinalStates());
		randomAutomatonBuilder.setNumberOfEpsilonTransitions(randomInitPanel.getNumberOfEpsilonTransitions());
		randomAutomatonBuilder.setAlphabet(randomInitPanel.getAlphabet());
		getGuiScanner().setAutomatonContainer(new AutomatonContainer(randomAutomatonBuilder.build()));
	}

	private void handleThompsonInit() {
		ThompsonInitPanel thompsonInitPanel = initPanel.getThompsonInitPanel();
		String expression = thompsonInitPanel.getExpression();
		ThompsonAutomatonBuilder thompsonAutomatonBuilder = new ThompsonAutomatonBuilder(expression);
		try {
			Automaton automaton = thompsonAutomatonBuilder.build();
			getGuiScanner().setAutomatonContainer(new AutomatonContainer(automaton));
		} catch (ParseException exception) {
			throw new IllegalArgumentException("L'expression n'est pas valide, impossible de générer l'automate correspondant");
		}
	}

	private void handleFileInit() {
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
			getGuiScanner().setAutomatonContainer(new AutomatonContainer(automaton));
		} catch (FileFormatException exception) {
			throw new IllegalArgumentException("Le fichier n'est pas correctement écrit, impossible de générer l'automate");
		} catch (IOException exception) {
			throw new IllegalArgumentException("Une erreur a eu lieu lors de la lecture du fichier");
		}
	}
}
