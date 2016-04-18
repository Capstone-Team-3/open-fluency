package com.openfluency.flashcard

import com.openfluency.Constants
import com.openfluency.auth.Role
import com.openfluency.auth.User
import com.openfluency.language.Language
import grails.plugin.springsecurity.SpringSecurityService
import spock.lang.Specification

@TestFor(DeckController)
@Mock([ Role, Language, User, Deck])
class DeckControllerSpec extends Specification {

	void "User cannot view private deck that does not belong to user"() {
		given: "A private deck that does not belong to user"
			def studentRole = new Role(name: "Student", authority: Constants.ROLE_STUDENT).save(failOnError: true)
			def english = new Language(name: 'English', code: 'ENG-US').save(failOnError: true)
			def japanese = new Language(name: 'Japanese', code: 'JAP').save(failOnError: true)
			User.metaClass.encodePassword = {  } // Change encodePassword to do nothing since SpringSecurityService doesn't exist in mocked domain object
			def user = new User(username: "username", password: "password", enabled: true, accountExpired: false, accountLocked: false, passwordExpired: false, email: "email@email.com", userType: studentRole, nativeLanguage: english).save(failOnError: true)
			def user2 = new User(username: "username2", password: "password", enabled: true, accountExpired: false, accountLocked: false, passwordExpired: false, email: "email2@email.com", userType: studentRole, nativeLanguage: english).save(failOnError: true)
			def deck = new Deck(sourceLanguage: english, language: japanese, title: "Numbers", description: "Numbers", owner: user2, cardServerName: "Linear-With-Shuffle", privateDeck: true).save(failOnError: true)
		and: "A mock SpringSecurityService"
			SpringSecurityService springSecurityService = Stub()
			springSecurityService.principal >> user
			controller.springSecurityService = springSecurityService
		when:
			controller.show(deck, null)
		then:
			controller.flash.message == "You don't have permissions to see this deck!"
			model.deckInstance == null
	}
	
	void "User cannot edit private deck that does not belong to user"() {
		given: "A private deck that does not belong to user"
			def studentRole = new Role(name: "Student", authority: Constants.ROLE_STUDENT).save(failOnError: true)
			def english = new Language(name: 'English', code: 'ENG-US').save(failOnError: true)
			def japanese = new Language(name: 'Japanese', code: 'JAP').save(failOnError: true)
			User.metaClass.encodePassword = {  } // Change encodePassword to do nothing since SpringSecurityService doesn't exist in mocked domain object
			def user = new User(username: "username", password: "password", enabled: true, accountExpired: false, accountLocked: false, passwordExpired: false, email: "email@email.com", userType: studentRole, nativeLanguage: english).save(failOnError: true)
			def user2 = new User(username: "username2", password: "password", enabled: true, accountExpired: false, accountLocked: false, passwordExpired: false, email: "email2@email.com", userType: studentRole, nativeLanguage: english).save(failOnError: true)
			def deck = new Deck(sourceLanguage: english, language: japanese, title: "Numbers", description: "Numbers", owner: user2, cardServerName: "Linear-With-Shuffle", privateDeck: true).save(failOnError: true)
		and: "A mock SpringSecurityService"
			SpringSecurityService springSecurityService = Stub()
			springSecurityService.principal >> user
			controller.springSecurityService = springSecurityService
		when:
			controller.edit(deck)
		then:
			controller.flash.message == "You don't have permissions to edit this deck!"
			model.deckInstance == null
	}
	
	void "User cannot delete private deck that does not belong to user"() {
		given: "A private deck that does not belong to user"
			def studentRole = new Role(name: "Student", authority: Constants.ROLE_STUDENT).save(failOnError: true)
			def english = new Language(name: 'English', code: 'ENG-US').save(failOnError: true)
			def japanese = new Language(name: 'Japanese', code: 'JAP').save(failOnError: true)
			User.metaClass.encodePassword = {  } // Change encodePassword to do nothing since SpringSecurityService doesn't exist in mocked domain object
			def user = new User(username: "username", password: "password", enabled: true, accountExpired: false, accountLocked: false, passwordExpired: false, email: "email@email.com", userType: studentRole, nativeLanguage: english).save(failOnError: true)
			def user2 = new User(username: "username2", password: "password", enabled: true, accountExpired: false, accountLocked: false, passwordExpired: false, email: "email2@email.com", userType: studentRole, nativeLanguage: english).save(failOnError: true)
			def deck = new Deck(sourceLanguage: english, language: japanese, title: "Numbers", description: "Numbers", owner: user2, cardServerName: "Linear-With-Shuffle", privateDeck: true).save(failOnError: true)
		and: "A mock SpringSecurityService"
			SpringSecurityService springSecurityService = Stub()
			springSecurityService.principal >> user
			controller.springSecurityService = springSecurityService
		when:
			controller.delete(deck)
		then:
			controller.flash.message == "You don't have permissions to delete this deck!"
	}
}
