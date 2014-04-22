<table class="table">
	<thead>
		<tr>
			<th>Deck Title</th>
			<th>Source Language</th>
			<th>Learning</th>
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
				<td>${it.sourceLanguage.name}</td>
				<td>${it.language.name}</td>
				<td>${it.flashcardCount}</td>
				<td>
					<g:render template="/deck/progress" model="[progress: it.progress]"/>
				</td>
				<td class="right">
					<g:if test="${it.flashcardCount}">
						<g:link class="btn btn-sm btn-info" action="show" controller="deck" id="${it.id}">View Deck</g:link>
					</g:if>
					<g:if test="${it.owner.id != userInstance.id}">
						<g:link class="btn btn-sm btn-danger" action="remove" controller="deck" id="${it.id}">Remove</g:link>
					</g:if>
				</td>
			</tr>
		</g:each>
	</tbody>
</table>