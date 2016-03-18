package com.openfluency.flashcard

import com.openfluency.language.Unit
import com.openfluency.media.Image
import com.openfluency.media.Audio
import com.openfluency.language.UnitMapping
import com.openfluency.language.Pronunciation
import com.openfluency.language.Alphabet

/**
* A Flashcard displays one of the possible combinations of associations that a Language Unit has. 
* Example:
* The unit 亜 in Kanji has 4 different meanings in english: "Asia", "rank next", "come after", and "-ous", each mapped through a UnitMapping.
* A Flashcard displays one of these UnitMappings. Additionally, every UnitMapping has many Images and many Pronunciations each with many Audio files.
* The Flashcard displays only of each.
*/
class Flashcard {
	/** A Flashcard represents a UnitMapping which maps two units of different Alphabets. The primaryAlphabet represents the Alphabet which this card teaches */
	Alphabet primaryAlphabet		
	/** The UnitMapping that this Flashcard displays */
	UnitMapping unitMapping 		
	/** One of the Pronunciations of the unit in this card */
	Pronunciation pronunciation 	
	/** The image to be displayed on the flashcard - this is optional */
	Image image 					
	/** Audio of the pronunciation - this is optional*/
	Audio audio 					
	/** the Deck a Flashcard belongs to - a flashcard only exists in the context of a deck */
	Deck deck 						

	Date dateCreated
	Date lastUpdated
	/**
	 *  Gets the primaray / focal language unit of the card - this is the symbol the user will learn
	 *  @Return the Unit representing the focal symbol
	 */
	// Front
	Unit getPrimaryUnit() {
		return unitMapping.unit1.alphabet == primaryAlphabet ? unitMapping.unit1 : unitMapping.unit2
	}
	/**
	 *  Gets the secondary / meaning language unit of the card - this is the 'translation' that the user knows
	 *  @Return the unit representing the meaning in a known language
	 */
	// Back
	Unit getSecondaryUnit() {
		return unitMapping.unit1.alphabet != primaryAlphabet ? unitMapping.unit1 : unitMapping.unit2
	}
    static constraints = {
    	audio nullable: true
    	image nullable: true
    }
}
