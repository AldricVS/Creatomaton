package process;

import java.io.File;
import java.io.IOException;
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
import exceptions.FileFormatException;
import process.builders.AutomatonBuilder;
import process.builders.ThompsonAutomatonBuilder;
import process.factory.ThompsonAutomatonFactory;
import process.file.AutomatonFileHelper;

public class Commande {

	private Options options;
	
	
	public Commande () {
		options = new Options();
		
		Option load = new Option("L","load",true,"charger un fichier .crea");
		// dire leq fichier qu'il peu prendre .crea
		load.setType(AutomatonManager.class);
		Option random = new Option("R","random",true,"creer un automate aléatoire");
		Option thompson = new Option("T","thompson",true,"creer un automate selon une expression");

		// Creation du groupe d'option
		OptionGroup group = new OptionGroup();
		// Ajout des options exclusives
		group.addOption(load) ;
		group.addOption(random);
		group.addOption(thompson);

		// Possibilite de rendre un groupe obligatoire
		group.setRequired(true);

		// Ajout du groupe dans le conteneur Options 
		options.addOptionGroup(group);
		
		Option sync = new Option("S","sync",false,"créer un automate synchronisé");
		options.addOption(sync);
		
		Option det = new Option("D","det",false,"créer un automate déterministe");
		options.addOption(det);

		Option mir = new Option("M","mir",false,"créer un miroir de l'automate");
		options.addOption(mir);
		
		Option mini = new Option("m","mini",false,"créer un automate minimal");
		options.addOption(mini);
		
		Option all = new Option("A","all",false,"sync det mir et mini à la fois");
		options.addOption(all);

		Option val = new Option("V","val",false,"vérifie si l'automate valide le mot");
		options.addOption(val);
		
		Option equi = new Option("E","equi",false,"vérifie l'équivalence avec un autre automate stocké dans un fichier .crea");
		options.addOption(equi);

		}
		
	public void traitement (String[] argument) {		
		
		CommandLineParser parser = new DefaultParser();
		Automaton automaton = null;
		try {
            CommandLine cmd = parser.parse(options, argument, false);

            if(cmd.hasOption("help")){
                // Affiche l'aide
            	HelpFormatter formatter = new HelpFormatter();
            	formatter.printHelp("MonServeur", options);
            	
            }
            if(cmd.hasOption("L")){
                System.out.println("récupération d'un fichier");
                String fichier =  cmd.getOptionValue("load");
                AutomatonFileHelper automatonFileHelper = new AutomatonFileHelper();
                try {
					automaton = automatonFileHelper.loadAutomaton(fichier);
				} catch (IllegalArgumentException | FileFormatException | IOException e) {
					e.printStackTrace();
				}
                
            }
            else if (cmd.hasOption("random")) {
                System.out.println("création d'un automate aléatoire ");
               String expression =cmd.getOptionValue("random");
               
         

            }
            else if (cmd.hasOption("thompson")) {
                System.out.println("création d'un automate de thomson ");
                String expression =cmd.getOptionValue("thompson");


            }
            if  (cmd.hasOption("syn")) {
            	AutomatonBuilder builder = new AutomatonBuilder(automaton);
            	builder.buildSynchronizedAutomaton();
            }
            if (cmd.hasOption("det")) {
            	AutomatonBuilder builder = new AutomatonBuilder(automaton);
            	builder.buildDeterministicAutomaton();
            }
            if (cmd.hasOption("min")) {
            	AutomatonBuilder builder = new AutomatonBuilder(automaton);
            	builder.buildMinimalAutomaton();
            }
            if (cmd.hasOption("mir")) {
            	AutomatonBuilder builder = new AutomatonBuilder(automaton);
            	builder.buildMirrorAutomaton();
            }
            if (cmd.hasOption("all")) {
            	AutomatonBuilder builder = new AutomatonBuilder(automaton);
            	builder.buildSynchronizedAutomaton();
            	builder.buildDeterministicAutomaton();
            	builder.buildMinimalAutomaton();
            	builder.buildMirrorAutomaton();
            }

        } catch (ParseException e) {
            // Affichage de l'aide
        	HelpFormatter formatter = new HelpFormatter();
        	formatter.printHelp("Commandes utilisables", options);
        	System.err.println("Parsing failed : " + e.getMessage());
        }
	}
	
}


