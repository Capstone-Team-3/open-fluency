import geb.spock.GebReportingSpec

import spock.lang.*

import com.openfluency.auth.User
import pages.user.RegisterPage
import pages.user.AuthPage
import pages.*
import pages.deck.*
import pages.course.*
import pages.chapter.*
import pages.quiz.*

/**
 * This functional test script tests the general instructor functionalities
 */

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
		dashboardHeading.text() == "instructor's Dashboard"
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
		createChapterButton.click()
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
	

	def "Instructor navigates to Edit Deck"() {
		when:
		editDeckButton.click()
		then:
		at EditDeckPage
	}
	
	def "Instructor edits a deck"() {
		when:
		deckTitle = "Test Deck - Edit"
		deckDescription = "This is a test deck!"
		sourceLanguageSelect.value('2')
		languageSelect.value('1')
		cardServerAlgoSelect.value('SM2-Spaced-Repetition')
		saveDeckButton.click()
		then:
		at ShowDeckPage
		deckTitle.text() == "Test Deck - Edit"
		deckDescription.text() == "This is a test deck!"
	}
	
	def "Instructor deletes a Flashcard from a deck"() {
		when:
		def numberOfFlashcards = flashcardResult.size()
		// http://stackoverflow.com/questions/20903513/how-do-i-test-that-the-page-has-reloaded-when-i-click-a-link-to-the-current-page
		browser.js.exec '$(document.body).attr("data-not-reloaded",true);'
		$('a', class: contains('flashcard-delete-')).click()
		waitFor {
			browser.js.exec 'return $(document.body).attr("data-not-reloaded") === undefined';
		}
		then:
		at ShowDeckPage
		flashcardResult.size() == (numberOfFlashcards - 1)
	}
	
	def "Instructor navigates to anki deck import"() {
		when:
		decksNav.click()
		waitFor {
			deckNavList .present
		}
		uploadAnkiDeck.click()
		then:
		at UploadNewAnkiDeckPage
	}

	def "Instructor navigates to help page"() {
		when:
		help.click()
		then:
		at HelpPage
	}
	
	def "Instructor navigates to profile page"() {
		when:
		profile.click()
		then:
		at ProfilePage
	}
}
