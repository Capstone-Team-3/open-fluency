<%@ page import="com.openfluency.Constants" %>
<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main">
</head>
<body>
	<g:form action="save" controller="quiz">
		<input name="course.id" value="${courseInstance.id}" type="hidden">
		<div class="container quiz-create">
			<div class="row">
				
				<div class="col-lg-12">
					<ul class="breadcrumb">
						<li>
							<a href="${createLink(uri:'/') }">Home</a>
						</li>
						<li>
							<g:link action="list" controller="course">My Courses</g:link>
						</li>
						<li>
							<g:link action="show" controller="course" id="${courseInstance.id}">${courseInstance.getCourseNumber()}: ${courseInstance.title}</g:link>
						</li>
						<li>
							<g:link action="create" controller="quiz">Add Quiz</g:link>
						</li>
					</ul>
				</div>

				<div class="col-lg-6 col-lg-offset-3">
					
					<h1 class="text-center">Create Quiz for ${courseInstance.title}</h1>
					
					<div class="form-group">
						<label for="title">Title:</label>
						<g:textField name="title" value="${quizInstance?.title}" class="form-control"/>
					</div>
					
					<div class="form-group">
						<label for="maxCardTime">
							Maximum seconds allowed per card (if 0, the quiz will not be timed):
						</label>
						<g:field type="number" min="0" max="10" name="maxCardTime" value="0" class="form-control"/>
					</div>
					
					<div class="form-group">
						<label for="testElement">Test students on:</label>
						<select class="form-control" name="testElement">
							<g:each in="${Constants.CARD_ELEMENTS}" status="i" var="element">
								<option value="${i}">${element}s of the words/characters</option>
							</g:each>
						</select>
					</div>

					<div class="form-group live-time-group">
						<label for="liveTime">Available starting:</label>
						<g:datePicker name="liveTime" value="${new Date()}" class="form-control"/>
					</div>

					<label for="included-chapters">Chapters to include:</label>
					<div class="panel panel-default">
						<div class="panel-body">
							<ul class="list-unstyled">
								<g:each in="${courseInstance.chapters}">
									<li><input type="checkbox" data-chapter="${it.id}" class="chapter-selector"/> ${it.title}</li>
								</g:each>					
							</ul>
						</div><!-- end panel-body -->
					</div><!-- end panel -->

				</div>
			</div>
			
			<div class="row" id="include-chapters"></div>

			<div class="row">
				<div class="col-lg-12 center">
					<button type="submit" class="btn btn-info">Create Quiz</button>
				</div>
			</div>
		</div>
	</g:form>
	<g:javascript>initializeQuizCreator();</g:javascript>
	<g:javascript>
	/*	$(document).ready(function() {
			$('select').addClass('form-control');
		});
	*/	
	</g:javascript>
</body>
</html>