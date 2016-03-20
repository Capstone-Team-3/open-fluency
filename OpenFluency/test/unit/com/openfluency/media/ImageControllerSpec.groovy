package com.openfluency.media

import com.openfluency.Constants
import com.openfluency.auth.Role
import com.openfluency.auth.User
import com.openfluency.language.Alphabet
import com.openfluency.language.Language
import com.openfluency.language.Unit
import com.openfluency.language.UnitMapping
import spock.lang.*

@TestFor(ImageController)
@Mock([Image, UnitMapping, Unit, User, Language, Alphabet, Role])
class ImageControllerSpec extends Specification {

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.imageInstanceList
            model.imageInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.imageInstance!= null
    }
	
    void "Test the save action correctly persists an instance"() {
		setup:
			MediaService mediaService = Stub()
			controller.mediaService = mediaService
		
        when:"The save action is executed with a valid instance"
			request.addHeader("Accept","application/json")
			params.url = "https://www.google.com"
			params['unitMapping.id'] = '123'
            def result = controller.save()

        then:
			// TODO
    		response != null
    }
	
    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            def image = new Image(params)
            controller.show(image)

        then:"A model is populated containing the domain instance"
            model.imageInstance == image
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            def image = new Image(params)
            controller.edit(image)

        then:"A model is populated containing the domain instance"
            model.imageInstance == image
    }

    void "Test the update action returns to the edit view when passed invalid parameters"() {
        when:"An invalid domain instance is passed to the update action"
            def image = new Image()
            image.validate()
			request.contentType = FORM_CONTENT_TYPE
            controller.update(image)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.imageInstance == image
    }
	
	void "Test the update action performs an update on a valid domain instance"() {	
		given: "A valid Image instance"
			def studentRole = new Role(name: "Student", authority: Constants.ROLE_STUDENT).save()
			def english = new Language(name: 'English', code: 'ENG-US').save()
			def japanese = new Language(name: 'Japanese', code: 'JAP').save()
			def latin = new Alphabet(name: "Latin", language: english, code: "pinyin").save()
			def kanji = new Alphabet(name: 'Kanji', language: japanese, code: "kanji", encodeEntities: true).save()
			User.metaClass.encodePassword = {  } // Change encodePassword to do nothing since SpringSecurityService doesn't exist in mocked domain object
			def user = new User(username: "username", password: "password", enabled: true, accountExpired: false, accountLocked: false, passwordExpired: false, email: "email@email.com", userType: studentRole, nativeLanguage: english).save()
			def unitMapping = new UnitMapping(unit1: new Unit(alphabet: latin, literal: 'one'), unit2: new Unit(alphabet: kanji, literal: '壱' )).save()
			def image = new Image(owner: user, url: 'http://www.google.com/', unitMapping: unitMapping).save(flush: true)

		when:"A valid domain instance is passed to the update action"
			image.url = 'http://www.bing.com/'
			request.contentType = FORM_CONTENT_TYPE
			controller.update(image)

		then:"A redirect is issues to the show action"
			response.redirectedUrl == "/image/show/$image.id"
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
            def image = new Image(owner: user, url: 'http://www.google.com/', unitMapping: unitMapping).save(flush: true)

        then:"It exists"
            Image.count() == 1

        when:"The domain instance is passed to the delete action"
			request.contentType = FORM_CONTENT_TYPE
            controller.delete(image)

        then:"The instance is deleted"
            Image.count() == 0
            response.redirectedUrl == '/image/index'
            flash.message != null
    }
}
