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
				<p> <b>username:</b>
					${userInstance.username}
				</p>
				<p> <b>password:</b>
					${userInstance.password}
				</p>
				<p>
					<b>email:</b>
					${userInstance.email}
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