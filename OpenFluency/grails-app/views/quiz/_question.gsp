<%@ page import="com.openfluency.Constants" %>
<g:set var="testElement" value="${questionInstance.quiz.effectiveTestElement}"/>
<div class="col-lg-3">
	<div class="panel panel-default question-panel">
		<div class="panel-heading center">
			<g:if test="${isOwner}">
				<div class="card-actions">
					<g:link action="deleteQuestion" id="${questionInstance.id}" controller="quiz" class="btn btn-xs btn-danger" onclick="return confirm('Are you sure?')"><span class="glyphicon glyphicon-remove"></span></g:link>
				</div>
			</g:if>
			<g:if test="${testElement == Constants.SYMBOL}">
				<h1>${questionInstance.flashcard.secondaryUnit.print}</h1>
			</g:if>
			<g:elseif test="${testElement == Constants.MANUAL}">
				<h1>${questionInstance.question}</h1>
			</g:elseif>
			<g:else>
				<h1>${questionInstance.flashcard.primaryUnit.print}</h1>
			</g:else>
		</div>
		<div class="panel-body text-center">
			<h4>Multiple choice options:</h4>
			<table class="table">
				
				<g:each in="${questionInstance.options}">
					<tr>
						<g:if test="${testElement == Constants.MEANING}">
							<td>${it.flashcard.secondaryUnit.print}</td>
						</g:if>
						<g:elseif test="${testElement == Constants.PRONUNCIATION}">
							<td>${it.flashcard.pronunciation}</td>
						</g:elseif>
						<g:elseif test="${testElement == Constants.SYMBOL}">
							<td>${it.flashcard.primaryUnit.print}</td>
						</g:elseif>
						<g:elseif test="${testElement == Constants.MANUAL}">
							<td>${it.option}</td>
						</g:elseif>
					</tr>
				</g:each>
			</table>
		</div>
	</div>
	<!-- end panel -->
</div>
<!-- end col-lg-3 -->