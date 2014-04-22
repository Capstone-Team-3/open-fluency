<%@ page import="com.openfluency.Constants" %>
<%@ page import="com.openfluency.course.Quiz" %>
<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"></head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-lg-12">
				<h1>Quiz: ${quizInstance.title}</h1>
				<h3>Course: ${quizInstance.course.title}</h3>
				<p>Testing ${Constants.CARD_ELEMENTS[quizInstance.testElement]}</p>
				<p>Max Card Time: ${quizInstance.maxCardTime}</p>
			</div>

			<g:each in="${quizInstance.questions}">
				<g:render template="/quiz/question" model="[questionInstance: it]"/>
			</g:each>
		</div>
	</div>
</body>
</html>