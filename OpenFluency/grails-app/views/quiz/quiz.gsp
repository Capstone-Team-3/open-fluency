<%@ page import="com.openfluency.Constants" %>
<g:set var="testElement" value="${answerInstance.question.quiz.effectiveTestElement}"/>
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
					<input type="hidden" name="maxCardTime" id="maxCardTime" value="${quizInstance.maxCardTime}" />
					<input type="hidden" name="quiz" value="${quizInstance.id}" />

					<div class="center">
						<strong><span class="glyphicon glyphicon-time"></span> Time Remaining</strong>
						<div id="clock" class="clock"></div>
					</div>
					<g:if test="${testElement == Constants.SYMBOL}">
						<h1 class="unit text-center">${answerInstance.question.flashcard.secondaryUnit.print}</h1>
					</g:if>
					<g:else>
						<h1 class="unit text-center">${answerInstance.question.flashcard.primaryUnit.print}</h1>
					</g:else>

					<div class="col-lg-6 col-lg-offset-3">
						<p> <strong>Select the matching ${Constants.CARD_ELEMENTS[testElement].toLowerCase()}:</strong> 
						</p>
						<ul class="list-group">
							<g:each in="${answerInstance.question.selections}">
								<li class="list-group-item">
									<label>
										<input type="radio" name="option" id="option" value="${it.id}" checked>
										<g:if test="${testElement == Constants.MEANING}">${it.secondaryUnit.print}</g:if>
										<g:elseif test="${testElement == Constants.PRONUNCIATION}">${it.pronunciation}</g:elseif>
										<g:elseif test="${testElement == Constants.SYMBOL}">${it.primaryUnit.print}</g:elseif>
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
	<g:javascript>initCountdown();</g:javascript>
</body>