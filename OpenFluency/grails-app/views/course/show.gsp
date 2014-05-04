<%@ page import="com.openfluency.course.Registration" %>
<%@ page import="com.openfluency.course.Quiz" %>
<%@ page import="com.openfluency.Constants" %>
<%@ page import="com.openfluency.course.QuizService" %>
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
							<g:if test="${Registration.countByCourseAndUser(courseInstance, userInstance) == 0}">
								<g:link class="btn btn-info" action="enroll" controller="course" id="${courseInstance.id}">Enroll</g:link>
							</g:if>
							<g:else>
								<g:link class="btn btn-danger" action="drop" controller="course" id="${courseInstance.id}" onclick="return confirm('Are you sure you want to drop this course?')">Drop Course</g:link>
							</g:else>

						</sec:ifAllGranted>
						<g:if test="${isOwner}">
							<g:link action="edit" id="${courseInstance?.id}" class="btn btn-sm btn-warning">Edit</g:link>
							<g:link action="delete" id="${courseInstance?.id}" class="btn btn-sm btn-danger">Delete</g:link>
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
							<g:link class="btn btn-info add-chapter"  action="create" controller="chapter" id="${courseInstance.id}" >Add Chapters</g:link>
						</g:if>
					</h2>

					<div class="container">
						<div class="row">
							<g:each in="${courseInstance.chapters}">
								<div class="col-lg-4">
									<div class="panel panel-default">
										<div class="panel-heading">
											<h4>
												<g:link action="show" id="${it.id}" controller="chapter">${it.title}</g:link>
											</h4>
										</div>
										<g:if test="${!isOwner}">
											<div class="donut-container">
												<div class="panel-body">
													<div class="col-lg-4 progress-donut center" data-progress="${it.progress[Constants.MEANING]}" id="meaning-progress-${it.id}">
														<p>Meaning</p>
													</div>

													<div class="col-lg-4 progress-donut center" data-progress="${it.progress[Constants.PRONUNCIATION]}" id="pronunciation-progress-${it.id}">
														<p>Pronunciation</p>
													</div>
												</div>
											</div>
										</g:if>
										<g:else>
											<div class="panel-body">
												<p></p>
											</div>
										</g:else>
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

					<div class="container">
						<div class="row">
							<g:each in="${quizesInstanceList}">
								<div class="col-lg-3">
									<div class="panel panel-default">
										<div class="panel-heading">
											<h4>
												<g:link action="show" id="${it.id}" controller="quiz">${it.title}</g:link>
											</h4>
										</div>
										<div class="panel-body">${it.questions.size()} Questions</div>
										<div class="panel-footer">
											<div class="continue">
												<g:if test="${isOwner}">
													<g:link action="edit" controller="quiz" id="${it.id}" class="btn btn-warning">Edit</g:link>
													<g:link action="delete" controller="quiz" id="${it.id}" class="btn btn-danger" onclick="return confirm('are you sure?')">Remove</g:link>
												</g:if>
												<g:else>
													<g:if test="${it.finalGrade}">
														<h4>
															Current Grade : 
													    ${it.finalGrade}
														</h4>
														<g:link class="view-more" action="take" controller="quiz" id="${it.id}">
															View Report
															<span class="glyphicon glyphicon-arrow-right"></span>
														</g:link>
													</g:if>
													<g:elseif test="${ ( it?.liveTime && (it.liveTime <= new Date())) }" >
														<g:if test="${Registration.countByCourseAndUser(courseInstance, userInstance) == 1}">
															<g:link action="take" controller="quiz" id="${it.id}" class="btn btn-success">Take Quiz</g:link>
														</g:if>
													</g:elseif>
												</g:else>
											</div>
										</div>
									</div>
								</div>
							</g:each>
						</div>
					</div>

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
						</div>
					</g:if>
				</div>
			</div>
		</div>
	</div>
	<g:javascript>initializeDonuts();</g:javascript>
</body>
</html>