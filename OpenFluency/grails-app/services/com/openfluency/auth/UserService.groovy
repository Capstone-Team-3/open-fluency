package com.openfluency.auth

import com.openfluency.auth.Role
import com.openfluency.language.LanguageProficiency
import com.openfluency.language.Language
import com.openfluency.language.Proficiency
import grails.transaction.Transactional

@Transactional
class UserService {

    def createUser(String username, String password, String email, String userTypeId, List<String> languageIds, List<String> proficiencyIds) {
    	// Create the user
    	def userInstance = new User(username: username, password: password, email: email, userType: Role.load(userTypeId))
    	userInstance.save(flush: true)

    	if(userInstance.hasErrors()) {
    		return userInstance
    	}

    	// Create the proficiencies
    	languageIds.eachWithIndex() { languageId, i ->
    		new LanguageProficiency(
    			language: Language.load(languageId), 
    			proficiency: Proficiency.load(proficiencyIds[i]),
    			user: userInstance
    			).save(flush: true)
    	}

    	return userInstance
    }
}
