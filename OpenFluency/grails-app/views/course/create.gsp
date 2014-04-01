<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"/>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-lg-6 col-lg-offset-3">
				<h1>New Course</h1>
				
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

					<div class="form-group">
						<label>Title</label>
						<input class="form-control" type="text" name="title" value="${courseInstance?.title}"/>
					</div>

					<div class="form-group">
						<label>Description</label>
						<textarea class="form-control" name="description" value="${courseInstance?.description}"></textarea>
					</div>

					<button class="btn btn-info">Create</button>
				</g:form>
			</div>
		</div>
	</div>
</body>
</html>