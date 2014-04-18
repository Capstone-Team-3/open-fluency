<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main">
	<g:set var="entityName" value="${message(code: 'audio.label', default: 'Audio')}" />
	<title><g:message code="default.create.label" args="[entityName]" /></title> 
</head>
<body>
	<a href="#create-audio" class="skip" tabindex="-1">
		<g:message code="default.link.skip.label" default="Skip to content&hellip;"/>
	</a>
	<div class="nav" role="navigation">
		<ul>
			<li>
				<a class="home" href="${createLink(uri: '/')}">
					<g:message code="default.home.label"/>
				</a>
			</li>
			<li>
				<g:link class="list" action="index">
					<g:message code="default.list.label" args="[entityName]" />
				</g:link>
			</li>
		</ul>
	</div>
	<div id="create-audio" class="content scaffold-create" role="main">
		<h1>
			<g:message code="default.create.label" args="[entityName]" />
		</h1>
		<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
		</g:if>
		<g:hasErrors bean="${audioInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${audioInstance}" var="error">
					<li>
						<g:message error="${error}"/>
					</li>
				</g:eachError>
			</ul>
		</g:hasErrors>
		<g:form url="[resource:audioInstance, action:'save']" >
			<fieldset class="form">
				<g:render template="form"/>
				<br/>
				<audio id="audioClip" controls autoplay></audio>
				<br/>
				<input id="start_button" name="start_button" type="button" value="Start Recording"/>
				<input id="stop_button" name="stop_button" type="button" value="Stop Recording"/>
				<input id="audioWAV" name="audioWAV" value="" type="hidden"/>
				<br/>
				<br/>

			</fieldset>
			<fieldset class="buttons">
				<input id="create_button" name="create_button" type="button" value="Create Audio"/>
				<!--g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /-->
			</fieldset>
		</g:form>
	</div>

</br>

<g:javascript src="recorderWorker.js"/>
<g:javascript src="recorder.js"/>

<!-- This will be moved to the application.js file-->
<g:javascript>
	var recorder;
	var formData;
var audio = document.querySelector('audio');

var onFail = function(e){
	console.log('Rejected!',e);
}

var onSuccess = function(s){
	var context = new webkitAudioContext();
	var mediaStreamSource = context.createMediaStreamSource(s);
	recorder = new Recorder(mediaStreamSource);
	recorder.record();
}

window.URL = window.URL || window.webkitURL;
navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.msGetUserMedia;

function startRecording() {
	console.log("Started Recording");
	if (navigator.getUserMedia) {onSuccess
		navigator.getUserMedia({audio: true}, onSuccess, onFail);
	} else {
		console.log('navigator.getUserMedia not present');
	}
}

function stopRecording(){
	console.log("Stopped Recording");
	recorder.stop();
	recorder.exportWAV(function(s){
		audio.src = window.URL.createObjectURL(s);
		console.log(s);
		stashBlob(s);
	});
}

$("#start_button").click(function(){
	startRecording();
});

$("#stop_button").click(function(){
	stopRecording();
});

$("#create_button").click(function(){
	saveAudio();
});

function stashBlob(blob){
	formData = new FormData();
	formData.append('blob', blob);
	formData.append('pronunciation.id', $('#pronunciation').val());
	formData.append('url', 'test');
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
</g:javascript>

</body>

</html>