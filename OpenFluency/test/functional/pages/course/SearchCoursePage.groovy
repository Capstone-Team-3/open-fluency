package pages.course

import pages.MainLayoutPage

class SearchCoursePage extends MainLayoutPage {
	
	static url = "course/search"

	static at = {
		$("#main").text() == "Course Search"
	}

	static content = {
		decksTable(wait: true) { $(".decks-table") }
		languageFilter(wait: true) { $("#filter-lang") }
		keywordFilter(wait: true) { $("#search-text") }
		searchCourseButton(wait: true) { $("#run-search") }
	}
}