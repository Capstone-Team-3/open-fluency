package com.openfluency.media

import com.openfluency.auth.User
import com.openfluency.flashcard.Flashcard

/**
 *  The Customization class is used to map User customizations to Flashcards.
 *  While technically Audio and Image are not required, it would not make sense to create one with neither
 */
class Customization {
    /** the User who created and will see the customization */
	User owner
    /** the Flashcard the customization is for */
	Flashcard card
    /** The audio recording attached to the customization */
	Audio audioAssoc
    /** the image attached to the customization */
	Image imageAssoc

    static constraints = {
    	owner blank: false, nullable: false
    	card blank: false, nullable: false
    	audioAssoc nullable: true
    	imageAssoc nullable: true 
    }
}
