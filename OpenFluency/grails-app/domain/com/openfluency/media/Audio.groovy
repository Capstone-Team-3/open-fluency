package com.openfluency.media

import com.openfluency.language.Pronunciation

/**
* Every Unit has many Pronunciations in different Alphabets. Each Pronunciation can have different Audio files depending on the context (UnitMapping)
*/
class Audio {

	// User owner 	// The user that uploaded the audio. It's commented out since the user class is not created yet, Ben is taking care of this
	String url		// The URL or the audio file, probably an S3 url

	Pronunciation pronunciation 	// This is the Audio for a Pronunciation in some Alphabet

	Date dateCreated
	Date lastUpdated

    static constraints = {
    }
}
