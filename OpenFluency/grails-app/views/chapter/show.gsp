<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"/>
</head>
<body>
	<div class="container chapter-show">

		<div class="chapter-header">
			<g:link action="show" controller="course" id="${chapterInstance.course.id}">${chapterInstance.course.title}</g:link>
			<h1>${chapterInstance.title}</h1>
			<p>${chapterInstance.description}</p>
		</div><!-- end chapter-header -->
		
		<div class="row">
			<g:each in="${chapterInstance.deck.flashcards}">
				<div class="col-lg-3">
					<g:render template="/flashcard/flashcard" model="[flashcardInstance: it]"/>
				</div><!-- end col-lg-3 -->
			</g:each>
		</div><!-- end row -->
		
	</div><!-- end container -->
</body>
</html>