<%@ page import="com.openfluency.language.Language" %>
<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"/>
</head>
<body>
	<div class="container">
		<ul class="breadcrumb">
			<li>
				<a href="${createLink(uri:'/') }">Home</a>
			</li>
			<li>Decks</li>
			<li>
				<g:link action="create" controller="deck" >Create New Deck</g:link>
			</li>

		</ul>
		<div class="row">
			<div class="col-lg-6 col-lg-offset-3">
				<h1 id="main">Create New Deck</h1>

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

					<div class="form-group">
						<label for="alphabet" class="control-label">
							Source Language - The language you already know
							<span class="required-indicator">*</span>
						</label>
						<select class="form-control" name="sourceLanguage.id">
							<g:each in="${Language.list()}">
								<option value="${it.id}">${it.name}</option>
							</g:each>
						</select>
					</div>

					<div class="form-group">
						<label for="alphabet" class="control-label">
							Language - The language you're learning
							<span class="required-indicator">*</span>
						</label>
						<select class="form-control" name="language.id">
							<g:each in="${Language.list()}">
								<option value="${it.id}">${it.name}</option>
							</g:each>
						</select>
					</div>

					<div class="form-group">
						<label for="Repetition Algorithm" class="control-label">
							The Repetition Algorithm you'd like to use </br>(SM2SpacedRepetion is recommended)
							<span class="required-indicator">*</span>
						</label>
						<select class="form-control" name="cardServerAlgo">
							<g:each in="${cardServerAlgos}">
								<option value="${it}">${it}</option>
							</g:each>
						</select>
					</div>

					<div class="center">
						<button class="btn btn-info" id="create-deck">Create Deck</button>
					</div>
				</g:form>
			</div>
		</div>
	</div>
</body>
</html>