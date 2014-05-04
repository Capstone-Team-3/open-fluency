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
				def literal = it.toString()
				// If it ends with a number, remove the number
				if(literal.matches("^.+?\\d\$")) {
					literal = literal.substring(0, literal.length() - 1)
				}
				new Pronunciation(unit: unit, alphabet: Alphabet.findByCode(it.@r_type), literal: literal).save()
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

	/**
    * Find an existing unit with the given literal in the given language or create one
    */
    Unit getUnit(String literal, Language language) {
        
        Unit unit
        
        // First check if there's an existing unit for the symbol
        def existingSymbols = Unit.withCriteria {
            eq('literal', literal)
            alphabet {
                eq('language', language)
            }
        }

        if(existingSymbols) {
            unit = existingSymbols[0]
        }
        else {
            unit = new Unit(literal: literal, alphabet: Alphabet.findByLanguage(language)).save()
        }

        return unit
    }

    /**
    * Find an existing pronunciation by literal and by unit
    */
    Pronunciation getPronunciation(String literal, Unit unit, Language language) {
       
        Pronunciation pronunciation

        // Check if there is a pronunciation for this literal
        def existingPronunciations = Pronunciation.withCriteria {
            eq('literal', literal)
            eq('unit', unit)
            alphabet {
                eq('language', language)
            }
        }

        if(existingPronunciations) {
            pronunciation = existingPronunciations[0]
        }
        else {
            pronunciation = new Pronunciation(literal: literal, alphabet: Alphabet.findByLanguage(language), unit: unit).save()
        }

        return pronunciation
    }

    /**
    * Find a unit mapping between the two given units or create it
    */
    UnitMapping getUnitMapping(Unit unit1, Unit unit2) {
    	
    	def unitMapping

    	// Check direction 1
    	unitMapping = UnitMapping.findByUnit1AndUnit2(unit1, unit2)

    	// Check direction 2
    	if(!unitMapping) {
    		unitMapping = UnitMapping.findByUnit1AndUnit2(unit2, unit1)

    		// Not found, create it
    		if(!unitMapping) {
    			unitMapping = new UnitMapping(unit1: unit1, unit2: unit2).save()
    		}
    	}

    	return unitMapping
    }
}
