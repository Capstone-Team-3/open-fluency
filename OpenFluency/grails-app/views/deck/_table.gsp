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
				<td>${it.flashcardCount}</td>
				<td>
					<div class="progress">
						<div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width:${it.progress}%">${it.progress}%</div>
						<!-- end progress-bar -->
					</div>
					<!-- end progress -->
				</td>
				<td class="right">
					<g:if test="${it.flashcardCount}">
						<g:link class="btn btn-sm btn-success" action="practice" controller="deck" id="${it.id}">Practice Flashcards</g:link>
					</g:if>
					<g:if test="${it.owner.id != userInstance.id}">
						<g:link class="btn btn-sm btn-danger" action="remove" controller="deck" id="${it.id}">Remove</g:link>
					</g:if>
				</td>
			</tr>
		</g:each>
	</tbody>
</table>