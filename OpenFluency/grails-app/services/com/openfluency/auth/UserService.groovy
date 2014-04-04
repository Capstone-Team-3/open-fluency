package com.openfluency.auth

import com.openfluency.auth.Role
import com.openfluency.language.LanguageProficiency
import com.openfluency.language.Language
import com.openfluency.language.Proficiency
import grails.transaction.Transactional

@Transactional
class UserService {

    def createUser(String username, String password, String email, String userTypeId, List<String> languageIds, List<String> proficiencyIds) {        
        // Find native language ID to pass to userInstance User constructor
        String nativeLangId
        languageIds.eachWithIndex() { languageId, i ->
            if (proficiencyIds[i] == "1"){ // native
                nativeLangId = languageId
            }
        }

        // Create the user
    	def userInstance = new User(username: username, password: password, email: email, userType: Role.load(userTypeId), nativeLanguage: Language.load(nativeLangId))
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

        // Create the role mappings
        UserRole.create userInstance, userInstance.userType, true

    	return userInstance
    }

    def editUser(User userInstance,List<String> languageIds, List<String> proficiencyIds){
        userInstance.save(flush: true)

        //remove old proficiencies for the user
        List<LanguageProficiency> oldProficiencyList = LanguageProficiency.findAllByUser(userInstance)
        oldProficiencyList.each {oldLP -> oldLP.delete(flush: true)}

        // Create current proficiencies for the user
        languageIds.eachWithIndex() { languageId, i ->
            new LanguageProficiency(
                language: Language.load(languageId), 
                proficiency: Proficiency.load(proficiencyIds[i]),
                user: userInstance
                ).save(flush: true)
        }
    }
}
