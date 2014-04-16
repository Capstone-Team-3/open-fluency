package pages

import pages.MainLayoutPage

class DashboardPage extends MainLayoutPage {
	
	static url = "home/index"

	static at = {
		$(".dashboard").size() > 0
	}

	static content = {
		
	}
}