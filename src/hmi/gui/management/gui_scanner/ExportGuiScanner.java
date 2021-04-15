package hmi.gui.management.gui_scanner;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import data.Automaton;
import hmi.gui.panels.export.ExportPanel;
import process.file.AutomatonFileHelper;
import process.file.ImageCreator;

public class ExportGuiScanner extends PartGuiScanner {

	private ExportPanel exportPanel;

	public ExportGuiScanner(GuiScanner guiScanner, ExportPanel exportPanel) {
		super(guiScanner);
		this.exportPanel = exportPanel;
	}

	@Override
	public void scanPart() throws IllegalArgumentException {
		if (!exportPanel.isNoExportButtonChecked()) {
			File selectedFolder = exportPanel.getSelectedFolder();
			String name = exportPanel.getNameText();
			if (name.trim().isEmpty() || !selectedFolder.isDirectory()) {
				throw new IllegalArgumentException("Le chemin de destination n'est pas un dossier ou le nom n'a pas été renseigné.");
			}
			Map<String, Automaton> automatons = getGuiScanner().getAutomatonContainer().getAutomatons();
			if (exportPanel.isFileButtonChecked()) {
				handleFileExport(selectedFolder, name, automatons);
			} else {
				handleImageExport(selectedFolder, name, automatons);
			}
		}
	}

	/**
	 * @param selectedFolder
	 * @param name
	 * @param automatons
	 */
	private void handleFileExport(File selectedFolder, String name, Map<String, Automaton> automatons) {
		AutomatonFileHelper automatonFileHelper = new AutomatonFileHelper(selectedFolder.getAbsolutePath());
		try {
			for (Entry<String, Automaton> entry : automatons.entrySet()) {
				Automaton automaton = entry.getValue();
				String specificName = entry.getKey();
				automatonFileHelper.saveAutomaton(automaton, name + "_" + specificName);
			}
		} catch (IOException exception) {
			throw new IllegalArgumentException("Une erreur a eu lieu lors de la création des fichiers.");
		}
	}

	/**
	 * @param selectedFolder
	 * @param name
	 * @param automatons
	 */
	private void handleImageExport(File selectedFolder, String name, Map<String, Automaton> automatons) {
		try {
			for (Entry<String, Automaton> entry : automatons.entrySet()) {
				Automaton automaton = entry.getValue();
				String specificName = entry.getKey();
				String filename = selectedFolder.getAbsolutePath() + "/" + name + "_" + specificName;
				ImageCreator imageCreator = new ImageCreator(automaton, filename);
				imageCreator.createImageFile();
			}
		} catch (IOException exception) {
			throw new IllegalArgumentException("Une erreur a eu lieu lors de la création des images.");
		}
	}

}
