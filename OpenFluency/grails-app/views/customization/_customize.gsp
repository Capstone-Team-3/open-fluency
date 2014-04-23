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
							<input class="form-control" id="c_query" name="c_k" type="text" size="60" placeholder="Enter a keyword here to search photos" />
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
$('#customize-container').hide();
$('#custom-image-container').hide();
$('#custom-audio-container').hide();
</g:javascript>

<g:javascript src="customize.js"/>