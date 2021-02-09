package test.demo;

import java.io.IOException;

import data.Automaton;
import exceptions.FileFormatException;
import process.builders.AutomatonBuilder;
import process.file.AutomatonFileHelper;

public class CustomFileTest {
	
	public static void main(String[] args) throws IllegalArgumentException, FileFormatException, IOException {
		//charger et afficher un automate
		AutomatonFileHelper automatonFileHelper = new AutomatonFileHelper();
		String filename = "src/test/demo/creaFiles/automaton.crea";
		Automaton automatonLoaded = automatonFileHelper.loadAutomaton(filename);
		BuilderTest.createImage(automatonLoaded, "loadedAutomaton", false);
		
		
		//le modifier et le sauvegarder
		AutomatonBuilder automatonBuilder = new AutomatonBuilder(automatonLoaded);
		Automaton modifiedAutomaton = automatonBuilder.buildSynchronizedAutomaton();
		BuilderTest.createImage(modifiedAutomaton, "modifiedAutomaton", false);
		automatonFileHelper.saveAutomaton(modifiedAutomaton, "src/test/demo/creaFiles/automatonModifie.crea");
	}
	
}
