package com.openfluency.auth



import spock.lang.*
import grails.test.mixin.*
import spock.lang.Specification
import com.openfluency.language.*
import com.openfluency.Constants



/**
 *
 */

@TestFor(UserService)
class UserServiceSpec extends Specification {

    def setup() {
    	def	japanese = Language.findByName('Japanese')
    	def	studentRole = Role.findByName("Student")
    	def	nativeP = Proficiency.findByProficiency('Native')
    	def testUser = new User(username: "testy",
    							password: "test",
    							enabled: true,
    							accountExpired: false,
    							accountLocked: false,
    							passwordExpired: false,
    							email: "test@email.com",
    							userType: studentRole,
    							nativeLanguage:japanese).save(flush: true)
    }

    def cleanup() {
    }

    void "test createUser with valid params"() {
    	when: "we create a user with valid params"
    		def testUser2 = service.createUser("Bob","1234","test@email.com",studentRole.id.toString(),japanese.id.toString(),[],[])
    	then: "we get that user back"
    		testUser2.name == "Bob"
    		testUser2.email == "test@email.com"
    }

    void "test editUser should change provided parameters, and not null pwd"(){
    	given: "testuser and its params"
    		def prof = testUser.getLanguageProficiencies()
    		def eml = testUser.email
    	when: "we edit the user's language proficiency, email but not password"
    		def respUser = service.editUser(testUser,[japanese.id.toString()],[nativeP.id.toString()],null,"aNewEmail@c.om")
    	then:
    		respUser.getLanguageProficiencies() != prof
    		respUser.password != null
    		respUser.email != eml
    		respUser.email == "aNewEmail@c.om"
    }
}
