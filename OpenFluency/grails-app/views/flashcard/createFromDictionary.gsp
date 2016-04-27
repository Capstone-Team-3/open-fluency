<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main"/>
        <title>OpenFluency</title>
        
<style>

#dictionary-container #dictionary{
	float: left;
}
#dictionary-results-table {
	cursor: pointer;
}

</style>
        
    </head>
    <body>
        <div class="container flashcard-create">

            <ul class="breadcrumb">
                <li>Home</li>
            </ul>

            <h1>Create Flashcard</h1>

            <div class="row marketing">
                <div class="col-lg-6">
                	<div class='col-lg-12 col-md-12 col-sm-12 col-xs-12' id="dictionary-container">
                    	<g:render template="/dictionary/dictionaryTable"/>
                   	</div>
                </div> 
                <div class="col-lg-6">
                    <g:form action="createTest" >
                    <div class="form-group row">
                        <label for="inputConcept" class="col-sm-2 form-control-label">Concept</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="inputConcept" name="concept" placeholder="concept">
                        </div>
                    </div>
                    <div class="form-group row">
                        <label for="inputMeaning" class="col-sm-2 form-control-label">Meaning</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="inputMeaning" name="meaning" placeholder="meaning">
                        </div>
                    </div>
                    <div class="form-group row">
                        <label for="inputPronunciation" class="col-sm-2 form-control-label">Pronunciation</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="inputPronunciation" name="pronunciation" placeholder="pronunciation">
                        </div>
                    </div>

                    <div class="form-group row">
                        <label for="select" class="col-sm-2 form-control-label">Choose a Deck</label>
                        <div class="col-sm-10">
                            <g:select name="deckId" from="${deckInstanceList}" optionKey="id" optionValue="${{xx->"${xx}"}}"  />  
                        </div>
                    </div>

					<h3 class="customize-heading">Add an Image (optional)</h3>

					<div class="form-group-image">

						<div class="flashcard-image-create">
						</div>

						<div class="hide">
							<div class="form-group">
								<label class="control-label">Paste an image URL:</label>
								<g:textField class="form-control" size = "70" id="imageLink" name="imageLink" value="${flashcardInstance?.image}"/>
							</div>
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
						%{--
						<input id="show_flickr_search" style="margin-bottom:10px;" type="button" onclick="showHideFlickrSearch()" value="Flickr Search" class="btn btn-info"/>
						--}%
					</div>
					<!-- end form-group-image -->

					<g:if test="${flashcardInstance?.audio}">
						<div class="form-group">

							<label class="tooltiper control-label" class="tooltiper"  data-toggle="tooltip"  data-placement="right" title="What audio clip provides pronunciation for this card?">Audio</label>

							<g:textField class="form-control" name="audio" value="${flashcardInstance?.audio}"/>

						</div>
						<!-- end form-group -->
					</g:if>

					<h3 class="customize-heading">Add Audio (optional)</h3>

					<div class="form-group-audio">

						<label class="tooltiper control-label" class="tooltiper"  data-toggle="tooltip"  data-placement="right" title="Record a pronunciation" >Record an audio pronunciation file:</label>

						<small class="clearfix audio-warning"> <strong>Note:</strong>
							A browser pop-up may appear asking you to 'Allow' microphone use!
						</small>

						<audio id="audioClip" controls autoplay></audio>

						<div class="audio-controls">
							<input id="start_rec_button" name="start_button" type="button" value="Start Recording" class="btn btn-info"/>
							<input id="stop_rec_button" name="stop_button" type="button" value="Stop Recording" class="btn btn-info"/>
							<input id="save_rec_button" name="save_button" type="button" value="Save Recording" class="btn btn-warning"/>
							<input id="audio_id" name="audio_id" type="hidden" value=""/>
						</div>
						<!-- end audio-controls -->

					</div>
					<!-- end form-group audio -->


                    <div class="form-group row">
                        <div class="col-sm-offset-2 col-sm-10">
                            <button type="submit" class="btn btn-secondary">Submit</button>
                        </div>
                    </div>
                    </g:form>
                </div> 
            </div>
            
            
            
            <div class="container">
  <h2>Large Modal</h2>
  <!-- Trigger the modal with a button -->
  <button type="button" class="btn btn-info btn-lg" data-toggle="modal" data-target="#myModal">Open Large Modal</button>

  <!-- Modal -->
  <div class="modal fade" id="image-modal-create" role="dialog">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">Modal Header</h4>
        </div>
        <div class="modal-body">
          <h3>Flickr Search Results</h3>
				<div id="results"></div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        </div>
      </div>
    </div>
  </div>
</div>
            
            
            
            
            
            <g:javascript>
            var createFlashcardPage = true;
            
            
            $(document).ajaxSuccess(function() {
            $("tr[id^='dictionary-row-']").each(function(){
            console.log("Hellol");
            console.log($(this).attr('id'));
            var elem_id = $(this).attr('id');
            $("#" + elem_id).click(function() {
            var dom_id = "#" + elem_id;
            $("#inputConcept").attr("value", $(dom_id).data('concept'));
            $("#inputMeaning").attr("value", $(dom_id).data('meaning'));
            $("#inputPronunciation").attr("value",$(dom_id).data('pronunciation'));
            });
            });
            });
            
            
            
            
            </g:javascript>
            
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
            
            
        </div>
    </body>
</html>

