
	<tr><th class="tg-yw4l">Results:</th></tr>

<g:each status="i"  in="${ dictionarySearchResults }" var="it">
	<tr id="dictionary-row-${i}" data-concept="${it.concept}" data-meaning="${it.meaning}" data-pronunciation="${it.pronunciation}">
		<td>
			${ it.concept }: ${ it.meaning } : ${ it.pronunciation }
		</td>
	</tr>
</g:each>
