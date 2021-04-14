package hmi.gui.management;

import java.util.Optional;

/**
 * Contains the result of verifiying a word and / or a equivalence with another automaton
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class VerificationResult {
	
	String resultWord;
	String resultEquals;

	public Optional<String> getResultWord() {
		return Optional.ofNullable(resultWord);
	}

	public void setResultWord(String resultWord) {
		this.resultWord = resultWord;
	}

	public Optional<String> getResultEquals() {
		return Optional.ofNullable(resultEquals);
	}

	public void setResultEquals(String resultEquals) {
		this.resultEquals = resultEquals;
	}

	
}
