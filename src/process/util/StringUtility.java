package process.util;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Some methods to work with Strings
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class StringUtility {

	/**
	 * Retrieve from both Strings all letters in order to obtain the complete alphabet 
	 * @param s1 the first string
	 * @param s2 the second string
	 * @return the alphabet
	 */
	public static String alphabetOf2Strings(String s1, String s2) {
		String concat = s1 + s2;
		return alphabetOfString(concat);
	}
	
	/**
	 * Get all unique characters in the string in the alphabetical order
	 * @param s the string to get alphabet from
	 * @return the alphabet of the string
	 */
	public static String alphabetOfString(String s) {
		//remove duplicate letters
		HashSet<Character> chararacters = new HashSet<>();
		char[] charArray = s.toCharArray();
		for(char c : charArray) {
			chararacters.add(c);
		}
		
		//put all chars in a string
		StringBuilder sb = new StringBuilder();
		for(char c : chararacters) {
			sb.append(c);
		}
		String uniqueChars = sb.toString();
		
		//put them in the right order
		char c[] = uniqueChars.toCharArray();
		Arrays.sort(c);
		return String.valueOf(c);
	}
}
