package pages.course

import pages.MainLayoutPage

class ShowChapterPage extends MainLayoutPage {
	
	static url = "chapter/show"

	static at = {
		$(".chapter-header").size() > 0
	}

	static content = {
		chapterTitle(wait: true) { $(".chapter-title") }
		chapterDescription(wait: true) { $(".chapter-description") }
	}
}