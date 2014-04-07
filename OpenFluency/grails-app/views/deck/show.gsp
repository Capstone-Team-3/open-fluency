<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"/>
</head>
<body>
	<div class="container deck-show">
		<ul class="breadcrumb">
            <li><a href="${createLink(uri:'/') }">Home</a></li>
            <li>Decks</li>
            <li><g:link action="search" controller="deck" >Search for Decks</g:link></li>
            <li><a href="#">${deckInstance?.title}</a></li>
        </ul>
		<div class="deck-header text-center center-block">
			<h1>
				${deckInstance?.title}
			</h1>
			<p>${deckInstance?.description}</p>
			<g:if test="${flashcardCount}">
				<g:link class="btn btn-success" action="practice" id="${deckInstance.id}" controller="deck">Practice Flashcards</g:link>
			</g:if>
			<g:if test="${isOwner}">
				<g:link class="btn" action="search" controller="unit" params="${['filter-alph': deckInstance.language.id]}">Add Flashcards</g:link>
			</g:if>
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
			%{--
			<button class="btn" id="previous">Previous</button>
			<span id="offset">1</span>
			of
			<span id="total">${deckInstance.flashcardCount}</span>
			<button class="btn" id="next">Next</button>
			--}%
			<g:paginate controller="deck" action="show" id="${deckInstance.id}" total="${flashcardCount ?: 0}" />
		</div>

	</div>
	<!-- end container -->
</body>
</html>