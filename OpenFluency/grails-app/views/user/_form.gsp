<%@ page import="com.openfluency.language.Language" %>
<%@ page import="com.openfluency.auth.User" %>

<div class="${hasErrors(bean: userInstance, field: 'username', 'error')} required">
	<label for="username" class="control-label">
		<g:message code="user.username.label" default="Username" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField class="form-control" name="username" required="" value="${userInstance?.username}"/>
</div>

<div class="${hasErrors(bean: userInstance, field: 'password', 'error')} required">
	<label for="password" class="control-label">
		<g:message code="user.password.label" default="Password" />
		<span class="required-indicator">*</span>
	</label>
	<g:passwordField class="form-control" name="password" required="" value="${userInstance?.password}"/>
</div>

<div class="${hasErrors(bean: userInstance, field: 'email', 'error')} required">
	<label for="email" class="control-label">
		<g:message code="user.email.label" default="Email" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField class="form-control" name="email" required="" value="${userInstance?.email}"/>
</div>

<div class="${hasErrors(bean: userInstance, field: 'userType', 'error')} required">
	<label for="userType" class="control-label">
		<g:message code="user.userType.label" default="User Type" />
		<span class="required-indicator">*</span>
	</label>
	<select name="userType.id" class="form-control">
		<g:each var="role" in="${authorities}">
			<option value="${role.id}">${role.name}</option>
		</g:each>
	</select>
</div>

<div class="${hasErrors(bean: userInstance, field: 'nativeLanguage', 'error')} required">
	<label for="nativeLanguage" class="control-label">
		<g:message code="user.nativeLanguage.label" default="Native Language" />
		<span class="required-indicator">*</span>
	</label>
	<select name="nativeLanguage.id" class="form-control">
		<g:each var="language" in="${Language.list()}">
			<option value="${language.id}">${language.name}</option>
		</g:each>
	</select>
</div>