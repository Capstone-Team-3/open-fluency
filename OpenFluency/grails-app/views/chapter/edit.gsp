<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"/>
</head>
<body>
	<div class="container">
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
				<g:link action="show" controller="course" id="${chapterInstance.course.id}">
					${chapterInstance.course.getCourseNumber()}: ${chapterInstance.course.title}
				</g:link>
			</li>
			<li>
				<g:link action="show" controller="chapter" id="${chapterInstance.id}">
					${chapterInstance.title}
				</g:link>
			</li>
			<li>
				<a href="#">Edit Chapter</a>
			</li>
		</ul>
		<div class="row">
			<div class="col-lg-6 col-lg-offset-3">
				<h1>Edit chapter</h1>

				<g:hasErrors bean="${chapterInstance}">
					<ul class="errors" role="alert">
						<g:eachError bean="${chapterInstance}" var="error">
							<li>
								<g:message error="${error}"/>
							</li>
						</g:eachError>
					</ul>
				</g:hasErrors>

				<g:form action="update" controller="chapter" name="createChapterForm" id="${chapterInstance.id}">
					<input type="hidden" value="${chapterInstance.course.id}" name="courseId"/>
					<g:render template="form" model="[chapterInstance: chapterInstance]"/>
					<button class="btn btn-info">Save</button>
				</g:form>
			</div>
		</div>
	</div>
</body>
</html>