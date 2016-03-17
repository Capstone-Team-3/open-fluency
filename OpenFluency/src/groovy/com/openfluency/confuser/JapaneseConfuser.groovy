package com.openfluency.confuser;

import com.openfluency.language.Alphabet;

import cscie99.team2.lingolearn.server.confuser.CharacterType;
import cscie99.team2.lingolearn.server.confuser.Confuser;
import cscie99.team2.lingolearn.shared.Card;
import cscie99.team2.lingolearn.shared.error.ConfuserException;

class JapaneseConfuser implements ConfuserInterface {

	/**
	 * Get a random list of confusers of given type limited to the count 
	 * provided, these results are checked against the black list to 
	 * ensure that nothing inappropriate is returned. 
	 * 
	 * @param word The word (in Japanese) that you want to generate confusers for
	 * @param alphabet The alphabet (Kanji, Katakana, or Hiragana) that the word is encoded in
	 * @param count The number that should be returned, if -1 is provided then
	 * all results are returned without processing.
	 * @return List of strings containing the confuser results, The length of the list will be between 0 and the requested count.
	 */
	List<String> getConfusers(String word, Alphabet alphabet, int count) {
		
		Confuser confuser = new Confuser();
		
		Card card = new Card(kanji: "", hiragana: "", katakana: "");
		
		CharacterType characterType = CharacterType.Unknown;
		
		if (alphabet.code == "kanji") {
			characterType = CharacterType.Kanji;
			card.kanji = word;
		}
		else if (alphabet.code == "ja_on") {
			characterType = CharacterType.Katakana;
			card.katakana = word;
		}
		else if (alphabet.code == "ja_kun") {
			characterType = CharacterType.Hiragana;
			card.hiragana = word;
		}
		
		return confuser.getConfusers(card, characterType, count);
	}

}
