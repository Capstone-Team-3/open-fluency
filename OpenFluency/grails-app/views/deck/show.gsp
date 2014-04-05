<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"/>
</head>
<body>
	<div class="container deck-show">

		<div class="deck-header text-center center-block">
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>

			<h1>
				${deckInstance?.title}
				<g:if test="${flashcardCount > 0}">
					<g:link class="btn btn-success" action="practice" id="${deckInstance.id}" controller="deck">Practice Flashcards</g:link>
				</g:if>
			</h1>
			<p>${deckInstance?.description}</p>
			<g:link action="search" controller="unit">Add Flashcards</g:link>
		</div>
		<!-- end deck-header -->

		<div class="row">
			<g:each in="${flashcardInstanceList}">
				<div class="col-lg-3">
					<g:render template="/flashcard/flashcard" model="[flashcardInstance: it]"/>
				</div>
				<!-- end col-lg-3 -->
			</g:each>
		</div>
		<!-- end row -->

		<!-- this is the panel that indicates progress through the deck, perhaps this should move to a template for decks too -->
		<div class="pagination center-block text-center">
			%{-- <button class="btn" id="previous">Previous</button>
			<span id="offset">1</span>
			of
			<span id="total">${deckInstance.flashcardCount}</span>
			<button class="btn" id="next">Next</button> --}%
			<g:paginate controller="deck" action="show" id="${deckInstance.id}" total="${flashcardCount ?: 0}" />
		</div>

	</div>
	<!-- end container -->
</body>
</html>