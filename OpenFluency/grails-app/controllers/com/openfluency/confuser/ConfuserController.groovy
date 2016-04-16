package com.openfluency.confuser

import com.openfluency.language.Alphabet
import com.openfluency.language.Language
import grails.converters.JSON

class ConfuserController {

	def confuserService
	
	def generate (String languageCode, String alphabetCode, String word, int number) {
		
		Language language = Language.findByCode(languageCode)
		
		Alphabet alphabet = null
		
		if (alphabetCode == null) {
			alphabet = confuserService.getAlphabet(word, language);
		}
		else {
			alphabet = Alphabet.findByCode(alphabetCode)
		}
		
		ConfuserFactory consuferFactory = new ConfuserFactory()
		ConfuserInterface confuser = consuferFactory.getConfuser(language)
		
		List<String> confusers = confuser.getConfusers(word, alphabet, number)
		
		render confusers as JSON
	}
}




