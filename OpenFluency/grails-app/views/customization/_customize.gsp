<div id="customize-container" class="col-lg-7">

	<!--g:form action="save" controller="customization" name="createCustomizationForm"-->
		<input type="hidden" id="c_fId" name="flashcardId" value="${flashcardInstance.id}"/>
		<input type="hidden" id="c_umId" name="unitMappingId" value="${flashcardInstance.unitMapping.id}"/>
		<input type="hidden" id="c_pId" name="pronunciationId" value="${flashcardInstance.pronunciation.id}"/>
					
		<div class="form-group">
			<input class="btn btn-info" id="custom-image" value="Customize Image?"/>
			<div id="custom-image-container">
				<label for="c_imageLink">URL:</label>
				<input type="text" size="80" id="c_imageLink" name="c_imageLink" value="${flashcardInstance?.image.url}"/>
			
				<div class="flickr-search-container">
					<h4>Flickr Image Search</h4>
					<label for="query">Query:</label>
					<input id="c_query" name="c_query" type="text" size="60" placeholder="Type here to find your photo" />
					<button id="c_flickr_search" class="btn btn-info">Search</button>
					<div id="c_results"></div>
					<button id="c_flickr_back" class="btn btn-info">Back</button>
					<label id="c_flickr_page_number"></label>
					<button id="c_flickr_next" class="btn btn-info">Next</button>
				</div>
			</div>
		</div>

		<div class="form-group audio">
			<input class="btn btn-info" id="custom-audio" value="Customize Audio?"/>
			<div id="custom-audio-container">
				<audio id="c_audioClip" controls autoplay></audio>
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
	<!--/g:form-->
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

$('#customizationCreate').click(function(){
    $('#customize-container').hide();
    $('#customizationBtn').show();
    $("#closeCustomization").hide();
    saveCustomization();   
});

function saveCustomization(){
	// Create the packet
	var custData = new FormData();
	custData.append('flashcardId', $('#c_fId').val());
	custData.append('unitMappingId', $('#c_umId').val());
	custData.append('imageLink', $('#c_imageLink').val());
	custData.append('audioId', $('#c_audio_id').val());
	// Send it
	$.ajax({
		type: 'POST',
		url: '/OpenFluency/customization/save',
		data: custData,
		processData: false,
		contentType: false
	}).done(function(customizationInstance) {
		if(customizationInstance.id) {
			console.log("Success!");
			console.log(customizationInstance.id);
		} else {
			console.log("Something went wrong!");
		}
	});
}

$('#custom-image').click(function(){
	$('#custom-image-container').show();
	$('#custom-image').hide();
});

$('#custom-audio').click(function(){
	$('#custom-audio-container').show();
	$('#custom-audio').hide();
});

$("#c_start_rec_button").click(function(){
	setWorkerPath("../../js/recorderWorker.js"); 
	startRecording(); 
	$("#c_audioSaveMessage").show();
	$("#customizationCreate").removeClass('btn-success').addClass('btn-warning');
});

$("#c_stop_rec_button").click(function(){ 
	stopRecording("#c_audioClip", $("#c_pId").val(), ""); 
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

$("#c_flickr_search").click(function(){
	$("#c_flickr_page_number").val(1).text(1);
	console.log($('#c_query').val());
	searchImage("#c_query", "#c_results", "#c_imageLink",1);
});
$("#c_flickr_next").click(function(){
	var targetPage = $("#c_flickr_page_number").val();
	targetPage++;
	searchImage("#c_query", "#c_results", "#c_imageLink", targetPage);
	$("#c_flickr_page_number").val(targetPage).text(targetPage);
});
$("#c_flickr_back").click(function(){
	var targetPage = $("#c_flickr_page_number").val();
	if (targetPage > 1) { 
		targetPage--;
		searchImage("#c_query", "#c_results", "#c_imageLink", targetPage);
		$("#c_flickr_page_number").val(targetPage).text(targetPage);
	}
});

</g:javascript>