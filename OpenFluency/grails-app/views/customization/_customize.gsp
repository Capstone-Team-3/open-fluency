<div id="customize-container">
	<g:form action="save" controller="customization" name="createCustomizationForm">
		<input type="hidden" name="flashcardId" value="${flashcardInstance.id}"/>
		<input type="hidden" name="unitMappingId" value="${flashcardInstance.unitMapping.id}"/>
		<input type="hidden" name="pronunciationId" value="${flashcardInstance.pronunciation.id}"/>
					
		<div class="form-group">
			<label class="control-label">Customize the image (optional)?</label>
			<g:textField class="form-control" id="imageLink" name="imageLink" value="${flashcardInstance?.image}"/>
		</div>
		
		<div class="form-group">
			<label class="control-label">Customize the pronunciation audio (optional)?</label>
			<g:textField class="form-control" name="audio" value="${flashcardInstance?.audio}"/>
		</div>
		
		<div class="form-group audio">
			<label class="control-label">Customize the pronunciation audio (optional)?</label>
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
		
		<button id="customizationCreate" class="center btn btn-success">Create it!</button>
		<span id="audio-save-message">*did you save your audio?</span>
	</g:form>
</div>