package com.openfluency.confuser

import com.openfluency.language.Alphabet
import com.openfluency.language.Language
import spock.lang.Specification;

class ChineseConfuserSpec extends Specification {
	void "Test pin1yin1 tone substitution with word 'yes'"() {		given: "The word 'yes' written in pin1yin1"			String word = 'shi4'		when: "Pinyin Tone substitution generated for this word"			Language chinese = new Language(name: 'Chinese', code: 'CHN')			Alphabet pinyin = new Alphabet(name: "Pinyin", language: chinese, code: "pinyin")			ChineseConfuser japaneseConfuser = new ChineseConfuser()			List<String> confusers = japaneseConfuser.getPinyinToneSubstitution(word)		then: "Correct results are returned"			confusers != null			confusers.contains("shi1")
			confusers.contains("shi2")
			confusers.contains("shi3")
			//confusers.contains("shi")	}
	
	void "Test pin1yin1 tone substitution with word 'no'"() {
		given: "The word 'yes' written in pin1yin1"
			String word = 'bu2 shi4'
		when: "Pinyin Tone substitution generated for this word"
			Language chinese = new Language(name: 'Chinese', code: 'CHN')
			Alphabet pinyin = new Alphabet(name: "Pinyin", language: chinese, code: "pinyin")
			ChineseConfuser japaneseConfuser = new ChineseConfuser()
			List<String> confusers = japaneseConfuser.getPinyinToneSubstitution(word)
		then: "Correct results are returned"
			confusers != null
			confusers.contains("bu1 shi1")
			confusers.contains("bu1 shi2")
			confusers.contains("bu1 shi3")
			confusers.contains("bu1 shi4")
			//confusers.contains("bu1 shi")
			
			confusers.contains("bu2 shi1")
			confusers.contains("bu2 shi2")
			confusers.contains("bu2 shi3")
			//confusers.contains("bu2 shi")
			
			confusers.contains("bu3 shi1")
			confusers.contains("bu3 shi2")
			confusers.contains("bu3 shi3")
			confusers.contains("bu3 shi4")
			//confusers.contains("bu3 shi")
			
			confusers.contains("bu4 shi1")
			confusers.contains("bu4 shi2")
			confusers.contains("bu4 shi3")
			confusers.contains("bu4 shi4")
			//confusers.contains("bu4 shi")
			
			//confusers.contains("bu shi1")
			//confusers.contains("bu shi2")
			//confusers.contains("bu shi3")
			//confusers.contains("bu shi4")
			//confusers.contains("bu shi")
	}
	
	void "Test pīnyīn tone substitution with word 'yes'"() {
		given: "The word 'yes' written in pīnyīn"
			String word = 'shì'
		when: "Pinyin Tone substitution generated for this word"
			Language chinese = new Language(name: 'Chinese', code: 'CHN')
			Alphabet pinyin = new Alphabet(name: "Pinyin", language: chinese, code: "pinyin")
			ChineseConfuser japaneseConfuser = new ChineseConfuser()
			List<String> confusers = japaneseConfuser.getPinyinToneSubstitution(word)
		then: "Correct results are returned"
			confusers != null
			confusers.contains("shī")
			confusers.contains("shí")
			confusers.contains("shǐ")
			confusers.contains("shi")
	}
	
	void "Test pīnyīn tone substitution with word 'no'"() {
		given: "The word 'yes' written in pīnyīn"
			String word = 'bú shì'
		when: "Pinyin Tone substitution generated for this word"
			Language chinese = new Language(name: 'Chinese', code: 'CHN')
			Alphabet pinyin = new Alphabet(name: "Pinyin", language: chinese, code: "pinyin")
			ChineseConfuser japaneseConfuser = new ChineseConfuser()
			List<String> confusers = japaneseConfuser.getPinyinToneSubstitution(word)
		then: "Correct results are returned"
			confusers != null
			confusers.contains("bū shī")
			confusers.contains("bū shí")
			confusers.contains("bū shǐ")
			confusers.contains("bū shì")
			confusers.contains("bū shi")
			
			confusers.contains("bú shī")
			confusers.contains("bú shí")
			confusers.contains("bú shǐ")
			confusers.contains("bú shi")
			
			confusers.contains("bǔ shī")
			confusers.contains("bǔ shí")
			confusers.contains("bǔ shǐ")
			confusers.contains("bǔ shì")
			confusers.contains("bǔ shi")
			
			confusers.contains("bù shī")
			confusers.contains("bù shí")
			confusers.contains("bù shǐ")
			confusers.contains("bù shì")
			confusers.contains("bù shi")
			
			confusers.contains("bu shī")
			confusers.contains("bu shí")
			confusers.contains("bu shǐ")
			confusers.contains("bu shì")
			confusers.contains("bu shi")
	}
}
