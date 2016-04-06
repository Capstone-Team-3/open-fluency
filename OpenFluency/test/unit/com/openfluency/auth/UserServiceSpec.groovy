package com.openfluency.auth

import spock.lang.Ignore
import spock.lang.Specification
import com.openfluency.language.*
import com.openfluency.Constants
import grails.plugin.springsecurity.annotation.Secured

@TestFor(UserService)
@Mock([User, Role, Language, Proficiency, LanguageProficiency, UserRole])
class UserServiceSpec extends Specification {
    
    void "createUser() with valid params should create a valid user"() {
    	given: "the elements needed for valid registration params"
    		Language japanese = new Language(name: 'Japanese', code: 'JAP').save(failOnError: true)
    		def studentRole = new Role(name: "Student", authority: Constants.ROLE_STUDENT).save(flush: true, failOnError: true)
    		def nativeP = new Proficiency(proficiency: 'Native').save(flush: true, failOnError: true)
    	when: "we create a new user with valid params"
			User.metaClass.encodePassword = {  } // Change encodePassword to do nothing since SpringSecurityService doesn't exist in mocked domain object
    		def newUser = service.createUser("student", "test", "student@openfluency.com", studentRole.id.toString(), japanese.id.toString(), [], [])
    	then: "we should have a valid user"
    		newUser != null
    		newUser.hasErrors() == false
    		newUser.username == "student"
    }

    void "createUser() with invalid params should trigger 'hasErrors'"() {
		given: "The student role is defined"
			def	studentRole = new Role(name: "Student", authority: Constants.ROLE_STUDENT).save(flush: true, failOnError: true)
    	when: "we try to create an underdefined new user"
    		def newUser = service.createUser("Bob", null, "", "", "", null, null)
    	then: "we should get errors"
    		newUser.hasErrors() == true
    }
    
	void "editUser should change provided parameters"(){
    	given:
			User.metaClass.encodePassword = {  } // Change encodePassword to do nothing since SpringSecurityService doesn't exist in mocked domain object
    		Language japanese = new Language(name: 'Japanese', code: 'JAP').save(failOnError: true)
    		def studentRole = new Role(name: "Student", authority: Constants.ROLE_STUDENT).save(flush: true, failOnError: true)
    		def advancedP = new Proficiency(proficiency: 'Advanced').save(flush: true, failOnError: true)
    		User student = new User(username: "Student", password: "test", email: "stu@test.com", userType: studentRole, nativeLanguage: japanese)
    		def prof = student.getLanguageProficiencies()
    		def eml = student.email
    	when: "we edit the user's language proficiency, email but not password"
    		service.editUser(student,[japanese.id.toString()],[advancedP.id.toString()],null,"aNewEmail@c.om")
    	then:
			def testUser = User.load(student.id)
    		testUser.getLanguageProficiencies() != prof
    		testUser.password != null
    		testUser.email != eml
    		testUser.email == "aNewEmail@c.om"
    }

    void "resetUserPassword behavior"(){
    	when: "we provide emails but no student to match"
    		def wrongEmail = service.resetUserPassword("fake@email.com")
    	then: "fake email should not return a user"
    		wrongEmail == null
    }
}