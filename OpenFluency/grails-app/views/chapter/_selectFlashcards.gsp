<div class="flashcard-select col-lg-12 animated fadeInDown" id="flashcard-select-${chapterInstance.id}">
	<h4>${chapterInstance.title}</h4>
	<g:render template="/flashcard/flashcardSelect" var="flashcardInstance" collection="${chapterInstance.deck.flashcards}"/>
</div>