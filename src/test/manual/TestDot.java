package test.manual;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;

import data.Automaton;
import process.builders.AutomatonBuilder;
import process.builders.DotBuilder;
import process.builders.ThompsonAutomatonBuilder;

public class TestDot {

	private static final String PATH = "\"C:/Program Files (x86)/Graphviz/tmp/";

	public static void main(String[] args) {
		try {
			Automaton automaton = new ThompsonAutomatonBuilder("(((a*)(b*))+(e(a+b)))").build();
			AutomatonBuilder automatonBuilder = new AutomatonBuilder(automaton);
			automaton = automatonBuilder.buildMirrorAutomaton();
			DotBuilder builder = new DotBuilder(automaton);
			builder.setInReverseMode(true);
			// String res = builder.buildDotString();
			File f = new File("C:\\Program Files (x86)\\Graphviz\\tmp\\test_1.dot");

			builder.buildDotFile(f);

			// exec directly graphviz
			Process proc = Runtime.getRuntime().exec(
					"dot -Tjpg -o " + PATH + "result_1.jpg\" " + PATH + "test_1.dot\"");

			// wait until process is done
			try {
				System.out.println("Dot finished with code " + proc.waitFor());
			} catch (InterruptedException e) {

				e.printStackTrace();
			}

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String s = null;
			while ((s = stdInput.readLine()) != null) {
				System.out.println(s);
			}

			BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
			while ((s = stdError.readLine()) != null) {
				System.err.println(s);
			}

		} catch (ParseException | IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
