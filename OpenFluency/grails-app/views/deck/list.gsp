<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"/>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-lg-6 col-lg-offset-3">
				<h1>My Decks <g:link action="create" controller="deck" class="btn btn-info">Create</g:link></h1>
				<table class="table">
					<tr>
						<th>Title</th>
						<th>Flashcards</th>
					</tr>
					<g:each in="${deckInstanceList}">
						<tr>
							<td>
								<g:link action="show" controller="deck" id="${it.id}">${it.title}</g:link>
							</td>
							<td>${it.flashcards.size()}</td>
						</tr>
					</g:each>
				</table>
			</div>
		</div>
	</div>
</body>
</html>