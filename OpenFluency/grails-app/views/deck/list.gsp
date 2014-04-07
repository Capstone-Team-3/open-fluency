<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"/>
</head>
<body>
	<div class="container deck-list">
		<div class="row">
			<h1>
				My Decks
				<g:link action="create" controller="deck" class="btn btn-info">Create New Deck</g:link>
			</h1>
			<g:render template="table" model="[deckInstanceList: deckInstanceList]"/>
		</div>
		<div class="row">
			<h1>
				Other's Decks
			</h1>
			<g:render template="table" model="[deckInstanceList: othersDeckInstanceList, userInstance: userInstance]"/>
		</div>
	</div>
</body>
</html>