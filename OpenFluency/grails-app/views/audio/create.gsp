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
		<g:form url="[resource:audioInstance, action:'saveFile']" enctype="multipart/form-data" >
			<fieldset class="form">
				<g:render template="form"/>
				<br/>
				<audio id="audioClip" controls autoplay></audio>
				<br/>
				<input id="start_button" name="start_button" type="button" value="Start Recording"/>
				<input id="stop_button" name="stop_button" type="button" value="Stop Recording"/>
				<input id="audioWAV" name="audioWAV" value="" type="hidden"/>
				<label for="Upload">Upload</label><input id="upload" name="file" type="file"/>
				<br/>
				<br/>

			</fieldset>
			<fieldset class="buttons">
				<input id="create_button" name="create_button" type="button" value="Create Audio"/>
				<!--g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}"/>
				-->
			</fieldset>
		</g:form>
	</div>

</br>

<!-- all the javascript references needed for audio recording -->
<g:javascript src="recorderWorker.js"/>
<g:javascript src="recorder.js"/>
<g:javascript src="create_audio.js"/>
<g:javascript>
	
	$("#start_button").click(function(){ startRecording(); });
	$("#stop_button").click(function(){ stopRecording($("#pronunciation").val(),$("#url").val()); });
	$("#create_button").click(function(){ saveAudio(); });

</g:javascript>

</body>

</html>
