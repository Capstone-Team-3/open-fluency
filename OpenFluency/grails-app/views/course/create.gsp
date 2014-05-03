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
				<a href="#">Create New Course</a>
			</li>
		</ul>
		<div class="row">
			<div class="col-lg-6 col-lg-offset-3">
				<h1 id="main">Create New Course</h1>

				<g:hasErrors bean="${courseInstance}">
					<ul class="errors" role="alert">
						<g:eachError bean="${courseInstance}" var="error">
							<li>
								<g:message error="${error}"/>
							</li>
						</g:eachError>
					</ul>
				</g:hasErrors>

				<g:form action="save" controller="course" name="createCourseForm">	
					<g:render template="form" model="[courseInstance: courseInstance]"/>
					<div class="center">
						<button class="btn btn-info" id="create-course">Create Course</button>
					</div>
				</g:form>
			</div>
		</div>
	</div>
</body>
</html>