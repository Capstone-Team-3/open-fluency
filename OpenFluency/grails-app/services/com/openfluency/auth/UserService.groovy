package com.openfluency.auth

import com.openfluency.Constants
import com.openfluency.auth.Role
import com.openfluency.language.LanguageProficiency
import com.openfluency.language.Language
import com.openfluency.language.Proficiency
import grails.transaction.Transactional

@Transactional
class UserService {

    def mailService
    def springSecurityService

    /**
    * Create a user in the system
    */
    def createUser(String username, String password, String email, String userTypeId, String nativeLanguageId, List<String> languageIds, List<String> proficiencyIds) {
        
        // Create the user - the account will only be enabled if the user is a student
    	def userInstance = new User(enabled: (userTypeId == Role.findByAuthority(Constants.ROLE_STUDENT).id), username: username, password: password, email: email, userType: Role.load(userTypeId), nativeLanguage: Language.load(nativeLanguageId))
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

    def resetUserPassword(String email){
        def user = User.findByEmail(email)

        if(!user){
            return null
        }

        // Generate new random password
        def newPassword = (0..7).collect{(('a'..'z')+('A'..'Z')+(0..9))[new Random().nextInt(62)]}.join()
        user.password = newPassword
        user.enabled = true
        user.save(flush: true, failOnError: true)

        mailService.sendMail {
            to email
            subject 'OpenFluency - Password reset'
            html "<body><p>Hi $user.username. Your new password is: <b>$newPassword</b>. Please login and change it to something more friendly!</p></body>"
        }

        return user
    }

    def updateUser(User userInstance, Boolean enabled) {
        userInstance.enabled = enabled
        userInstance.save(failOnError: true, flush: true)
    }
}
