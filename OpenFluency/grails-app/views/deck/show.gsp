<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"/>
</head>
<body>
	<div class="container">

		<div class="row">
			<div class="col-lg-6 col-lg-offset-3">

				<g:if test="${flash.message}">
					<div class="alert alert-info" role="status">${flash.message}</div>
				</g:if>

				<h1>${deckInstance?.title}</h1>
				<p>${deckInstance?.description}</p>
				<g:link action="search" controller="unit">Add Flashcards</g:link>
			</div>
			<!-- end col-lg-6 -->
		</div>
		<!-- end row -->

		<div class="row">
			<g:each in="${deckInstance?.flashcards}">
				<div class="col-lg-3">
					<g:render template="/flashcard/flashcard" model="[flashcardInstance: it]"/>
				</div>
				<!-- end col-lg-3 -->
			</g:each>
		</div>
		<!-- end row -->

		<!-- this is the panel that indicates progress through the deck, perhaps this should move to a template for decks too -->
		<div>
			<p>
				<button id="previous">Previous</button>
				<span id="offset">1</span>
				of
				<span id="total">${deckInstance.flashcardCount}</span>
				<button id="next">Next</button>
			</p>
		</div>

	</div>
	<!-- end container -->
</body>
</html>