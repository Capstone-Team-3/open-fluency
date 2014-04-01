<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"/>
</head>
<body>
	<div class="container">
		<div class="row">
			
			<sec:ifAllGranted roles="ROLE_INSTRUCTOR">
				<!-- Instructor -->
				<div class="col-lg-6 col-lg-offset-3">
					<h1>
						Courses I teach
						<g:link action="create" controller="course" class="btn btn-info">Create</g:link>
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
				</div>
			</sec:ifAllGranted>

			<sec:ifAllGranted roles="ROLE_STUDENT">
				<!-- Student -->
				<div class="col-lg-6 col-lg-offset-3">
					<h1>
						Courses I'm taking
						<g:link action="search" controller="course" class="btn btn-info">Add more</g:link>
					</h1>
					<table class="table">
						<tr>
							<th>Title</th>
						</tr>
						<g:each in="${registrations}">
							<tr>
								<td>
									<g:link action="show" controller="course" id="${it.course.id}">${it.course.title}</g:link>
								</td>
							</tr>
						</g:each>
					</table>
				</div>
			</sec:ifAllGranted>
		</div>
	</div>
</body>
</html>