<%@ page import="com.openfluency.Constants" %>
<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"></head>
<body>
	<div class="container quiz">
		<div class="row">
			<div class="col-lg-12">
				<ul class="breadcrumb">
					<li>
						<a href="${createLink(uri:'/') }">Home</a>
					</li>
					<li>
						<g:link action="list" controller="course">Enrolled Courses</g:link>
					</li>
					<li>
						<g:link action="show" controller="course" id="${quizInstance.course.id}">
							${quizInstance.course.getCourseNumber()}: ${quizInstance.course.title}
						</g:link>
					</li>
					<li>
						<a href="#">${quizInstance.title}</a>
					</li>
				</ul>

				<g:form action="nextQuestion" controller="quiz" id="${answerInstance.id}">
					<input type="hidden" name="quiz" value="${quizInstance.id}" />

					<h1 class="unit text-center">${answerInstance.question.flashcard.primaryUnit.print}</h1>
					
					<div class="col-lg-6 col-lg-offset-3">
						<p><strong>Select the matching ${Constants.CARD_ELEMENTS[answerInstance.question.quiz.testElement].toLowerCase()}:</strong></p>
						<ul class="list-group">
							<g:each in="${answerInstance.question.selections}">
								<li class="list-group-item">
									<label>
										<input type="radio" name="option" id="option" value="${it.id}" checked>
										<g:if test="${answerInstance.question.quiz.testElement == Constants.MEANING}">${it.secondaryUnit.print}</g:if>
										<g:elseif test="${answerInstance.question.quiz.testElement == Constants.PRONUNCIATION}">${it.pronunciation}</g:elseif>
									</label>
								</li>
							</g:each>
						</ul>
						<button type="submit" class="btn btn-info center-block">Next Question</button>
					</div>
				</g:form>

			</div>
		</div>
	</div>
</body>