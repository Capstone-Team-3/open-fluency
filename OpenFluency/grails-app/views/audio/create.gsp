<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'audio.label', default: 'Audio')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#create-audio" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="create-audio" class="content scaffold-create" role="main">
			<h1><g:message code="default.create.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${audioInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${audioInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>
			<g:form url="[resource:audioInstance, action:'save']" >
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>
				<fieldset class="buttons">
					<g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
				</fieldset>
			</g:form>
		</div>
		
		</br>
		<audio controls autoplay></audio>
		</br>
		<input id="start_button" name="start_button" type="button" value="Start Recording" />
		<input id="stop_button" name="stop_button" type="button" value="Stop Recording" />

		<g:javascript src="recorderWorker.js"/>
		<g:javascript src="recorder.js"/>

		<g:javascript>
			var recorder;
			var audio = document.querySelector('audio');

			var onFail = function(e){
				console.log('Rejected!',e);
			}

			var onSuccess = function(s){
				console.log("In onSuccess");
				var context = new webkitAudioContext();
				var mediaStreamSource = context.createMediaStreamSource(s);
				recorder = new Recorder(mediaStreamSource);
				recorder.record();
			}

			window.URL = window.URL || window.webkitURL;
			navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.msGetUserMedia;

			function startRecording() {
		        console.log("Started Recording");
		        if (navigator.getUserMedia) {
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
				});
			}

			$("#start_button").click(function(){
				startRecording();
			});
			$("#stop_button").click(function(){
				stopRecording();
			});

		</g:javascript>

	</body>

</html>
