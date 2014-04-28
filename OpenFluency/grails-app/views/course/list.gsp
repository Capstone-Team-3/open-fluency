<%@ page import="com.openfluency.course.Registration" %>
<%@ page import="com.openfluency.Constants" %>
<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"/>
</head>
<body>
	<div class="container course-list">
		<ul class="breadcrumb">
			<li>
				<a href="${createLink(uri:'/') }">Home</a>
			</li>
			<li>
				<g:link action="search" controller="course">Courses</g:link>
			</li>
			<li>
				<g:link action="list" controller="course">Enrolled Courses</g:link>
			</li>
		</ul>

		<sec:ifAllGranted roles="ROLE_INSTRUCTOR">
			<!-- Instructor -->
			<h1>
				My Courses
				<g:link action="create" controller="course" class="btn btn-info">Create Course</g:link>
			</h1>
			<table class="table my-courses">
				<thead>
					<tr>
						<th>Code</th>
						<th>Title</th>
						<th>Students Enrolled</th>
						<th>Last Updated</th>
						<th>Start Date</th>
						<th>End Date</th>
					</tr>
				</thead>
				<tbody>
					<g:each in="${myCourses}">
						<tr>
							<td>${it.courseNumber}</td>
							<td>
								<g:link action="show" controller="course" id="${it.id}">${it.title}</g:link>
							</td>
							<td>
								<a href="#">${Registration.countByCourseAndStatusNotEqual(it, Constants.REJECTED)}</a>
							</td>
							<td>${it.lastUpdated}</td>
							<td>${it.startDate}</td>
							<td>${it.endDate}</td>
						</tr>
					</g:each>
				</tbody>
			</table>
		</sec:ifAllGranted>

		<sec:ifAllGranted roles="ROLE_STUDENT">
			<!-- Student -->
			<h1>
				Enrolled Courses
				<g:link action="search" controller="course" class="btn btn-info">Add Courses</g:link>
			</h1>
			<table class="table enrolled-courses">
				<thead>
					<tr>
						<th>Title</th>
						<th>Description</th>
						<th>Instructor</th>
					</tr>
				</thead>
				<tbody>
					<g:each in="${registrations}">
						<tr>
							<td>
								<g:link action="show" controller="course" id="${it.course.id}">${it.course.title}</g:link>
							</td>
							<td>${it.course.description}</td>
							<td>${it.course.owner.username}</td>
						</tr>
					</g:each>
				</tbody>
			</table>
		</sec:ifAllGranted>
	</div>
</body>
</html>