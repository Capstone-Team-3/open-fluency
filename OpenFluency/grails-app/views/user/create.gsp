<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
	</head>
	<body>

		<div class="container signup" role="main">
			<div class="col-lg-4">
				<h2>Sign Up</h2>
				<p>Already have an account? <g:link action="auth" controller="login">Sign In</g:link>.</p>

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
					
					<g:render template="form"/>

					<label for="languages" class="control-label">
						<g:message code="language.name.label" default="Language" />
						<span class="required-indicator">*</span>
					</label>
					
					<g:select id="language" class="many-to-one form-control" name="language.id" from="${languages}" optionKey="id" required="" value="${languages?.id}"/>

					<label for="proficiencies" class="control-label">
						<g:message code="proficiency.proficiency.label" default="Proficiency" />
						<span class="required-indicator">*</span>
					</label>
					
					<g:select id="proficiency" class="many-to-one form-control" name="proficiency.id" from="${proficiencies}" optionKey="id" required="" value="${proficiencies?.id}" />

					<g:submitButton name="sign-up" class="btn btn-primary btn-block" value="Sign Up" /> 

				</g:form>
			</div><!-- end col-lg-4 -->
		</div><!-- end container -->
	</body>
</html>
