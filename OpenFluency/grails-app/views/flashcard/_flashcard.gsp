<%@ page import="com.openfluency.media.Audio" %>
<div class="flashcard">
	<div class="panel panel-default">

		<h1>${flashcardInstance?.primaryUnit.print}</h1>
		<div class="pronunciation">${flashcardInstance?.pronunciation.literal}</div>
		<!-- image association -->
		<g:if test="${flashcardInstance.image}">
			<div class="flashcard-img" style="background-image: url(${flashcardInstance.image.url});"></div>
		</g:if>

		<div class="meaning">${flashcardInstance?.secondaryUnit.print}
			<!--  <button class="btn btn-default" id="hide-meaning" onclick = "this.style.visibility='hidden'">Hide Meaning</button> -->
		</div>
		<!-- 'practicing' needs to be set to true in the model passed to this page for audio to try to show - practiceCards does this -->
		<g:if test="${practicing}">
			<g:if test="${flashcardInstance.audio?.audioWAV}">
				<g:set var="audioSource" value="/OpenFluency/audio/sourceAudio/"/>
				<g:set var="audioId" value="${flashcardInstance.audio.id}"/>
				<li class="fieldcontain">
					<div class="audio-practice">
						<audio src="${audioSource + audioId}" controls play></audio>
					</div>
				</li>
			</g:if>
		</g:if>

	</div>
</div>
<g:javascript>
	console.log($())
</g:javascript>
