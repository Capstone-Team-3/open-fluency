package pages.user

import pages.MainLayoutPage

class RegisterPage extends MainLayoutPage {
	
	static url = "user/create"

	static at = {
		$("input", name: 'sign-up').size() > 0
	}

	static content = {
		
		email(wait: true) { $("input", name: 'email') }
		username(wait: true) { $("input", name: 'username') }
		password(wait: true) { $("input", name: 'password') }
		userType(wait: true) { $("select", name: 'userType.id') }
		nativeLanguage(wait: true) { $("input", name: 'nativeLanguage.id') }
		registerButton(wait: true) { $("input", name: 'sign-up') }
	}
}