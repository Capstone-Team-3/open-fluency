<%@ page import="com.openfluency.course.Registration" %>
<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"/>
</head>
<body>
	<div class="container course-show">
		<ul class="breadcrumb">
			<li>
				<a href="${createLink(uri:'/') }">Home</a>
			</li>
			<li>
				<g:link action="search" controller="course" >Courses</g:link>
			</li>
			<li>
				<g:link action="search" controller="course" >Search for Course</g:link>
			</li>
			<li>
				<a href="#">${courseInstance.getCourseNumber()}: ${courseInstance.title}</a>
			</li>
		</ul>
		<div class="course-header">
			<h1>
				<span class="course-title">${courseInstance.title}</span>
				<sec:ifAllGranted roles="ROLE_STUDENT">
					<g:if test="${!Registration.findAllByCourseAndUser(courseInstance, userInstance)}">
						<g:link class="btn btn-info" action="enroll" controller="course" id="${courseInstance.id}">Enroll</g:link>
					</g:if>
				</sec:ifAllGranted>

				
			</h1>
			<p class="course-description">${courseInstance.description}</p>

		</div>
		<!-- end course-header -->
<div class="dashboard">
		<div class="row">
			<div class="col-lg-12">
				<h2>Chapters</h2>
				<div class="container">
					<g:if test="${isOwner}">
						<!-- This is only displayed for the owner of the course -->
						<g:link class="btn btn-info" action="create" controller="chapter" id="${courseInstance.id}">Add Chapters</g:link>
					</g:if>
					<g:each in="${courseInstance.chapters}">
						<div class="col-lg-3">
							<div class="panel panel-default">
								<div class="panel-body">
									<h3>
										<g:link action="show" id="${it.id}" controller="chapter">${it.title}</g:link>
									</h3>
									<p>${it.deck.flashcards.size()} Flashcards</p>
									<g:render template="/deck/progress" model="[progress: it.progress]"/>
								</div>
							</div>
						</div>
						<!-- end col-lg-3 -->
					</g:each>
				</div> <!-- end container -->
			</div>
		</div>
		<!-- end row -->
		
		<div class="row">
			<div class="col-lg-12">
				<h2>Quizzes</h2>
				<div class="container">
					<g:if test="${isOwner}">
						<!-- This is only displayed for the owner of the course -->
						<g:link class="btn btn-info" action="create" controller="quiz" id="${courseInstance.id}">Add Quiz</g:link>
					</g:if>
					<g:each in="${quizesInstanceList}">
						<div class="col-lg-3">
							<div class="panel panel-default">
								<div class="panel-body">
									<h3>${it.title}</h3>
									<p>${it.questions.size()} Questions</p>
									<br>
									<div class="center">
										<g:link action="take" controller="quiz" id="${it.id}" class="btn btn-success">Take Quiz</g:link>
									</div>
								</div>
							</div>
						</div>
						<!-- end col-lg-3 -->
					</g:each>
				</div> <!-- end container -->
			</div>
		</div>
		<!-- end row -->
		
		<g:if test="${isOwner}">
			<div class="row">
				<div class="col-lg-12">
					<h2>Students</h2>
					<g:if test="${students.size()}">
					<g:link class="view-more" action="students" controller="course" id="${it.course.id}">${Registration.findAllByCourse(courseInstance).size()} Enrolled Students <span class="glyphicon glyphicon-arrow-right"></span></g:link>
				</g:if>
				<g:else >
					There are no students enrolled for this course.
				</g:else>
				</div>
			</div>
			<!-- end row -->
		</g:if>
	</div>
	</div>
	<!-- end container -->
</body>
</html>