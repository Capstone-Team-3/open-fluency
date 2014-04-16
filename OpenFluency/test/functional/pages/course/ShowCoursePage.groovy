package pages.course

import pages.MainLayoutPage

class ShowCoursePage extends MainLayoutPage {
	
	static url = "course/show"

	static at = {
		$(".course-header").size() > 0
	}

	static content = {
		courseTitle(wait: true) { $(".course-title") }
		courseDescription(wait: true) { $(".deck-description") }
	}
}