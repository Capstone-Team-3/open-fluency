package com.openfluency.media

import com.openfluency.Constants
import com.openfluency.auth.Role
import com.openfluency.auth.User
import com.openfluency.flashcard.Deck
import com.openfluency.flashcard.Flashcard
import com.openfluency.language.Alphabet
import com.openfluency.language.Language
import com.openfluency.language.Pronunciation
import com.openfluency.language.Unit
import com.openfluency.language.UnitMapping
import spock.lang.*

@TestFor(CustomizationController)
@Mock([Customization, Role, Language, Alphabet, User, UnitMapping, Unit, Deck, Pronunciation, Flashcard])
class CustomizationControllerSpec extends Specification {

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.customizationInstanceList
            model.customizationInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.customizationInstance!= null
    }

	@Ignore("needs work")
	void "Test the save action returns to the create view when passed invalid parameters"() {
		setup:
			MediaService mediaService = Stub() {
				createCustomization(_) >> {
					def customization = new Customization()
					customization.validate()
					return customization
				}
			}
			controller.mediaService = mediaService
		
		when:"The save action is executed with an invalid instance"
			request.contentType = FORM_CONTENT_TYPE
			controller.save()

		then:"The create view is rendered again with the correct model"
			model.customizationInstance!= null
			view == 'create'
	}
	
	@Ignore("needs work")
    void "Test the save action correctly persists an instance"() {
		given: "A mock MediaService"
			MediaService mediaService = Stub()
			controller.mediaService = mediaService
		
		and:"A valid Customization object"
			def studentRole = new Role(name: "Student", authority: Constants.ROLE_STUDENT).save()
			def english = new Language(name: 'English', code: 'ENG-US').save()
			def japanese = new Language(name: 'Japanese', code: 'JAP').save()
			def latin = new Alphabet(name: "Latin", language: english, code: "pinyin").save()
			def kanji = new Alphabet(name: 'Kanji', language: japanese, code: "kanji", encodeEntities: true).save()
			User.metaClass.encodePassword = {  } // Change encodePassword to do nothing since SpringSecurityService doesn't exist in mocked domain object
			def user = new User(username: "username", password: "password", enabled: true, accountExpired: false, accountLocked: false, passwordExpired: false, email: "email@email.com", userType: studentRole, nativeLanguage: english).save()
			def unitMapping = new UnitMapping(unit1: new Unit(alphabet: latin, literal: 'one'), unit2: new Unit(alphabet: kanji, literal: '壱' )).save()
			def deck = new Deck(sourceLanguage: english, language: japanese, title: "Numbers", description: "Numbers", owner: user, cardServerName: "Linear-With-Shuffle").save(failOnError: true)
			def pronunciation = new Pronunciation(unit: new Unit(alphabet: latin, literal: 'one'), alphabet: latin, literal: 'one')
			def flashcard = new Flashcard(primaryAlphabet: kanji, unitMapping: unitMapping, pronunciation: pronunciation, deck: deck)
			def customization = new Customization(owner: user, card: flashcard)
		
        when:"The save action is executed with a valid instance"
            request.contentType = FORM_CONTENT_TYPE
            controller.save()

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/customization/show/1'
            controller.flash.message != null
            Customization.count() == 1
    }
	
    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            def customization = new Customization(params)
            controller.show(customization)

        then:"A model is populated containing the domain instance"
            model.customizationInstance == customization
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            def customization = new Customization(params)
            controller.edit(customization)

        then:"A model is populated containing the domain instance"
            model.customizationInstance == customization
    }
	
	void "Test the update action returns to the edit view when passed invalid parameters"() {
		when:"An invalid domain instance is passed to the update action"
			request.contentType = FORM_CONTENT_TYPE
			def customization = new Customization()
			customization.validate()
			controller.update(customization)

		then:"The edit view is rendered again with the invalid instance"
			view == 'edit'
			model.customizationInstance == customization
	}
	
    void "Test the update action performs an update on a valid domain instance"() {
		given:"A valid Customization object"
			def studentRole = new Role(name: "Student", authority: Constants.ROLE_STUDENT).save()
			def english = new Language(name: 'English', code: 'ENG-US').save()
			def japanese = new Language(name: 'Japanese', code: 'JAP').save()
			def latin = new Alphabet(name: "Latin", language: english, code: "pinyin").save()
			def kanji = new Alphabet(name: 'Kanji', language: japanese, code: "kanji", encodeEntities: true).save()
			User.metaClass.encodePassword = {  } // Change encodePassword to do nothing since SpringSecurityService doesn't exist in mocked domain object
			def user = new User(username: "username", password: "password", enabled: true, accountExpired: false, accountLocked: false, passwordExpired: false, email: "email@email.com", userType: studentRole, nativeLanguage: english).save()
			def unitMapping = new UnitMapping(unit1: new Unit(alphabet: latin, literal: 'one'), unit2: new Unit(alphabet: kanji, literal: '壱' )).save()
			def deck = new Deck(sourceLanguage: english, language: japanese, title: "Numbers", description: "Numbers", owner: user, cardServerName: "Linear-With-Shuffle").save(failOnError: true)
			def pronunciation = new Pronunciation(unit: new Unit(alphabet: latin, literal: 'one'), alphabet: latin, literal: 'one')
			def flashcard = new Flashcard(primaryAlphabet: kanji, unitMapping: unitMapping, pronunciation: pronunciation, deck: deck)
			def customization = new Customization(owner: user, card: flashcard)
				
        when:"A valid domain instance is passed to the update action"
			request.contentType = FORM_CONTENT_TYPE
            controller.update(customization)

        then:"A redirect is issues to the show action"
            response.redirectedUrl == "/customization/show/$customization.id"
            flash.message != null
    }
	
    void "Test that the delete action deletes an instance if it exists"() {
        when:"A domain instance is created"
            def studentRole = new Role(name: "Student", authority: Constants.ROLE_STUDENT).save()
			def english = new Language(name: 'English', code: 'ENG-US').save()
			def japanese = new Language(name: 'Japanese', code: 'JAP').save()
			def latin = new Alphabet(name: "Latin", language: english, code: "pinyin").save()
			def kanji = new Alphabet(name: 'Kanji', language: japanese, code: "kanji", encodeEntities: true).save()
			User.metaClass.encodePassword = {  } // Change encodePassword to do nothing since SpringSecurityService doesn't exist in mocked domain object
			def user = new User(username: "username", password: "password", enabled: true, accountExpired: false, accountLocked: false, passwordExpired: false, email: "email@email.com", userType: studentRole, nativeLanguage: english).save()
			def unitMapping = new UnitMapping(unit1: new Unit(alphabet: latin, literal: 'one'), unit2: new Unit(alphabet: kanji, literal: '壱' )).save()
			def deck = new Deck(sourceLanguage: english, language: japanese, title: "Numbers", description: "Numbers", owner: user, cardServerName: "Linear-With-Shuffle").save()
			def pronunciation = new Pronunciation(unit: new Unit(alphabet: latin, literal: 'one'), alphabet: latin, literal: 'one').save()
			def flashcard = new Flashcard(primaryAlphabet: kanji, unitMapping: unitMapping, pronunciation: pronunciation, deck: deck).save()
			def customization = new Customization(owner: user, card: flashcard).save()

        then:"It exists"
            Customization.count() == 1

        when:"The domain instance is passed to the delete action"
			request.contentType = FORM_CONTENT_TYPE
            controller.delete(customization)

        then:"The instance is deleted"
            Customization.count() == 0
            response.redirectedUrl == '/customization/index'
            flash.message != null
    }
}
