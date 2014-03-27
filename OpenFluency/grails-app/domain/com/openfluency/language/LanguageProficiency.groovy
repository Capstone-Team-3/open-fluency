package com.openfluency.language

import com.openfluency.auth.User

class LanguageProficiency {

	Language language
	Proficiency proficiency 

    static constraints = {
    	language nullable: false
    	proficiency nullable: false
    }
}
