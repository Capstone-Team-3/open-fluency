package com.openfluency.confuser

import com.openfluency.language.Alphabet;
import com.openfluency.language.Language
import cscie99.team2.lingolearn.server.confuser.ConfuserTools
import cscie99.team2.lingolearn.server.confuser.PhraseType


import grails.transaction.Transactional

@Transactional
class ConfuserService {

	Alphabet getAlphabet(String phrase, Language language) {
		
		if (language == null) {
			return null
		}
		
		Alphabet alphabet = null;
		
		if (language.code == "JAP") {
			alphabet = getJapaneseAlphabet(phrase);
		}
		else if (language.code == "CHN") {
			alphabet = getChineseAlphabet(phrase);
		}
		
		return alphabet
	}
	
	Alphabet getChineseAlphabet(String phrase) {
		
		Alphabet alphabet = null;
		
		boolean hanzi = false;
		
		for (int ndx = 0; ndx < phrase.length(); ndx++) {
			if (Character.UnicodeBlock.of((char)phrase[ndx]) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) {
				hanzi = true;
			}
		}
		
		if (hanzi) {
			alphabet = Alphabet.findByCode("hanzi")
		}
		else {
			alphabet = Alphabet.findByCode("pinyin")
		}
		
		return alphabet;
	}
	
	Alphabet getJapaneseAlphabet(String phrase) {
		
		Alphabet alphabet = null;
		
		PhraseType phraseType = ConfuserTools.checkPhrase(phrase)
		
		if (phraseType == PhraseType.Hiragana) {
			alphabet = Alphabet.findByCode("ja_kun")
		}
		else if (phraseType == PhraseType.Katakana) {
			alphabet = Alphabet.findByCode("ja_on")
		}
		else if (phraseType == PhraseType.Kanji) {
			alphabet = Alphabet.findByCode("kanji")
		}
		
		return alphabet;
	}
}
