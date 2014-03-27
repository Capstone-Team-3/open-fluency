<%@ page import="com.openfluency.auth.User" %>



<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'username', 'error')} required">
	<label for="username">
		<g:message code="user.username.label" default="Username" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="username" required="" value="${userInstance?.username}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'password', 'error')} required">
	<label for="password">
		<g:message code="user.password.label" default="Password" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="password" required="" value="${userInstance?.password}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'email', 'error')} ">
	<label for="email">
		<g:message code="user.email.label" default="Email" />
		
	</label>
	<g:textField name="email" value="${userInstance?.email}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'userType', 'error')} required">
	<label for="userType">
		<g:message code="user.userType.label" default="User Type" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="userType" name="userType.id" from="${com.openfluency.auth.Role.list()}" optionKey="id" required="" value="${userInstance?.userType?.id}" class="many-to-one"/>

</div>

<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'languageProficiencies', 'error')} required">
	<label for="languageProficiencies">
		<g:message code="user.languageProficiencies.label" default="Language Proficiencies" />
		<span class="required-indicator">*</span>
	</label>
	
<ul class="one-to-many">
<g:each in="${userInstance?.languageProficiencies?}" var="l">
    <li><g:link controller="languageProficiency" action="show" id="${l.id}">${l?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="languageProficiency" action="create" params="['user.id': userInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'languageProficiency.label', default: 'LanguageProficiency')])}</g:link>
</li>
</ul>


</div>

<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'decks', 'error')} ">
	<label for="decks">
		<g:message code="user.decks.label" default="Decks" />
		
	</label>
	<g:select name="decks" from="${com.openfluency.flashcard.Deck.list()}" multiple="multiple" optionKey="id" size="5" value="${userInstance?.decks*.id}" class="many-to-many"/>

</div>

<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'courses', 'error')} ">
	<label for="courses">
		<g:message code="user.courses.label" default="Courses" />
		
	</label>
	<g:select name="courses" from="${com.openfluency.course.Course.list()}" multiple="multiple" optionKey="id" size="5" value="${userInstance?.courses*.id}" class="many-to-many"/>

</div>

<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'accountExpired', 'error')} ">
	<label for="accountExpired">
		<g:message code="user.accountExpired.label" default="Account Expired" />
		
	</label>
	<g:checkBox name="accountExpired" value="${userInstance?.accountExpired}" />

</div>

<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'accountLocked', 'error')} ">
	<label for="accountLocked">
		<g:message code="user.accountLocked.label" default="Account Locked" />
		
	</label>
	<g:checkBox name="accountLocked" value="${userInstance?.accountLocked}" />

</div>

<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'enabled', 'error')} ">
	<label for="enabled">
		<g:message code="user.enabled.label" default="Enabled" />
		
	</label>
	<g:checkBox name="enabled" value="${userInstance?.enabled}" />

</div>

<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'passwordExpired', 'error')} ">
	<label for="passwordExpired">
		<g:message code="user.passwordExpired.label" default="Password Expired" />
		
	</label>
	<g:checkBox name="passwordExpired" value="${userInstance?.passwordExpired}" />

</div>

