<%@ page import="com.openfluency.Constants" %>
<style>
#player {
 width: 90%;
 padding: 0px 0px 0px;
}​
</style>

<div class="col-lg-3">
	<div class="panel panel-default flashcard-panel">
		<div class="panel-heading center">
			<g:if test="${isOwner}">
				<div class="card-actions">
					<g:link action="deleteQuestion" id="${questionInstance.id}" controller="quiz" class="btn btn-xs btn-danger" onclick="return confirm('Are you sure?')"><span class="glyphicon glyphicon-remove"></span></g:link>
				</div>
			</g:if>
			<g:if test="${questionInstance?.questionType ==  Constants.IMAGE}">
			
			<div id="question-image" class="question-img" style="background-image: url('${questionInstance?.image?.getImageUri()}')"></div>
		
			</g:if>
			<g:elseif test="${questionInstance?.questionType ==  Constants.SOUND}">
			<audio id="player" controls="controls" preload="metadata">
 			<source src="${questionInstance?.sound?.getSoundUri()}" />
  			<b>Your browser does not support HTML5 audio element</b>
			</audio>
			</g:elseif>
			<g:else>
			<h4>${questionInstance.question}<h4>
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
</div>

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