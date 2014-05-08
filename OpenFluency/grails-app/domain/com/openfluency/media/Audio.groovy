package com.openfluency.media

import com.openfluency.auth.User
import com.openfluency.language.Pronunciation

/**
*  Audio objects contain audio clips for specific pronunciations.  Size is restricted to 1MB.
*/
class Audio { 
    /** the User who created the audio */
	User owner
    /** an optional string field that can link to a url */ 
	String url
    /** byte array for storing raw audio recordings */
	byte[] audioWAV
    /** the pronunciation that the audio is tied to */
	Pronunciation pronunciation

	Date dateCreated
	Date lastUpdated

    static constraints = {
    	audioWAV nullable: true, maxSize: 1000000
    	url nullable: true
    }

    String toString(){
    	"${pronunciation}"
    }

}
