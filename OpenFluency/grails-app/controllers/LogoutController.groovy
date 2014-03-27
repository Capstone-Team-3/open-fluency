import grails.plugin.springsecurity.SpringSecurityUtils

class LogoutController {

	/**
	 * Index action. Redirects to the Spring security logout uri.
	 */
	def index = {
		session.invalidate()
		SpringSecurityUtils.securityConfig.logout.afterLogoutUrl = createLink(controller: 'login')
		redirect uri: SpringSecurityUtils.securityConfig.logout.filterProcessesUrl // '/j_spring_security_logout'
	}
}
