<table class="table">
	<thead>
		<tr>
            <g:sortableColumn colspan="2" property="flashcard" title="Flashcard (ordered by deck when clicked)" params="${params}"/>
            <g:sortableColumn property="symbolRanking" title="Symbol" params="${params}"/>
            <g:sortableColumn property="meaningRanking" title="Meaning" params="${params}"/>
            <g:sortableColumn property="pronunciationRanking" title="Pronunciation" params="${params}"/>
            <g:sortableColumn property="lastUpdated" title="Last Update" params="${params}"/>
            <g:set var="level" value='["","hard","medium","easy"]'></g:set>
            <g:set var="colors" value='["none","danger","warning","success"]'></g:set>
		</tr>
	</thead>
	<tbody>
		<g:each in="${cardRankingInstanceList}">
			<tr>
				<td>
				<g:link action="show" controller="deck" id="${it.flashcard.deck.id}">${it.flashcard.primaryUnit.print}</g:link>
				</td>
				<td>
				${it.flashcard.secondaryUnit.print}
				</td>
				<td class=${colors[(it.symbolRanking!=null)?it.symbolRanking:0]}>${level[(it.symbolRanking!=null)?it.symbolRanking:0]}</td>
				<td class=${colors[(it.meaningRanking!=null)?it.meaningRanking:0]}>${level[(it.meaningRanking!=null)?it.meaningRanking:0]}</td>
				<td class=${colors[(it.pronunciationRanking!=null)?it.pronunciationRanking:0]}>${level[(it.pronunciationRanking!=null)?it.pronunciationRanking:0]}</td>
				<td>${it.lastUpdated.format('MM/dd/yyyy HH:mm')}</td>
			</tr>
		</g:each>
	</tbody>
</table>
<div class="pagination">
<g:paginate controller="CardRanking" action="list" total="${cardRankingInstanceTotal}" params="${params}"/>
</div>
