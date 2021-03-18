package process;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import org.apache.commons.cli.PosixParser;

import data.Automaton;
import data.Transition;
import exceptions.FileFormatException;
import process.builders.AutomatonBuilder;
import process.builders.RandomAutomatonBuilder;
import process.builders.ThompsonAutomatonBuilder;
import process.factory.ThompsonAutomatonFactory;
import process.file.AutomatonFileHelper;
import process.file.ImageCreator;

public class Commande {

	private Options options;
	private HashMap<String, Automaton> listAutomaton = new HashMap<>();

	public Commande() {
		options = new Options();

		Option load = new Option("L", "load", true, "charger un fichier .crea");
		// dire leq fichier qu'il peu prendre .crea
		load.setType(AutomatonManager.class);
		Option random = new Option("R", "random", true, "creer un automate aléatoire");
		Option thompson = new Option("T", "thompson", true, "creer un automate selon une expression");

		// Creation du groupe d'option
		OptionGroup group = new OptionGroup();
		// Ajout des options exclusives
		group.addOption(load);
		group.addOption(random);
		group.addOption(thompson);

		// Possibilite de rendre un groupe obligatoire
		group.setRequired(true);

		// Ajout du groupe dans le conteneur Options
		options.addOptionGroup(group);

		Option sync = new Option("S", "sync", false, "créer un automate synchronisé");
		options.addOption(sync);

		Option det = new Option("D", "det", false, "créer un automate déterministe");
		options.addOption(det);

		Option mir = new Option("M", "mir", false, "créer un miroir de l'automate");
		options.addOption(mir);

		Option mini = new Option("m", "mini", false, "créer un automate minimal");
		options.addOption(mini);

		Option all = new Option("A", "all", false, "sync det mir et mini à la fois");
		options.addOption(all);

		Option val = new Option("V", "val", true, "vérifie si l'automate valide le mot");
		val.setType(AutomatonManager.class);

		options.addOption(val);

		Option equi = new Option("E", "equi", true,"vérifie l'équivalence avec un autre automate stocké dans un fichier .crea");
		options.addOption(equi);

		Option graphviz = new Option("G", "graphviz", true, "export en image avec en paramètre le nom donné");
		Option file = new Option("F", "file", true, "export en fichier avec le nom du fichier passé");

		// Creation du groupe d'option
		OptionGroup group2 = new OptionGroup();
		// Ajout des options exclusives
		group2.addOption(graphviz);
		group2.addOption(file);
		// Ajout du groupe dans le conteneur Options
		options.addOptionGroup(group2);

	}

	public void traitement(String[] argument) {

		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse(options, argument, false);

			if (cmd.hasOption("help")) {
				// Affiche l'aide
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("MonServeur", options);

			}
			Automaton automaton = traitementAutomaton(cmd);
			listAutomaton.put("base", automaton);

			if (automaton != null) {
				traitementAlgo(cmd, automaton);
				extraction(cmd);
			} else {
				System.out.println("il faut d'abord utiliser soit random soit load soit thompson");
			}

		} catch (ParseException e) {
			// Affichage de l'aide
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("Commandes utilisables", options);
			System.err.println("Parsing failed : " + e.getMessage());
		}
	}

	public Automaton traitementAutomaton(CommandLine cmd) {
		Automaton automaton = null;
		if (cmd.hasOption("L")) {
			String fichier = cmd.getOptionValue("load");
			automaton = load(fichier);
		} else if (cmd.hasOption("random")) {
			String expression = cmd.getOptionValue("random");
			automaton = random(expression);
		} else if (cmd.hasOption("thompson")) {
			String expression = cmd.getOptionValue("thompson");
			automaton = thompson(expression);
		}
		return automaton;
	}

	public void traitementAlgo(CommandLine cmd, Automaton automaton) {
		Automaton newAutomaton = null;
		if (cmd.hasOption("all")) {
			newAutomaton = synchronisation(automaton);
			listAutomaton.put("syn", newAutomaton);

			newAutomaton = determinisation(automaton);
			listAutomaton.put("det", newAutomaton);

			newAutomaton = minimisation(automaton);
			listAutomaton.put("min", newAutomaton);

			newAutomaton = miroir(automaton);
			listAutomaton.put("mir", newAutomaton);

		} else {
			if (cmd.hasOption("syn")) {

				newAutomaton = synchronisation(automaton);
				listAutomaton.put("syn", newAutomaton);
			}
			if (cmd.hasOption("det")) {
				newAutomaton = determinisation(automaton);
				listAutomaton.put("det", newAutomaton);

			}
			if (cmd.hasOption("min")) {
				newAutomaton = minimisation(automaton);
				listAutomaton.put("min", newAutomaton);

			}
			if (cmd.hasOption("mir")) {
				newAutomaton = miroir(automaton);
				listAutomaton.put("mir", newAutomaton);

			}
		}
		if (cmd.hasOption("equi")) {
			String fichier = cmd.getOptionValue("equi");
			Automaton automaton2 = load(fichier);
			AutomatonManager manager =new AutomatonManager(automaton);
			boolean isEquals = manager.isEqualsByMinimalism(automaton2);
			if (isEquals) {
				System.out.println("les deux automates sont équivalent.");
			}
			else {
				System.out.println("les deux ne automates sont pas équivalent.");

			}
		}
		if (cmd.hasOption("val")) {
			String automate = cmd.getOptionValue("val");
			AutomatonManager manager =new AutomatonManager(automaton);
			boolean isValide = manager.validateAutomatonByDeterminism(automate);
			if (isValide) {
				System.out.println(" l'automate valide le mot " +automate);
			}
			else {
				System.out.println(" l'automate ne valide pas le mot.");
			}
				
		}

	}

	public Automaton random(String expression) {
		RandomAutomatonBuilder automatonRandom = new RandomAutomatonBuilder();
		Automaton automaton = null;
		try {
			// appelle de la methode qui renvoie un automate d'après l'expression
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return automaton;
	}

	public Automaton load(String fichier) {
		AutomatonFileHelper automatonFileHelper = new AutomatonFileHelper();
		Automaton automaton = null;
		try {
			automaton = automatonFileHelper.loadAutomaton(fichier);
		} catch (IllegalArgumentException | FileFormatException | IOException e) {
			e.printStackTrace();
		}
		return automaton;

	}

	public Automaton thompson(String expression) {

		ThompsonAutomatonFactory automatonThompson = new ThompsonAutomatonFactory();
		Automaton automaton = null;
		try {

			// appelle de la methode qui renvoie un automate de thompson d'après
			// l'expression
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return automaton;

	}

	public Automaton minimisation(Automaton automaton) {
		AutomatonBuilder builder = new AutomatonBuilder(automaton);
		return builder.buildMinimalAutomaton();
		
	}

	public Automaton determinisation(Automaton automaton) {
		AutomatonBuilder builder = new AutomatonBuilder(automaton);
		return builder.buildDeterministicAutomaton();
		
	}

	public Automaton miroir(Automaton automaton) {
		AutomatonBuilder builder = new AutomatonBuilder(automaton);
		return builder.buildMirrorAutomaton();
	}

	public Automaton synchronisation(Automaton automaton) {
		AutomatonBuilder builder = new AutomatonBuilder(automaton);
		return builder.buildSynchronizedAutomaton();
	}

	public void extraction(CommandLine cmd) {
		if (cmd.hasOption("graphviz")) {
			String name = cmd.getOptionValue("graphviz");
            for(Entry<String, Automaton> entry : listAutomaton.entrySet()) {
            	String key = entry.getKey(); 
            	Automaton automaton = entry.getValue();
        			try {
					ImageCreator picture = new ImageCreator(automaton, name+"_"+key);
					picture.createImageFile();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// ajouter l'appel de la fonction qui permet de faire l'export avec graphviz
				// pour automaton
			}
		}
		if (cmd.hasOption("file")) {
			String nameFile = cmd.getOptionValue("file");
			AutomatonFileHelper extractionFile = new AutomatonFileHelper();
			 for(Entry<String, Automaton> entry : listAutomaton.entrySet()) {
	            	String key = entry.getKey(); 
	            	Automaton automaton = entry.getValue();
				try {
					extractionFile.saveAutomaton(automaton, nameFile+"_"+key);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
