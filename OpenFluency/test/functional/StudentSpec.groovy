import geb.spock.GebReportingSpec

import spock.lang.*

import com.openfluency.auth.User
import pages.login.AuthPage
import pages.user.RegisterPage
import pages.DashboardPage

import pages.deck.CreateDeckPage
import pages.deck.ShowDeckPage
import pages.deck.SearchDeckPage

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
		$(".alert.alert-info").text() == "testuser, welcome to OpenFluency!"
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
		createDeckButton.click()
		then:
		at ShowDeckPage
		$(".alert.alert-info").text() == "Well done! You succesfully created a new deck!"
		deckTitle.text() == "Test Deck"
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

	def "Student searches for japanese decks - should return 6 results"() {
		when:
		languageFilter.value('1')
		searchDeckButton.click()
		then:
		at SearchDeckPage
		$(".deck-result").size() == 6
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
}