package test.manual;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import data.Automaton;
import process.builders.DotBuilder;
import process.builders.ThompsonAutomatonBuilder;

public class TestDot {
	
	public static void main(String[] args) {
		try {
			Automaton automaton = new ThompsonAutomatonBuilder("(((a*)+b)c)").build();
			DotBuilder builder = new DotBuilder(automaton);
			//String res = builder.buildDotString();
			File f = new File("C:\\Program Files (x86)\\Graphviz\\tmp\\test.dot");
			f.createNewFile();
			
			builder.buildDotFile(f);
			
		}catch (ParseException | IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
