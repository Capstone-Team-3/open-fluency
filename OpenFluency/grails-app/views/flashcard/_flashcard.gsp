<%@ page import="com.openfluency.media.Audio" %>
<div class="flashcard animated slideInRight" data-id="${flashcardInstance.id}">
	<div class="panel panel-default">
		<div class="panel-body">
			<div class="flashcard-header">
				<div class="card-actions">
					<g:if test="${isOwner}">
						<g:link action="delete" controller="flashcard" id="${flashcardInstance.id}" class="btn btn-danger btn-xs flashcard-delete-${flashcardInstance.id}"><span class="glyphicon glyphicon-remove"></span></g:link>
					</g:if>
					<g:if test="${practicing}">
						<a href="#" id="customizationBtn" title="Add your own image or audio pronunciation file to help you learn!">
							<span class="glyphicon glyphicon-pencil"></span>
						</a>
					</g:if>
				</div>
				<h1 id="flashcard-unit-text" class="flashcard-unit" data-unit="${flashcardInstance?.primaryUnit.print}">
					<% 	def flashcardPrimaryUnitStr = flashcardInstance?.primaryUnit.getPrint().trim();
						def charArr = flashcardPrimaryUnitStr.toCharArray();  %>
					<g:each in="${ charArr }">
						<span class='clickable-character'>${it;}</span>				
					</g:each>
				</h1>
				<div class="pronunciation" data-pronunciation="${flashcardInstance?.pronunciation.literal }">
					<g:if test="${flashcardPrimaryUnitStr != flashcardInstance?.pronunciation.literal }">
						pronounced "${flashcardInstance?.pronunciation.literal}"	
					</g:if>
					<g:else>
						&nbsp;
					</g:else>
					<g:if test="${!audioSysURL}">
						<g:set var="audioSysURL" value="${flashcardInstance?.audio?.url}" />
					</g:if>
					<g:if test="${!audioSysId}">
						<g:set var="audioSysId" value="${flashcardInstance?.audio?.id}" />
					</g:if>

					<g:if test="${audioSysURL}">
						<g:set var="audioSource" value=""/>
						<g:set var="audioId" value="${audioSysURL}"/>
						<span class="play-audio glyphicon glyphicon-volume-up"></span>
						<audio class="flashcard-audio hidden" id=${audioSysId} src="${audioSource + audioId}" controls></audio>
					</g:if>
					<g:elseif test="${audioSysId}">
						<g:set var="audioSource" value="/OpenFluency/audio/sourceAudio/"/>
						<g:set var="audioId" value="${audioSysId}"/>
						<span class="play-audio glyphicon glyphicon-volume-up"></span>
						<audio class="flashcard-audio hidden" id="flashcard-audio-${audioId}" src="${audioSource + audioId}" controls></audio>
					</g:elseif>
				</div>
			</div>

			<%
			
				String imageSource = flashcardInstance?.image?.url
			
				if (imageSource != null) {
					if (!(imageSource.startsWith('http://') || imageSource.startsWith('https://'))) {
						
						if (File.separatorChar=='\\') {
							imageSource = java.nio.file.Paths.get("/", imageSource).normalize()
							imageSource =  imageSource.replace('\\', '/')
						}
				
						String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
						imageSource = baseUrl + imageSource
					}
				}
			 %>
			 
			<g:if test="${imageURL}">
				<div id="image-container">
					<div id="flashcard-image" class="flashcard-img" style="background-image: url('${imageURL}') ; background-size: 100% 100%"></div>
				</div>
			</g:if>
			<g:elseif test="${imageSource}">
				<div id="image-container">
					<div id="flashcard-image" class="flashcard-img" style="background-image: url('${imageSource}') ; background-size: 100% 100%"></div>
				</div>
			</g:elseif>

			<div class="meaning">${flashcardInstance?.secondaryUnit.print}</div>
		</div>
	</div>
</div>

<script>


$('.clickable-character').click(function() {
	$('#dictionary-search-textbox').val($(this).text().replace(/^\s+|\s+$/g,''));
	$('#dictionary-search-button').click();
});


</script>
