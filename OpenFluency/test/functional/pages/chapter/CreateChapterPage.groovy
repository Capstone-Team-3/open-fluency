package pages.chapter

import pages.MainLayoutPage

class CreateChapterPage extends MainLayoutPage {
	
	static url = "chapter/create"

	static at = {
		$(".create-chapter").size() > 0
	}

	static content = {
		chapterTitle(wait: true) { $("input", name: 'title') }
		chapterDescription(wait: true) { $("textarea", name:"description") }
		deckSelect(wait: true) { $("select", name: 'deckId') }
		createChapterButton(wait: true) { $("#create-chapter") }
	}
}