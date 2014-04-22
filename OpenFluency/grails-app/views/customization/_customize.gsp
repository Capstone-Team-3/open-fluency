<div id="customize-container">

	<button id="closeCustomization" class="btn btn-danger pull-right"><span class="glyphicon glyphicon-remove"></span></button>
	
	<!--g:form action="save" controller="customization" name="createCustomizationForm"-->
		<input type="hidden" id="c_fId" name="flashcardId" value="${flashcardInstance.id}"/>
		<input type="hidden" id="c_umId" name="unitMappingId" value="${flashcardInstance.unitMapping.id}"/>
		<input type="hidden" id="c_pId" name="pronunciationId" value="${flashcardInstance.pronunciation.id}"/>
		
		<h3>Customize Flashcard</h3>
			
		<div class="customize-image">

			<h4 id="custom-image"><span class="small glyphicon glyphicon-chevron-right"></span> Add/Change Image</h4>
			
			<div id="custom-image-container">
				<div class="form-group">
					<label for="c_imageLink">Paste an image URL:</label>
					<div class="row">
						<div class="col-lg-4">
							<input class="form-control" type="text" size="80" id="c_imageLink" name="c_imageLink" value="${flashcardInstance?.image?.url}"/>
						</div>
					</div>
				</div>

				<div class="flickr-search-container">
					<label>Or, search Flickr for an image:</label>
					<div class="row">
						<div class="col-lg-4">
							<input class="form-control" id="c_query" name="c_query" type="text" size="60" placeholder="Enter a keyword here to search photos" />
						</div>
						<button id="c_flickr_search" class="btn btn-info">Search</button>
					</div>
					
					<div id="c_results"></div>
					<button id="c_flickr_back" class="btn btn-info hidden">Back</button>
					<label id="c_flickr_page_number"></label>
					<button id="c_flickr_next" class="btn btn-info hidden">Next</button>
				</div><!-- end flickr-search-container -->
			
			</div><!-- end custom-image-container -->

		</div><!-- end customize-image -->

		<div class="customize-audio">
			
			<h4 id="custom-audio"><span class="small glyphicon glyphicon-chevron-right"></span> Add/Change Audio</h4>
			
			<div class="form-group" id="custom-audio-container">
				
				<small class="clearfix"><strong>Note:</strong> A browser pop-up may appear asking you to 'Allow' microphone use!</small>
				<audio id="c_audioClip" controls autoplay></audio>
				<div class="audio-controls">
					<input id="c_start_rec_button" name="start_button" type="button" value="Start Recording" class="btn btn-info"/>
					<input id="c_stop_rec_button" name="stop_button" type="button" value="Stop Recording" class="btn btn-info"/>
					<input id="c_save_rec_button" name="save_button" type="button" value="Save Recording" class="btn btn-warning"/>
					<input id="c_audio_id" name="c_audio_id" type="hidden" value=""/> 
				</div>

			</div><!-- end custom-audio-container -->

		</div><!-- end form-group audio -->
		
		<button id="customizationCreate" class="center btn btn-success">Save Changes</button>
		<button id="customizationDelete" class="center btn btn-danger">Remove Current Customizations</button>
		<a href="#" id="cancel-customize">Cancel</a>
		<span id="c_audioSaveMessage" class="audio-save-message">*did you save your audio?</span>
	<!--/g:form-->

</div><!-- end customize-container -->

<g:javascript src="recorderWorker.js"/>
<g:javascript src="recorder.js"/>
<g:javascript src="create_audio.js"/>

<g:javascript>

/* Customize panel hidden by default at page load */
$('#customize-container').hide();
$('#custom-image-container').hide();
$('#custom-audio-container').hide();

$("#closeCustomization, #cancel-customize").on('click', function(){
    resetChevrons();
    $("#customizationBtn").show();
    $('#customize-container').hide();
    $('#custom-image-container').hide();
    $('#custom-audio-container').hide();
});

$('#customizationBtn').on('click', function(){ 
    $('#customize-container').show();
    $('#customizationBtn').hide();
});

$('#customizationCreate').on('click', function(){
    resetChevrons();
    $('#customize-container').hide();
    $('#customizationBtn').show();
    saveCustomization();
});

$('#customizationDelete').on('click', function(){
	deleteCustomization();
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
		//window.location.replace(document.URL);
	});
}

function deleteCustomization(){
	// Create deletion packet
	var custDeleteData = new FormData();
	custDeleteData.append('flashcardId', $('#c_fId').val());
	console.log('deleting: ' + $('#c_fId').val());
	// Send it
	$.ajax({
		type: 'POST',
		url: '/OpenFluency/customization/remove',
		data: custDeleteData,
		processData: false,
		contentType: false
	}).done(function(resp) {
		//window.location.replace(document.URL);
		console.log('Delete Done');
	});
}

$('#custom-image').on('click', function(){
	$('#custom-image-container').toggle('slow');
	toggleChevron(this);
});

$('#custom-audio').on('click', function(){
	$('#custom-audio-container').toggle('slow');
	toggleChevron(this);
});

function toggleChevron(container) {
	var $glyph = $('.glyphicon', container);
	$glyph.toggleClass('glyphicon-chevron-right');
	$glyph.toggleClass('glyphicon-chevron-down');
}

function resetChevrons() {
	var $audioContainer = $('#custom-audio span'),
		$imageContainer = $('#custom-image span');
	
	if ($imageContainer.hasClass('glyphicon-chevron-down')){
		toggleChevron('#custom-image');
	}
	if ($audioContainer.hasClass('glyphicon-chevron-down')){
		toggleChevron('#custom-audio');
	}
}

$("#c_start_rec_button").on('click', function(){
	setWorkerPath("../../js/recorderWorker.js"); 
	startRecording(); 
	$("#c_audioSaveMessage").show();
	$("#customizationCreate").removeClass('btn-success').addClass('btn-warning');
});

$("#c_stop_rec_button").on('click', function(){ 
	stopRecording("#c_audioClip", $("#c_pId").val(), ""); 
	$("#c_save_rec_button").show();
});


$("#c_audioSaveMessage").hide();
$("#c_save_rec_button").hide();
$("#c_save_rec_button").on('click', function(){
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

$("#c_flickr_search").on('click', function(){
	$('#c_flickr_back').removeClass('hidden');
	$('#c_flickr_next').removeClass('hidden');

	$("#c_flickr_page_number").val(1).text(1);
	//console.log($('#c_query').val());
	searchImage("#c_query", "#c_results", "#c_imageLink",1);
});
$("#c_flickr_next").on('click', function(){
	var targetPage = $("#c_flickr_page_number").val();
	targetPage++;
	searchImage("#c_query", "#c_results", "#c_imageLink", targetPage);
	$("#c_flickr_page_number").val(targetPage).text(targetPage);
});
$("#c_flickr_back").on('click', function(){
	var targetPage = $("#c_flickr_page_number").val();
	if (targetPage > 1) { 
		targetPage--;
		searchImage("#c_query", "#c_results", "#c_imageLink", targetPage);
		$("#c_flickr_page_number").val(targetPage).text(targetPage);
	}
});

</g:javascript>