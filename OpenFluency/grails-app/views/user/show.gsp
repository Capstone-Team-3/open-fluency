<%@ page import="com.openfluency.auth.User" %>
<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main">
	<g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-lg-12">
				<h1>User Profile</h1>
				<p><b>User type:</b> ${userInstance.userType.name}</p>
				<p> <b>Username:</b>
					${userInstance.username}
				</p>
				<p>
					<b>Email:</b>
					${userInstance.email}
				</p>
				<p>
					<b>Native Language:</b>
					${userInstance.nativeLanguage.name}
				</p>
				<h3>Proficiencies</h3>
				<g:each in="${userInstance.languageProficiencies}">
				<p>
					<b>${it.language.name}</b>
					${it.proficiency.proficiency}
				</p>
			</g:each>
		</div>
	</div>
</div>
</body>
</html>