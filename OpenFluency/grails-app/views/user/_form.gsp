<%@ page import="com.openfluency.language.Language" %>
<%@ page import="com.openfluency.auth.User" %>

<div class="form-group">
	<label for="username" class="control-label">
		Username
		<span class="required-indicator">*</span>
	</label>
	<input type="text" class="form-control" name="username" required="" value="${userInstance?.username}" ${disabled}/>
</div>

<div class="form-group">
	<label for="password" class="control-label">
		Password
		<span class="required-indicator">*</span>
	</label>
	<g:passwordField class="form-control" name="password" required="" value="${userInstance?.password}"/>
</div>

<div class="form-group">
	<label for="email" class="control-label">
		Email
		<span class="required-indicator">*</span>
	</label>
	<input class="form-control" type="email" name="email" required="" value="${userInstance?.email}"/>
</div>

<div class="form-group">
	<label for="userType" class="control-label">
		<g:message code="user.userType.label" default="User Type" />
		<span class="required-indicator">*</span>
	</label>
	<select name="userType.id" class="form-control" ${disabled}>
		<g:each var="role" in="${authorities}">
			<option value="${role.id}">${role.name}</option>
		</g:each>
	</select>
</div>

<div class="form-group">
	<label for="nativeLanguage" class="control-label">
		Native Language
		<span class="required-indicator">*</span>
	</label>
	<select name="nativeLanguage.id" class="form-control">
		<g:each var="language" in="${Language.list()}">
			<g:if test="${language.id == userInstance?.nativeLanguage?.id}">
				<option selected value="${language.id}">${language.name}</option>
			</g:if>
			<g:else>
				<option value="${language.id}">${language.name}</option>
			</g:else>
		</g:each>
	</select>
</div>