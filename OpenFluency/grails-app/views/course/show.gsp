<%@ page import="com.openfluency.course.Registration" %>
<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"/>
</head>
<body>
	<div class="container course-show">
		<div class="row">
			<div class="col-lg-12">
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
			</div>
		</div>

		<div class="row">
			<div class="col-lg-8 col-lg-offset-2">

				<div class="course-header text-center">
					<h1>
						<span class="course-title">${courseInstance.title}</span>
						<sec:ifAllGranted roles="ROLE_STUDENT">
							<g:if test="${!Registration.findAllByCourseAndUser(courseInstance, userInstance)}">
								<g:link class="btn btn-info" action="enroll" controller="course" id="${courseInstance.id}">Enroll</g:link>
							</g:if>
						</sec:ifAllGranted>
						<g:if test="${isOwner}">
							<g:link action="edit" id="${courseInstance?.id}" class="btn btn-sm btn-warning">Edit</g:link>
						</g:if>
					</h1>
					<p class="course-description">${courseInstance.description}</p>
				</div>
			</div>
		</div>

		<div class="dashboard">
			<div class="row">
				<div class="col-lg-12">

					<h2>
						Chapters
						<g:if test="${isOwner}">
							<!-- This is only displayed for the owner of the course -->
							<g:link class="btn btn-info" action="create" controller="chapter" id="${courseInstance.id}">Add Chapters</g:link>
						</g:if>
					</h2>

					<div class="container">
						<div class="row">
							<g:each in="${courseInstance.chapters}">
								<div class="col-lg-3">
									<div class="panel panel-default">
										<div class="panel-heading">
											<h4>
												<g:link action="show" id="${it.id}" controller="chapter">${it.title}</g:link>
											</h4>
										</div>
										<div class="panel-body">
											<p>${it.deck.flashcards.size()} Flashcards</p>
											<g:render template="/deck/progress" model="[progress: it.progress]"/>
										</div>
										<div class="panel-footer center">
											<g:if test="${isOwner}">
												<g:link action="edit" controller="chapter" id="${it.id}" class="btn btn-warning">Edit</g:link>
												<g:link action="delete" controller="chapter" id="${it.id}" class="btn btn-danger" onclick="return confirm('are you sure?')">Remove</g:link>
											</g:if>
										</div>
									</div>
								</div>
							</g:each>
						</div>
					</div>
				</div>
			</div>

			<div class="row">
				<div class="col-lg-12">

					<h2>
						Quizzes
						<g:if test="${isOwner}">
							<!-- This is only displayed for the owner of the course -->
							<g:link class="btn btn-info" action="create" controller="quiz" id="${courseInstance.id}">Add Quiz</g:link>
						</g:if>
					</h2>

					<div class="container col-lg-12">
						<g:each in="${quizesInstanceList}">
							<div class="col-lg-3">
								<div class="panel panel-default">
									<div class="panel-body">
										<h3>${it.title}</h3>
										${it.questions.size()} Questions
									</div>

									<div class="panel-footer">
										<div class="continue">
											<g:if test="${isOwner}">
												<g:link class="view-more" action="show" controller="quiz" id="${it.id}" >
													View Quiz
													<span class="glyphicon glyphicon-arrow-right"></span>
												</g:link>
											</g:if>
											<g:else>
												<g:link action="take" controller="quiz" id="${it.id}" class="btn btn-success">Take Quiz</g:link>
											</g:else>
										</div>
									</div>
								</div>
							</div>
							<!-- end col-lg-3 -->
						</g:each>
					</div>
					<!-- end container -->

					<g:if test="${isOwner}">
						<div class="row">
							<div class="col-lg-12">
								<h2>Students</h2>
								<g:if test="${students.size()}">
									<g:link class="view-more" action="students" controller="course" id="${it.course.id}">
										${Registration.findAllByCourse(courseInstance).size()} Enrolled Students
										<span class="glyphicon glyphicon-arrow-right"></span>
									</g:link>
								</g:if>
								<g:else >There are no students enrolled for this course.</g:else>
							</div>
							<!-- end col-lg-12 -->
						</div>
						<!-- end row -->
					</g:if>

				</div>
				<!-- end col-lg-12 -->
			</div>
			<!-- end row -->
		</div>
		<!-- end dashboard -->

	</div>
	<!-- end container -->

</body>
</html>