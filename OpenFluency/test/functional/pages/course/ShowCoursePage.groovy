package pages.course

import pages.MainLayoutPage

class ShowCoursePage extends MainLayoutPage {
	
	static url = "course/show"

	static at = {
		$(".course-header").size() > 0
	}

	static content = {
		courseTitle(wait: true) { $(".course-title") }
		courseDescription(wait: true) { $(".course-description") }
		
		// TODO editCourseButton(wait: true) {}
		// TODO deleteCourseButton(wait: true) {}
		
		createChapterButton(wait: true) { $('.add-chapter') }
		
		// TODO createChapterQuizButton(wait: true) {}
		// TODO createManualQuizButton(wait: true) {}
		// TODO importQuizButton(wait: true) {}
	}
}