<%@ page import="com.openfluency.language.Language" %>
<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"/>
</head>
<body>
	<div class="container deck-create">
		<ul class="breadcrumb" id="breadcrumb">
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
					<g:render template="form" model="[deckInstance: deckinstance]"/>
					<div class="center">
						<button class="btn btn-info" id="create-deck">Create Deck</button>
					</div>
				</g:form>
			</div>
		</div>
	</div>
	<g:javascript src="jquery.sticky.js" />
</body>
</html>