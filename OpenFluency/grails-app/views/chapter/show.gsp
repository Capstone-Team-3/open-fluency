<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"/>
</head>
<body>
	<div class="container chapter-show">
		<ul class="breadcrumb">
            <li><a href="${createLink(uri:'/') }">Home</a></li>
            <li><g:link action="search" controller="course" >Courses</g:link></li>
            <li><g:link action="search" controller="course" >Search for Course</g:link></li>
            <li><g:link action="show" controller="course" id="${chapterInstance.course.id}">${chapterInstance.course.getCourseNumber()}: ${chapterInstance.course.title}</g:link></li>
            <li><a href="#">${chapterInstance.title}</a></li>
        </ul>
		<div class="chapter-header">
			<h1>${chapterInstance.title}</h1>
			<p>${chapterInstance.description}</p>
			<g:if test="${flashcardCount > 0}"><!-- should also only display if enrolled -->
				<g:link class="btn btn-success" action="practice" id="${chapterInstance.id}" controller="chapter">Practice Flashcards</g:link>
			</g:if>
		</div>
		<!-- end chapter-header -->

		<div class="row">
			<g:each in="${chapterInstance.deck.flashcards}">
				<div class="col-lg-3">
					<g:render template="/flashcard/flashcard" model="[flashcardInstance: it]"/>
				</div>
				<!-- end col-lg-3 -->
			</g:each>
		</div>
		<!-- end row -->
	</div>
	<!-- end container -->

	<g:javascript>
		$(function(){

		})
	</g:javascript>
</body>
</html>