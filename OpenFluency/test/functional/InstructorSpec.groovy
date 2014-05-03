import geb.spock.GebReportingSpec

import spock.lang.*

import com.openfluency.auth.User
import pages.user.RegisterPage
import pages.user.AuthPage
import pages.*
import pages.deck.*
import pages.course.*


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



	
}
