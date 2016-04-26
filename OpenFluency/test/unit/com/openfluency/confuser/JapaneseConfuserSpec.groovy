package com.openfluency.confuser

import com.openfluency.language.Alphabet
import com.openfluency.language.Language
import spock.lang.Specification
import spock.lang.Ignore

import cscie99.team2.lingolearn.shared.error.ConfuserException;

class JapaneseConfuserSpec extends Specification {

    void "Test rule that cannot have long 'nothing' before a word. Should have the long dash inside the parentheses"() {
		given: "A word written in Katakana with parentheses"
			String word = '(センチ)メートル'
		when: "Confusers are generated for this word"
			Language japanese = new Language(name: 'Japanese', code: 'JAP')
			Alphabet katakana = new Alphabet(name: 'Katakana', language: japanese, code: "ja_on")
			JapaneseConfuser japaneseConfuser = new JapaneseConfuser()
			List<String> confusers = japaneseConfuser.getConfusers(word, katakana, -1)
		then: "Correct results are returned"
			confusers != null
			!confusers.contains('(ーセンチ)メートル')
			!confusers.contains('(センチ)ーメートル')			
    }
	
	void "Test rule that cannot have small tsu and long vowel"() {
		given: "A word written in Katakana"
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
		given: "A word written in Katakana"
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
		given: "A word written in Katakana"
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
		given: "A word written in Katakana"
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
		given: "A word written in Katakana"
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
	
	void "no small tsu before r"() {
	given: "A word written in Katakana"
		String word = "サルサ"
	when: "Confusers are generated for these words"
		Language japanese = new Language(name: 'Japanese', code: 'JAP')
		Alphabet katakana = new Alphabet(name: 'Katakana', language: japanese, code: "ja_on")
		JapaneseConfuser japaneseConfuser = new JapaneseConfuser()
		List<String> confusers = japaneseConfuser.getConfusers(word, katakana, -1)
	then: "Correct results are returned"
		confusers != null
		!confusers.contains('サッルーサ')
	}
	
	void "no long vowel before small tsu"() {
		given: "words written in Hiragana"
			String word1 = "とし"
			String word2 = "とき"
		when: "Confusers are generated for these words"
			Language japanese = new Language(name: 'Japanese', code: 'JAP')
			Alphabet hiragana = new Alphabet(name: 'Hiragana', language: japanese, code: "ja_kun")
			JapaneseConfuser japaneseConfuser = new JapaneseConfuser()
			List<String> confusers1 = japaneseConfuser.getConfusers(word1, hiragana, -1)
			List<String> confusers2 = japaneseConfuser.getConfusers(word2, hiragana, -1)
		then: "Correct results are returned"
			confusers1 != null
			!confusers1.contains('とうっし')
			confusers2 != null
			!confusers2.contains('とうっき')
	}
	
	void "there are more options for this such as the removal of the long vowel"() {
		given: "A word written in Hiragana"
			String word = "わふう"
		when: "Confusers are generated for these words"
			Language japanese = new Language(name: 'Japanese', code: 'JAP')
			Alphabet hiragana = new Alphabet(name: 'Hiragana', language: japanese, code: "ja_kun")
			JapaneseConfuser japaneseConfuser = new JapaneseConfuser()
			List<String> confusers = japaneseConfuser.getConfusers(word, hiragana, -1)
		then: "Correct results are returned"
			confusers != null
			confusers.contains('わあふう')
			confusers.contains('わふ') // removal of the long vowel
	}
	
	void "a third option would be to have both long"() {
		given: "A word written in Hiragana"
			String word = "みぎ"
		when: "Confusers are generated for these words"
			Language japanese = new Language(name: 'Japanese', code: 'JAP')
			Alphabet hiragana = new Alphabet(name: 'Hiragana', language: japanese, code: "ja_kun")
			JapaneseConfuser japaneseConfuser = new JapaneseConfuser()
			List<String> confusers = japaneseConfuser.getConfusers(word, hiragana, -1)
		then: "Correct results are returned"
			confusers != null
			confusers.contains('みいぎ')
			confusers.contains('みぎい')
			confusers.contains('みいぎい')
	}
	
	void "If pass non-Japanese alphabet to JapaneseConfuser then ConfuserException will be thrown"() {
		setup:
			String word = 'shì'
			Language chinese = new Language(name: 'Chinese', code: 'CHN')
			Alphabet pinyin = new Alphabet(name: "Pinyin", language: chinese, code: "pinyin")
			JapaneseConfuser japaneseConfuser = new JapaneseConfuser()
		
		when:
			japaneseConfuser.getConfusers(word, pinyin, -1)
			
		then:
			thrown ConfuserException
	}
	
}
