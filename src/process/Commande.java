package process;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import data.Automaton;
import exceptions.FileFormatException;
import process.builders.AutomatonBuilder;
import process.builders.RandomAutomatonBuilder;
import process.builders.ThompsonAutomatonBuilder;
import process.file.AutomatonFileHelper;
import process.file.ImageCreator;

/**
 * Main class used for handling Command Line Argument
 * 
 * @author Cholé Gateau
 */
public class Commande {

	private static final String CMD_ALL = "all";
	private static final String CMD_DETER = "det";
	private static final String CMD_EQUIVALENCE = "equi";
	private static final String CMD_FICHIER = "file";
	private static final String CMD_GRAPHVIZ = "graphviz";
	private static final String CMD_HELP = "help";
	private static final String CMD_LOAD = "load";
	private static final String CMD_MINI = "mini";
	private static final String CMD_MIRROR = "mir";
	private static final String CMD_RANDOM = "random";
	private static final String CMD_SYNC = "sync";
	private static final String CMD_THOMPSON = "thompson";
	private static final String CMD_VALIDATION = "val";

	private static final String CMD_BASE_AUTOMATON = "base";

	private Options options = new Options();;
	private HashMap<String, Automaton> mapAutomaton = new HashMap<>();

	/**
	 * Add all defined argument that can be read from the Command Line.
	 */
	public Commande() {
		// Creation du groupe d'option
		OptionGroup group = new OptionGroup();

		Option load = new Option("L", CMD_LOAD, true, "charger un fichier .crea");
		load.setType(AutomatonManager.class);
		Option random = new Option("R", CMD_RANDOM, true, "creer un automate aléatoire");
		Option thompson = new Option("T", CMD_THOMPSON, true, "creer un automate selon une expression");

		// Ajout des options exclusives
		group.addOption(load);
		group.addOption(random);
		group.addOption(thompson);

		// Rends le groupe obligatoire
		group.setRequired(true);
		// Ajout le groupe dans le conteneur Options
		options.addOptionGroup(group);

		// Ajout des options complémentaires
		Option sync = new Option("S", CMD_SYNC, false, "Créer un automate synchronisé");
		options.addOption(sync);

		Option det = new Option("D", CMD_DETER, false, "Créer un automate déterministe");
		options.addOption(det);

		Option mir = new Option("M", CMD_MIRROR, false, "Créer un miroir de l'automate");
		options.addOption(mir);

		Option mini = new Option("m", CMD_MINI, false, "Créer un automate minimal");
		options.addOption(mini);

		Option all = new Option("A", CMD_ALL, false, "Crée les Automates synchronisé, determinisé, miroir et minimal");
		options.addOption(all);

		Option val = new Option("V", CMD_VALIDATION, true, "Vérifie si l'automate valide le mot donné");
		val.setType(AutomatonManager.class);
		options.addOption(val);

		Option equi = new Option("E", CMD_EQUIVALENCE, true,
				"Vérifie l'équivalence avec un autre automate dans un fichier .crea");
		options.addOption(equi);

		// Creation du groupe d'option
		OptionGroup group2 = new OptionGroup();
		Option graphviz = new Option("G", CMD_GRAPHVIZ, true, "Export en image vers le nom du fichier donné");
		Option file = new Option("F", CMD_FICHIER, true, "Export en fichier vers le nom du fichier donné");
		// Ajout des options exclusives
		group2.addOption(graphviz);
		group2.addOption(file);
		// Ajout du groupe dans le conteneur Options
		options.addOptionGroup(group2);

	}

	public void traitement(String[] argument) {
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, argument, false);
		} catch (ParseException e) {
			System.err.println("Parsing échoué : " + e.getMessage());
			// Affichage de l'aide
			System.err.println("Affichage de l'aide :");
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("Commandes utilisables", options);
			return;
		}
		if (cmd.hasOption(CMD_HELP)) {
			// Affichage de l'aide
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("Creatomaton", options);
			// vu qu'on affiche l'aide, pas la peine de faire le reste ?
			return;
		}

		Automaton automaton = traitementAutomaton(cmd);
		if (automaton != null) {
			mapAutomaton.put(CMD_BASE_AUTOMATON, automaton);
			traitementAlgo(cmd, automaton);
			extraction(cmd);
		} else {
			System.err.println("L'automate n'a pas pu etre chargé.");
			System.out.println("il faut d'abord utiliser soit random soit load soit thompson");
		}

	}

	/**
	 * Create the base Automaton depending on the method specified
	 * 
	 * @param cmd CommandLine Argument that specifie the method to use
	 * @return the newly formed Automaton, or null if no method has been selected
	 */
	public Automaton traitementAutomaton(CommandLine cmd) {
		Automaton automaton = null;
		if (cmd.hasOption(CMD_LOAD)) {
			String fichier = cmd.getOptionValue(CMD_LOAD);
			automaton = load(fichier);
		} else if (cmd.hasOption(CMD_RANDOM)) {
			String expression = cmd.getOptionValue(CMD_RANDOM);
			automaton = random(expression);
		} else if (cmd.hasOption(CMD_THOMPSON)) {
			String expression = cmd.getOptionValue(CMD_THOMPSON);
			automaton = thompson(expression);
		}
		return automaton;
	}

	/**
	 * Check from the Command Line all argument given. Will put the new Automaton
	 * inside a Map.
	 * 
	 * @param cmd       CommandLine that will given all argument.
	 * @param automaton the base Automaton to apply change
	 */
	public void traitementAlgo(CommandLine cmd, Automaton automaton) {
		// newAutomaton that will receive all generated Automaton
		Automaton newAutomaton = null;
		// Check argument ALL
		if (cmd.hasOption(CMD_ALL)) {
			newAutomaton = synchronisation(automaton);
			mapAutomaton.put(CMD_SYNC, newAutomaton);

			newAutomaton = determinisation(automaton);
			mapAutomaton.put(CMD_DETER, newAutomaton);

			newAutomaton = minimisation(automaton);
			mapAutomaton.put(CMD_MINI, newAutomaton);

			newAutomaton = miroir(automaton);
			mapAutomaton.put(CMD_MIRROR, newAutomaton);

		} else {
			// check argument SYNC
			if (cmd.hasOption(CMD_SYNC)) {
				newAutomaton = synchronisation(automaton);
				mapAutomaton.put(CMD_SYNC, newAutomaton);
				System.out.println("Création de l'automate synchronisé terminé");
			}
			// check argument DET
			if (cmd.hasOption(CMD_DETER)) {
				newAutomaton = determinisation(automaton);
				mapAutomaton.put(CMD_DETER, newAutomaton);
				System.out.println("Création de l'automate déterministe terminé");
			}
			// check argument MINI
			if (cmd.hasOption(CMD_MINI)) {
				newAutomaton = minimisation(automaton);
				mapAutomaton.put(CMD_MINI, newAutomaton);
				System.out.println("Création de l'automate minimal terminé");
			}
			// check argument MIR
			if (cmd.hasOption(CMD_MIRROR)) {
				newAutomaton = miroir(automaton);
				mapAutomaton.put(CMD_MIRROR, newAutomaton);
				System.out.println("Création de l'automate miroir terminé");
			}
		}
		// check argument EQUI
		if (cmd.hasOption(CMD_EQUIVALENCE)) {
			String fileName = cmd.getOptionValue(CMD_EQUIVALENCE);
			Automaton automatonFromFile = load(fileName);
			AutomatonManager manager = new AutomatonManager(automaton);
			boolean isEquals = manager.isEqualsByMinimalism(automatonFromFile);
			if (isEquals) {
				System.out.println("Les deux automates sont équivalent.");
			} else {
				System.out.println("Les deux ne automates sont pas équivalent.");

			}
		}
		// check argument VAL
		if (cmd.hasOption(CMD_VALIDATION)) {
			String wordToValidate = cmd.getOptionValue(CMD_VALIDATION);
			AutomatonManager manager = new AutomatonManager(automaton);
			boolean isValid = manager.validateAutomatonByDeterminism(wordToValidate);
			if (isValid) {
				System.out.println("L'automate accepte le mot " + wordToValidate);
			} else {
				System.out.println("L'automate ne valide pas le mot " + wordToValidate);
			}
		}

	}

	/**
	 * Create a random Automaton based on the given expression
	 * 
	 * @param expression the expression used to create the Automaton
	 * @return the newly generated Automaton
	 */
	public Automaton random(String expression) {
		RandomAutomatonBuilder automatonRandom = new RandomAutomatonBuilder();
		return automatonRandom.build();
	}

	/**
	 * Load the Automaton from the given file
	 * 
	 * @param fileName the path to the file
	 * @return the Automaton created based on the given file
	 */
	public Automaton load(String fileName) {
		AutomatonFileHelper automatonFileHelper = new AutomatonFileHelper();
		Automaton automaton = null;
		try {
			automaton = automatonFileHelper.loadAutomaton(fileName);
		} catch (IllegalArgumentException | FileFormatException | IOException e) {
			e.printStackTrace();
		}
		return automaton;

	}

	/**
	 * Create an Automaton based on the given expression
	 * 
	 * @param expression the expression to parse
	 * @return the newly created Automaton that will accept word from the expression
	 */
	public Automaton thompson(String expression) {
		ThompsonAutomatonBuilder automatonThompson = new ThompsonAutomatonBuilder(expression);
		Automaton automaton = null;
		try {
			automaton = automatonThompson.build();
		} catch (java.text.ParseException e) {
			System.err.println(e.getMessage());
		}
		return automaton;
	}

	/**
	 * Launch the minimisation on the given Automaton
	 * 
	 * @param automaton Automaton to be minimised
	 * @return the minimal Automaton
	 */
	public Automaton minimisation(Automaton automaton) {
		AutomatonBuilder builder = new AutomatonBuilder(automaton);
		return builder.buildMinimalAutomaton();

	}

	/**
	 * Launch the determinisation on the given Automaton
	 * 
	 * @param automaton Automaton to be determinised
	 * @return the determinist Automaton
	 */
	public Automaton determinisation(Automaton automaton) {
		AutomatonBuilder builder = new AutomatonBuilder(automaton);
		return builder.buildDeterministicAutomaton();

	}

	/**
	 * Reverse the given Automaton
	 * 
	 * @param automaton Automaton to be reversed
	 * @return the mirror Automaton
	 */
	public Automaton miroir(Automaton automaton) {
		AutomatonBuilder builder = new AutomatonBuilder(automaton);
		return builder.buildMirrorAutomaton();
	}

	/**
	 * Launch the synchronisation on the given Automaton
	 * 
	 * @param automaton Automaton to be synchronised
	 * @return the synchronised Automaton
	 */
	public Automaton synchronisation(Automaton automaton) {
		AutomatonBuilder builder = new AutomatonBuilder(automaton);
		return builder.buildSynchronizedAutomaton();
	}

	/**
	 * Extract all generated Automaton to an Image or a crea File depend on given
	 * the argument.
	 * 
	 * @param cmd CommandLine that will check which argument has been given
	 */
	public void extraction(CommandLine cmd) {
		if (cmd.hasOption(CMD_GRAPHVIZ)) {
			String nameFile = cmd.getOptionValue(CMD_GRAPHVIZ);
			for (Entry<String, Automaton> entry : mapAutomaton.entrySet()) {
				String key = entry.getKey();
				Automaton automaton = entry.getValue();
				try {
					ImageCreator picture = new ImageCreator(automaton, nameFile + "_" + key);
					picture.createImageFile();
				} catch (IllegalArgumentException e) {
					System.err.println(e.getMessage());
				} catch (IOException e) {
					System.err.println("Couldn't create the image at path : " + nameFile);
				}
			}
		}
		if (cmd.hasOption(CMD_FICHIER)) {
			String nameFile = cmd.getOptionValue(CMD_FICHIER);
			AutomatonFileHelper extractionFile = new AutomatonFileHelper();
			for (Entry<String, Automaton> entry : mapAutomaton.entrySet()) {
				String key = entry.getKey();
				Automaton automaton = entry.getValue();
				try {
					extractionFile.saveAutomaton(automaton, nameFile + "_" + key);
				} catch (IOException e) {
					System.err.println("Couldn't create the image at path : " + nameFile);
				}
			}
		}
	}

}
