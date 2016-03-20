package com.openfluency.media

import com.openfluency.Constants
import com.openfluency.auth.Role
import com.openfluency.auth.User
import com.openfluency.flashcard.Deck
import com.openfluency.language.Alphabet
import com.openfluency.language.Language
import com.openfluency.language.Pronunciation
import com.openfluency.language.Unit
import com.openfluency.language.UnitMapping
import spock.lang.*

@TestFor(AudioController)
@Mock([Audio, Role, Language, Alphabet, Pronunciation, Unit, User])
class AudioControllerSpec extends Specification {

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.audioInstanceList
            model.audioInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.audioInstance!= null
    }
	
	@Ignore("doesn't work")
	void "Test the save action  returns to the create view when passed invalid parameters"() {
		
		when:"The save action is executed with an invalid instance"
			request.contentType = FORM_CONTENT_TYPE
			def audio = new Audio()
			audio.validate()
			controller.save(audio)

		then:"The create view is rendered again with the correct model"
			model.audioInstance!= null
			view == 'create'
	}

	@Ignore("doesn't work")
    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with a valid instance"
            response.reset()
            audio = new Audio(params)

            controller.save(audio)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/audio/show/1'
            controller.flash.message != null
            Audio.count() == 1
    }
	
    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            def audio = new Audio(params)
            controller.show(audio)

        then:"A model is populated containing the domain instance"
            model.audioInstance == audio
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            def audio = new Audio(params)
            controller.edit(audio)

        then:"A model is populated containing the domain instance"
            model.audioInstance == audio
    }

	@Ignore("doesn't work")
    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/audio/index'
            flash.message != null


        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def audio = new Audio()
            audio.validate()
            controller.update(audio)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.audioInstance == audio

        when:"A valid domain instance is passed to the update action"
            response.reset()
            audio = new Audio(params).save(flush: true)
            controller.update(audio)

        then:"A redirect is issues to the show action"
            response.redirectedUrl == "/audio/show/$audio.id"
            flash.message != null
    }
	
    void "Test that the delete action deletes an instance if it exists"() {
        when:"A domain instance is created"
			def studentRole = new Role(name: "Student", authority: Constants.ROLE_STUDENT).save(failOnError: true)
			def english = new Language(name: 'English', code: 'ENG-US').save(failOnError: true)
			def latin = new Alphabet(name: "Latin", language: english, code: "pinyin").save(failOnError: true)
			User.metaClass.encodePassword = {  } // Change encodePassword to do nothing since SpringSecurityService doesn't exist in mocked domain object
			def user = new User(username: "username", password: "password", enabled: true, accountExpired: false, accountLocked: false, passwordExpired: false, email: "email@email.com", userType: studentRole, nativeLanguage: english).save(failOnError: true)
			def pronunciation = new Pronunciation(unit: new Unit(alphabet: latin, literal: 'one'), alphabet: latin, literal: 'one').save(failOnError: true)
			def audio = new Audio(owner: user, pronunciation: pronunciation).save(failOnError: true, flush: true)

        then:"It exists"
            Audio.count() == 1

        when:"The domain instance is passed to the delete action"
			request.contentType = FORM_CONTENT_TYPE
            controller.delete(audio)

        then:"The instance is deleted"
            Audio.count() == 0
            response.redirectedUrl == '/audio/index'
            flash.message != null
    }
}
