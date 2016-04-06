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
						<g:link action="list" controller="course">Enrolled Courses</g:link>
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
					<h1>
						Results for ${answerInstanceList[0].question.quiz.title}<br>
						<span class="label label-info">Final grade: ${gradeInstance.correctAnswers/answerInstanceList.size()*100}%</span>
					</h1>
				</header>

				<table class="table table-bordered">
					<thead>
						<tr>
							<th>Character/word</th>
							<th>You chose:</th>
							<th>Correct answer:</th>
						</tr>
					</thead>
					<tbody>
						<g:each in="${answerInstanceList}">
							<g:if test="${it.selection.id == it.question.correctOption.id}">
							<tr>
							</g:if>
							<g:else>
							<tr class="warning">
							</g:else>
								
								<td>${it.question.question}</td>
		
								<td>${it.selection.option}</td>
								<td>${it.question.correctOption.option} <span class="glyphicon glyphicon-ok pull-right"></span></td>

							</tr>
						</g:each>
					</tbody>
				</table>

				<p class="well summary text-center"><strong>You answered ${gradeInstance.correctAnswers} out of ${answerInstanceList.size()} correctly.</strong></p>
			</div>
		</div>
	</div>
</body>