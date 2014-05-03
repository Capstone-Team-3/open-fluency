package pages.chapter

import pages.MainLayoutPage

class CreateChapterPage extends MainLayoutPage {
	
	static url = "chapter/create"

	static at = {
		$("#main").text() == "New Chapter for Test course"
	}

	static content = {
		chapterTitle(wait: true) { $("input", name: 'title') }
		chapterDescription(wait: true) { $("textarea", name:"description") }
		deckSelect(wait: true) { $("select", name: 'deckId') }
		createChapterButton(wait: true) { $("#create-chapter") }
	}
}