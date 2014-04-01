<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"/>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-lg-6 col-lg-offset-3">
				<h1>Courses Available</h1>
				<table class="table">
					<tr>
						<th>Title</th>
						<th></th>
					</tr>
					<g:each in="${courseInstanceList}">
						<tr>
							<td>
								<g:link action="show" controller="course" id="${it.id}">${it.title}</g:link>
							</td>
							<td>
								<g:link action="enroll" controller="course" id="${it.id}" class="btn btn-info">Enroll</g:link>
							</td>
						</tr>
					</g:each>
				</table>
			</div>
		</div>
	</div>
</body>
</html>