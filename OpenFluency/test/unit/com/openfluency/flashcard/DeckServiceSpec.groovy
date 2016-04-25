package com.openfluency.flashcard

import spock.lang.Specification

import com.openfluency.Constants
import com.openfluency.auth.Role
import com.openfluency.auth.User
import com.openfluency.language.Language

@TestFor(DeckService)
@Mock([ Role, Language, User, Deck])
class DeckServiceSpec extends Specification {

	void "Private decks belonging to user shows up in search results"() {
		given: "A private deck belong to user"
			def studentRole = new Role(name: "Student", authority: Constants.ROLE_STUDENT).save(failOnError: true)
			def english = new Language(name: 'English', code: 'ENG-US').save(failOnError: true)
			def japanese = new Language(name: 'Japanese', code: 'JAP').save(failOnError: true)
			User.metaClass.encodePassword = {  } // Change encodePassword to do nothing since SpringSecurityService doesn't exist in mocked domain object
			def user = new User(username: "username", password: "password", enabled: true, accountExpired: false, accountLocked: false, passwordExpired: false, email: "email@email.com", userType: studentRole, nativeLanguage: english).save(failOnError: true)
			def deck = new Deck(sourceLanguage: english, language: japanese, title: "Numbers", description: "Numbers", owner: user, cardServerName: "Linear-With-Shuffle", privateDeck: true).save(failOnError: true)
		when: "searchDecks are called"
			List<Deck> decks = service.searchDecks(japanese.id, null, user);
		then: "Results include the private deck belong to user"
			decks.contains(deck)
	}
	
	void "Private decks not belonging to user do not show up in search results"() {
		given: "A private deck that does not belong to user"
			def studentRole = new Role(name: "Student", authority: Constants.ROLE_STUDENT).save(failOnError: true)
			def english = new Language(name: 'English', code: 'ENG-US').save(failOnError: true)
			def japanese = new Language(name: 'Japanese', code: 'JAP').save(failOnError: true)
			User.metaClass.encodePassword = {  } // Change encodePassword to do nothing since SpringSecurityService doesn't exist in mocked domain object
			def user = new User(username: "username", password: "password", enabled: true, accountExpired: false, accountLocked: false, passwordExpired: false, email: "email@email.com", userType: studentRole, nativeLanguage: english).save(failOnError: true)
			def user2 = new User(username: "username2", password: "password", enabled: true, accountExpired: false, accountLocked: false, passwordExpired: false, email: "email2@email.com", userType: studentRole, nativeLanguage: english).save(failOnError: true)
			def deck = new Deck(sourceLanguage: english, language: japanese, title: "Numbers", description: "Numbers", owner: user2, cardServerName: "Linear-With-Shuffle", privateDeck: true).save(failOnError: true)
		when: "searchDecks are called"
			List<Deck> decks = service.searchDecks(japanese.id, null, user);
		then: "Results do not include the private deck"
			!decks.contains(deck)
	}
	
	void "Public decks not belonging to user do show up in the search results"() {
		given: "A private deck that does not belong to user"
			def studentRole = new Role(name: "Student", authority: Constants.ROLE_STUDENT).save(failOnError: true)
			def english = new Language(name: 'English', code: 'ENG-US').save(failOnError: true)
			def japanese = new Language(name: 'Japanese', code: 'JAP').save(failOnError: true)
			User.metaClass.encodePassword = {  } // Change encodePassword to do nothing since SpringSecurityService doesn't exist in mocked domain object
			def user = new User(username: "username", password: "password", enabled: true, accountExpired: false, accountLocked: false, passwordExpired: false, email: "email@email.com", userType: studentRole, nativeLanguage: english).save(failOnError: true)
			def user2 = new User(username: "username2", password: "password", enabled: true, accountExpired: false, accountLocked: false, passwordExpired: false, email: "email2@email.com", userType: studentRole, nativeLanguage: english).save(failOnError: true)
			def deck = new Deck(sourceLanguage: english, language: japanese, title: "Numbers", description: "Numbers", owner: user2, cardServerName: "Linear-With-Shuffle", privateDeck: false).save(failOnError: true)
		when: "searchDecks are called"
			List<Deck> decks = service.searchDecks(japanese.id, null, user2);
		then: "Results do include the public deck"
			decks.contains(deck)

	}
}
