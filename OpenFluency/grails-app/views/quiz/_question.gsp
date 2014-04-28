<div class="col-lg-3">
	<div class="panel panel-default question-panel">
		<div class="panel-body text-center">
			
			<h1>${questionInstance.flashcard.primaryUnit.print}</h1>
			<h4>Multiple choice options:</h4>
			<table class="table">
				<g:each in="${questionInstance.options}">
					<tr>
						<td>${it.flashcard.primaryUnit.print}</td>
					</tr>
				</g:each>
			</table>

		</div><!-- end panel-body -->
	</div><!-- end panel -->
</div><!-- end col-lg-3 -->