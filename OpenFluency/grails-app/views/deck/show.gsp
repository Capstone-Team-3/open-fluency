<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"/>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-lg-6 col-lg-offset-3">
				<h1>${deckInstance.title}</h1>
				<p>${deckInstance.description}</p>
			</div>
		</div>
		<br>
		<div class="row">
			<g:each in="${deckInstance.flashcards}">
				<div class="col-lg-3">
					<g:render template="/flashcard/flashcard" model="[flashcardInstance: it]"/>
				</div>
			</g:each>
		</div>
	</div>
</body>
</html>