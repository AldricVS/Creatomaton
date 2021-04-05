package process;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;

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
 * @author Chloé Gateau 
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
	private static final String CMD_COPY_FILES = "copie";

	private static final String CMD_BASE_AUTOMATON = "base";

	private Options options = new Options();
	private boolean mustCopyFiles = false;

	// Pour random automaton
	private static final int RANDOM_PROPERTIES_MAX_COUNT = 4;
	private static final String RANDOM_PROPERTY_NUMBER_STATES = "nStates";
	private static final String RANDOM_PROPERTY_NUMBER_FINAL_STATES = "nFinalStates";
	private static final String RANDOM_PROPERTY_NUMBER_EPSILON = "nEpsilonTrans";
	private static final String RANDOM_PROPERTY_ALPAHBET = "alphabet";

	private HashMap<String, Automaton> mapAutomaton = new HashMap<>();

	/**
	 * Add all defined argument that can be read from the Command Line.
	 */
	public Commande() {
		createLoadOptionGroup();
		createOperationOptions();
		createExtractionOptionGroup();

	}

	private void createExtractionOptionGroup() {
		// Creation du groupe d'option
		OptionGroup group2 = new OptionGroup();
		Option graphviz = new Option("G", CMD_GRAPHVIZ, true, "Export en image vers le nom du fichier donné");
		Option file = new Option("F", CMD_FICHIER, true, "Export en fichier vers le nom du fichier donné");
		// Ajout des options exclusives
		group2.addOption(graphviz);
		group2.addOption(file);
		// Ajout du groupe dans le conteneur Options
		options.addOptionGroup(group2);
		Option copie = new Option("C", CMD_COPY_FILES, false, "créer un copie de l'extraction si un fichier existe déjà avec ce nom");
		options.addOption(copie);
	}

	private void createOperationOptions() {
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

		Option equi = new Option("E", CMD_EQUIVALENCE, true, "Vérifie l'équivalence avec un autre automate dans un fichier .crea");
		options.addOption(equi);
		
		Option help = new Option("H", CMD_HELP, false, "affiche l'aide");
		options.addOption(help);
	}

	private void createLoadOptionGroup() {
		// Creation du groupe d'option
		OptionGroup group = new OptionGroup();

		Option load = new Option("L", CMD_LOAD, true, "charger un fichier .crea");
		load.setType(AutomatonManager.class);

		// random is special (multiple operations) and must be used like that :
		// "-Ralphabet=abcd -RnStates=10 ..." === 1 option
		String randomOptionHelp = "Crée un automate aléatoirement. Plusieurs propriétés : "
				+ System.lineSeparator()
				+ "-RnStates=x ==> l'automate aura x états (par défaut 5)"
				+ System.lineSeparator()
				+ "-RnFinalStates=x ==> l'automate aura x états finaux (par défaut 1)"
				+ System.lineSeparator()
				+ "-RnEspilonTrans=x ==> l'automate aura x epsilon-transitions (par défaut 0)"
				+ System.lineSeparator()
				+ "-Ralphabet=s ==> l'automate pourra avoir des transitions contenant n'importe quel caractère dans la chaine s";
		Option random = new Option("R", CMD_RANDOM, true, randomOptionHelp);
		random.setArgName("property=value");
		random.setArgs(RANDOM_PROPERTIES_MAX_COUNT);
		Option thompson = new Option("T", CMD_THOMPSON, true, "créer un automate selon une expression");

		// Ajout des options exclusives
		group.addOption(load);
		group.addOption(random);
		group.addOption(thompson);

		// Rends le groupe obligatoire
		group.setRequired(true);
		// Ajout le groupe dans le conteneur Options
		options.addOptionGroup(group);
	}

	public void parseArguments(String[] argument) {
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

		// Must check if need to overwrite before anything
		if (cmd.hasOption(CMD_COPY_FILES)) {
			mustCopyFiles = true;
		}

		Automaton automaton = parseLoadArguments(cmd);
		if (automaton != null) {
			mapAutomaton.put(CMD_BASE_AUTOMATON, automaton);
			parseTreatmentArguments(cmd, automaton);
			extractAutomatons(cmd);
		} else {
			System.err.println("L'automate n'a pas pu etre chargé.");
			System.out.println("Veuillez d'abord utiliser soit \"random\", soit \"load\", soit \"Thompson\"");
		}

	}

	/**
	 * Create the base Automaton depending on the method specified
	 * 
	 * @param cmd CommandLine Argument that specifie the method to use
	 * @return the newly formed Automaton, or null if no method has been selected
	 */
	public Automaton parseLoadArguments(CommandLine cmd) {
		Automaton automaton = null;
		if (cmd.hasOption(CMD_LOAD)) {
			String fichier = cmd.getOptionValue(CMD_LOAD);
			automaton = loadFromFile(fichier);
		} else if (cmd.hasOption(CMD_RANDOM)) {
			automaton = loadRandom(cmd);
		} else if (cmd.hasOption(CMD_THOMPSON)) {
			String expression = cmd.getOptionValue(CMD_THOMPSON);
			automaton = loadThompsonExpression(expression);
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
	public void parseTreatmentArguments(CommandLine cmd, Automaton automaton) {
		// newAutomaton that will receive all generated Automaton
		Automaton newAutomaton = null;
		// Check argument ALL
		if (cmd.hasOption(CMD_ALL)) {
			newAutomaton = treatSynchronisation(automaton);
			mapAutomaton.put(CMD_SYNC, newAutomaton);

			newAutomaton = treatDeterminisation(automaton);
			mapAutomaton.put(CMD_DETER, newAutomaton);

			newAutomaton = treatMinimisation(automaton);
			mapAutomaton.put(CMD_MINI, newAutomaton);

			newAutomaton = treatMiroir(automaton);
			mapAutomaton.put(CMD_MIRROR, newAutomaton);

		} else {
			// check argument SYNC
			if (cmd.hasOption(CMD_SYNC)) {
				newAutomaton = treatSynchronisation(automaton);
				mapAutomaton.put(CMD_SYNC, newAutomaton);
				System.out.println("Création de l'automate synchronisé terminé");
			}
			// check argument DET
			if (cmd.hasOption(CMD_DETER)) {
				newAutomaton = treatDeterminisation(automaton);
				mapAutomaton.put(CMD_DETER, newAutomaton);
				System.out.println("Création de l'automate déterministe terminé");
			}
			// check argument MINI
			if (cmd.hasOption(CMD_MINI)) {
				newAutomaton = treatMinimisation(automaton);
				mapAutomaton.put(CMD_MINI, newAutomaton);
				System.out.println("Création de l'automate minimal terminé");
			}
			// check argument MIR
			if (cmd.hasOption(CMD_MIRROR)) {
				newAutomaton = treatMiroir(automaton);
				mapAutomaton.put(CMD_MIRROR, newAutomaton);
				System.out.println("Création de l'automate miroir terminé");
			}
		}
		// check argument EQUI
		if (cmd.hasOption(CMD_EQUIVALENCE)) {
			String fileName = cmd.getOptionValue(CMD_EQUIVALENCE);
			Automaton automatonFromFile = loadFromFile(fileName);
			AutomatonManager manager = new AutomatonManager(automaton);
			boolean isEquals = manager.isEqualsByMinimalism(automatonFromFile);
			if (isEquals) {
				System.out.println("Les deux automates sont équivalents.");
			} else {
				System.out.println("Les deux ne automates sont pas équivalents.");

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
	public Automaton loadRandom(CommandLine cmd) {
		RandomAutomatonBuilder randomAutomatonBuilder = new RandomAutomatonBuilder();
		// retreive all properties
		Properties optionProperties = cmd.getOptionProperties(CMD_RANDOM);
		String alphabetProperty = optionProperties.getProperty(RANDOM_PROPERTY_ALPAHBET);
		String numberStatesProperty = optionProperties.getProperty(RANDOM_PROPERTY_NUMBER_STATES);
		String numberFinalStatesProperty = optionProperties.getProperty(RANDOM_PROPERTY_NUMBER_FINAL_STATES);
		String numberEpsilonTransitionsProperty = optionProperties.getProperty(RANDOM_PROPERTY_NUMBER_EPSILON);

		// For each of them, check if they are null and / or readable
		if (alphabetProperty != null) {
			randomAutomatonBuilder.setAlphabet(alphabetProperty);
		}
		int numberOfStates = readNumber(numberStatesProperty);
		if (numberOfStates > 0) {
			randomAutomatonBuilder.setNumberOfStates(numberOfStates);
		}
		int numberOfFinalStates = readNumber(numberFinalStatesProperty);
		if (numberOfFinalStates > 0) {
			randomAutomatonBuilder.setNumberOfFinalStates(numberOfFinalStates);
		}
		int numberOfEpsilonTransitions = readNumber(numberEpsilonTransitionsProperty);
		if (numberOfEpsilonTransitions > 0) {
			randomAutomatonBuilder.setNumberOfEpsilonTransitions(numberOfEpsilonTransitions);
			;
		}

		return randomAutomatonBuilder.build();
	}

	private int readNumber(String numberString) {
		if (numberString == null) {
			return -1;
		}
		try {
			int number = Integer.parseInt(numberString);
			return number;
		} catch (NumberFormatException e) {
			// Number cannot be read
			return -1;
		}
	}

	/**
	 * Load the Automaton from the given file
	 * 
	 * @param fileName the path to the file
	 * @return the Automaton created based on the given file
	 */
	public Automaton loadFromFile(String fileName) {
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
	public Automaton loadThompsonExpression(String expression) {
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
	public Automaton treatMinimisation(Automaton automaton) {
		AutomatonBuilder builder = new AutomatonBuilder(automaton);
		return builder.buildMinimalAutomaton();

	}

	/**
	 * Launch the determinisation on the given Automaton
	 * 
	 * @param automaton Automaton to be determinised
	 * @return the determinist Automaton
	 */
	public Automaton treatDeterminisation(Automaton automaton) {
		AutomatonBuilder builder = new AutomatonBuilder(automaton);
		return builder.buildDeterministicAutomaton();

	}

	/**
	 * Reverse the given Automaton
	 * 
	 * @param automaton Automaton to be reversed
	 * @return the mirror Automaton
	 */
	public Automaton treatMiroir(Automaton automaton) {
		AutomatonBuilder builder = new AutomatonBuilder(automaton);
		return builder.buildMirrorAutomaton();
	}

	/**
	 * Launch the synchronisation on the given Automaton
	 * 
	 * @param automaton Automaton to be synchronised
	 * @return the synchronised Automaton
	 */
	public Automaton treatSynchronisation(Automaton automaton) {
		AutomatonBuilder builder = new AutomatonBuilder(automaton);
		return builder.buildSynchronizedAutomaton();
	}

	/**
	 * Extract all generated Automaton to an Image or a crea File depend on given
	 * the argument.
	 * 
	 * @param cmd CommandLine that will check which argument has been given
	 */
	public void extractAutomatons(CommandLine cmd) {
		if (cmd.hasOption(CMD_GRAPHVIZ)) {
			String nameFile = cmd.getOptionValue(CMD_GRAPHVIZ);
			for (Entry<String, Automaton> entry : mapAutomaton.entrySet()) {
				String key = entry.getKey();
				Automaton automaton = entry.getValue();

				try {
					String finalName = nameFile + "_" + key;
					ImageCreator imageCreator = new ImageCreator(automaton, finalName);
					imageCreator.setMustEraseDotFiles(!mustCopyFiles);
					File outputFile = imageCreator.createImageFile();
					System.out.println(String.format("Image \"%s\" enregistrée à \"%s\"", finalName, outputFile.getAbsolutePath()));
				} catch (IllegalArgumentException e) {
					System.err.println(e.getMessage());
				} catch (IOException e) {
					System.err.println("Couldn't create the image at path : " + nameFile);
				}
			}
		}
		if (cmd.hasOption(CMD_FICHIER)) {
			String nameFile = cmd.getOptionValue(CMD_FICHIER);
			if (nameFile.endsWith(".crea")) {
				nameFile = nameFile.substring(0, nameFile.lastIndexOf(".crea"));
			}
			AutomatonFileHelper automatonFileHelper = new AutomatonFileHelper();
			automatonFileHelper.setMustOverwriteFiles(!mustCopyFiles);
			for (Entry<String, Automaton> entry : mapAutomaton.entrySet()) {
				String key = entry.getKey();
				Automaton automaton = entry.getValue();
				try {
					String finalName = nameFile + "_" + key;
					File outputFile = automatonFileHelper.saveAutomaton(automaton, nameFile + "_" + key);
					System.out.println(String.format("Image \"%s\" enregistrée à \"%s\"", finalName, outputFile.getAbsolutePath()));
				} catch (IOException e) {
					System.err.println("Couldn't create the image at path : " + nameFile);
				}
			}
		}
	}

}
