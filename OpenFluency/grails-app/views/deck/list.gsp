<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"/>
</head>
<body>
	<div class="container deck-list">
		<div class="row my-decks">
			<h1>
				My Decks
				<g:link action="create" controller="deck" class="btn btn-info">Create New Deck</g:link>
				<g:link action="create" controller="document" class="btn btn-info">Upload New Anki Deck</g:link>
			</h1>
			<g:render template="table" model="[deckInstanceList: deckInstanceList]"/>
		</div>
		<div class="row other-decks">
			<h1>
				Other's Decks
				<g:link action="search" controller="deck" class="btn btn-info">Search For Decks</g:link>
			</h1>
			<g:render template="table" model="[deckInstanceList: othersDeckInstanceList, userInstance: userInstance]"/>
		</div>
	</div>
	<g:javascript>initializeDonuts();</g:javascript>
</body>
</html>
