import geb.spock.GebReportingSpec

import spock.lang.*

import com.openfluency.auth.User
import pages.user.RegisterPage
import pages.DashboardPage

import pages.deck.CreateDeckPage
import pages.deck.ShowDeckPage
import pages.deck.ListDeckPage
import pages.deck.SearchDeckPage

import pages.course.ListCoursePage
import pages.course.SearchCoursePage
import pages.course.ShowCoursePage

@Stepwise
class StudentSpec extends GebReportingSpec {

	def "Navigate to RegisterPage"() {
		when:
		to RegisterPage
		then:
		at RegisterPage
	}

	def "Student registers with valid params"() {
		when:
		// Fill out form
		username = "testuser"
		password = "testpassword"
		email = "test@user.com"
		registerButton.click()
		then: "Register"
		at DashboardPage
		flashMessage.text() == "testuser, welcome to OpenFluency!"
		$(".dashboard > h1").text() == "testuser's Dashboard"
	}

	def "Student navigates to deck creation"() {
		when:
		decksNav.click()
		waitFor { 
			deckNavList.present
		}
		createDeck.click()
		then:
		at CreateDeckPage
	}

	def "Student creates a new deck"() {
		when:
		deckTitle = "Test Deck"
		deckDescription = "This is a test deck!"
		sourceLanguageSelect.value('2')
		languageSelect.value('1')
		cardServerAlgoSelect.value('SM2-Spaced-Repetition')
		createDeckButton.click()
		then:
		at ShowDeckPage
		flashMessage.text() == "Well done! You succesfully created a new deck!"
		deckTitle.text() == "Test Deck Edit Delete"
		deckDescription.text() == "This is a test deck!"
	}

	def "Student navigates to search decks"() {
		when:
		decksNav.click()
		waitFor { 
			deckNavList.present
		}
		searchDeck.click()
		then:
		at SearchDeckPage
	}

	def "Student searches for english decks - should return 0 results"() {
		when:
		languageFilter.value('2')
		searchDeckButton.click()
		then:
		at SearchDeckPage
		$(".deck-result").size() == 0
	}

	def "Student searches for deck with keywords 'Test Deck' - should get 1 result"() {
		when:
		keywordFilter = "Test Deck"
		languageFilter.value('1')
		searchDeckButton.click()
		then:
		at SearchDeckPage
		$(".deck-result").size() == 1
	}

	def "Student searches for japanese decks - should return 8 results"() {
		when:
		languageFilter.value('1')
		keywordFilter = ""
		searchDeckButton.click()
		then:
		at SearchDeckPage
		$(".deck-result").size() == 8
	}

	/*def "Student adds a deck to his/her own collection"() {
		when:
		$('.add-deck-4').click()
		then: 
		at ListDeckPage
		flashMessage.text() == "You succesfully added Kanji for Dummies 1 to your decks!"
		$(".show-deck-4").size() == 1
		$('.other-decks tbody tr').size() == 1
	}

	def "Student removes the added deck from his/her own collection"() {
		when:
		$(".remove-deck-4").click()
		then: 
		at ListDeckPage
		flashMessage.text() == "You succesfully removed Kanji for Dummies 1 from your decks!"
		$('.other-decks tbody tr').size() == 0	
	}*/

	def "Student navigates to enrolled courses - no registrations should exist"() {
		when:
		coursesNav.click()
		waitFor { 
			courseNavList.present
		}
		enrolledCourses.click()
		then:
		at ListCoursePage
		$(".enrolled-courses tbody > tr").size() == 0
	}

	def "Student navigates to search courses"() {
		when:
		coursesNav.click()
		waitFor { 
			courseNavList.present
		}
		searchCourse.click()
		then:
		at SearchCoursePage
		$(".course-result").size() == 2
	}

	def "Student searches for english courses - should return 0 results"() {
		when:
		languageFilter.value('2')
		searchCourseButton.click()
		then:
		at SearchCoursePage
		$(".deck-result").size() == 0
	}

	def "Student searches for course with keywords 'Advanced' - should get 1 result"() {
		when:
		keywordFilter = "Advanced"
		languageFilter.value('1')
		searchCourseButton.click()
		then:
		at SearchCoursePage
		$(".course-result").size() == 1
	}

	def "Student enrolls in course"() {
		when:
		$('.enroll').click()
		then:
		at ShowCoursePage
		flashMessage.text() == "Well done! Your registration is pending approval!"
		courseTitle.text() == "Kanji for More Advanced Dummies"
	}
}