<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main">
	<g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
	<title><g:message code="default.list.label" args="[entityName]" /></title> 
</head>
<body>
	<div class="container">
		<div class="col-lg-12">
			<table class="table">
				<thead>
					<tr>
						<g:sortableColumn property="username" title="${message(code: 'user.username.label', default: 'Username')}" />
						<g:sortableColumn property="email" title="${message(code: 'user.email.label', default: 'Email')}" />
						<th>
							<g:message code="user.userType.label" default="User Type" />
						</th>
						<th>Actions</th>
					</tr>
				</thead>
				<tbody>
					<g:each in="${userInstanceList}" status="i" var="userInstance">
						<tr>
							<td>
								<g:link action="show" id="${userInstance.id}">${fieldValue(bean: userInstance, field: "username")}</g:link>
							</td>
							<td>${fieldValue(bean: userInstance, field: "email")}</td>
							<td>${fieldValue(bean: userInstance, field: "userType")}</td>
							<td>
								<g:if test="${!userInstance.enabled}">
									<g:link action="enable" id="${userInstance.id}" class="btn btn-success">Enable</g:link>
								</g:if>
								<g:if test="${userInstance.enabled}">
									<g:link action="disable" id="${userInstance.id}" class="btn btn-danger">Disable</g:link>
								</g:if>
							</td>
						</tr>
					</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${userInstanceCount ?: 0}" />
			</div>
		</div>
	</div>
</body>
</html>