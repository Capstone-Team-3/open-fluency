<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"/>
	<title>OpenFluency</title>
</head>
<body>
	<div class="container flashcard-create">
		<ul class="breadcrumb">
            <li><a href="${createLink(uri:'/') }">Home</a></li>
            <li>Decks</li>
            <li><a href="${createLink(uri:'/unit/search?filter-alph=1') }">Flashcard Search</a></li>
            <li><a href="#">Create Flashcard </a></li>
        </ul>
		<div class="row">
			<div class="col-lg-5">
				<g:hasErrors bean="${flashcardInstance}">
					<ul class="errors" role="alert">
						<g:eachError bean="${flashcardInstance}" var="error">
							<li>
								<g:message error="${error}"/>
							</li>
						</g:eachError>
					</ul>
				</g:hasErrors>

				<h1>Create flashcard for ${unitInstance?.print}</h1>

				<g:form action="save" controller="flashcard" name="createFlashcardForm">

					<input type="hidden" name="unit" value="${unitInstance.id}"/>

					<div class="form-group">
						<label class="control-label">
							<span style="cursor:help;" title="What meaning do you want to use?">Meaning</span>
							<span class="required-indicator">*</span>
						</label>
						<select name="unitMapping" class="form-control">
							<g:each in="${unitInstance.unitMappings}">
								<option value="${it.id}">
									${it.unit1.id == unitInstance.id ? it.unit2.print : it.unit1.print}
								</option>
							</g:each>
						</select>
					</div>

					<div class="form-group">
						<label class="control-label">
							<span style="cursor:help;" title="What pronunciation do you want to use?">Pronunciation</span>
							<span class="required-indicator">*</span>
						</label>
						<g:select class="form-control" name="pronunciation" from="${unitInstance.pronunciations}" optionKey="id" optionValue="literal" id="fc_pronunciation"/>
					</div>

					<div class="form-group">
						<label class="control-label">
							<span style="cursor:help;" title="What deck should this card go into?">Choose a Deck</span>
							<span class="required-indicator">*</span>
						</label>
						<g:select class="form-control" name="deck" from="${userDecks}" optionKey="id" optionValue="title" value="${deckId}"/>
					</div>

					<div class="form-group-image">
						<label class="control-label">
							<span style="cursor:help;" title="What image should be associated with this card?">Image</span>
						</label>	
						<table >
							<tr>
								<td>
									<g:textField class="form-control" size = "70" id="imageLink" name="imageLink" value="${flashcardInstance?.image}"/>
								</td>
								<td>
									&nbsp
								</td>
								<td>
									<input id="show_flickr_search" style="margin-bottom:10px;" type="button" onclick="showHideFlickrSearch()" value="Flickr Search" class="btn btn-info"/>
								</td>
							</tr>
						</table>
					</div>
					<g:if test="${flashcardInstance?.audio}">
						<div class="form-group">
							<label class="control-label">
								<span style="cursor:help;" title="What audio clip provides pronunciation for this card?">Audio</span>
							</label>
							<g:textField class="form-control" name="audio" value="${flashcardInstance?.audio}"/>
						</div>
					</g:if>
					<div class="form-group audio">
						<label class="control-label">
							<span style="cursor:help;" title="Record a pronunciation">Record an Audio (pronunciation)</span>
						</label>
						<audio id="audioClip" controls autoplay></audio>
						</br>
						<input id="start_rec_button" name="start_button" type="button" value="Start Recording" class="btn btn-info"/>
						<input id="stop_rec_button" name="stop_button" type="button" value="Stop Recording" class="btn btn-info"/>
						<input id="save_rec_button" name="save_button" type="button" value="Save Recording" class="btn btn-warning"/>
						<input id="audio_id" name="audio_id" type="hidden" value=""/>
						</br>
						<span><i>*may need to click 'Allow' in audio permissions pop up</i></span>
						</br>
					</div>
					<button id="goCreate" class="center btn btn-success">Create it!</button>
					<span id="audioSaveMessage" class="audio-save-message">*did you save your audio?</span>
				</g:form>
			</div>
			<div id = "flickr_div" style="padding-bottom:5px; display:none; border:3px solid #C0C0C0; border-radius:10px; " class="col-lg-7" border = "1">
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

	<!-- all page specific click event handlers relating to image searh and audio are in the create_flashcard.js file -->
	<g:javascript src="create_flashcard.js"/>
	<!-- all the javascript references needed for audio recording -->
	<g:javascript src="recorderWorker.js"/>
	<g:javascript src="recorder.js"/>
	<g:javascript src="create_audio.js"/>
	<!-- this line is left hear as it relies on taking a formData created in create_audio.js and passes to create_flashcard.js -->
	<g:javascript>
	function showHideFlickrSearch(){
		if(document.getElementById('flickr_div').style.display == 'block')
			document.getElementById('flickr_div').style.display = 'none'
		else if(document.getElementById('flickr_div').style.display == 'none')
			document.getElementById('flickr_div').style.display = 'block'			
	} 

	</g:javascript>

</body>
</html> 