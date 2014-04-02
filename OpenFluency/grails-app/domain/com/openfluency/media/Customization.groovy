package com.openfluency.media

import com.openfluency.auth.User
import com.openfluency.flashcard.Flashcard

class Customization {

	User owner
	Flashcard card
	Audio audioAssoc
	Image imageAssoc

    static constraints = {
    	owner blank: false, nullable: false
    	card blank: false, nullable: false
    	audioAssoc nullable: true
    	imageAssoc nullable: true 
    }
}
