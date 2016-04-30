/*----------------------------------------------------------------------------*/
/* Image/Audio Association Functionality for the Create Flashcard Page
/*----------------------------------------------------------------------------*/

/*----------------------------------------------------------------------------*/
/* Flickr Image Search
/*----------------------------------------------------------------------------*/

// search Flickr for images matching the query
$("#flickr_search").click(function(){
	$("#flickr_page_number").val(1).text(1);
	searchImage("#query", "#results", "#imageLink", 1);
});

// show next set of results when next button clicked
$("#flickr_next").click(function(){
	var targetPage = $("#flickr_page_number").val();
	targetPage++;
	searchImage("#query", "#results", "#imageLink", targetPage);
	$("#flickr_page_number").val(targetPage).text(targetPage);
});

// show previous set of results when previous button clicked
$("#flickr_back").click(function(){
	var targetPage = $("#flickr_page_number").val();
	if (targetPage > 1) { 
		targetPage--;
		searchImage("#query", "#results", "#imageLink",targetPage);
		$("#flickr_page_number").val(targetPage).text(targetPage);
	}
});

/*----------------------------------------------------------------------------*/
/* Record Audio Pronunciation File
/*----------------------------------------------------------------------------*/

// hide save buttons when recording not in progress
$("#audioSaveMessage").hide();
$("#save_rec_button").hide();

// start recording the pronunciation file 
$("#start_rec_button").click(function(){
	setWorkerPath("../js/recorderWorker.js");
	startRecording();
	$("#audioSaveMessage").show();
	$("#goCreate").removeClass('btn-success').addClass('btn-warning');
});

// stop recording the pronunciation file
$("#stop_rec_button").click(function(){
	stopRecording("#audioClip", $("#fc_pronunciation").val(), "");
	$("#save_rec_button").show();
});

// save the recording and hide the save buttons
$("#save_rec_button").click(function(){
	saveAudioRecording(formData);
	$("#audioSaveMessage").hide();
	$("#save_rec_button").hide();
	$("#goCreate").removeClass('btn-warning').addClass('btn-success');
});

/**
 * Save the audio pronunciation file.
 * @param formDataObj: the audio file and related data to be saved.
 */
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
                       $("#audio_id").val(audioInstance.id);
				}
	   });
}

$("#audio_search").click( function( event ) {
	event.preventDefault();
	var fd = new FormData(document.forms.namedItem("createFlashcardForm"));
	var url="${createLink(controller:'audio',action:'saveFile')}";
	url = '/OpenFluency/audio/saveFile';
	var audioFile=$("#audiofile").val()
	var xm = $.ajax({
	   url: url,
	   type: "POST",
	   data: fd,
	   processData: false,
	   contentType: false,
	   success: function(audioInstance) {
		   $("#audio_id").val(audioInstance.id);
	   },
	   error: function(jqXHR, textStatus, errorMessage) {
		   console.log(errorMessage); // Optional
	   }
	});
});
