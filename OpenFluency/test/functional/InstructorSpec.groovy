import geb.spock.GebReportingSpec

import spock.lang.*

import com.openfluency.auth.User
import pages.user.RegisterPage
import pages.user.AuthPage
import pages.*
import pages.deck.*
import pages.course.*
import pages.chapter.*
import pages.unit.*


@Stepwise
class InstructorSpec extends GebReportingSpec {

	def "Navigate to RegisterPage"() {
		when:
		to RegisterPage
		then:
		at RegisterPage
	}

	def "Instructor registers with valid params"() {
		when:
		// Fill out form
		username = "testInstructor"
		password = "testpassword"
		email = "test@user.com"
		userType.value('2')
		registerButton.click()
		then: "Register"
		at IndexPage
		flashMessage.text() == "testInstructor, your account is pending approval!"
	}

	def "Navigate to AuthPage"() {
		when:
		to AuthPage
		then:
		at AuthPage
	}
	
	def "Instructor sign-in with known username and password"() {
		when:
		// sign-in with instructor username
		username = "instructor"
		password = "test"
		signinButton.click()
		then:
		at DashboardPage
		$(".dashboard > h1").text() == "instructor's Dashboard"
	}

	def "Instructor navigates to course creation"() {
		when:
		coursesNav.click()
		waitFor {
			courseNavList.present
		}
		createCourse.click()
		then:
		at CreateCoursePage
	}

	def "Instructor creates a new Course"() {
		when:
		courseTitle = "Test course"
		courseDescription = "This is a test course!"
		languageSelect.value('1')
		visible.value('true')
		createCourseButton.click()
		then:
		at ShowCoursePage
		courseTitle.text() == "Test course"
		courseDescription.text() == "This is a test course!"
	}

	def "Instructor adds chapter to course"() {
		when:
		$('.add-chapter').click()
		then:
		at CreateChapterPage
	}
	
	def "Instructor creates a new Chapter"() {
		when:
		chapterTitle = "Test chapter"
		chapterDescription = "This is a test chapter!"
		deckSelect.value('4')
		createChapterButton.click()
		then:
		at ShowCoursePage
		courseTitle.text() == "Test course"
		courseDescription.text() == "This is a test course!"
	}
	def "Instructor navigates to chapter"() {
		when:
		$('.chapter-show').click()
		then:
		at ShowChapterPage
		$(".flashcard-result").size() == 5
	}
	def "Instructor navigates to flashcard search"() {
		when:
		$('.add-flashcard').click()
		then:
		at SearchUnitPage
		$('.search-button').click()
		$('.id-1').click()
		
	}
	def "Instructor creates a new Flashcard"() {
		when:
		$('#goCreate').click()
		then:
		at ShowDeckPage
		flashcardResult.size() == 6
	}
	def "Instructor deletes a Flashcard from a deck"() {
		when:
		$('.flashcard-delete-6').click()
		waitFor {
			flashcardResult.present
		}
		then:
		at ShowDeckPage
		flashcardResult.size() == 5
	}
/*	
	def "Instructor navigates to Edit Deck"() {
		when:
		editDeckButton.click()
		then:
		at ShowDeckPage
	}

	def "Instractur edits a deck"() {
		when:
		deckTitle = "Test Deck - Edit"
		deckDescription = "This is a test deck!"
		sourceLanguageSelect.value('2')
		languageSelect.value('1')
		cardServerAlgoSelect.value('SM2-Spaced-Repetition')
		saveDeckButton.click()
		then:
		at ShowDeckPage
		flashMessage.text() == "Well done! You succesfully created a new deck!"
		deckTitle.text() == "Test Deck - Edit"
		deckDescription.text() == "This is a test deck!"
	}*/
	
}
