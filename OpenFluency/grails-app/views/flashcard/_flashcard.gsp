<div class="panel panel-default">
	<h1 class="unit-large">${flashcardInstance?.primaryUnit.print}</h1>
	<table class="table">
		<tr>
			<th>Pronunciation in ${flashcardInstance?.pronunciation.alphabet.name}</th>
			<td>${flashcardInstance?.pronunciation.literal}</td>
		</tr>
		<tr>
			<th>Currently in deck</th>
			<td><g:link action="show" controller="deck" id="${flashcardInstance.deck.id}">${flashcardInstance?.deck.title}</g:link></td>
		</tr>
	</table>
</div>