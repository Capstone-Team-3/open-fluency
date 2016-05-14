package com.openfluency.deck

import grails.test.mixin.TestFor
import grails.test.mixin.Mock

import com.openfluency.auth.Role;
import com.openfluency.auth.User
import com.openfluency.language.*;
import com.openfluency.flashcard.*;
import com.openfluency.Constants

import spock.lang.*

@TestFor(PreviewDeckController)
@Mock([PreviewDeck, User, Document, Role, Language, Alphabet, User, Deck, Pronunciation, Flashcard])

class PreviewDeckControllerSpec extends Specification {
	def user
	def document
	def setup() {
		def studentRole = new Role(name: "Student", authority: Constants.ROLE_STUDENT).save(failOnError: true)
		def english = new Language(name: 'English', code: 'ENG-US').save(failOnError: true)
		def japanese = new Language(name: 'Japanese', code: 'JAP').save(failOnError: true)
		def latin = new Alphabet(name: "Latin", language: english, code: "pinyin").save(failOnError: true)
		def kanji = new Alphabet(name: 'Kanji', language: japanese, code: "kanji", encodeEntities: true).save(failOnError: true)
		User.metaClass.encodePassword = {  } // Change encodePassword to do nothing since SpringSecurityService doesn't exist in mocked domain object
		user = new User(username: "username", password: "password", enabled: true, accountExpired: false, accountLocked: false, passwordExpired: false, email: "email@email.com", userType: studentRole, nativeLanguage: english).save(failOnError: true)
		document = new Document(filename: 'doc', fullPath: "path", language: japanese, owner: user).save(failOnError: true)
		Document.metaClass.hasErrors() { return false } // So the object returned by the mock documentService will be considered valid by the controller
		Document.metaClass.id = document.id
		controller.springSecurityService = [
			encodePassword: 'password',
			reauthenticate: { String u -> true},
			currentUser: user,
			loggedIn: true,
			principal: user]

	}

    def populateValidParams(params) {
        assert params != null
        // Populate valid properties like...
		Language language = new Language(name:"name",code:"code").save()
        params["name"] = 'someValidName'
        params["document"] = document.id
        params["owner_id"] = user.id
        params["filename"] = "Filename"
        params["language_id"] = language.id
    }

	/*  This version of Grails Junit Does not support String query in GORM
    This test cannot be performed
    void "Test the index action returns the correct model"() {
        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.previewDeckInstanceList
            model.previewDeckInstanceCount == 0
    }
    */

    void "Test the save action correctly persists an instance"() {
		Role role = new Role(authority:"auth",name:"name")
		Language language = new Language(name:"name",code:"code")
		def loggedInUser = new User(username: "user",password:"pass",
			enabled:true,
			accountLocked: false,
			email: "email",
			userType: role,
			nativeLanguage: language,
			accountExpired:false).save() // This way the user will have an ID
		System.out.println("Logged In User "+ loggedInUser);
		controller.springSecurityService = [
			encodePassword: 'password',
			reauthenticate: { String u -> true},
			loggedIn: true,
			principal: loggedInUser]
        when:"The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            def previewDeck = new PreviewDeck()
            previewDeck.validate()
            controller.save(previewDeck)

        then:"The create view is rendered again with the correct model"
            model.previewDeckInstance!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            previewDeck = new PreviewDeck(params)

            controller.save(previewDeck)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/previewDeck/show/1'
            controller.flash.message != null
            PreviewDeck.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        /*
        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def previewDeck = new PreviewDeck(params).save()
            controller.show(previewDeck)

        then:"A model is populated containing the domain instance"
            model.previewDeckInstance == previewDeck
            */
    }

	/*
    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def previewDeck = new PreviewDeck(params)
            controller.edit(previewDeck)

        then:"A model is populated containing the domain instance"
            model.previewDeckInstance == previewDeck
    }
    */

	/*
    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/previewDeck/index'
            flash.message != null


        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def previewDeck = new PreviewDeck()
            previewDeck.validate()
            controller.update(previewDeck)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.previewDeckInstance == previewDeck

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            previewDeck = new PreviewDeck(params).save(flush: true)
            controller.update(previewDeck)

        then:"A redirect is issues to the show action"
            response.redirectedUrl == "/previewDeck/show/$previewDeck.id"
            flash.message != null
    }
            */

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/previewDeck/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def previewDeck = new PreviewDeck(params).save(flush: true)

        then:"It exists"
            PreviewDeck.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(previewDeck)

        then:"The instance is deleted"
            PreviewDeck.count() == 0
            response.redirectedUrl == '/previewDeck/index'
            flash.message != null
    }
}
