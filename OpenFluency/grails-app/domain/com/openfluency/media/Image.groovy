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
	/** The user that uploaded the image. */
	User owner 		
	/** The URL point to the image - probably a Flickr link, but could be to anywhere accessible */
	String url		
	// The mapping (concept, translation...) that this Image represents
	UnitMapping unitMapping 	

	Date dateCreated
	Date lastUpdated

    static constraints = {
		unitMapping nullable: true
    }

    String toString(){
    	"${unitMapping} -> ${url}"
    }
}
