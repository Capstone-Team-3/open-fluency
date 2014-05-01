<%@ page import="com.openfluency.media.Audio" %>
<div class="flashcard animated slideInRight">
	<div class="panel panel-default">
		<div class="panel-body">
			<div class="flashcard-header">
				<a href="#" id="customizationBtn">
					<span class="glyphicon glyphicon-pencil"></span>
				</a>
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
		<div class="panel-footer">
			<g:if test="${isOwner}">
				<g:link action="delete" controller="flashcard" id="${flashcardInstance.id}" class="btn btn-danger btn-xs">Delete</g:link>
			</g:if>
		</div>
	</div>
</div>