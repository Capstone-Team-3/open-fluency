package com.openfluency.confuser

import com.openfluency.language.Alphabet
import com.openfluency.language.Language
import grails.test.mixin.TestFor
import spock.lang.Specification

/**s
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(ConfuserService)
@Mock([Language, Alphabet])
class ConfuserServiceSpec extends Specification {

	void "Test that getChineseAlphabet correctly identifies hanzi"() {
		given:
			def word = "没关系"
		and:
			def chinese = new Language(name: 'Chinese', code: 'CHN').save(failOnError: true)
			def hanzi = new Alphabet(name: 'Hanzi', language: chinese, code: "hanzi").save(failOnError: true)
			def pinyin = new Alphabet(name: "Pinyin", language: chinese, code: "pinyin").save(failOnError: true)
		when: 
			def alphabet = service.getChineseAlphabet(word)
		then:
			alphabet.code == "hanzi"
	}
	
	void "Test that getChineseAlphabet correctly identifies pinyin"() {
		given:
			def word = "méi guān xi"
		and:
			def chinese = new Language(name: 'Chinese', code: 'CHN').save(failOnError: true)
			def hanzi = new Alphabet(name: 'Hanzi', language: chinese, code: "hanzi").save(failOnError: true)
			def pinyin = new Alphabet(name: "Pinyin", language: chinese, code: "pinyin").save(failOnError: true)
		when:
			def alphabet = service.getChineseAlphabet(word)
		then:
			alphabet.code == "pinyin"
	}
}
