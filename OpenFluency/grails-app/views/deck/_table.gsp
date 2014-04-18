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
					<g:render template="/deck/progress" model="[progress: it.progress]"/>
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