package test.manual;

import process.Commande;

public class CommandParseTest {
	
	public static void main(String[] args) {
		String arg[] = {"-L"};
		Commande commande = new Commande();
		commande.traitement(arg);
	}
	
}
