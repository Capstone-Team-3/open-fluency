package com.openfluency.media

import com.openfluency.language.UnitMapping
import com.openfluency.auth.User

/**
* An Image represents a UnitMapping between two Units. 
* Example:
* The unit 案 in Kanji has several different meanings in english, including "bench" and "table".
* Each mapping represents how the units are used in different contexts, and will have one or many Images associated.
*/
class Image { 

	User owner 		// The user that uploaded the image.
	String url		// The URL or the image, probably an S3 url

	UnitMapping unitMapping 	// The mapping that this Image represents

	Date dateCreated
	Date lastUpdated

    static constraints = {
    }

    String toString(){
    	"${unitMapping} -> ${url}"
    }
}
