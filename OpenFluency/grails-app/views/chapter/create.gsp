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
				<a href="#">Create New Chapter</a>
			</li>
		</ul>
		<div class="row">
			<div class="col-lg-6 col-lg-offset-3">
				<h1 id="main">New Chapter for ${courseInstance.title}</h1>

				<g:hasErrors bean="${chapterInstance}">
					<ul class="errors" role="alert">
						<g:eachError bean="${chapterInstance}" var="error">
							<li>
								<g:message error="${error}"/>
							</li>
						</g:eachError>
					</ul>
				</g:hasErrors>

				<g:form action="save" controller="chapter" name="createChapterForm">
					<input type="hidden" value="${courseInstance.id}" name="courseId"/>
					<g:render template="form" model="[courseInstance: courseInstance]"/>
					<button class="btn btn-info" id="create-chapter">Create</button>
				</g:form>
			</div>
		</div>
	</div>
</body>
</html>