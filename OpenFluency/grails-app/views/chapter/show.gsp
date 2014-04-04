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

		<!-- this is the panel that indicates progress through the deck, perhaps this should move to a template for decks too -->
		<div>
			<p>
				<button id="previous">Previous</button>
				<span id="offset">1</span>
				of
				<span id="total">${chapterInstance.deck.flashcardCount}</span>
				<button id="next">Next</button>
			</p>
		</div>

	</div>
	<!-- end container -->

	<g:javascript>
		$(function(){

		})
	</g:javascript>
</body>
</html>