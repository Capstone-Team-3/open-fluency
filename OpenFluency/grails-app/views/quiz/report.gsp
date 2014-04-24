<%@ page import="com.openfluency.Constants" %>
<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"></head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-lg-12">
				<h1>Quiz Result</h1>
				<h2>Course: ${answerInstanceList[0].question.quiz.title}</h2>
				<h2>Final grade: ${gradeInstance.correctAnswers/answerInstanceList.size()*100.0}%</h2>
				<table class="table">
					<tr>
						<th>You chose:</th>
						<th>Correct answer:</th>
						<th>Correct</th>
					</tr>
					<g:each in="${answerInstanceList}">
						<tr>
							<td>${it.selection?.id}</td>
							<td>${it.question.flashcard.id}</td>
							<td>${it.selection == it.question.flashcard}
						</tr>
					</g:each>
				</table>
			</div>
		</div>
	</div>
</body>