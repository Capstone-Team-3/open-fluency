package com.openfluency.confuser

import com.openfluency.course.Chapter;
import com.openfluency.language.Alphabet
import com.openfluency.language.Language
import cscie99.team2.lingolearn.server.confuser.PhraseType
import cscie99.team2.lingolearn.server.confuser.ConfuserTools
import grails.converters.JSON

class ConfuserController {

	
	def generate (String languageCode, String alphabetCode, String word, int number) {
		
		Language language = Language.findByCode(languageCode)
		
		Alphabet alphabet = null
		
		if (alphabetCode == null) {
			
			if (language.code == "JAP") {
				alphabet = getJapaneseAlphabet(word);
			}
			else if (language.code == "CHN") {
				alphabet = Alphabet.findByCode("hanzi")
			}
		}
		else {
			alphabet = Alphabet.findByCode(alphabetCode)
		}
		
		ConfuserFactory consuferFactory = new ConfuserFactory()
		ConfuserInterface confuser = consuferFactory.getConfuser(language)
		
		List<String> confusers = confuser.getConfusers(word, alphabet, number)
		
		render confusers as JSON
	}
	
	private Alphabet getJapaneseAlphabet(String phrase) {
		
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
