package com.openfluency.auth

import com.openfluency.Constants
import com.openfluency.auth.Role
import com.openfluency.language.LanguageProficiency
import com.openfluency.language.Language
import com.openfluency.language.Proficiency
import grails.transaction.Transactional

/**
 *  UserService provides transactional services for handling all User CRUD functionality
 */
@Transactional
class UserService {

    def mailService
    def springSecurityService

    /**
    * Creates a new user in the system
    * @Param username - a unique username to identify the user with
    * @Param password - a password in string format - it is encrypted before adding to the database
    * @Param email - a user email address - should be valid as it is used for 
    * @Param userTypeId - a String of the Role the user will have in the system - see Role, UserRole and Constants for up to date Roles
    * @Param nativeLanguageId - the String-ified Long identifier of the user's native language 
    * @Param languageIds - a List of String-ified Long identifiers of the user's other languages
    * @Param proficiencyIds - a List of String-ified Lond identifiers of the proficiencies for the user's languages (should match 1-1)
    */
    User createUser(String username, String password, String email, String userTypeId, String nativeLanguageId, 
                   List<String> languageIds, List<String> proficiencyIds) {
        
        // Create the user - the account will only be enabled if the user is a student
    	def userInstance = new User(enabled: (userTypeId == Role.findByAuthority(Constants.ROLE_STUDENT).id.toString()), username: username, password: password, email: email, userType: Role.load(userTypeId), nativeLanguage: Language.load(nativeLanguageId))
    	
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

    /**
     * Provides editing functionality to non-unique user fields
     * @Param userInstance - the User being edited
     * @Param languageIds - a List of String-ified Long identifiers of the user's other languages
     * @Param proficiencyIds - a List of String-ified Lond identifiers of the proficiencies for the user's languages (should match 1-1)
     * @Param password - optional with null default - a password in string format - it is encrypted before adding to the database
     * @Param email - a user email address - should be valid as it is used for password recovery
     */
    def editUser(User userInstance,List<String> languageIds, List<String> proficiencyIds, String password=null, String email){
        
        // Update email and password
        if(password) {
            userInstance.password = password
        }
        if(email) {
            userInstance.email = email
        }
        userInstance.save(flush: true)

        // Remove old proficiencies for the user
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

    /**
     *  Provides email based password reset / recovery for a user if they provide their valid email
     *  @Param email - the users registered email address
     */
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

    /**
     *  Updates a user's enabled status
     *  @Param userInstance - reference to the User
     *  @Param enabled - boolean of the status to update to
     */
    def updateUser(User userInstance, Boolean enabled) {
        userInstance.enabled = enabled
        userInstance.save(failOnError: true, flush: true)
    }
}
