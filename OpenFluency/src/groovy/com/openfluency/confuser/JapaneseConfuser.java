package com.openfluency.confuser;

import java.util.List;

import com.openfluency.language.Alphabet;

import cscie99.team2.lingolearn.server.confuser.CharacterType;
import cscie99.team2.lingolearn.server.confuser.Confuser;
import cscie99.team2.lingolearn.shared.Card;
import cscie99.team2.lingolearn.shared.error.ConfuserException;

public class JapaneseConfuser implements ConfuserInterface {

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
	@Override
	public List<String> getConfusers(String word, Alphabet alphabet, int count) throws ConfuserException {
		
		Confuser confuser = new Confuser();
		
		Card card = new Card();
		
		CharacterType characterType = CharacterType.Unknown;
		
		if (alphabet.getCode().equals("kanji")) {
			characterType = CharacterType.Kanji;
			card.setKanji(word);
		}
		else if (alphabet.getCode().equals("ja_on")) {
			characterType = CharacterType.Katakana;
			card.setKatakana(word);
		}
		else if (alphabet.getCode().equals("ja_kun")) {
			characterType = CharacterType.Hiragana;
			card.setHiragana(word);
		}
		
		return confuser.getConfusers(card, characterType, count);
	}

}
