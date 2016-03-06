package com.openfluency.confuser;

import java.util.List;

import com.openfluency.language.Alphabet;

import cscie99.team2.lingolearn.server.confuser.CharacterType;
import cscie99.team2.lingolearn.server.confuser.Confuser;
import cscie99.team2.lingolearn.shared.Card;
import cscie99.team2.lingolearn.shared.error.ConfuserException;

public class JapaneseConfuser implements ConfuserInterface {

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
