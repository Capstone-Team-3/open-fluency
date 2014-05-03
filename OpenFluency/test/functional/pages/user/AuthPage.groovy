package pages.user

import geb.Page

class AuthPage extends Page {
	
	static url = "login/auth"

	static at = {
		$("input", name: 'sign-in').size() > 0
	}

	static content = {
		
		username(wait: true) { $("input", name: 'j_username') }
		password(wait: true) { $("input", name: 'j_password') }
		signinButton(wait: true) { $("input", name: 'sign-in') }
	}
}

