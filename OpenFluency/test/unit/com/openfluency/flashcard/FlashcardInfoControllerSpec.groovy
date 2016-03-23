package com.openfluency.flashcard

import com.openfluency.Constants
import com.openfluency.auth.Role
import com.openfluency.auth.User
import com.openfluency.language.Alphabet
import com.openfluency.language.Language
import com.openfluency.language.Pronunciation
import com.openfluency.language.Unit
import com.openfluency.language.UnitMapping
import spock.lang.*

@TestFor(FlashcardInfoController)
@Mock([FlashcardInfo, Role, Language, Alphabet, User, UnitMapping, Unit, Deck, Pronunciation, Flashcard])
class FlashcardInfoControllerSpec extends Specification {

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.flashcardInfoInstanceList
            model.flashcardInfoInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.flashcardInfoInstance!= null
    }
	
	void "Test the save action redirects to the create view when passed invalid parameters"() {

		when:"The save action is executed with an invalid instance"
			request.contentType = FORM_CONTENT_TYPE
			def flashcardInfo = new FlashcardInfo()
			flashcardInfo.validate()
			controller.save(flashcardInfo)

		then:"The create view is rendered again with the correct model"
			model.flashcardInfoInstance!= null
			view == 'create'
	}

    void "Test the save action correctly persists an instance"() {

        given:"A valid FlashcardInfo instance"
			def studentRole = new Role(name: "Student", authority: Constants.ROLE_STUDENT).save(failOnError: true)
			def english = new Language(name: 'English', code: 'ENG-US').save(failOnError: true)
			def japanese = new Language(name: 'Japanese', code: 'JAP').save(failOnError: true)
			def latin = new Alphabet(name: "Latin", language: english, code: "pinyin").save(failOnError: true)
			def kanji = new Alphabet(name: 'Kanji', language: japanese, code: "kanji", encodeEntities: true).save(failOnError: true)
			User.metaClass.encodePassword = {  } // Change encodePassword to do nothing since SpringSecurityService doesn't exist in mocked domain object
			def user = new User(username: "username", password: "password", enabled: true, accountExpired: false, accountLocked: false, passwordExpired: false, email: "email@email.com", userType: studentRole, nativeLanguage: english).save(failOnError: true)
			def unitMapping = new UnitMapping(unit1: new Unit(alphabet: latin, literal: 'one'), unit2: new Unit(alphabet: kanji, literal: '壱' )).save(failOnError: true)
			def deck = new Deck(sourceLanguage: english, language: japanese, title: "Numbers", description: "Numbers", owner: user, cardServerName: "Linear-With-Shuffle").save(failOnError: true)
			def pronunciation = new Pronunciation(unit: new Unit(alphabet: latin, literal: 'one'), alphabet: latin, literal: 'one').save(failOnError: true)
			def flashcard = new Flashcard(primaryAlphabet: kanji, unitMapping: unitMapping, pronunciation: pronunciation, deck: deck).save(failOnError: true)
			def flashcardInfo = new FlashcardInfo(flashcard: flashcard, user: user, deck: deck, algoName: 'algoName', queue: 1, viewPriority: 0).save(failOnError: true, flush: true)
			
        when:"The save action is executed with a valid instance"
            request.contentType = FORM_CONTENT_TYPE
            controller.save(flashcardInfo)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/flashcardInfo/show/1'
            controller.flash.message != null
            FlashcardInfo.count() == 1
    }
	
    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            def flashcardInfo = new FlashcardInfo(params)
            controller.show(flashcardInfo)

        then:"A model is populated containing the domain instance"
            model.flashcardInfoInstance == flashcardInfo
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            def flashcardInfo = new FlashcardInfo(params)
            controller.edit(flashcardInfo)

        then:"A model is populated containing the domain instance"
            model.flashcardInfoInstance == flashcardInfo
    }
	
	void "Test the update action redirects to the edit view when passed invalid parameters"() {
		
		given:"A invalid FlashcardInfo instance"
			def flashcardInfo = new FlashcardInfo()
			flashcardInfo.validate()
			
		when:"An invalid domain instance is passed to the update action"
			request.contentType = FORM_CONTENT_TYPE
			controller.update(flashcardInfo)

		then:"The edit view is rendered again with the invalid instance"
			view == 'edit'
			model.flashcardInfoInstance == flashcardInfo
	}

    void "Test the update action performs an update on a valid domain instance"() {
		
		given:"A valid FlashcardInfo instance"
			def studentRole = new Role(name: "Student", authority: Constants.ROLE_STUDENT).save(failOnError: true)
			def english = new Language(name: 'English', code: 'ENG-US').save(failOnError: true)
			def japanese = new Language(name: 'Japanese', code: 'JAP').save(failOnError: true)
			def latin = new Alphabet(name: "Latin", language: english, code: "pinyin").save(failOnError: true)
			def kanji = new Alphabet(name: 'Kanji', language: japanese, code: "kanji", encodeEntities: true).save(failOnError: true)
			User.metaClass.encodePassword = {  } // Change encodePassword to do nothing since SpringSecurityService doesn't exist in mocked domain object
			def user = new User(username: "username", password: "password", enabled: true, accountExpired: false, accountLocked: false, passwordExpired: false, email: "email@email.com", userType: studentRole, nativeLanguage: english).save(failOnError: true)
			def unitMapping = new UnitMapping(unit1: new Unit(alphabet: latin, literal: 'one'), unit2: new Unit(alphabet: kanji, literal: '壱' )).save(failOnError: true)
			def deck = new Deck(sourceLanguage: english, language: japanese, title: "Numbers", description: "Numbers", owner: user, cardServerName: "Linear-With-Shuffle").save(failOnError: true)
			def pronunciation = new Pronunciation(unit: new Unit(alphabet: latin, literal: 'one'), alphabet: latin, literal: 'one').save(failOnError: true)
			def flashcard = new Flashcard(primaryAlphabet: kanji, unitMapping: unitMapping, pronunciation: pronunciation, deck: deck).save(failOnError: true)
			def flashcardInfo = new FlashcardInfo(flashcard: flashcard, user: user, deck: deck, algoName: 'algoName', queue: 1, viewPriority: 0).save(failOnError: true, flush: true)
		
        when:"A valid domain instance is passed to the update action"
			request.contentType = FORM_CONTENT_TYPE
            controller.update(flashcardInfo)

        then:"A redirect is issues to the show action"
            response.redirectedUrl == "/flashcardInfo/show/$flashcardInfo.id"
            flash.message != null
    }
	
    void "Test that the delete action deletes an instance if it exists"() {
        when:"A domain instance is created"
			def studentRole = new Role(name: "Student", authority: Constants.ROLE_STUDENT).save(failOnError: true)
			def english = new Language(name: 'English', code: 'ENG-US').save(failOnError: true)
			def japanese = new Language(name: 'Japanese', code: 'JAP').save(failOnError: true)
			def latin = new Alphabet(name: "Latin", language: english, code: "pinyin").save(failOnError: true)
			def kanji = new Alphabet(name: 'Kanji', language: japanese, code: "kanji", encodeEntities: true).save(failOnError: true)
			User.metaClass.encodePassword = {  } // Change encodePassword to do nothing since SpringSecurityService doesn't exist in mocked domain object
			def user = new User(username: "username", password: "password", enabled: true, accountExpired: false, accountLocked: false, passwordExpired: false, email: "email@email.com", userType: studentRole, nativeLanguage: english).save(failOnError: true)
			def unitMapping = new UnitMapping(unit1: new Unit(alphabet: latin, literal: 'one'), unit2: new Unit(alphabet: kanji, literal: '壱' )).save(failOnError: true)
			def deck = new Deck(sourceLanguage: english, language: japanese, title: "Numbers", description: "Numbers", owner: user, cardServerName: "Linear-With-Shuffle").save(failOnError: true)
			def pronunciation = new Pronunciation(unit: new Unit(alphabet: latin, literal: 'one'), alphabet: latin, literal: 'one').save(failOnError: true)
			def flashcard = new Flashcard(primaryAlphabet: kanji, unitMapping: unitMapping, pronunciation: pronunciation, deck: deck).save(failOnError: true)
            def flashcardInfo = new FlashcardInfo(flashcard: flashcard, user: user, deck: deck, algoName: 'algoName', queue: 1, viewPriority: 0).save(failOnError: true, flush: true)

        then:"It exists"
            FlashcardInfo.count() == 1

        when:"The domain instance is passed to the delete action"
			request.contentType = FORM_CONTENT_TYPE
            controller.delete(flashcardInfo)

        then:"The instance is deleted"
            FlashcardInfo.count() == 0
            response.redirectedUrl == '/flashcardInfo/index'
            flash.message != null
    }
}
