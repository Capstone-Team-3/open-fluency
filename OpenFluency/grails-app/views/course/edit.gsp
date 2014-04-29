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
				<g:link action="show" controller="course" id="${courseInstance.id}">
					${courseInstance.getCourseNumber()}: ${courseInstance.title}
				</g:link>
			</li>
			<li>
				<a href="#">Edit Course</a>
			</li>
		</ul>
		<div class="row">
			<div class="col-lg-6 col-lg-offset-3">
				<h1>Edit Course</h1>

				<g:hasErrors bean="${courseInstance}">
					<ul class="errors" role="alert">
						<g:eachError bean="${courseInstance}" var="error">
							<li>
								<g:message error="${error}"/>
							</li>
						</g:eachError>
					</ul>
				</g:hasErrors>

				<g:form action="update" controller="course" name="updateCourseForm" id="${courseInstance?.id}">	
					<g:render template="form" model="[courseInstance: courseInstance]"/>
					<div class="center">
						<button class="btn btn-info">Save</button>
					</div>
				</g:form>
			</div>
		</div>
	</div>
</body>
</html>