package test.manual;

import process.Commande;

public class CommandParseTest {
	
	public static void main(String[] args) {
		//String arg[] = {"-L","src/test/manual/creaFiles/automaton.crea","-V","a","-F","data/input/test.crea"};
		
		String randomAutomatonArgs[] = {"-Ralphabet=abcd", "-RnStates=8", "-RnEpsilonTrans=2", "-RnFinalStates=3", "-V", "a", "-G", "random"};
		
		Commande commande = new Commande();
		commande.traitement(randomAutomatonArgs);
	}
	
}
