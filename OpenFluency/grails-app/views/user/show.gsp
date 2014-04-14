<%@ page import="com.openfluency.auth.User" %>
<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main">
	<g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
</head>
<body>
	<div class="container profile">
		<h1>Profile</h1>
			<ul class="list-group">
				<li class="list-group-item"><b>Username:</b> ${userInstance.username}</li>
				<li class="list-group-item"><b>User Type:</b> ${userInstance.userType.name}</li>
				<li class="list-group-item"><b>Email:</b> ${userInstance.email}</li>
				<li class="list-group-item"><b>Native Language:</b> ${userInstance.nativeLanguage.name}</li>
				<li class="list-group-item"><b>Proficiencies:</b>
					<ul class="proficiencies">
						<g:each in="${userInstance.languageProficiencies}">
							<li>
								${it.proficiency.proficiency} ${it.language.name}
							</li>
						</g:each>
					</ul>
				</li>
			</ul>
	</div><!-- end container -->
</body>
</html>