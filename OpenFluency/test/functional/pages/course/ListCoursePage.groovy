package pages.course

import pages.MainLayoutPage

class ListCoursePage extends MainLayoutPage {
	
	static url = "course/list"

	static at = {
		$(".course-list").size() > 0
	}

	static content = {
		
	}
}