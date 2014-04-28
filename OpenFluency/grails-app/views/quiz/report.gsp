<%@ page import="com.openfluency.Constants" %>
<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"></head>
<body>
	<div class="container quiz-report">
		<div class="row">

			<div class="col-lg-12">

				<ul class="breadcrumb">
					<li>
						<a href="${createLink(uri:'/') }">Home</a>
					</li>
					<li>
						<g:link action="list" controller="course" >Enrolled Courses</g:link>
					</li>
					<li>
						<g:link action="show" controller="course" id="${answerInstanceList[0].question.quiz.course.id}">${answerInstanceList[0].question.quiz.course.title}</g:link>
					</li>
					<li>
						<g:link action="take" controller="quiz" id="${answerInstanceList[0].question.quiz.id}">${answerInstanceList[0].question.quiz.title}</g:link>
					</li>
				</ul>

			</div>

			<div class="col-lg-8 col-lg-offset-2">

				<header class="text-center">
					<h1>Results for ${answerInstanceList[0].question.quiz.title}</h1>
					<h2>Final grade: ${gradeInstance.correctAnswers/answerInstanceList.size()*100.0}%</h2>
				</header>

				<table class="table table-bordered">
					<tr>
						<th>You chose:</th>
						<th>Correct answer:</th>
					</tr>
					<g:each in="${answerInstanceList}">
						<g:if test="${it.selection == it.question.flashcard}">
							<tr>
						</g:if>
						<g:else>
							<tr class="danger">
						</g:else>
							<td>${it.selection?.id}</td>
							<td>${it.question.flashcard.id}</td>
						</tr>
					</g:each>
				</table>

			</div>
		</div>
	</div>
</body>