<%@ page import="com.openfluency.media.Audio" %>
<div class="flashcard">

	<div class="panel panel-default">

		<div class="flashcard-header">
			
			<a id="customizationBtn"><span class="glyphicon glyphicon-pencil"></span></a>
			
			<h1 class="flashcard-unit">${flashcardInstance?.primaryUnit.print}</h1>
			
			<div class="pronunciation">
				pronounced '${flashcardInstance?.pronunciation.literal}'

				<g:if test="${audioSysId}">
					<span id="flashcard-play-audio" class="glyphicon glyphicon-volume-up"></span>
					<g:set var="audioSource" value="/OpenFluency/audio/sourceAudio/"/>
					<g:set var="audioId" value="${audioSysId}"/>
					<div class="audio-practice">
						<audio id="flashcard-audio" src="${audioSource + audioId}" autobuffer></audio>
					</div>
				</g:if>

			</div><!-- end pronunciation -->

		</div><!-- end flashcard-header -->

		<!--g:if test="${flashcardInstance.image}"-->
		<g:set var="imageSource" value="${flashcardInstance?.image?.url}"/>
		<g:if test="${imageURL}">
			<g:set var="imageSource" value="${imageURL}"/>
		</g:if>
		<g:if test="${imageSource}">
			<div id="flashcard-image" class="flashcard-img" style="background-image: url(${imageSource});"></div>
		</g:if>

		<div class="meaning">
			${flashcardInstance?.secondaryUnit.print}
			<!--  <button class="btn btn-default" id="hide-meaning" onclick = "this.style.visibility='hidden'">Hide Meaning</button> -->
		</div>

	</div>
</div>

<g:javascript>
	$('#flashcard-play-audio').on('click', function(){
		$('#flashcard-audio').trigger('play');
	});
</g:javascript>