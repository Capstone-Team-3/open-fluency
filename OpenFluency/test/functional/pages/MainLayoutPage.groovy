package pages

import geb.Page

class MainLayoutPage extends Page {
	
	static content = {
		navBarBrand { $(".navbar-brand") }
		decksNav { $(".decks-nav") }
		coursesNav { $(".courses-nav") }
		
		deckNavList { $(".deckNavList") }
		myDecks { $(".my-decks") }
		uploadAnkiDeck { $(".create") }
		createDeck { $(".create-deck") }
		searchDeck { $(".deck-search") }
		
		courseNavList { $(".courseNavList") }
		enrolledCourses { $(".enrolled-courses") }
		myCourses { $(".my-courses") }
		createCourse { $(".create-course") }
		searchCourse { $(".course-search") }
		
		profile { $("a[href='/OpenFluency/user/profile']") }
		help { $("a", text: contains('Help')) }
		logout { $("a", text: contains('Logout')) }

		flashMessage { $(".alert.alert-info") }
	}
}