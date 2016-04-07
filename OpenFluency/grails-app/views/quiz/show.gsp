v<%@ page import="com.openfluency.Constants" %>
<%@ page import="com.openfluency.course.Quiz" %>
<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"></head>
<body>
	<div class="container quiz-show">
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
						<g:link action="show" controller="course" id="${quizInstance.course.id}">
							${quizInstance.course.getCourseNumber()}: ${quizInstance.course.title}
						</g:link>
					</li>
					<li>
						<g:link action="show" controller="quiz" id="${quizInstance.id}">${quizInstance.title}</g:link>
					</li>
				</ul>

				<header class="text-center">
					<h1>
						Quiz: ${quizInstance.title}
						<g:if test="${isOwner}">
							<g:link action="edit" id="${quizInstance.id}" controller="quiz" class="btn btn-warning">
								<span class="glyphicon glyphicon-pencil"></span>
							</g:link>
						</g:if>
					</h1>
					<h3>${quizInstance.course.title}</h3>
				</header>

				<div class="description">
					<ul class="list-unstyled">
						<li> 
							<strong>Maximum time allowed per card:</strong>
							<g:if test="${quizInstance.maxCardTime > 0}">
								${quizInstance.maxCardTime} seconds
							</g:if>
							<g:else>
								None specified.
							</g:else>
						</li>
						<li>
							<strong>Quiz available starting:</strong>
							${quizInstance.liveTime.format('MM/dd/yyyy')}
						</li>
						<li>
							<g:if test="${quizInstance.endTime != null}">
							<strong>Quiz available until:</strong>	
							${quizInstance.endTime.format('MM/dd/yyyy hh:mm')}</li>
							</g:if>
						</li>
					</ul>
				</div>

			</div>
			<!-- end col-lg-12 -->

			<g:each in="${quizInstance.questions}">
				<g:render template="/quiz/question" model="[questionInstance: it]"/>
			</g:each>

		</div>
		<!-- end row -->

	</div>
	<!-- end container -->
</body>
</html>