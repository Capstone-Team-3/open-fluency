package cscie99.team2.lingolearn.server.confuser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cscie99.team2.lingolearn.shared.Card;
import cscie99.team2.lingolearn.shared.error.ConfuserException;

/**
 * This class encapsulates a means of getting characters that are similar to a
 * given example for the purposes of confusing humans.
 */
public class Confuser {
	// The path to the directory of confusers
	private final static String CONFUSER_DIRECTORY = "/confusers/";
	
	// The extension for the files
	private final static String CONFUSER_EXTENSION = ".confusers";
	
	// The language code we are working with
	private final static String CONFUSER_LANGUAGE = "jp";
	
	// The following dictionary is has the vowels mapped to the hiragana 
	// that they can elongate, since this shouldn't change we want to 
	// make sure it is an immutable object.
	private final static Map<Character, String> vowelCombinations;
	static {
		Map<Character, String> map = new HashMap<Character, String>();
		map.put('あ', "かさたなはまやらわがざだばぱ");
		map.put('い', "きしちにひみりぎじびぴ");
		map.put('う', "くすつぬふむゆるぐずぶぷこそとのほもよろをごぞどぼぽ");
		map.put('え', "けせてねへめれげぜでべぺ");
		vowelCombinations = Collections.unmodifiableMap(map);
	};
	
	/**
	 * Get a random list of confusers of given type limited to the count 
	 * provided, these results are checked against the black list to 
	 * ensure that nothing inappropriate is returned. 
	 * 
	 * @param card The card to get the confusers for.
	 * @param type The focus of the confusers.
	 * @param count The number that should be returned, if -1 is provided then
	 * all results are returned without processing.
	 * @return A list of string zero to the requested count of confusers.
	 */
	public List<String> getConfusers(Card card, CharacterType type, int count) throws ConfuserException {
		try {
			// Start by running the relevant functions
			List<String> results = new ArrayList<String>();
			switch (type) {
				case Hiragana:
					results.addAll(getNManipulation(card.getHiragana()));
					results.addAll(getSmallTsuManiuplation(card.getHiragana()));
					results.addAll(getHiraganaManipulation(card.getHiragana()));
					break;
				case Katakana:
					results.addAll(getNManipulation(card.getKatakana()));
					results.addAll(getSmallTsuManiuplation(card.getKatakana()));
					results.addAll(getKatakanaManiuplation(card.getKatakana()));
					break;
				case Kanji:
					results.addAll(getKanjiBoundries(card));
					results.addAll(getKanjiSubsitution(card.getKanji()));
					break;
				default:
					throw new ConfuserException("An invalid type, " + type + " was provided.");
			}
			// Should we return everything?
			if (count == -1) {
				return results;
			}
			// Check to make sure all of the results are appropriate
			for (int ndx = 0; ndx < results.size(); ndx++) {
				if (ConfuserTools.onBlackList(results.get(ndx), "jp")) {
					results.remove(ndx);
					ndx--;
				}
			}
			// Trim the results down to the count and return them
			Random random = new Random();
			while (results.size() > count) {
				int ndx = random.nextInt(results.size());
				results.remove(ndx);
			}
			return results;
		}
		catch (IOException ex) {
			throw new ConfuserException("There was an error while reading the blacklist.", ex);
		}
	}

	/**
	 * Add vowel elongation to the provided phrase.  This algorithm attempts to
	 * either add or remove a single character on a single pass through the 
	 * entire phrase.
	 * 
	 * @param phrase The phrase to be manipulated.
	 * @return A list of valid manipulations of the provided phrase.
	 */
	public List<String> getHiraganaManipulation(String phrase) {
		// The following are the parameters for the manipulation
		char n = 'ん';
		String invalidFollowers = "ゃゅょ";
		// Start by iterating through each of the characters in the phrase
		List<String> phrases = new ArrayList<String>();
		for (int ndx = 0; ndx < phrase.length(); ndx++) {
			char ch = phrase.charAt(ndx);
			// Press on if we can't insert after this character
			if (ch == n || invalidFollowers.contains(String.valueOf(ch))) {
				continue;
			}
			// Get the next character in the phrase
			char next = '\0';
			if (ndx != (phrase.length() - 1)) {
				next = phrase.charAt(ndx + 1);
			}
			// Check to see if we shouldn't extend this character
			if (invalidFollowers.contains(String.valueOf(next))) {
				continue;
			}
			// Test to make sure if this is a vowel we can double
			if (vowelCombinations.keySet().contains(ch)) {
				// The only vowels that are doubled in regular use are お (o) and  え (e)
				if (next != ch && (ch == 'え' || ch == 'お')) {
					phrases.add(insertCharacter(phrase, ndx, ch));
				}
				continue;
			}
			// Iterate through the vowel combinations to find the character
			// to use for the replacement
			for (char replacement : vowelCombinations.keySet()) {
				// Check to see if a vowel has already been extended, if so
				// we can skip the next character
				if (next == replacement) {
					ndx++;
					break;
				}
				// Check to see if the current character makes for a valid 
				// vowel to be extended
				if (vowelCombinations.get(replacement).contains(String.valueOf(ch))) {
					phrases.add(insertCharacter(phrase, ndx, replacement));
					break;
				}
			}
		}
		return phrases;
	}
	
	/**
	 * Get a list of kanji phrases that have the hiragana extended off the kanji
	 * where appropriate.
	 * 
	 * @param card The card to build the phrases off of.
	 * @return A list of phrases built from the card, or an empty list if there
	 * are no valid confusers.
	 */
	public List<String> getKanjiBoundries(Card card) throws ConfuserException {
		// Check to see if there is only one character which we can't work with
		// and also check to see if the phrase ends with する (to do) since this 
		// approach is invalid
		if (card.getKanji().length() == 1 || card.getKanji().endsWith("する")) {
			return new ArrayList<String>();
		}

		// Prepare to run
		int offset = 0;
		CharacterType previous = null;
		StringBuilder phrase = new StringBuilder();

		// We need to maintain the order of the values, so use two lists
		ArrayList<String> kanjiOrder = new ArrayList<String>();
		ArrayList<Integer> kanjiOffset = new ArrayList<Integer>();

		// First, iterate through the string and break it down into sub strings
		// based upon where it transitions from hiragana to kanji
		String kanji = card.getKanji();
		boolean hiraganaFound = false;
		for (int ndx = 0; ndx < kanji.length(); ndx++) {
			// Store the character
			char ch = kanji.charAt(ndx);
			phrase.append(ch);
			// Check for a boundary
			CharacterType type = ConfuserTools.checkCharacter(ch);
			if (type == CharacterType.Kanji && previous == CharacterType.Hiragana) {
				String value = phrase.toString();
				kanjiOrder.add(value.substring(0, value.length() - 1));
				kanjiOffset.add(offset);
				offset = ndx;
				phrase = new StringBuilder(String.valueOf(ch));
			}
			// Update the previous character type and the flag
			hiraganaFound = (type == CharacterType.Hiragana) ? true : hiraganaFound;
			previous = type;
		}
		// Store any remaining phrase information or exit if we didn't find any
		// hiragana that we can use to do the extension
		List<String> phrases = new ArrayList<String>();
		if (!hiraganaFound) {
			return phrases;
		}
		kanjiOrder.add(phrase.toString());
		kanjiOffset.add(offset);
		
		// Iterate through kana and use that to build out substrings
		for (int ndx = 0; ndx < kanjiOrder.size(); ndx++) {
			// Note the items in this pairing
			String kana = kanjiOrder.get(ndx);
			offset = kanjiOffset.get(ndx);
			// Discard this string if it is only a single character
			if (kana.length() <= 1) {
				continue;
			}
			// Get the hiragana for this substring
			char ch = kana.charAt(kana.length() - 1);
			String hiragana = card.getHiragana();
			// In the event that a phrase starts with a hiragana and ends with
			// kanji (e.g. お菓子  - sweets, candy) we can't easily find a
			// boundary so we should just press on through the rest of the 
			// phrase since we mith come to something else
			if (hiragana.indexOf(ch, offset) == -1) {
				continue;
			}
			hiragana = hiragana.substring(kanjiOffset.get(ndx), hiragana.indexOf(ch, offset) + 1);
			// Press on if we don't have at least two characters to work with
			if (hiragana.length() <= 1) {
				continue;
			}
			hiragana = hiragana.substring(1);
			// Replace the hiragana in the current kana with what we have
			String replacement = kana.replace(String.valueOf(ch), hiragana);
			if (replacement.equals(kana)) {
				continue;
			}
			// Generate the updated kanji and append them to the results
			phrase = new StringBuilder();
			for (String value : kanjiOrder) {
				if (value.equals(kana)) {
					phrase.append(kana.replace(String.valueOf(ch), hiragana));
				} else {
					phrase.append(value);
				}
			}
			phrases.add(phrase.toString());
		}

		// Return the results
		return phrases;
	}
	
	/**
	 * Get a list of kanji phrases that are similar to the one that has been
	 * provided. In the event that no kanji confusers for the given card then
	 * null will be returned, otherwise, confusers up to the provided count will
	 * be provided.
	 * 
	 * @param card
	 * @return
	 */
	public List<String> getKanjiSubsitution(String phrase) throws ConfuserException, IOException {
		// Read the confusers from the resource file
		List<String> confusers = readConfusers();
		// Iterate through the characters in this phrase
		List<String> phrases = new ArrayList<String>();
		for (int ndx = 0; ndx < phrase.length(); ndx++) {
			char ch = phrase.charAt(ndx);
			if (ConfuserTools.checkCharacter(ch) != CharacterType.Kanji) {
				continue;
			}
			// Iterate through each of the lines for confusers
			for (String confuser : confusers) {
				if (confuser.contains(String.valueOf(ch))) {
					phrases.addAll(getReplacements(phrase, ch, ndx, confuser));
					break;
				}
			}
		}
		// Return the confusers
		return phrases;		
	}
		
	/**
	 * Add or remove vowel elongation characters (ー) from the provided phrase. 
	 * This algorithm attempts to either add or remove a single character on a
	 * single pass through the entire phrase.
	 * 
	 * @param phrase The phrase to be manipulated.
	 * @return A list of valid manipulations of the provided phrase.
	 */
	public List<String> getKatakanaManiuplation(String phrase) {
		// The following are the parameters for the manipulation
		char choonpu = 'ー';
		char n = 'ン'; 
		char xtsu = 'っ';
		String invalidFollowers = "ャュョェ";
		// Start scanning through the phrase for relevant matches and either
		// add or remove the choopu as required
		List<String> phrases = new ArrayList<String>();
		for (int ndx = 0; ndx < phrase.length(); ndx++) {
			char ch = phrase.charAt(ndx);
			// Press on if we can't insert after this character
			if (ch == n || ch == xtsu) {
				continue;
			}
			// Check to make sure the next character is not an extension
			if (ndx != (phrase.length() - 1)) {
				char next = phrase.charAt(ndx + 1);
				if (next == choonpu || invalidFollowers.contains(String.valueOf(next))) {
					continue;
				}
			}
			// Are we doing a delete?
			if (ch == choonpu) {
				phrases.add(phrase.substring(0, ndx) + phrase.substring(ndx + 1));
				continue;
			}
			// We must be performing an insert instead
			phrases.add(insertCharacter(phrase, ndx, choonpu));
		}
		return phrases;
	}
	
	/**
	 * Manipulate the phrase provided to add or remove n characters (ん, ン) as
	 * appropriate.
	 * 
	 * @param phrase The phrase to be manipulated.
	 * @return A list of manipulations or an empty list if there is no valid
	 * work to be done.
	 */
	public List<String> getNManipulation(String phrase) throws ConfuserException {
		if (phrase == null || phrase.isEmpty()) {
			throw new ConfuserException("The phrase provided was null or empty."); 
		}
		// Determine if the phrase is in hiragana or katakana and adjust
		// our assumptions accordingly
		char n = 'ん';
		String characters = "なにぬねの";
		if (ConfuserTools.checkCharacter(phrase.charAt(0)) == CharacterType.Katakana) {
			n = 'ン';
			characters = "ナニヌネノ";
		}
		// Now start scanning through the phrase for relevant matches for this 
		// we are only focusing on the characters that are in the middle of 
		// the phrase
		List<String> phrases = new ArrayList<String>();
		for (int ndx = 1; ndx < phrase.length() - 1; ndx++) {
			char ch = phrase.charAt(ndx);
			// If this is an n character, remove it if the next is an n* sound
			if (ch == n) {
				char next = phrase.charAt(ndx + 1);
				if (characters.contains(String.valueOf(next))) {
					phrases.add(phrase.substring(0, ndx) + phrase.substring(ndx + 1));
					// Make sure we skip the next character to avoid doubling n's
					ndx++;
				}
			}
			// If this is an n* sound, add a n before it
			else if (characters.contains(String.valueOf(ch))) {
				phrases.add(phrase.substring(0, ndx) + n + phrase.substring(ndx));
			}
		}
		return phrases;
	}
	
	/**
	 * Generate all of the valid replacements for the parameters provided.
	 * 
	 * @param phrase The kanji phrase have the substitutions applied to.
	 * @param kanji The kanji that is to be replaced.
	 * @param index The index that the kanji exists at.
	 * @param replacements The kanji family as read from the confusers list.
	 * @return The list of updated kanji phrases.
	 */
	private List<String> getReplacements(String phrase, char kanji, int index, String replacements) {
		// Make sure the kanji provided does not appear in the list
		replacements = replacements.replace(String.valueOf(kanji), "");
		// Iterate through the replacements and generate new strings with 
		// the character at the index being replaced
		List<String> phrases = new ArrayList<String>();
		for (int ndx = 0; ndx < replacements.length(); ndx++) { 
			char replacement = replacements.charAt(ndx);
			// Make sure the phrase is built correctly
			if (index == 0) {
				phrases.add(String.valueOf(replacement) + phrase.substring(1));
				continue;
			}
			int offset = (index == 1) ? 1 : index - 1;
			if (index == (phrase.length() - 1)) {
					phrases.add(phrase.substring(0, offset) + String.valueOf(replacement));
			} else {
				String bah = phrase.substring(0, offset) + replacement + phrase.substring(index + 1);

				phrases.add(bah);
			}
		}
		return phrases;
	}
	
	/**
	 * Add or remove the small tsu (っ, ッ) from the phrase as warranted.
	 * 
	 * @param phrase The hiragana to be manipulated.
	 * @return A list of manipulations or an empty list if there is no valid
	 * work to be done.
	 */
	public List<String> getSmallTsuManiuplation(String phrase) {
		// The following are the parameters for xtsu (っ, ッ) manipulation
		char xtsu = 'っ';
		String characters = "かきくけこさしたちつてとはひふへほぱぴぷぺぽ";
		String invalidFollowers = "あいうえおんなにぬねのょ";
		if (ConfuserTools.checkCharacter(phrase.charAt(0)) == CharacterType.Katakana) {
			xtsu = 'ッ';
			characters = "カキクケコサシタチツテトハヒフヘホパピプペポ";
			invalidFollowers = "アイウエオンナニヌネノ";
		}
		// Now start scanning through the phrase for relevant matches for this 
		// we are only focusing on the characters that are in the middle of 
		// the phrase
		List<String> phrases = new ArrayList<String>();
		for (int ndx = 0; ndx < phrase.length(); ndx++) {
			char ch = phrase.charAt(ndx);
			// Get the next character and check to see if it is invalid, if so
			// we can just press on
			if (ndx != phrase.length() - 1) {
				char next = phrase.charAt(ndx + 1);
				if (invalidFollowers.contains(String.valueOf(next))) {
					continue;
				}
			}
			// If this is a small tsu character, remove it
			if (ch == xtsu) {
				phrases.add(phrase.substring(0, ndx) + phrase.substring(ndx + 1));
			}
			// If this is a matching sound, add a small tsu
			else if (characters.contains(String.valueOf(ch))) {
				// Are we at the end of the phrase?
				if ((ndx + 1) == phrase.length()) {
					continue;
				}
				// Make sure the next character is not the small tsu
				if (phrase.charAt(ndx + 1) == xtsu) {
					continue;
				}
				// Make sure the phrase is built correctly
				phrases.add(insertCharacter(phrase, ndx, xtsu));
			}
		}
		return phrases;
	}
		
	/**
	 * Insert the replacement character provided at the index indicated correctly.
	 * 
	 * @param phrase The phrase to be manipulated.
	 * @param ndx The index to make the insert at, this is treated as ndx + 1 for the
	 * actual insert operation.
	 * @param addition The character to use for the insertion.
	 * @return The original string with the indicated character inserted.
	 */
	private String insertCharacter(String phrase, int ndx, char addition) {
		if (ndx == 0) {
			return String.valueOf(phrase.charAt(0)) + addition + phrase.substring(1);
		} else if (ndx == (phrase.length() - 1)) {
			return phrase + addition;
		}
		return phrase.substring(0, ndx + 1) + addition + phrase.substring(ndx + 1);
	}
	
	/**
	 * Read the kanji families for confusers from the resource file.
	 * 
	 * @return The confuser families as a list of strings.
	 */
	private List<String> readConfusers() throws ConfuserException, IOException {
		// Open the black list for this language and check to see if it exists
		String path = CONFUSER_DIRECTORY + CONFUSER_LANGUAGE + CONFUSER_EXTENSION;
		InputStream confusers = ConfuserTools.class.getResourceAsStream(path);
		if (confusers == null) {
			throw new ConfuserException("Unable to open the confusers for, " + CONFUSER_LANGUAGE);
		}
		// Prepare the stream reader
		BufferedReader reader = null;
		InputStreamReader stream = null;
		try {
			// Open up the stream to be read
			stream = new InputStreamReader(confusers);
			reader = new BufferedReader(stream);
			// Read and process the contents
			List<String> results = new ArrayList<String>();
			String data;
			while ((data = reader.readLine()) != null) {
				if (data.isEmpty() || data.charAt(0) == '#') {
					continue;
				}
				results.add(data.replace(" ", ""));
			}
			return results;
		} catch (FileNotFoundException ex) {
			throw new ConfuserException("Unable to open the confusers for, " + CONFUSER_LANGUAGE);
		} catch (IOException ex) {
			throw new ConfuserException("An error occured while reading the next line", ex);
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (stream != null) {
				stream.close();
			}
		}
	}
}
