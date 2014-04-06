package com.openfluency.flashcard

import com.openfluency.auth.User

/**
* When a user wants to share one of it's own Decks with another user, a Share is
* created. A Share is also created when a user adds someone else's Deck to it's own.
*/
class Share {

	Deck deck
	User receiver
	
    static constraints = {
    }
}
