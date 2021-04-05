package process.helpers;

import java.util.Arrays;

/**
 * This class permits to create a simple thompson expression, that can be later
 * be used to create a thompson automaton with
 * {@link process.builders.ThompsonAutomatonBuilder ThompsonAutomatonBuilder}.
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class ThompsonExpressionCreator {

	private String alphabet = "abc";

	public ThompsonExpressionCreator(String alphabet) {
		// sort the alphabet and remove same occurences before
		rearrangeAlphabet(alphabet);
	}
	
	public ThompsonExpressionCreator() {
	}

	public String getAlphabet() {
		return alphabet;
	}

	public void setAlphabet(String alphabet) {
		rearrangeAlphabet(alphabet);
	}

	public String createExpression() {
		return "";
	}

	/**
	 * Sort the alphabet and remove duplicates from it
	 */
	private void rearrangeAlphabet(String alphabet) {
		char[] charArray = alphabet.toCharArray();
		Arrays.sort(charArray);
		this.alphabet = removeDuplicates(charArray);
	}
	
	/**
	 * Remove all duplicate characters from a sorted string.
	 */
	private String removeDuplicates(char[] charArray) {
		char previousChar = charArray[0];
		StringBuilder stringBuilder = new StringBuilder(previousChar);
		for(int index = 1; index < charArray.length; index++){
			if(charArray[index] != previousChar) {
				stringBuilder.append(charArray[index]);
			}
			previousChar = charArray[index];
		}
		
		return stringBuilder.toString();
	}
}
