package test.apps;

import process.Commande;

public class CommandLineApp {

	public static void main(String[] args) {
		Commande commande = new Commande();
		commande.parseArguments(args);
	}
}
