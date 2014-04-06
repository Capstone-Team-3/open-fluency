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

			<table class="table">
				<thead>
					<tr>
						<th>Deck Title</th>
						<th>Flashcards</th>
						<th>Progress</th>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<g:each in="${deckInstanceList}">
						<tr>
							<td>
								<g:link action="show" controller="deck" id="${it.id}">${it.title}</g:link>
							</td>
							<td>${it.flashcards.size()}</td>
							<td>
								<div class="progress">
									<div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width:60%">60%</div>
									<!-- end progress-bar -->
								</div>
								<!-- end progress -->
							</td>
							<td>
								<g:if test="${it.flashcardCount}">
									<g:link class="pull-right btn btn-sm btn-success" action="practice" controller="deck" id="${it.id}">Practice Flashcards</g:link>
								</g:if>
							</td>
						</tr>
					</g:each>
				</tbody>
			</table>

		</div>
	</div>
</body>
</html>