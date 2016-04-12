-<%@ page import="com.openfluency.course.Registration" %>
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
						<g:link action="search" controller="course">Courses</g:link>
					</li>
					<li>
						<g:link action="search" controller="course">Search for Course</g:link>
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
							<g:link action="edit" id="${courseInstance?.id}" class="btn btn-warning"><span class="glyphicon glyphicon-pencil"></span></g:link>
							<g:link action="delete" id="${courseInstance?.id}" class="btn btn-danger"><span class="glyphicon glyphicon-trash"></span></g:link>
						</g:if>
					</h1>
					<p class="course-description">${courseInstance.description}</p>

					<g:if test="${isOwner}">
						<g:if test="${students.size()}">
							<g:link class="btn btn-success" action="students" controller="course" id="${courseInstance.id}">
								View Students (${Registration.findAllByCourse(courseInstance).size()})
							</g:link>
						</g:if>
						<g:else>There are no students enrolled for this course.</g:else>
					</g:if>

				</div>
			</div>
		</div>

		<div class="row">
			<div class="col-lg-12">

				<h2 class="sub-heading">
					Chapters
					<g:if test="${isOwner}">
						<g:link class="btn btn-xs btn-info add-chapter"  action="create" controller="chapter" id="${courseInstance.id}"><span class="glyphicon glyphicon-plus"></span></g:link>
					</g:if>
				</h2>

				<div class="row course-row">
					<g:each in="${courseInstance.chapters}">
						<div class="col-lg-4">
							<div class="panel panel-default">
								<div class="panel-heading">
									<div class="card-actions">
										<g:if test="${isOwner}">
											<g:link action="edit" controller="chapter" id="${it.id}" class="btn btn-xs btn-warning"><span class="glyphicon glyphicon-pencil"></span></g:link>
											<g:link action="delete" controller="chapter" id="${it.id}" class="btn btn-xs btn-danger" onclick="return confirm('are you sure?')"><span class="glyphicon glyphicon-remove"></span></g:link>
										</g:if>
									</div>
									<h4>
										<g:link action="show" id="${it.id}" controller="chapter" class="chapter-show">${it.title}</g:link>
									</h4>
									<p>${it.deck.getFlashcardCount()} Flashcards</p>
								</div>
								<g:if test="${!isOwner}">
									<div class="donut-container">
										<div class="panel-body">
											<div class="col-lg-4  col-md-4 col-sm-4 col-xs-4 progress-donut center" data-progress="${it.progress[Constants.MEANING]}" id="meaning-progress-${it.id}">
												<p>${it.deck.language} to ${it.deck.sourceLanguage}</p>
											</div>

											<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4 progress-donut center" data-progress="${it.progress[Constants.SYMBOL]}" id="symbol-progress-${it.id}">
												<p>${it.deck.sourceLanguage} to ${it.deck.language}</p>
											</div>

											<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4 progress-donut center" data-progress="${it.progress[Constants.PRONUNCIATION]}" id="pronunciation-progress-${it.id}">
												<p>Pronunciations in ${it.deck.language}</p>
											</div>
										</div>
									</div>
								</g:if>
								<g:else>
									<div class="panel-body">
										<p>${it.description}</p>
									</div>
								</g:else>
							</div><!-- end panel -->
						</div><!-- end col-lg-4 -->
					</g:each>
				</div><!-- end row -->
			</div><!-- end col-lg-12 -->
		</div><!-- end row -->

		<div class="row">
			<div class="col-lg-12">

				<h2 class="sub-heading">
					Quizzes
					<g:if test="${isOwner}">
						<!-- This is only displayed for the owner of the course -->
						<g:link class="btn btn-xs btn-info" action="create" controller="quiz" id="${courseInstance.id}"><span class="glyphicon glyphicon-plus"></span></g:link>
					</g:if>
				</h2>

				<div class="row quiz-row">
					<g:each in="${quizesInstanceList}">
						<div class="col-lg-3">
							<div class="panel panel-default">
								<div class="panel-heading">
									<g:if test="${isOwner}">
										<div class="card-actions">
											<g:link action="edit" controller="quiz" id="${it.id}" class="btn btn-xs btn-warning"><span class="glyphicon glyphicon-pencil"></span></g:link>
											<g:link action="delete" controller="quiz" id="${it.id}" class="btn btn-xs btn-danger" onclick="return confirm('are you sure?')"><span class="glyphicon glyphicon-remove"></span></g:link>
										</div>
									</g:if>
									<h4>
										<g:if test="${isOwner}">
											<g:link action="show" id="${it.id}" controller="quiz">${it.title}</g:link>
										</g:if>
										<g:else>${it.title}</g:else>
									</h4>
									<p>${it.questions.size()} questions</p>
								</div>
								<div class="panel-body text-center">
									<g:if test="${!isOwner}">
										<g:if test="${it.finalGrade}">
											<div class="quiz-complete bg-success">
												<p><strong>Completed - Grade: ${it.finalGrade}%</strong></p>
												<g:link class="btn btn-info" action="take" controller="quiz" id="${it.id}">View Report</g:link>
											</div>
										</g:if>
										<g:elseif test="${ ( it?.liveTime && (it.liveTime <= new Date())) }" >
											<g:if test="${Registration.countByCourseAndUser(courseInstance, userInstance) == 1}">
												<div class="take-quiz">
													<p>Ready to take the quiz?</p>
													<g:link action="take" controller="quiz" id="${it.id}" class="take-quiz-btn btn btn-success">Start Quiz</g:link>
												</div>
											</g:if>
										</g:elseif>
									</g:if>
									<g:else>
										<ul class="list-unstyled text-left">
										<li><strong>Tests:</strong> 
											<g:if test="${Constants.CARD_ELEMENTS[it.testElement] != null}">
												<g:if test="${Constants.CARD_ELEMENTS[it.testElement].toLowerCase() == "meaning"}">
													Meanings of words/characters (${it.course.getChapters()[0].deck.language} to ${it.course.getChapters()[0].deck.sourceLanguage})
												</g:if>
												<g:elseif test="${Constants.CARD_ELEMENTS[it.testElement].toLowerCase() == "symbol"}">
													Meanings of words/characters (${it.course.getChapters()[0].deck.sourceLanguage} to ${it.course.getChapters()[0].deck.language})
												</g:elseif>
												<g:else>
													Pronunciations of ${it.course.getChapters()[0].deck.language} words/characters
												</g:else>
											</g:if>
										</li>
											<li><strong>Available:</strong> ${it.liveTime.format('MM/dd/yyyy hh:mm')}</li>
										</ul>
									</g:else>
								</div><!-- end panel-body -->
							</div><!-- end panel -->
						</div><!-- end col-lg-3 -->
					</g:each>
				</div><!-- end row -->
			</div><!-- end col-lg-12 -->
		</div><!-- end row -->
	</div><!-- end container -->

	<g:javascript>initializeDonuts();</g:javascript>
</body>
</html>