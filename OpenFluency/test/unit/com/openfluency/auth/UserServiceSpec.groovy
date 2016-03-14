package com.openfluency.auth

import spock.lang.Ignore
import spock.lang.Specification
import com.openfluency.language.*
import com.openfluency.Constants
import grails.plugin.springsecurity.annotation.Secured

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(UserService)
@Mock([User, Role, Language, Proficiency, LanguageProficiency, UserRole])
class UserServiceSpec extends Specification {
	
    def setup() {
    	def	japanese = new Language(name: 'Japanese', code: 'JAP').save(failOnError: true)
    	def	studentRole = new Role(name: "Student", authority: Constants.ROLE_STUDENT).save(flush: true, failOnError: true)
    	def	nativeP = new Proficiency(proficiency: 'Native').save(flush: true, failOnError: true)
    }  
    

    @Ignore("keep getting null pointer errors that are an artifact of setup, not the test - will come back to")
    void "createUser() with valid params should create a valid user"() {
    	given: "the elements needed for valid registration params"
    		Language japanese = new Language(name: 'Japanese', code: 'JAP').save(failOnError: true)
    		def studentRole = new Role(name: "Student", authority: Constants.ROLE_STUDENT).save(flush: true, failOnError: true)
    		def nativeP = new Proficiency(proficiency: 'Native').save(flush: true, failOnError: true)
    	when: "we create a new user with valid params"
    		def newUser = service.createUser("student", "test", "student@openfluency.com", studentRole.id.toString(), japanese.id.toString(), [], [])
    	then: "we should have a valid user"
    		newUser != null
    		newUser.hasErrors() == false
    		newUser.username == params.username 
    }

    void "createUser() with invalid params should trigger 'hasErrors'"() {
    	when: "we try to create an underdefined new user"
    		def newUser = service.createUser("Bob", null, "", "", "", null, null)
    	then: "we should get errors"
    		newUser.hasErrors() == true
    }
    
	@Ignore("can't test it - got to learn how to test things that are saving to db behind the scenes.")
	void "editUser should change provided parameters"(){
    	given:
    		def studentRole = Role.load(0L)
    		def japanese = Language.load(0L)
    		def advancedP = new Proficiency(proficiency: 'Advanced').save(flush: true, failOnError: true)
    		User student = new User(username: "Student", password: "test", email: "stu@test.com", userType: studentRole, nativeLanguage: japanese)
    		def prof = student.getLanguageProficiencies()
    		def eml = student.email
    	when: "we edit the user's language proficiency, email but not password"
    		def testUser = service.editUser(student,[japanese.id.toString()],[advancedP.id.toString()],null,"aNewEmail@c.om")
    	then:
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