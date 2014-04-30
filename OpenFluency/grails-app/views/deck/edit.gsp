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
				<g:link action="create" controller="deck" >Edit Deck</g:link>
			</li>

		</ul>
		<div class="row">
			<div class="col-lg-6 col-lg-offset-3">
				<h1 id="main">Edit Deck</h1>

				<g:hasErrors bean="${deckInstance}">
					<ul class="errors" role="alert">
						<g:eachError bean="${deckInstance}" var="error">
							<li>
								<g:message error="${error}"/>
							</li>
						</g:eachError>
					</ul>
				</g:hasErrors>

				<g:form action="update" controller="deck" name="createDeckForm" id="${deckInstance.id}">
					<g:render template="form" model="[deckInstance: deckInstance]"/>
					<div class="center">
						<button class="btn btn-info" id="create-deck">Save</button>
					</div>
				</g:form>
			</div>
		</div>
	</div>
</body>
</html>