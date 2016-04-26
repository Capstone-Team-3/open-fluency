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
 *
 * As a student learning a new word, I would like to lookup words in a dictionary to help me to better internalize the word. 
 * I would like to lookup:
 *     - Meaning of individual characters comprising the word 
 *     - Words where the characters are being used
 */

@Stepwise
class DictionarySpec extends GebReportingSpec {

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
		$(".dashboard > h1").text() == "student's Dashboard"
	}
	
	// TODO
}