package com.openfluency.confuser;

import com.openfluency.language.Alphabet;

import cscie99.team2.lingolearn.server.confuser.CharacterType;
import cscie99.team2.lingolearn.server.confuser.ConfuserTools;
import cscie99.team2.lingolearn.shared.error.ConfuserException;

class ChineseConfuser implements ConfuserInterface {

	// The path to the directory of confusers
	private final static String CONFUSER_DIRECTORY = "/confusers/";
	
	// The extension for the files
	private final static String CONFUSER_EXTENSION = ".confusers";
	
	// The language code we are working with
	private final static String CONFUSER_LANGUAGE = "ch";
	
	/**
	 * Get a random list of confusers of given type limited to the count 
	 * provided, these results are checked against the black list to 
	 * ensure that nothing inappropriate is returned. 
	 * 
	 * @param word The word (in Chinese) that you want to generate confusers for
	 * @param alphabet The alphabet (Hanzi) that the word is encoded in
	 * @param count The number that should be returned, if -1 is provided then
	 * all results are returned without processing.
	 * @return List of strings containing the confuser results, The length of the list will be between 0 and the requested count.
	 */
	List<String> getConfusers(String word, Alphabet alphabet, int count) {
		
		try {
			List<String> results = [];
			
			if (alphabet.code != "hanzi") {
				throw new ConfuserException("Invalid alphabet (" + alphabet.getCode() +" ).");
			}
		
			results.addAll(getHanziSubsitution(word));
			
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
	 * Get a list of hanzi phrases that are similar to the one that has been
	 * provided. In the event that no hanzi confusers for the given card then
	 * null will be returned, otherwise, confusers up to the provided count will
	 * be provided.
	 * 
	 * @param card
	 * @return
	 */
	List<String> getHanziSubsitution(String phrase) {
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
	 * Generate all of the valid replacements for the parameters provided.
	 * 
	 * @param phrase The Hanzi phrase have the substitutions applied to.
	 * @param kanji The Hanzi that is to be replaced.
	 * @param index The index that the Hanzi exists at.
	 * @param replacements The Hanzi family as read from the confusers list.
	 * @return The list of updated Hanzi phrases.
	 */
	private List<String> getReplacements(String phrase, char kanji, int index, String replacements) {
		// Make sure the Hanzi provided does not appear in the list
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
	 * Read the Hanzi families for confusers from the resource file.
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
