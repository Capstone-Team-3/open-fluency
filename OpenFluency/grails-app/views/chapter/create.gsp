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
						<div class="${hasErrors(bean: chapterInstance, field: 'title', 'error')} required">
							<label for="title" class="control-label">
								<g:message code="chapter.title.label" default="Title" />
								<span class="required-indicator">*</span>
							</label>
							<input class="form-control" type="text" name="title"  required="" value="${chapterInstance?.title}"/>
						</div>
					</div>
					
					<div class="form-group">
						<div class="${hasErrors(bean: chapterInstance, field: 'description', 'error')} required">
							<label for="description" class="control-label">
								<g:message code="chapter.description.label" default="Description" />
								<span class="required-indicator">*</span>
							</label>
							<textarea class="form-control" name="description" required="" value="${chapterInstance?.description}"></textarea>
						</div>
					</div>
					
					<div class="form-group">
						<div class="${hasErrors(bean: chapterInstance, field: 'Choose a deck', 'error')} required">
							<label for="choose a deck" class="control-label">
								<g:message code="chapter.choose a deck.label" default="Choose a deck" />
								<span class="required-indicator">*</span>
							</label>
							<g:select class="form-control" name="deckId" required="" from="${userDecks}" noSelection="['':'-Choose a deck-']" optionKey="id" optionValue="title"/>
						</div>
					</div>
					
					<button class="btn btn-info">Create</button>
				</g:form>
			</div>
		</div>
	</div>
</body>
</html>
