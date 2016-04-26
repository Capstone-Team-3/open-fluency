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
					<input type="hidden" name="maxCardTime" id="maxCardTime" value="${quizInstance.maxCardTime}" />
					<input type="hidden" name="quiz" value="${quizInstance.id}" />

					<g:if test="${quizInstance.maxCardTime > 0}">
						<div class="center">
							<strong><span class="glyphicon glyphicon-time"></span> Time Remaining</strong>
							<div id="clock" class="clock"></div>
						</div>
					</g:if>
					

				<g:if test="${answerInstance?.question.question == "image"}">
				<div id="flashcard-image" class="flashcard-img" style="background-image: url(${answerInstance?.question.image.getImageUri()})"></div>
				</g:if>
				<g:elseif test="${answerInstance?.question.question == "sound"}">

					<audio controls="controls" autoplay preload="metadata">
 					 <source src="${answerInstance.question.sound.getSoundUri()}" />
  					<b>Your browser does not support HTML5 audio element</b>
					</audio>
					</g:elseif>
					<g:else>
					<h1 class="unit text-center">${answerInstance.question.question}</h1>
					</g:else>
					<div class="col-lg-6 col-lg-offset-3">
						<p> <strong>Select the correct answer:</strong> 
						</p>
						<ul class="list-group">
							<g:each in="${answerInstance.question.selections}">
								<li class="list-group-item">
									<label>
										<input type="radio" name="option" id="option" value="${it.id}" checked>
										${it.option}
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
	
	<g:if test="${quizInstance.maxCardTime > 0}">
		<g:javascript>initCountdown();</g:javascript>
	</g:if>
</body>