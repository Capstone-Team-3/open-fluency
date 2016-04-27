import geb.spock.GebReportingSpec

import spock.lang.*

import com.openfluency.auth.User

import pages.*
import pages.chapter.*
import pages.course.*
import pages.deck.*
import pages.user.*

/**
 * This functional test script tests for the following uses cases:
 *    - As a student, I want to be able to have a personal deck that is viewable and editable only by me
 *    - As a student, I want to create private decks of flashcards from existing decks to study
 */

@Stepwise
class PrivateDeckSpec extends GebReportingSpec {
	
	def "Navigate to AuthPage"() {
		when:
		to AuthPage
		then:
		at AuthPage
	}
	
	def "Student sign-in with known username and password"() {
		when:
		username = "student"
		password = "test"
		signinButton.click()
		then:
		at DashboardPage
		dashboardHeading.text() == "student's Dashboard"
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

	def "Student creates a new private deck"() {
		when:
		deckTitle = "Private Deck"
		deckDescription = "This is a private deck!"
		sourceLanguageSelect.value('2')
		languageSelect.value('1')
		cardServerAlgoSelect.value('SM2-Spaced-Repetition')
		selectPrivateDeckRadioButtonOption('true')
		createDeckButton.click()
		then:
		at ShowDeckPage
		flashMessage.text() == "Well done! You succesfully created a new deck!"
		deckTitle.text() == "Private Deck"
		deckDescription.text() == "This is a private deck!"
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
	
	def "Student searches for deck with keywords 'Private Deck' - should get 1 results"() {
		when:
		keywordFilter = "Private Deck"
		languageFilter.value('1')
		searchDeckButton.click()
		then:
		at SearchDeckPage
		$(".deck-result").size() == 1
	}
	
	def "Student Logouts and then navigate to AuthPage"() {
		given: "student logs out"
		logout.click()
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
	
	def "Instructor navigates to search decks"() {
		when:
		decksNav.click()
		waitFor {
			deckNavList.present
		}
		searchDeck.click()
		then:
		at SearchDeckPage
	}
	
	def "Instructor searches for deck with keywords 'Private Deck' - should get 0 results"() {
		when:
		keywordFilter = "Private Deck"
		languageFilter.value('1')
		searchDeckButton.click()
		then:
		at SearchDeckPage
		$(".deck-result").size() == 0
	}
}