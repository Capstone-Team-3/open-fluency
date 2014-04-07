<div class="flashcard">
	<div class="panel panel-default">

		<h1>${flashcardInstance?.primaryUnit.print}</h1>
		<div class="pronunciation">${flashcardInstance?.pronunciation.literal}</div>
		<!-- image association -->
		<g:if test="${flashcardInstance.image}">
			<div class="flashcard-img" style="background-image: url(${flashcardInstance.image.url});"></div>
		</g:if>
		<div class="meaning">${flashcardInstance?.secondaryUnit.print}</div>
	</div>
</div>