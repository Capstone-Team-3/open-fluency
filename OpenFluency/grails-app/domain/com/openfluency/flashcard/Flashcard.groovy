package com.openfluency.flashcard

import com.openfluency.media.Image
import com.openfluency.media.Audio
import com.openfluency.language.UnitMapping
import com.openfluency.language.Pronunciation
import com.openfluency.language.Alphabet

/**
* A Flashcard displays one of the possible combinations of associations that a unit has. 
* Example:
* The unit 亜 in Kanji has 4 different meanings in english: "Asia", "rank next", "come after", and "-ous", each mapped through a UnitMapping.
* A Flashcard displays one of these UnitMappings. Additionally, every UnitMapping has many Images and many Pronunciations each with many Audio files.
* The Flashcard displays only of each.
*/
class Flashcard {

	Alphabet primaryAlphabet		// A Flashcard represents a UnitMapping which maps two units of different Alphabets. The primaryAlphabet represents the Alphabet which this card teaches
	UnitMapping unitMapping 		// The mapping that this Flashcard displays
	Pronunciation pronunciation 	// One of the Pronunciations of the unit in this card

	Image image 	// The image to be displayed on the flashcard
	Audio audio 	// Audio of the pronunciation

	Date dateCreated
	Date lastUpdated

    static constraints = {
    }
}
