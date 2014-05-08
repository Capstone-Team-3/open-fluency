<%@ page import="com.openfluency.Constants" %>
<%@ page import="com.openfluency.course.Quiz" %>
<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"></head>
<body>
	<div class="container quiz-show">
		<div class="row">
			<div class="col-lg-12">

				<header class="text-center">
					<h1>Quiz: ${quizInstance.title}</h1>
					<h3>
						<g:link action="show" controller="course" id="${quizInstance.course.id}">${quizInstance.course.title}</g:link>
					</h3>
				</header>
				<g:form action="update" controller="quiz" id="${quizInstance.id}">
					<input name="course.id" value="${quizInstance.course.id}" type="hidden"/>
					<div class="col-lg-6 col-lg-offset-3">
						<g:render template="form" model="[quizInstance: quizInstance, courseInstance: quizInstance.course]"/>
						<button type="submit" class="btn btn-info">Update</button>
						<g:link class="btn btn-default" controller="course" action="show" id="${quizInstance.course.id}">Cancel</g:link>
					</div>
					<div class="row" id="include-chapters"></div>
				</g:form>
			</div>
			<!-- end col-lg-12 -->
		</div>
		<!-- end row -->
		<br>
		<div class="row">
			<div class="col-lg-12">
				<h2 class="h3">Quiz Questions</h2>
			</div>
			<g:each in="${quizInstance.questions}">
				<g:render template="/quiz/question" model="[questionInstance: it, isOwner: isOwner]"/>
			</g:each>
		</div>
	</div>
	<!-- end container -->
	<g:javascript>initializeQuizCreator();</g:javascript>
</body>
</html>