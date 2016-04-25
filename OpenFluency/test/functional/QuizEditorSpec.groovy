import geb.spock.GebReportingSpec

import spock.lang.*

import com.openfluency.auth.User

import pages.*
import pages.chapter.*
import pages.course.*
import pages.deck.*
import pages.user.*
import pages.quiz.*

/**
 * This functional test script tests the instructors ability to create quizzes with the Quiz Editor functionality
 */

@Stepwise
class QuizEditorSpec extends GebReportingSpec {

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

	def "Instructor navigates to 'My Courses' list"() {
		when:
		coursesNav.click()
		waitFor {
			courseNavList.present
		}
		myCourses.click()
		then:
		at ListCoursePage
	}
	
	def "Instructor navigates to 'Kanji for Dummies' course"() {
		when:
		$("a", text: contains('Kanji for Dummies')).click()
		then:
		at ShowCoursePage
		courseTitle.text() == "Kanji for Dummies"
	}
	
	// TODO
	
	def "Instructor creates chapter quiz"() {
		when:
			go "/OpenFluency/quiz/create/1"
		then:
			at CreateChapterQuizPage
			
		when: "fill out form"
			quizTitle = "Test Chapter Quiz"
			maxCardTime = "20"
			quizType.value('0')
			createQuizButton.click()
		then:
			at ShowQuizPage
	}
	
	def "Instructor creates manual quiz"() {
		when:
			go "/OpenFluency/quizEditor/create/1"
		then:
			at CreateManualQuizPage
			
		when: "fill out form"
			quizTitle = "Test Manual Quiz"
			maxCardTime = "20"
			createQuizButton.click()
		then:
			at ShowQuizPage
	}
	
}