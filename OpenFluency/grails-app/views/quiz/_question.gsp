<%@ page import="com.openfluency.Constants" %>
<div class="col-lg-3">
	<div class="panel panel-default question-panel">
		<div class="panel-heading center">
			<g:if test="${isOwner}">
				<div class="card-actions">
					<g:link action="deleteQuestion" id="${questionInstance.id}" controller="quiz" class="btn btn-xs btn-danger" onclick="return confirm('Are you sure?')"><span class="glyphicon glyphicon-remove"></span></g:link>
				</div>
			</g:if>
			<g:if test="${questionInstance?.question == "image"}">
			<div id="flashcard-image" class="flashcard-img" style="background-image: url(${questionInstance?.image.getImageUri()})"></div>
			</g:if>
			<g:elseif test="${questionInstance?.question == "sound"}">

            <br/>
			<audio controls="controls" preload="metadata">
 			 <source src="${questionInstance.sound.getSoundUri()}" />
  			<b>Your browser does not support HTML5 audio element</b>
			</audio><br />

			<!--
			<span id="pronounced">(pronunciation text)</span>
			<span class="play-audio glyphicon glyphicon-volume-up"></span>
			<audio class="flashcard-audio" id="um-flashcard-audio" src="${questionInstance.sound.getSoundUri()}" controls></audio>

			<div id="audio-url-droppable">
			<span id="clear-audio-url" class="close-thin"></span>
			<span id="audio-url-display">(audio url)</span>
			</div>
			-->
			<!-- <div id="flashcard-image" class="flashcard-img" style="background-image: url(${questionInstance.sound.getSoundUri()})"></div>  -->
			</g:elseif>
			<g:else>
			<h1>${questionInstance.question}</h1>
			</g:else>
		</div>
		<div class="panel-body text-center">
			<h4>Multiple choice options:</h4>
			<table class="table">
				
				<g:each in="${questionInstance.options}">
					<tr>
						<td>${it.option}</td>
					</tr>
				</g:each>
			</table>
		</div>
	</div>
	<!-- end panel -->
</div>
<!-- end col-lg-3 -->

<script>
$(document).ready(function(){
	var initializeAudio = function() {
	$(".play-audio").click(function() {
		var audioSrcID = $(this).next(".flashcard-audio").attr("id");
		$('#' + audioSrcID).load().get(0).play();
	});
};
	initializeUnitMappingDraggable();
});
	
</script>