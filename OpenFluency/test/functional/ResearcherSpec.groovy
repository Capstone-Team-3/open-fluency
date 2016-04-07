import geb.spock.GebReportingSpec

import spock.lang.*

import com.openfluency.auth.User
import pages.user.RegisterPage
import pages.user.AuthPage
import pages.*
import pages.deck.*
import pages.course.*
import pages.chapter.*


@Stepwise
class ResearcherSpec extends GebReportingSpec {

	def "Navigate to RegisterPage"() {
		when:
		to RegisterPage
		then:
		at RegisterPage
	}

	def "Researcher registers with valid params"() {
		when:
		// Fill out form
		username = "testResearcher"
		password = "testpassword"
		email = "test@user.com"
		userType.value('3')
		registerButton.click()
		then: "Register"
		at IndexPage
		flashMessage.text() == "testResearcher, your account is pending approval!"
	}

	def "Navigate to AuthPage"() {
		when:
		to AuthPage
		then:
		at AuthPage
	}
	
	def "Researcher sign-in with known username and password"() {
		when:
		// sign-in with instructor username
		username = "researcher"
		password = "test"
		signinButton.click()
		then:
		at DashboardPage
		$(".dashboard > h1").text() == "researcher's Dashboard"
	}
}