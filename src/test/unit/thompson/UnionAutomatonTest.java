package test.unit.thompson;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.BeforeClass;
import org.junit.Test;

import data.Automaton;
import data.State;
import data.Transition;
import process.factory.ThompsonAutomatonFactory;

/**
 * Tests to check a "letter Thompson" automaton
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class UnionAutomatonTest {
	
	private static Automaton letterAutomaton1;
	private static Automaton letterAutomaton2;
	private static Automaton automaton;
	
	@BeforeClass
	public static void createLetterAutomaton() {
		letterAutomaton1 = ThompsonAutomatonFactory.createLetterAutomaton('a');
		letterAutomaton2 = ThompsonAutomatonFactory.createLetterAutomaton('b');
		//TODO TESTS !!!!! 
	}
	
	
}
