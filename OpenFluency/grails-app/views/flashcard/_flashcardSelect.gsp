<div class="flashcard col-lg-3">
	<div class="panel panel-default">
		<div class="flashcard-header">
			<h1 class="flashcard-unit">${flashcardInstance?.primaryUnit.print}</h1>
			<div class="pronunciation">pronounced '${flashcardInstance?.pronunciation.literal}'</div>
		</div>
		<g:set var="imageSource" value="${flashcardInstance?.image?.url}"/>
		<g:if test="${imageURL}">
			<g:set var="imageSource" value="${imageURL}"/>
		</g:if>
		<g:if test="${imageSource}">
			<div id="flashcard-image" class="flashcard-img" style="background-image: url(${imageSource});"></div>
		</g:if>
		<div class="meaning">${flashcardInstance?.secondaryUnit.print}</div>
		<input name="flashcardId" type="checkbox" value="${flashcardInstance.id}"/>
	</div>
</div>