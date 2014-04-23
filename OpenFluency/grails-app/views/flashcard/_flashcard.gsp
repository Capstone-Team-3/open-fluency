<%@ page import="com.openfluency.media.Audio" %>
<div class="flashcard">
	<div class="panel panel-default">

		<div class="flashcard-header">
			<a href="#" id="customizationBtn"><span class="glyphicon glyphicon-pencil"></span></a>
			<h1 class="flashcard-unit">${flashcardInstance?.primaryUnit.print}</h1>
			<div class="pronunciation">pronounced '${flashcardInstance?.pronunciation.literal}'</div>
		</div><!-- end flashcard-header -->
		
		<!--g:if test="${flashcardInstance.image}"-->
		<g:set var="imageSource" value="${flashcardInstance?.image?.url}"/>
		<g:if test="${imageURL}">
			<g:set var="imageSource" value="${imageURL}"/>
		</g:if>
		<g:if test="${imageSource}">
			<div id="flashcard-image" class="flashcard-img" style="background-image: url(${imageSource});"></div>
		</g:if>

		<div class="meaning">${flashcardInstance?.secondaryUnit.print}
			<!--  <button class="btn btn-default" id="hide-meaning" onclick = "this.style.visibility='hidden'">Hide Meaning</button> -->
		</div>

		<!-- 'practicing' needs to be set to true in the model passed to this page for audio to show - practiceCards does this -->
		<g:if test="${practicing}">
			<!--g:if test="${flashcardInstance.audio?.audioWAV}"-->
			<g:if test="${audioSysId}">
				<g:set var="audioSource" value="/OpenFluency/audio/sourceAudio/"/>
				<g:set var="audioId" value="${audioSysId}"/>
				<li class="fieldcontain">
					<div class="audio-practice">
						<audio id="flashcard-audio" src="${audioSource + audioId}" controls play></audio>
					</div>
				</li>
			</g:if>
		</g:if>

	</div>
</div>
<g:javascript>
	console.log($())
</g:javascript>