var recorder,
	formData,
	audio = document.querySelector('audio');

var onFail = function(e){
	console.log('Rejected!',e);
};

var onSuccess = function(s){
	var context = new webkitAudioContext();
	var mediaStreamSource = context.createMediaStreamSource(s);
	recorder = new Recorder(mediaStreamSource);
	recorder.record();
};

window.URL = window.URL || window.webkitURL;
navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.msGetUserMedia;

function startRecording(){
	console.log("Started Recording");
	if (navigator.getUserMedia) {onSuccess
		navigator.getUserMedia({audio: true}, onSuccess, onFail);
	} else {
		console.log('navigator.getUserMedia not present');
	}
}

function stopRecording(audioId, pronunciationID, urlStr){
	console.log("Stopped Recording");
	recorder.stop();
	recorder.exportWAV(function(s){
		document.querySelector(audioId).src = window.URL.createObjectURL(s);
		console.log(s);
		stashBlob(s, pronunciationID, urlStr);
	});
}

function stashBlob(blob, pronunciationID, urlStr){
	formData = new FormData();
	formData.append('blob', blob);
	formData.append('pronunciation.id', pronunciationID);
	formData.append('url', urlStr);
}

function saveAudio(){
	// Create the packet
	var fd = formData;
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
			window.location = "/OpenFluency/audio/index/";
		} else {
			console.log("Something went wrong!");
		}
	});
}