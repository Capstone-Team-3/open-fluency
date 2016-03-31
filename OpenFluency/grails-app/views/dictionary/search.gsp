
	<tr><th class="tg-yw4l">Results:</th></tr>

<g:each in="${ dictionarySearchResults }">
	<tr>
		<td>
			${ it.concept }: ${ it.meaning } : ${ it.pronunciation }
		</td>
	</tr>
</g:each>