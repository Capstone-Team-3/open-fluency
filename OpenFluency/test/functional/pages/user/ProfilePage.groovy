package pages

import pages.MainLayoutPage

class ProfilePage extends MainLayoutPage {
	
	static url = "user/profile"

	static at = {
		$("h1").text() == "Your Profile"
	}
}