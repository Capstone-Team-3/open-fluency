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
	
	%{-- <g:select id="userType" class="form-control many-to-one" name="userType.id" from="${com.openfluency.auth.Role.list()}" optionKey="id" required="" value="${userInstance?.userType?.id}" /> --}%

	<select class="form-control">
		<g:each var="role" in="${com.openfluency.auth.Role.list()}">
            <option value="${userInstance?.userType?.id}">${role.toString().replace("ROLE_","")}</option>
        </g:each>
	</select>
</div>