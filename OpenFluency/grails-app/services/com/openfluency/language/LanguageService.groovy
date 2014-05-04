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
	void loadLanguage(String dictionaryUrl, Alphabet sourceAlphabet, Alphabet targetAlphabet) {

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

		def dictionaryText = new File(dictionaryUrl).text
		
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

	/**
	* Search for units of a given language, who's matching unit contains the given keyword
	*/
	Map searchUnits(Long languageId, String keyword, Long offset) {
		log.info "Searching Units with languageId: $languageId and Keywords: $keyword"

		def query = "FROM UnitMapping WHERE (unit1.alphabet.language.id = ${languageId} OR unit2.alphabet.language.id = ${languageId})"

		if(keyword) {
			query += " AND (unit1.literal LIKE '%${keyword}%' OR unit2.literal LIKE '%${keyword}%')"
		}

		log.info query

		offset = offset ? offset : 0

		// Get all the unit mappings that map the searched params and then collect the unit for the given language
		def unitInstanceList = UnitMapping.executeQuery(query, [max: 10, offset: offset]).collect {
			it.unit1.alphabet.language.id == languageId ? it.unit1 : it.unit2
		}

		// Get the counts
		Integer unitCount = UnitMapping.executeQuery("SELECT COUNT(id) $query")[0]

		return [unitInstanceList: unitInstanceList, unitCount: unitCount]
	}

	/**
	* Utility to convert a String to an Int
	* @return the number if conversion was succesful, null otherwise
	*/
	Integer toInt(String stringVal) {
		return (stringVal && stringVal.isNumber()) ? (stringVal as Integer) : null
	}
}
