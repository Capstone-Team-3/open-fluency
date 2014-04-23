<%@ page import="com.openfluency.Constants" %>
<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main">
	<g:set var="entityName" value="${message(code: 'quiz.label', default: 'Quiz')}" />
	<title><g:message code="default.create.label" args="[entityName]" /></title> 
</head>
<body>
	<g:form action="save" controller="quiz">
		<input name="course.id" value="${courseInstance.id}" type="hidden">
		<div class="container">
			<div class="row">
				<div class="col-lg-12">
					<h1>Create Quiz for ${courseInstance.title}</h1>
					<div class="well">
						<div class="form-group">
							<label for="title">Title</label>
							<g:textField name="title" value="${quizInstance?.title}" class="form-control"/>
						</div>
						<div class="form-group">
							<label for="maxCardTime">
								Maximum time per card (seconds). If 0, the test will not be timed
							</label>
							<g:field type="number" min="0" max="10" name="maxCardTime" value="0" class="form-control"/>
						</div>
						<div class="form-group">
							<label for="maxCardTime">Element to hide on each card</label>
							<select class="form-control" name="testElement">
								<g:each in="${Constants.CARD_ELEMENTS}" status="i" var="element">
									<option value="${i}">${element}</option>
								</g:each>
							</select>
						</div>
						<div class="form-group">
							<label for="maxCardTime">Live time</label>
							<g:datePicker name="liveTime" value="${new Date()}"
              class="form-control"/>
						</div>
					</div>

					<h4>Chapters to include</h4>
					<div class="well">
						<table class="table">
							<g:each in="${courseInstance.chapters}">
								<tr>
									<td>${it.title}</td>
									<td>
										<input type="checkbox" data-chapter="${it.id}" class="chapter-selector"/>
									</td>
								</tr>
							</g:each>
						</table>
					</div>
				</div>
			</div>
			<div class="row" id="include-chapters"></div>
			<div class="row">
				<div class="col-lg-12 center">
					<button type="submit" class="btn btn-info btn-lg">Create it!</button>
				</div>
			</div>
		</div>
	</g:form>
	<g:javascript>initializeQuizCreator();</g:javascript>
</body>
</html>