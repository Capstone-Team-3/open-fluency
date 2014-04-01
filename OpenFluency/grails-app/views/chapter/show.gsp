<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"/>
</head>
<body>
	<div class="container">

		<div class="row">
			<div class="col-lg-6 col-lg-offset-3">
				<h1>${chapterInstance.title}</h1>
				<p>${chapterInstance.description}</p>
			</div><!-- end col-lg-6 -->
		</div><!-- end row -->
		
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