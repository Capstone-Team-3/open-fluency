
$("#flickr_search").click(function(){
	$("#flickr_page_number").val(1).text(1);
	searchImage("#query", "#results", "#imageLink",1);
});
$("#flickr_next").click(function(){
	var targetPage = $("#flickr_page_number").val();
	targetPage++;
	searchImage("#query", "#results", "#imageLink", targetPage);
	$("#flickr_page_number").val(targetPage).text(targetPage);
});
$("#flickr_back").click(function(){
	var targetPage = $("#flickr_page_number").val();
	if (targetPage > 1) { 
		targetPage--;
		searchImage("#query", "#results", "#imageLink",targetPage);
		$("#flickr_page_number").val(targetPage).text(targetPage);
	}
});

$("#start_rec_button").click(function(){ 
	startRecording(); 
	$("#audioSaveMessage").show();
	$("#goCreate").removeClass('btn-success').addClass('btn-warning');
});

$("#stop_rec_button").click(function(){ 
	stopRecording($("#fc_pronunciation").val(), ""); 
	$("#save_rec_button").show();
});


$("#audioSaveMessage").hide();
$("#save_rec_button").hide();
$("#save_rec_button").click(function(){
	saveAudioRecording(formData);
	$("#audioSaveMessage").hide();
	$("#save_rec_button").hide();
	$("#goCreate").removeClass('btn-warning').addClass('btn-success');
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
			$("#audio_id").val(audioInstance.id);
			console.log(audioInstance.id);
			//window.location = "/OpenFluency/flashcard/save/";
		} else {
			console.log("Something went wrong!");
		}
	});
}