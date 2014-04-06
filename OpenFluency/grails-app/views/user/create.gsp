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
			<h1>Sign Up</h1>
			<p>
				Already have an account?
				<g:link action="auth" controller="login">Sign In</g:link>.
			</p>

			<g:hasErrors bean="${userInstance}">
				<ul class="errors" role="alert">
					<g:eachError bean="${userInstance}" var="error">
						<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>
						>
						<g:message error="${error}"/>
					</li>
				</g:eachError>
			</ul>
		</g:hasErrors>

		<g:form url="[resource:userInstance, action:'save']" > 
			<g:render template="form"/>
			<div class="proficiencies">
				<label class="control-label">
					Language Proficiencies
					<a id="addproficiency" class="btn btn-default">Add</a>
				</label>
			</div>
			<g:submitButton name="sign-up" class="btn btn-primary" value="Sign Up" />
		</g:form>
	</div>
</div>

<g:javascript>
	$(function() {
	$('#addproficiency').click(function(){
		$.ajax({
			url: "${g.createLink(action: 'addLanguageProficiency', controller: 'language')}",
			context: document.body
		}).done(function(data) {
			$('.proficiencies').append(data);
			$('.remove-proficiency').unbind('click').click(function(){
				$(this).parents('.panel').remove();
				return false;
			});
		});
	});
})
</g:javascript>

</body>
</html>