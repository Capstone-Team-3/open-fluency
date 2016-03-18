package com.openfluency.auth

import com.openfluency.Constants
import com.openfluency.language.Language;
import com.sun.media.sound.ModelAbstractChannelMixer;

import grails.plugin.springsecurity.SpringSecurityService
import spock.lang.*

@TestFor(UserController)
@Mock([User, Language, Role])
class UserControllerSpec extends Specification {

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.userInstanceList
            model.userInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
		given: "Parameters specifying the user to create"
			params.username = "username"
			params.password = "password"
			params.enabled = true
			params.accountExpired = false
			params.accountLocked = false
			params.passwordExpired = false
			params.email = "email@email.com"
			params.userType = new Role(name: "Student", authority: Constants.ROLE_STUDENT)
			params.nativeLanguage = new Language(name: 'English', code: 'ENG-US')
			
		and: "A mock SpringSecurityService"
			SpringSecurityService springSecurityService = Stub()
			springSecurityService.currentUser >> null
			controller.springSecurityService = springSecurityService

        when:"The create action is executed"
            def model = controller.create()

        then:"The model is correctly created"
            model.userInstance != null
			model.userInstance.username == "username"
    }	

	void "Test the save action correctly persists an instance"() {
		
		given: "Parameters specifying a valid user instance"
			def studentRole = new Role(name: "Student", authority: Constants.ROLE_STUDENT).save()
			def english = new Language(name: 'English', code: 'ENG-US').save()
			def user = new User(username: "username", password: "password", enabled: true, accountExpired: false, accountLocked: false, passwordExpired: false, email: "email@email.com", userType: studentRole, nativeLanguage: english)			
			
			params.username = user.username
			params.password = "password"
			params.email = user.email
			params.userType = [id: String.valueOf(user.userType.id)]
			params['nativeLanguage.id'] = String.valueOf(user.nativeLanguage.id)
			params.language = []
			params.proficiency = []

		and: "A mock SpringSecurityService"
			SpringSecurityService springSecurityService = Stub()
			springSecurityService.currentUser >> null
			controller.springSecurityService = springSecurityService
			
		and: "A mock UserService"
			UserService userService = Stub() {
				createUser(_) >> { return user.save(flush: true) }
			}
			
			controller.userService = userService
			
		when: "The save action is executed"
			controller.save()

		then:"A redirect is issued to the home page"
			response.redirectedUrl == '/'
			controller.flash.message != null		
	}

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            def user = new User(params)
            controller.show(user)

        then:"A model is populated containing the domain instance"
            model.userInstance == user
    }

    void "Test that the edit action returns the correct model"() {

		given: "A mock SpringSecurityService"
			SpringSecurityService springSecurityService = Stub()
			controller.springSecurityService = springSecurityService
			
        and: "A domain instance"
			params.username = "username"
			params.password = "password"
			params.enabled = true
			params.accountExpired = false
			params.accountLocked = false
			params.passwordExpired = false
			params.email = "email@email.com"
			params.userType = new Role(name: "Student", authority: Constants.ROLE_STUDENT)
			params.nativeLanguage = new Language(name: 'English', code: 'ENG-US')
            def user = new User(params)
			user.springSecurityService = springSecurityService
			user.save()
			
			springSecurityService.principal >> user
		
		and: "HTTP method is POST"
			request.method = 'POST'
			
		when: "The edit action is executed"
			def model = controller.edit()

        then: "A model is populated containing the domain instance"
            model.userInstance == user
    }
	
    void "Test the update action performs an update on a valid domain instance"() {
		given: "Parameters specifying the user to create"
			params.username = "username"
			params.password = "password"
			params.enabled = true
			params.accountExpired = false
			params.accountLocked = false
			params.passwordExpired = false
			params.email = "email@email.com"
			params.userType = new Role(name: "Student", authority: Constants.ROLE_STUDENT)
			params.nativeLanguage = new Language(name: 'English', code: 'ENG-US')
			
		and: "A mock SpringSecurityService"
			SpringSecurityService springSecurityService = Stub()
			controller.springSecurityService = springSecurityService
			
		and: "A mock UserService"
			UserService userService = Stub()	
			controller.userService = userService
		
        when:"A valid domain instance is passed to the update action"
            User user = new User(params)
			user.springSecurityService = springSecurityService
			user.save(flush: true)
            controller.update(user)

        then:"A redirect is issued to the home page"
            response.redirectedUrl == "/"
            flash.message == "${user.username}, your profile has been updated."
    }
	
	void "Test that the delete action deletes an instance if it exists"() {
		
		given:"A user"
			def studentRole = new Role(name: "Student", authority: Constants.ROLE_STUDENT).save()
			def english = new Language(name: 'English', code: 'ENG-US').save()
			def user = new User(username: "username", password: "password", enabled: true, accountExpired: false, accountLocked: false, passwordExpired: false, email: "email@email.com", userType: studentRole, nativeLanguage: english)
		
		and: "A mock SpringSecurityService"
			SpringSecurityService springSecurityService = Stub()
			user.springSecurityService = springSecurityService
			
		when: "The save method is called on the user object"
			user.save()
			
		then: "The user is saved"
			User.count() == 1
				
        when:"The user is passed to the delete action"
            controller.delete(user)

        then:"The instance is deleted"
            User.count() == 0
    }
}
