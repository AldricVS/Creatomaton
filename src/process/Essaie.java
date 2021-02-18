package process;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;

public class Essaie {

	private Options options = new Options();
	
	
	public void commande () {
		Options options = new Options();
		
		Option load = new Option("L","load",true,"charger un fichier .crea");
		// dire leq fichier qu'il peu prendre .crea
		load.setType(AutomatonManager.class);
		Option random = new Option("R","random",false,"creer un automate aléatoire");
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
}


