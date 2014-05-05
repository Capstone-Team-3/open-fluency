<%@ page import="com.openfluency.Constants" %>
<div class="col-lg-3">
	<div class="panel panel-default question-panel">
		<div class="panel-heading center">
			<g:if test="${isOwner}">
				<div class="card-actions">
					<g:link action="deleteQuestion" id="${questionInstance.id}" controller="quiz" class="btn btn-xs btn-danger" onclick="return confirm('Are you sure?')"><span class="glyphicon glyphicon-remove"></span></g:link>
				</div>
			</g:if>
			<g:if test="${Constants.CARD_ELEMENTS[questionInstance.quiz.testElement] == "Symbol"}">
				<h1>${questionInstance.flashcard.secondaryUnit.print}</h1>
			</g:if>
			<g:else>
				<h1>${questionInstance.flashcard.primaryUnit.print}</h1>
			</g:else>
		</div>
		<div class="panel-body text-center">
			<h4>Multiple choice options:</h4>
			<table class="table">
				<g:each in="${questionInstance.options}">
					<tr>
						<g:if test="${Constants.CARD_ELEMENTS[questionInstance.quiz.testElement] == "Meaning"}">
							<td>${it.flashcard.secondaryUnit.print}</td>
						</g:if>
						<g:elseif test="${Constants.CARD_ELEMENTS[questionInstance.quiz.testElement] == "Symbol"}">
							<td>${it.flashcard.primaryUnit.print}</td>
						</g:elseif>
						<g:else>
							<td>${it.flashcard.pronunciation}</td>
						</g:else>
					</tr>
				</g:each>
			</table>
		</div>
	</div>
	<!-- end panel -->
</div>
<!-- end col-lg-3 -->