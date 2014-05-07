package com.openfluency.language

import com.openfluency.auth.User

/**
 *  LanguageProficiency allows the system to map out a user's various language proficicencies (self reported)
 */
class LanguageProficiency {
    /** the User for whom the proficiency is recorded */
	User user
    /** the Language pertaining to the proficiency */
	Language language
    /** the user's proficiency level in said language */
	Proficiency proficiency 

    static constraints = {
    }

    String toString(){
    	return "${language}:${proficiency}"
    }

    /**
     * @Return a string containing the language id paired to the proficiency
     */
    String getLanguageProficiencyMap(){
    	return "${language.id}:${proficiency}"
    }
}
