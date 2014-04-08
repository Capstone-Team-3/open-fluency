package com.openfluency.media

import com.openfluency.auth.User
import com.openfluency.language.Pronunciation

/**
* Every Unit has many Pronunciations in different Alphabets. Each Pronunciation can have different Audio files depending on the context (UnitMapping)
*/
class Audio { 

	User owner 	// The user that uploaded the audio. 
	String url		// The URL or the audio file, probably an S3 url
	byte[] audioWAV

	Pronunciation pronunciation 	// This is the Audio for a Pronunciation in some Alphabet

	Date dateCreated
	Date lastUpdated

    static constraints = {
    	audioWAV nullable: true, maxSize: 1000000
    	url nullable: true
    }

    String toString(){
    	"${pronunciation} -> ${url}"
    }

}
