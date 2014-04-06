<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main">
	<g:set var="entityName" value="${message(code: 'image.label', default: 'Image')}" />
	<title><g:message code="default.create.label" args="[entityName]" /></title> 
	<style>
	img {
		cursor: pointer;
		margin: 5px;
	}

	img:HOVER {
		box-shadow: 0px 0px 5px rgba(0,0,0,0.2);
		padding-bottom: 2px;
	}
	</style>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-lg-6 col-lg-offset-3">
				<h1>Create Image</h1>
				<g:hasErrors bean="${imageInstance}">
					<ul class="errors" role="alert">
						<g:eachError bean="${imageInstance}" var="error">
							<li>
								<g:message error="${error}"/>
							</li>
						</g:eachError>
					</ul>
				</g:hasErrors>
				<g:form url="[resource:imageInstance, action:'save']" >
					<fieldset class="form">
						<g:render template="form"/>
					</fieldset>
					<br>
					<g:submitButton name="create" class="btn btn-info" value="${message(code: 'default.button.create.label', default: 'Create')}" />
				</g:form>
			</div>
		</div>

	</div>
</div>

<div id="wrapper" class="container"></div>

</body>

<g:javascript>
	$(function(){
	
});
</g:javascript>

</html>