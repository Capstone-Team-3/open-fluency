/*----------------------------------------------------------------------------*/
/* Functionality for Creating Audio Pronunciation Recordings
/*----------------------------------------------------------------------------*/

var recorder,
	formData,
	audio = document.querySelector('audio');

var onFail = function(e){
	console.log('Rejected!', e);
};

var onSuccess = function(s){
	var context = new webkitAudioContext();
	var mediaStreamSource = context.createMediaStreamSource(s);
	recorder = new Recorder(mediaStreamSource);
	recorder.record();
};

window.URL = window.URL || window.webkitURL;
navigator.getUserMedia = navigator.getUserMedia ||
	navigator.webkitGetUserMedia || navigator.mozGetUserMedia ||
	navigator.msGetUserMedia;

/**
 * Start recording the audio pronunciation file.
 */
function startRecording(){
	if (navigator.getUserMedia) {onSuccess
		navigator.getUserMedia({audio: true}, onSuccess, onFail);
	}
}

/**
 * End recording of the audio pronunciation file.
 * @param audioId - the ID of the HTML audio element
 * @param pronunciationID - the ID of the text pronunciation associated with the
 * audio file (i.e. selected for the same flashcard).
 * @param urlStr - the URL where the data will be saved.
 */
function stopRecording(audioId, pronunciationID, urlStr){
	recorder.stop();
	recorder.exportWAV(function(s){
		document.querySelector(audioId).src = window.URL.createObjectURL(s);
		stashBlob(s, pronunciationID, urlStr);
	});
}

/**
 * Create the data packet for the audio file.
 * @param blob - the audio WAV data.
 * @param pronunciationID - the ID of the pronunciation the audio is associated
 * with.
 * @param urlStr - the URL where the data will be saved.
 */
function stashBlob(blob, pronunciationID, urlStr){
	formData = new FormData();
	formData.append('blob', blob);
	formData.append('pronunciation.id', pronunciationID);
	formData.append('url', urlStr);
}

/**
 * Save the newly recorded audio pronunciation file and associated data.
 */
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
			window.location = "/OpenFluency/audio/index/";
		}
	});
}