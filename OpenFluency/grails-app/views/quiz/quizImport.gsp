<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"></head>
<body>
	<div class="container quiz-create">
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
						<g:link action="show" controller="course" id="${courseInstance.id}">${courseInstance.getCourseNumber()}: ${courseInstance.title}</g:link>
					</li>
					<li>
						<g:link action="create" controller="quiz">Add Quiz</g:link>
					</li>
				</ul>
			</div>
						
			<g:form action="loadQuizFromCSV" controller="quiz" enctype="multipart/form-data">
				<input name="course.id" value="${courseInstance.id}" type="hidden"/>
				<div class="col-lg-6 col-lg-offset-3">

					<h1 class="text-center">Import Quiz for ${courseInstance.title}</h1>
					<g:render template="importForm" model="[quizInstance: quizInstance, courseInstance: courseInstance]"/>
				
				</div>
			
			</g:form>
		</div>
	</div>
	<!-- end container -->
	<g:javascript>initializeQuizCreator();</g:javascript>
</body>
</html>
