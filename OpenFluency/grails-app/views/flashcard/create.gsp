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
		
        <h1>Create Flashcard for ${unitInstance?.print}</h1>

		<div class="row">
			<div class="col-lg-5 flashcard-create-form">
				
				<g:hasErrors bean="${flashcardInstance}">
					<ul class="errors" role="alert">
						<g:eachError bean="${flashcardInstance}" var="error">
							<li>
								<g:message error="${error}"/>
							</li>
						</g:eachError>
					</ul>
				</g:hasErrors>

				<g:form action="save" controller="flashcard" name="createFlashcardForm">

					<input type="hidden" name="unit" value="${unitInstance.id}"/>
					<div class="form-group">
						
						<label class="control-label">
							<span style="cursor:help;" title="Which meaning of this character/word do you want to use?">Meaning</span>
							<span class="required-indicator">*</span>
						</label>

						<select name="unitMapping" class="form-control">
							<g:each in="${unitInstance.unitMappings}">
								<option value="${it.id}">
									${it.unit1.id == unitInstance.id ? it.unit2.print : it.unit1.print}
								</option>
							</g:each>
						</select>

					</div><!-- end form-group -->

					<div class="form-group">

						<label class="control-label">
							<span style="cursor:help;" title="What pronunciation of the character/word do you want to use?">Pronunciation</span>
							<span class="required-indicator">*</span>
						</label>
						
						<g:select class="form-control" name="pronunciation" from="${unitInstance.pronunciations}" optionKey="id" optionValue="literal" id="fc_pronunciation"/>

					</div><!-- end form-group -->

					<div class="form-group">

						<label class="control-label">
							<span style="cursor:help;" title="What deck should this card go into?">Choose a Deck</span>
							<span class="required-indicator">*</span>
						</label>
						
						<g:select class="form-control" name="deck" from="${userDecks}" optionKey="id" optionValue="title" value="${deckId}"/>

					</div><!-- end form-group -->

					<h3 class="customize-heading">Add an Image (optional)</h3>

					<div class="form-group-image">
						
						<div class="form-group">
							<label class="control-label">Paste an image URL:</label>
							<g:textField class="form-control" size = "70" id="imageLink" name="imageLink" value="${flashcardInstance?.image}"/>
						</div>

						<div class="form-group">
							<label for="query" class="control-label">Or, search Flickr for an image:</label>
							<div class="input-group">
		                        <g:textField class="form-control" name="query" placeholder="Type a keyword" id="query" />
		                        <span class="input-group-btn">
		                            <input type="button" class="btn btn-info" id="flickr_search" value="Search" />
		                        </span>
		                    </div>
	                    </div>

						%{-- <input id="show_flickr_search" style="margin-bottom:10px;" type="button" onclick="showHideFlickrSearch()" value="Flickr Search" class="btn btn-info"/> --}%

					</div><!-- end form-group-image -->

					<g:if test="${flashcardInstance?.audio}">
						<div class="form-group">
							
							<label class="control-label">
								<span style="cursor:help;" title="What audio clip provides pronunciation for this card?">Audio</span>
							</label>
							
							<g:textField class="form-control" name="audio" value="${flashcardInstance?.audio}"/>

						</div><!-- end form-group -->
					</g:if>

					<h3 class="customize-heading">Add Audio (optional)</h3>

					<div class="form-group-audio">

						<label class="control-label">
							<span style="cursor:help;" title="Record a pronunciation">Record an audio pronunciation file:</span>
						</label>
						
						<small class="clearfix"><strong>Note:</strong> A browser pop-up may appear asking you to 'Allow' microphone use!</small>
						
						<audio id="audioClip" controls autoplay></audio>
						
						<div class="audio-controls">
							<input id="start_rec_button" name="start_button" type="button" value="Start Recording" class="btn btn-info"/>
							<input id="stop_rec_button" name="stop_button" type="button" value="Stop Recording" class="btn btn-info"/>
							<input id="save_rec_button" name="save_button" type="button" value="Save Recording" class="btn btn-warning"/>
							<input id="audio_id" name="audio_id" type="hidden" value=""/>
						</div><!-- end audio-controls -->
						
					</div><!-- end form-group audio -->

					<button id="goCreate" class="center btn btn-success">Create Flashcard</button>
					<p id="audioSaveMessage" class="audio-save-message">* Did you save your audio recording?</p>

				</g:form>
			
			</div><!-- end col-lg-5 -->

			<div id="flickr_div" class="col-lg-7">

				<h3>Flickr Search Results</h3>
				<div id="results"></div>

				<div class="pagination">
					<button id="flickr_back" class="btn btn-default">Back</button>
					<label id="flickr_page_number"></label>
					<button id="flickr_next" class="btn btn-default">Next</button>				
				</div>

			</div>

		</div><!-- end row -->
	</div><!-- end container -->

	<!-- all page specific click event handlers relating to image searh and audio are in the create_flashcard.js file -->
	<g:javascript src="create_flashcard.js"/>
	<!-- all the javascript references needed for audio recording -->
	<g:javascript src="recorderWorker.js"/>
	<g:javascript src="recorder.js"/>
	<g:javascript src="create_audio.js"/>
	<!-- this line is left hear as it relies on taking a formData created in create_audio.js and passes to create_flashcard.js -->
	<g:javascript>
		$('#flickr_search').on('click', function() {
			$('#flickr_div').css('display', 'block');
		});
	</g:javascript>

</body>
</html> 