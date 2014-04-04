package com.openfluency.language

import grails.transaction.Transactional

@Transactional
class LanguageService {

	/**
	* Load a language into the system
	* @param dictionaryUrl: path to the file that contains the dictionary XML
	* @param sourceAlphabet: the alphabet being created
	* @param targetAlphabet: the alphabet of the meanings 
	*/
	void loadLanguage(String dictionaryUrl, Alphabet sourceAlphabet, Alphabet targetAlphabet, boolean local=false) {

		// Format of the XML is as follows:
		// <dictionary>
    	// 		...
    	//		<ch>
		// 			<uc>4e9c</uc>					--> literal value - separate chars are separated by $ (4e9c$5f9d...)
		// 			<gr>8</gr>						--> Grade in which its taught
		// 			<sc>7</sc>						--> stroke count
		// 			<freq>1509</freq>				--> frequency of usage
		// 			<rd r_type="pinyin">ya4</rd>	--> readings
		// 			<rd r_type="ja_on">ア</rd>
		// 			<rd r_type="ja_kun">つ.ぐ</rd>	
		// 			<mn>Asia</mn>					--> meanings
		// 			<mn>rank next</mn>
		// 			<mn>come after</mn>
		// 			<mn>-ous</mn>
		// 		</ch>
		// 		...
		// </dictionary>

		def dictionaryText = local ? new File(dictionaryUrl).text : dictionaryUrl.toURL().text
		
		def dictionary = new XmlSlurper().parseText(dictionaryText)
		int i = 0
		for(ch in dictionary.ch) {
			// Find or create Unit
			Unit unit = getUnit(sourceAlphabet, ch.uc.toString(), ch.gr.toString(), ch.sc.toString(), ch.freq.toString()).save()

			// For every reading in this character, create a unit mapping
			ch.mn.each {
				Unit otherUnit = getUnit(targetAlphabet, it.toString(), null, null, null).save()
				new UnitMapping(unit1: unit, unit2: otherUnit).save()
			}

			// For every reading in this character, create a pronunciation
			ch.rd.each {
				new Pronunciation(unit: unit, alphabet: Alphabet.findByCode(it.@r_type), literal: it.toString()).save()
			}

			log.info "Loaded ${i++}: ${ch.uc}"
		}
	}

	/**
	* Utility to retrieve a Unit. If the unit exists (found by literal), then return it, otherwise create it and then return it
	*/
	Unit getUnit(Alphabet alphabet, String literal, String grade, String strokeCount, String frequency) {
		
		// Check if the unit exists (by literal)
		Unit unit = Unit.findByLiteral(literal)

		// If the unit doesn't exist yet, create it
		if(!unit) {
			unit = new Unit(alphabet: alphabet, literal: literal, grade: toInt(grade), strokeCount: toInt(strokeCount), frequency: toInt(frequency)).save(failOnError: true)				
		}

		return unit
	}

	List<Unit> searchUnits(Long alphabetId, String keyword) {
		log.info "Searching Units with alphabetId: $alphabetId and Keywords: $keyword"

		Unit.withCriteria {

            // Apply language criteria
            if(alphabetId) {
                alphabet {
                    eq('id', alphabetId)
                }
            }

            // Search using keywords in any meaning
            // this is going to be harder since we have to search through all the meanings
        }
	}

	/**
	* Utility to convert a String to an Int
	* @return the number if conversion was succesful, null otherwise
	*/
	Integer toInt(String stringVal) {
		return (stringVal && stringVal.isNumber()) ? (stringVal as Integer) : null
	}
}
