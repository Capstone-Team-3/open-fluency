<div class="col-lg-3">
	<div class="well">
		<h1>Unit: ${questionInstance.flashcard.primaryUnit.print}</h1>
		<h4>Question options</h4>
		<table class="table">
			<g:each in="${questionInstance.options}">
				<tr>
					<td>${it.flashcard.primaryUnit.print}</td>
				</tr>
			</g:each>
		</table>
	</div>
</div>