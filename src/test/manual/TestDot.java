package test.manual;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

import data.Automaton;
import process.builders.DotBuilder;
import process.builders.ThompsonAutomatonBuilder;

public class TestDot {
	
	public static void main(String[] args) {
		try {
			Automaton automaton = new ThompsonAutomatonBuilder("((a*)+b)").build();
			DotBuilder builder = new DotBuilder(automaton);
			String res = builder.buildDotString();
			File f = new File("C:\\Program Files (x86)\\Graphviz\\tmp\\test.dot");
			f.createNewFile();
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			bw.write(new String(res.getBytes("UTF-8"), StandardCharsets.UTF_8));
			bw.close();
		}catch (ParseException | IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
