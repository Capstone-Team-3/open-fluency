package com.openfluency.confuser

import com.openfluency.language.Alphabet
import com.openfluency.language.Language
import spock.lang.Specification


class JapaneseConfuserSpec extends Specification {

    void "Test rule that cannot have long 'nothing' before a word. Should have the long dash inside the parentheses"() {
		given: "A word written in Katakana with parentheses"
			String word = '(センチ)メートル'
		when: "Confusers are generated for this word"
			Language japanese = new Language(name: 'Japanese', code: 'JAP')
			Alphabet katakana = new Alphabet(name: 'Katakana', language: japanese, code: "ja_on")
			JapaneseConfuser japaneseConfuser = new JapaneseConfuser()
			String[] confusers = japaneseConfuser.getConfusers(word, katakana, -1)
		then: "Correct results are returned"
			confusers != null
			!confusers.contains('(ーセンチ)メートル')
			!confusers.contains('(センチ)ーメートル')			
    }
	
	void "Test rule that cannot have small tsu and long vowel"() {
		given: "A word written in Katana"
			String word = 'クーデター'
		when: "Confusers are generated for this word"
			Language japanese = new Language(name: 'Japanese', code: 'JAP')
			Alphabet katakana = new Alphabet(name: 'Katakana', language: japanese, code: "ja_on")
			JapaneseConfuser japaneseConfuser = new JapaneseConfuser()
			List<String> confusers = japaneseConfuser.getConfusers(word, katakana, -1)
		then: "Correct results are returned"
			confusers != null
			!confusers.contains('クッーデター')
			!confusers.contains('クーデタッー')
	}
	
	void "Test rule that cannot have long with small tsu"() {
		given: "A word written in Katana"
			String word = 'デッサン'
		when: "Confusers are generated for this word"
			Language japanese = new Language(name: 'Japanese', code: 'JAP')
			Alphabet katakana = new Alphabet(name: 'Katakana', language: japanese, code: "ja_on")
			JapaneseConfuser japaneseConfuser = new JapaneseConfuser()
			List<String> confusers = japaneseConfuser.getConfusers(word, katakana, -1)
		then: "Correct results are returned"
			confusers != null
			!confusers.contains('デーッサン')
	}
	
	void "Test rule that no small tsu before vowels, only consonants"() {
		given: "A word written in Katana"
			String word = 'マリファナ'
		when: "Confusers are generated for this word"
			Language japanese = new Language(name: 'Japanese', code: 'JAP')
			Alphabet katakana = new Alphabet(name: 'Katakana', language: japanese, code: "ja_on")
			JapaneseConfuser japaneseConfuser = new JapaneseConfuser()
			List<String> confusers = japaneseConfuser.getConfusers(word, katakana, -1)
		then: "Correct results are returned"
			confusers != null
			!confusers.contains('マリフッァナ')
	}
	
	void "Test rule that the small tsu is only before the next consonants and so it cannot occur at the end."() {
		given: "A word written in Katana"
			String word = 'タバコ'
		when: "Confusers are generated for this word"
			Language japanese = new Language(name: 'Japanese', code: 'JAP')
			Alphabet katakana = new Alphabet(name: 'Katakana', language: japanese, code: "ja_on")
			JapaneseConfuser japaneseConfuser = new JapaneseConfuser()
			List<String> confusers = japaneseConfuser.getConfusers(word, katakana, -1)
		then: "Correct results are returned"
			confusers != null
			!confusers.contains('タバコッ')
	}
	
	void "Cannot have duplicate confuser words"() {
		given: "A word written in Katana"
			String word = 'サンバ'
		when: "Confusers are generated for this word"
			Language japanese = new Language(name: 'Japanese', code: 'JAP')
			Alphabet katakana = new Alphabet(name: 'Katakana', language: japanese, code: "ja_on")
			JapaneseConfuser japaneseConfuser = new JapaneseConfuser()
			List<String> confusers = japaneseConfuser.getConfusers(word, katakana, -1)
		then: "The confuser list doesn't contain duplicates"
			List<String> uniqueList = new ArrayList<String>(confusers)
			uniqueList.unique()
			uniqueList.size() == confusers.size()
	}
}
