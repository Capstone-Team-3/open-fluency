<%@ page import="com.openfluency.language.Alphabet" %>
<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"/>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-lg-6 col-lg-offset-3">
				<h1>New Deck</h1>

				<g:hasErrors bean="${deckInstance}">
					<ul class="errors" role="alert">
						<g:eachError bean="${deckInstance}" var="error">
							<li>
								<g:message error="${error}"/>
							</li>
						</g:eachError>
					</ul>
				</g:hasErrors>

				<g:form action="save" controller="deck" name="createDeckForm">

					<div class="form-group">
						<label>Title</label>
						<input class="form-control" type="text" name="title" value="${deckInstance?.title}"/>
					</div>

					<div class="form-group">
						<label>Description</label>
						<textarea class="form-control" name="description" value="${deckInstance?.description}"></textarea>
					</div>

					<label for="alphabet" class="control-label">
						Alphabet
						<span class="required-indicator">*</span>
					</label>
					<select class="form-control" name="alphabet.id">
						<g:each in="${Alphabet.list()}">
							<option value="${it.id}">${it.language.name} - ${it.name}</option>
						</g:each>
					</select>

					<br>
					<div class="center">
						<button class="btn btn-info">Create</button>
					</div>
				</g:form>
			</div>
		</div>
	</div>
</body>
</html>