package cscie99.team2.lingolearn.server.confuser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import cscie99.team2.lingolearn.shared.error.ConfuserException;

/**
 * The following class contains functions that are largely for the support of
 * the confuser algorithm.
 */
public class ConfuserTools {
	// The path to the directory of blacklists
	private final static String BLACKLIST_DIRECTORY = "/blacklist/";
	
	// The extension for the files
	private final static String BLACKLIST_EXTENSION = ".blacklist";
	
	// The following the valid punctuation characters for Japanese
	private final static String JAPANESE_PUNCTUATION = "｛｝（）［］【】、，…‥。・〽「」『』　〜：！？";
	
	/**
	 * Check to see if the supplied string is on the blacklist.
	 * 
	 * @param pharse The phrase to be checked.
	 * @return True if the phrase is on the blacklist, false otherwise. If a
	 * blacklist for the given language cannot be found in WEB-INF/blacklists
	 * then it is assumed that all works are valid.
	 */
	public static boolean onBlackList(String pharse, String langaugeCode) throws ConfuserException, IOException {
		// Open the black list for this language and check to see if it exists
		String path = BLACKLIST_DIRECTORY + langaugeCode + BLACKLIST_EXTENSION;
		InputStream blacklist = ConfuserTools.class.getResourceAsStream(path);
		if (blacklist == null) {
			return false;
		}
		// Start processing the contents of the file
		BufferedReader reader = null;
		try {
			// Create a reader for the file
			reader = new BufferedReader(new InputStreamReader(blacklist));
			// Load the context of the file and return if we find a match
			String data;
			while ((data = reader.readLine()) != null) {
				// Press on if the line is blank or is a comment
				if (data.isEmpty() || data.startsWith("#")) {
					continue;
				}
				// Only keep values before a comment
				if (data.contains("#")) {
					data = data.substring(0, data.indexOf('#'));
				}
				// Do we have a match?
				if (data.trim().equals(pharse)) {
					return true;
				}
			}
			return false;
		} catch (FileNotFoundException ex) {
			throw new ConfuserException("Unable to open the blacklist for " + langaugeCode);
		} catch (IOException ex) {
			throw new ConfuserException("An error occured while reading the next line", ex);
		} finally {
			if (reader != null) {
				reader.close();
			}
		}	
	}
	
	/**
	 * Check to see what type of Japanese character the given character is.
	 * Note that this code does not detect if a kanji is limited ot use in 
	 * Chinese as opposed to Japanese.
	 * 
	 * @param character The character to be tested.
	 * @return The type of character provided, or unknown if it was not determined.
	 */
	public static CharacterType checkCharacter(char character) {
		// Check to see if this is a hiragana
		if (character >= 0x3041 && character <= 0x3096) {
			return CharacterType.Hiragana;
		}
		// Check to see if this is a katakana
		if (character >= 0x30A1 && character <= 0x30FC) {
			return CharacterType.Katakana;
		}
		// Check to see if this is kanji
		if (character >= 0x4E00 && character <= 0x9FAF) {
			return CharacterType.Kanji;
		}
		// Check to see if this is a punctuation character
		if (JAPANESE_PUNCTUATION.contains(String.valueOf(character))) {
			return CharacterType.Punctuation;
		}
		// Check to see if this is an Arabic number
		if (Character.isDigit(character)) {
			return CharacterType.Number;
		}
		// Fall through to unknown
		return CharacterType.Unknown;
	}
	
	/**
	 * Examine the characters in the phrase and return on the basis of what
	 * type of phrase we are looking at.
	 * 
	 * @param phrase The phrase to be examined.
	 * @return The type of phrase that was provided.
	 */
	public static PhraseType checkPhrase(String phrase) {
		boolean hiragana = false;
		boolean kanji = false;
		boolean katakana = false;
		for (int ndx = 0; ndx < phrase.length(); ndx++) {
			switch (checkCharacter(phrase.charAt(ndx))) {
				case Hiragana: hiragana = true;
					break;
				case Kanji: kanji = true;
					break;
				case Katakana: katakana = true;
					break;
				// Just press on for punctuation and numbers since it can 
				// appear in any valid string
				case Punctuation:
				case Number:
					break;
				// If we don't know what the character type is, return now
				case Unknown:
					return PhraseType.Unknown;
			}
		}
		// Hiragana phrases should only have hiragana
		if (hiragana && !kanji && !katakana) {
			return PhraseType.Hiragana;
		}
		// Katakana phrases should have only katakana
		if (katakana && !kanji && !hiragana) {
			return PhraseType.Katakana;
		}
		// Kanji phrases may mix kanji and hiragana
		if (kanji && !katakana) {
			return PhraseType.Kanji;
		}
		// Return mixed by default
		return PhraseType.Mixed;
	}
}
