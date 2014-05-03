package com.openfluency.language

import com.openfluency.auth.User

class LanguageProficiency {

	User user
	Language language
	Proficiency proficiency 

    static constraints = {
    }

    String toString(){
    	return "${language}:${proficiency}"
    }
}
