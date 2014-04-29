<div class="col-lg-3">
	<div class="panel panel-default question-panel">
		<div class="panel-heading center">
			<h1>${questionInstance.flashcard.primaryUnit.print}</h1>
		</div>
		<div class="panel-body text-center">
			<h4>Multiple choice options:</h4>
			<table class="table">
				<g:each in="${questionInstance.options}">
					<tr>
						<td>${it.flashcard.primaryUnit.print}</td>
					</tr>
				</g:each>
			</table>
		</div>
		<div class="panel-footer center">
			<g:if test="${isOwner}">
				<g:link action="deleteQuestion" id="${questionInstance.id}" controller="quiz" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure?')">Remove</g:link>
			</g:if>
		</div>
	</div>
	<!-- end panel -->
</div>
<!-- end col-lg-3 -->