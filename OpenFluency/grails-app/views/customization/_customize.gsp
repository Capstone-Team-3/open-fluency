<div id="customize-container" class="col-lg-7">

	<g:form action="save" controller="customization" name="createCustomizationForm">
		<input type="hidden" name="flashcardId" value="${flashcardInstance.id}"/>
		<input type="hidden" name="unitMappingId" value="${flashcardInstance.unitMapping.id}"/>
		<input type="hidden" id="c_pId" name="pronunciationId" value="${flashcardInstance.pronunciation.id}"/>
					
		<div class="form-group">
			<input class="btn btn-info" id="custom-image" value="Customize Image?"/>
			<div id="custom-image-container">
				<g:textField class="form-control" id="imageLink" name="imageLink" value="${flashcardInstance?.image}"/>
			
				<div class="flickr-search-container">
					<h1>Flickr Search</h1>
					<label for="query">Query:</label>
					<input id="query" name="query" type="text" size="60" placeholder="Type here to find your photo" />
					<button id="flickr_search" class="btn btn-info">Search</button>
					<div id="results"></div>
					<button id="flickr_back" class="btn btn-info">Back</button>
					<label id="flickr_page_number"></label>
					<button id="flickr_next" class="btn btn-info">Next</button>
				</div>
			</div>
		</div>

		<div class="form-group audio">
			<input class="btn btn-info" id="custom-audio" value="Customize Audio?"/>
			<div id="custom-audio-container">
				<audio id="audioClip" controls autoplay></audio>
				</br>
				<input id="c_start_rec_button" name="start_button" type="button" value="Start Recording" class="btn btn-info"/>
				<input id="c_stop_rec_button" name="stop_button" type="button" value="Stop Recording" class="btn btn-info"/>
				<input id="c_save_rec_button" name="save_button" type="button" value="Save Recording" class="btn btn-warning"/>
				<input id="c_audio_id" name="c_audio_id" type="hidden" value=""/>
				</br>
				<span><i>*may need to click 'Allow' in audio permissions pop up</i></span>
				</br>
			</div>
		</div>
		
		<button id="customizationCreate" class="center btn btn-success">Customize it!</button>
		<span id="c_audioSaveMessage" class="audio-save-message">*did you save your audio?</span>
	</g:form>
</div>

<g:javascript>
	$('#customize-container').hide();
</g:javascript>

<g:javascript src="recorderWorker.js"/>
<g:javascript src="recorder.js"/>
<g:javascript src="create_audio.js"/>
<g:javascript>
$('#custom-image-container').hide();
$('#custom-audio-container').hide();

$("#c_start_rec_button").click(function(){ 
	startRecording(); 
	$("#c_audioSaveMessage").show();
	$("#customizationCreate").removeClass('btn-success').addClass('btn-warning');
});

$("#c_stop_rec_button").click(function(){ 
	stopRecording($("#c_pId").val(), ""); 
	$("#c_save_rec_button").show();
});


$("#c_audioSaveMessage").hide();
$("#c_save_rec_button").hide();
$("#c_save_rec_button").click(function(){
	saveAudioRecording(formData);
	$("#c_audioSaveMessage").hide();
	$("#c_save_rec_button").hide();
	$("#customizationCreate").removeClass('btn-warning').addClass('btn-success');
});

function saveAudioRecording(formDataObj){
	// Create the packet
	var fd = formDataObj;
	// Send it
	$.ajax({
		type: 'POST',
		url: '/OpenFluency/audio/save',
		data: fd,
		processData: false,
		contentType: false
	}).done(function(audioInstance) {
		if(audioInstance.id) {
			console.log("Success!");
			$("#c_audio_id").val(audioInstance.id);
			console.log(audioInstance.id);
		} else {
			console.log("Something went wrong!");
		}
	});
}
</g:javascript>