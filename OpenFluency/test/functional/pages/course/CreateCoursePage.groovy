package pages.course

import pages.MainLayoutPage

class CreateCoursePage extends MainLayoutPage {
	
	static url = "course/show"

	static at = {
		$("#main").text() == "Create New Course"
	}

	static content = {
		courseTitle(wait: true) { $("input", name: 'title') }
		courseDescription(wait: true) { $("textarea", name:"description") }
		visible(wait: true) { $("select", name: 'visible') }
		languageSelect(wait: true) { $("select", name: 'language.id') }
		createCourseButton(wait: true) { $("#create-course") }
	}
}