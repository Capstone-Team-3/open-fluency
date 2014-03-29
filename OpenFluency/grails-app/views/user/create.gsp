<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#create-user" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="create-user" class="content scaffold-create" role="main">
			<h1><g:message code="default.create.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${userInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${userInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>
			
			<%-- Add a language -> proficiencies map to flash so that the mappings can be accesed in the controller--%>
			<% flash.languageProcifiencies = [:] %>
			<%-- example of adding a proficiencies pair--%>
			<% flash.languageProcifiencies.japanese = 'fluent' %>
			
			<g:form url="[resource:userInstance, action:'save']" >
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>

				<div class="fieldcontain">
					<label for="languages">
						<g:message code="language.name.label" default="Language" />
						<span class="required-indicator">*</span>
					</label>
					<g:select id="language" name="language.id" from="${languages}" optionKey="id" required="" value="${languages?.id}" class="many-to-one"/>

				</div>

				<div class="fieldcontain">
					<label for="proficiencies">
						<g:message code="proficiency.proficiency.label" default="Proficiency" />
						<span class="required-indicator">*</span>
					</label>
					<g:select id="proficiency" name="proficiency.id" from="${proficiencies}" optionKey="id" required="" value="${proficiencies?.id}" class="many-to-one"/>

				</div>

				<fieldset class="buttons">
					<g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /> 
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
