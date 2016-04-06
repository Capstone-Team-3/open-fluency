<%@ page import="com.openfluency.Constants" %>
<div class="col-lg-3">
	<div class="panel panel-default question-panel">
		<div class="panel-heading center">
			<g:if test="${isOwner}">
				<div class="card-actions">
					<g:link action="deleteQuestion" id="${questionInstance.id}" controller="quiz" class="btn btn-xs btn-danger" onclick="return confirm('Are you sure?')"><span class="glyphicon glyphicon-remove"></span></g:link>
				</div>
			</g:if>
			
			<h1>${questionInstance.question}</h1>

		</div>
		<div class="panel-body text-center">
			<h4>Multiple choice options:</h4>
			<table class="table">
				
				<g:each in="${questionInstance.options}">
					<tr>
						<td>${it.option}</td>
					</tr>
				</g:each>
			</table>
		</div>
	</div>
	<!-- end panel -->
</div>
<!-- end col-lg-3 -->