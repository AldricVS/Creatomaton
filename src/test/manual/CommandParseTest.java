package test.manual;

import process.Commande;

public class CommandParseTest {
	
	public static void main(String[] args) {
		String arg[] = {"-L","src/test/manual/creaFiles/automaton.crea","-V","a","-G","test"};
		Commande commande = new Commande();
		commande.traitement(arg);
	}
	
}
