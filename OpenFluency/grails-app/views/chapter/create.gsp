<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"/>
</head>
<body>
	<div class="container">
		<ul class="breadcrumb">
            <li><a href="${createLink(uri:'/') }">Home</a></li>
            <li><g:link action="search" controller="course" >Courses</g:link></li>
            <li><g:link action="search" controller="course" >Search for Course</g:link></li>
            <li><a href="#">Create New Chapter</a></li>
        </ul>
		<div class="row">
			<div class="col-lg-6 col-lg-offset-3">
				<h1>New Chapter for ${courseInstance.title}</h1>

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

					<div class="form-group">
						<label>Title</label>
						<span class="required-indicator">*</span>
						<input class="form-control" type="text" name="title" value="${chapterInstance?.title}"/>
					</div>

					<div class="form-group">
						<label>Description</label>
						<span class="required-indicator">*</span>
						<textarea class="form-control" name="description" value="${chapterInstance?.description}"></textarea>
					</div>

					<div class="form-group">
						<label>Choose a deck</label>
						<span class="required-indicator">*</span>
						<g:select class="form-control" name="deckId" from="${userDecks}" noSelection="['':'-Choose a deck-']" optionKey="id" optionValue="title"/>
					</div>

					<button class="btn btn-info">Create</button>
				</g:form>
			</div>
		</div>
	</div>
</body>
</html>