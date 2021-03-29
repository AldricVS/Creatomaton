package test.manual;

import process.Commande;

public class CommandParseTest {
	
	public static void main(String[] args) {
		//String arg[] = {"-L","src/test/manual/creaFiles/automaton.crea","-V","a","-F","data/input/test.crea"};
		
		String randomAutomatonArgs[] = {"-T", "(a*.b*)+(c.(a+b))","-G", "random"};
		
		Commande commande = new Commande();
		commande.traitement(randomAutomatonArgs);
	}
	
}
