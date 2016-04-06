package com.openfluency.course

import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.TestFor
import spock.lang.Specification
import com.openfluency.Constants
import com.openfluency.auth.Role
import com.openfluency.auth.User
import com.openfluency.language.Alphabet
import com.openfluency.language.Language

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(QuizEditorController)
@Mock([Language, Alphabet, Role, User, Course, Quiz, Question, QuestionOption])
class QuizEditorControllerSpec extends Specification {

    void "test creating a quiz"() {
		
		given: "Valid parameters representing a quiz"
			Language japanese = new Language(name: 'Japanese', code: 'JAP').save(failOnError: true)
			Language english = new Language(name: 'English', code: 'ENG-US').save(failOnError: true)
			Alphabet kanji = new Alphabet(name: 'Kanji', language: japanese, code: "kanji", encodeEntities: true).save(failOnError: true)
			Role instructorRole = new Role(name: "Instructor", authority: Constants.ROLE_INSTRUCTOR).save(flush: true, failOnError: true)
			User.metaClass.encodePassword = {  } // Change encodePassword to do nothing since SpringSecurityService doesn't exist in mocked domain object
			User instructor = new User(username: "instructor", password: "test", enabled: true, accountExpired: false, accountLocked: false, passwordExpired: false, email: "instructor@openfluency.com", userType: instructorRole, nativeLanguage: english).save(failOnError: true)
			Course kanji1 = new Course(visible: true, open: true, language: japanese, title: "Kanji for Dummies", description: "Start here if you have no idea what you're doing", owner: instructor).save(failOnError: true)
			
			params.title = "Manually Created Quiz"
			params["course.id"] = kanji.id
			params.maxCardTime = 20
			params.liveTime = new Date()
			params.questions = "MANUAL,Person,人,その,猫,ありがとう"
			
		and: "A mock SpringSecurityService"
			SpringSecurityService springSecurityService = Stub()
			springSecurityService.principal >> instructor
			controller.springSecurityService = springSecurityService
			
		when: "The save action is executed"
			controller.save()
			
		then: "The quiz is created"
			Quiz.count == 1
			Quiz quiz = Quiz.findByTitle("Manually Created Quiz")
			quiz.course == kanji1
    }
}
