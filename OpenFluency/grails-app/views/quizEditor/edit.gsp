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
						<g:link action="show" controller="course" id="${quizInstance.course.id}">${quizInstance.course.title}</g:link>
					</li>
					<li>
						<g:link action="edit" controller="quizEditor" id="${quizInstance.id}">Edit Quiz</g:link>
					</li>
				</ul>
			</div>

			<g:form action="update" controller="quizEditor">
				<input name="course.id" value="${quizInstance.course.id}" type="hidden"/>
				<div class="col-lg-6 col-lg-offset-3">

					<h1 class="text-center">Edit Quiz for ${quizInstance.course.title}</h1>
					<g:render template="form" model="[quizInstance: quizInstance, courseInstance: courseInstance]"/>
					<button type="submit" class="btn btn-info">Save Quiz</button>
				</div>
			</g:form>
		</div>
	</div>
</body>
</html>