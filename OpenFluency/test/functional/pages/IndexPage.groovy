package pages
import geb.Page

class IndexPage extends Page {
	
	static url = "home/index"

	static at = {
		$(".dashboard").size() == 0
	}

	static content = {
		flashMessage { $(".alert.alert-info") }
	}
}