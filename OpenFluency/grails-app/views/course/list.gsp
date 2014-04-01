<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"/>
</head>
<body>
	<div class="container course-list">
		<div class="row">

			<sec:ifAllGranted roles="ROLE_INSTRUCTOR">
				<!-- Instructor -->
				<h1>
					My Courses
					<g:link action="create" controller="course" class="btn btn-info">Create Course</g:link>
				</h1>
				<table class="table">
					<tr>
						<th>Title</th>
					</tr>
					<g:each in="${myCourses}">
						<tr>
							<td>
								<g:link action="show" controller="course" id="${it.id}">${it.title}</g:link>
							</td>
						</tr>
					</g:each>
				</table>
			</sec:ifAllGranted>

			<sec:ifAllGranted roles="ROLE_STUDENT">
				<!-- Student -->
				<h1>
					Enrolled Courses
				<g:link action="search" controller="course" class="btn btn-info">Add Courses</g:link>
				</h1>
				<table class="table">
					<tr>
						<th>Title</th>
						<th>Description</th>
						<th>Instructor</th>
					</tr>
					<g:each in="${registrations}">
						<tr>
							<td>
								<g:link action="show" controller="course" id="${it.course.id}">${it.course.title}</g:link>
							</td>
							<td>${it.course.description}</td>
							<td>${it.course.owner.username}</td>
						</tr>
					</g:each>
				</table>
			</sec:ifAllGranted>
		</div>
	</div>
</body>
</html>