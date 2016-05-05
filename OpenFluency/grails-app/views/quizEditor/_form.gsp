<%@ page import="com.openfluency.Constants" %>
<div class="form-group">
	<label for="title">Title:</label>
	<input required name="title" type="text"  value="${quizInstance?.title}" class="form-control"/>
</div>


<div class="form-group">
	<label for="maxCardTime">
		Maximum seconds allowed per card (if 0, the quiz will not be timed):
	</label>
	<g:field required type="number" min="0" max="60" name="maxCardTime" value="${quizInstance?.maxCardTime ? quizInstance?.maxCardTime : 0 }" class="form-control"/>
</div>

<div class="form-group live-time-group">
	<label for="liveTime">Available starting:</label>
	<g:datePicker name="liveTime" value="${quizInstance ? quizInstance.liveTime : new Date()}" class="form-control"/>
</div>

<div class="form-group end-time-group">
	<label for="endTime">Available until:</label>
	<g:datePicker name="endTime" value="${quizInstance ? quizInstance.endTime : new Date()}" class="form-control"/>
</div>

<div class="form-group" id="questionList">

	<g:each var="question" status="i" in="${quizInstance?.questions}">

		<div class="question panel panel-default question-panel">
		   
			<div class="panel-heading">
				<label>Question ${i + 1}</label>
				    <!-- <div class="form-inline">  -->
					<span class="btn btn-xs btn-danger" onclick="if (confirm('are you sure?')) { $(this).parent().parent().remove(); writeCSV(); }"><span class="glyphicon glyphicon-remove"></span></span>
				   <!--   </div> -->
			</div>

				<h3 class="customize-heading">Standard Text Based Question</h3>
				<div class="form-inline">
					<input name="question" class="form-control" size="64" type="text" onchange="writeCSV();" onkeyup="writeCSV();" 
					value="${question.question}"/>

					<h3 class="customize-heading">Add Audio or Image Question(optional)</h3>

					<div class="form-group-audio">

					<!--
					<span class="play-audio glyphicon glyphicon-volume-up" style="visibility: hidden;"></span>
								<audio class="flashcard-audio hidden" id="flashcard-audio-4" src="/OpenFluency/audio/sourceAudio/4" controls></audio>   -->

					
						<div class="audio-controls">
							<input class="remove_form_control" id="audio_id" name="audio_id" type="hidden" value=""/>
						<!-- end audio-controls -->
						<label for="audiofile" class="tooltiper control-label" class="tooltiper"  data-toggle="tooltip"  data-placement="right" title="Tip: See forvo.com for samples">Upload audio or image file (mp3, wav, oga, or aac) or Image file(gif, jpg)</label>
						<input id="audiofile" class="remove_form_control" accept=".mp3,.wav,.oga,.aac,.gif,.jpg" name="audiofile" type="file" value=""/>
						<span class="input-group-btn">
							<input type="button" onclick="uploadAudioFile(this);" class="btn btn-info" name="audio_search" id="audio_search" class="remove_form_control" value="Upload Audio or Image File" />
						</span>
						<audio id="player" controls="controls" preload="metadata">
 							<source src="${question?.sound?.getSoundUri()}" />
  							<b>Your browser does not support HTML5 audio element</b>
						</audio>
						</div>
					</div>
			
				</div>

				<input name="hiddenAudio" class="form-control" type="hidden" onchange="writeCSV();" onkeyup="writeCSV();" value="${question?.sound?.getSoundUri()}"/>


				<input name="hiddenImage" class="form-control" type="hidden" onchange="writeCSV();" onkeyup="writeCSV();" value="${question?.image?.getImageUri()}"/>
		
				<label>Correct Answer</label>
		
				<div class="form-inline">
					<input name="correctAnswer" class="form-control" type="text" onchange="writeCSV();" onkeyup="writeCSV();" value="${question.correctOption?.option}"/>
					<span class="btn btn-xs btn-info" onclick="getConfusers(this);"><span class="glyphicon glyphicon-cog"></span> Generate Confuser Answers</span>
				</div>
				
				<div class="form-inline">
					<label>Wrong Answers</label>
					<span class="btn btn-xs btn-info" onclick="addWrongAnswer(this); writeCSV();"><span class="glyphicon glyphicon-plus"></span></span>
				</div>
		
				<g:each var="wrongOption" in="${question.wrongOptions}">
					<div class="form-inline">
						<input name="wrongAnswer" class="form-control" type="text" onchange="writeCSV();" onkeyup="writeCSV();" value="${wrongOption.option}"/>
						<span class="btn btn-xs btn-danger" onclick="$(this).parent().remove(); writeCSV();"><span class="glyphicon glyphicon-remove"></span></span>
					</div>
				</g:each>
		</div>
	
	
	</g:each>
</div>

<div class="form-group">
	<span class="btn btn-info" onclick="addQuestion(); writeCSV();"><span class="glyphicon glyphicon-plus"></span> Add a Question</span>
</div>

 <div class="form-group">
	 <div id="showCSV" class="btn btn-info" onclick="showCSV()">Show Raw Quiz CSV</div>
	 <textArea class="form-control" name="questions" style="display:none" readonly></textArea>
 </div>
 

<!--
	<g:javascript src="create_flashcard.js"/>
	 all the javascript references needed for audio recording 
	<g:javascript src="recorderWorker.js"/>
	<g:javascript src="recorder.js"/>
	<g:javascript src="create_audio.js"/>
-->
 <script type="text/javascript">

/**
 * Save the audio pronunciation file.
 * @param formDataObj: the audio file and related data to be saved.
 */



function uploadAudioFile(that) {

	        var file = $('#audiofile').val()
            var form_data = new FormData();
            form_data.append("file", $('#audiofile').get(0).files[0]);
			var url = "/OpenFluency/QuizEditor/uploadFile";

			$.ajax({
			   
				type: 'POST',
				url: url,
				data: form_data, 
				dataType: "text",  //
				mimeType: "multipart/form-data",
				processData: false,
				contentType: false
			})
			.done(function(soundInstance) {
	
			var question = $(that).parent().parent();

			console.log(soundInstance);
			console.log("reach ajax return");
			// this is WRONG... since its hard coded to an ID...but working on Fix
			$("#player").attr("src",soundInstance);
			});
	 	}



		function getConfusers(that) {
			
			var question = $(that).parent().parent();
			var input = question.find("input[name=correctAnswer]").val();

			var languageCode = "${courseInstance?.language?.code}" || "JAP";
			var url = "/OpenFluency/Confuser/generate?languageCode=" + languageCode + "&number=-1&word=" + input;

			$.ajax({
				url: url
			})
			.done(function(jarray) {

				console.log(jarray);
				
				var wrongAnswers = question.find("input[name=wrongAnswer]");
				var length = wrongAnswers.length;
				
				wrongAnswers.each(function(index, item) {
					if (index <= jarray.length) {
						$(item).val(jarray[index]);
					}
				});

				writeCSV();
			});
	 	}
 
 		function addWrongAnswer(that) {
 			var question = $(that).parent().parent();

 			var wrongAnswerHtml = "<div class=\"form-inline\">";
 			wrongAnswerHtml += "<input name=\"wrongAnswer\" class=\"form-control\" type=\"text\" onchange=\"writeCSV();\" onkeyup=\"writeCSV();\" value=\"\"/>";
 			wrongAnswerHtml += "<span class=\"btn btn-xs btn-danger\" onclick=\"$(this).parent().remove(); writeCSV();\"><span class=\"glyphicon glyphicon-remove\"></span></span>";
 			wrongAnswerHtml += "</div>";

 			question.append(wrongAnswerHtml);
 	 	}

 		function showCSV() {
	 		if ($("textArea").is(':visible')) {
		 		$("textArea").hide();
		 		$("#showCSV").html("Show Raw Quiz CSV");
		 	}
	 		else {
	 			$("textArea").show();
	 			$("#showCSV").html("Hide Raw Quiz CSV");
		 	}
	 	}
 
 		function addQuestion() {

			var questionHtml = "<div class=\"question panel panel-default question-panel\">";
			questionHtml += "<div class=\"panel-heading\">";
			questionHtml += "<label>Question</label>";
			questionHtml += "<div class=\"card-actions\">";
			questionHtml += "<span class=\"btn btn-xs btn-danger\" onclick=\"if (confirm('are you sure?')) { $(this).parent().parent().parent().remove(); writeCSV(); }\"><span class=\"glyphicon glyphicon-remove\"></span></span>";
			questionHtml += "</div>";
			questionHtml += "</div>";
			questionHtml += "<div class=\"form-group\">";
			questionHtml += "<input name=\"question\" class=\"form-control\" type=\"text\" onchange=\"writeCSV();\" onkeyup=\"writeCSV();\" value=\"\"/>";
			questionHtml += "</div>";


			questionHtml += "<input name=\"hiddenAudio\" class=\"form-control\" type=\"hidden\" onchange=\"writeCSV();\"" 
			questionHtml += "onkeyup=\"writeCSV();\" value=\"${question?.sound?.getSoundUri()}\"/>"
			questionHtml += "<input name=\"hiddenImage\" class=\"form-control\" type=\"hidden\" onchange=\"writeCSV();\"" 
			questionHtml += "onkeyup=\"writeCSV();\" value=\"${question?.image?.getImageUri()}\"/>"

			questionHtml += "<label>Correct Answer</label>";
			questionHtml += "<div class=\"form-inline\">";
			questionHtml += "<input name=\"correctAnswer\" class=\"form-control\" type=\"text\" onchange=\"writeCSV();\" onkeyup=\"writeCSV();\" value=\"\"/>";
			questionHtml += "<span class=\"btn btn-xs btn-info\" onclick=\"getConfusers(this);\"><span class=\"glyphicon glyphicon-cog\"></span> Generate Confuser Answers</span>";
			questionHtml += "</div>";
			questionHtml += "<div class=\"form-inline\">";
			questionHtml += "<label>Wrong Answers</label>";
			questionHtml += "<span class=\"btn btn-xs btn-info\" onclick=\"addWrongAnswer(this); writeCSV();\"><span class=\"glyphicon glyphicon-plus\"></span></span>";
			questionHtml += "</div>";
			questionHtml += "<div class=\"form-inline\">";
			questionHtml += "<input name=\"wrongAnswer\" class=\"form-control\" type=\"text\" onchange=\"writeCSV();\" onkeyup=\"writeCSV();\" value=\"\"/>";
			questionHtml += "<span class=\"btn btn-xs btn-danger\" onclick=\"$(this).parent().remove(); writeCSV();\"><span class=\"glyphicon glyphicon-remove\"></span></span>";
			questionHtml += "</div>";
			questionHtml += "<div class=\"form-inline\">";
			questionHtml += "<input name=\"wrongAnswer\" class=\"form-control\" type=\"text\" onchange=\"writeCSV();\" onkeyup=\"writeCSV();\" value=\"\"/>";
			questionHtml += "<span class=\"btn btn-xs btn-danger\" onclick=\"$(this).parent().remove(); writeCSV();\"><span class=\"glyphicon glyphicon-remove\"></span></span>";
			questionHtml += "</div>";
			questionHtml += "<div class=\"form-inline\">";
			questionHtml += "<input name=\"wrongAnswer\" class=\"form-control\" type=\"text\" onchange=\"writeCSV();\" onkeyup=\"writeCSV();\" value=\"\"/>";
			questionHtml += "<span class=\"btn btn-xs btn-danger\" onclick=\"$(this).parent().remove(); writeCSV();\"><span class=\"glyphicon glyphicon-remove\"></span></span>";
			questionHtml += "</div>";
			questionHtml += "</div>";

 			var questionList = $("#questionList");


 			var html = questionList.append(questionHtml);
 				
 	 	}	

        function writeCSV() {
            var s = "";
			$(".question").each(function(index,item) {
				s += "MANUAL,"
				 
				//var inputs = $(this).find("input");
				var inputs = $(this).find('input:not(.btn-info, .remove_form_control)');
				var length = inputs.length;
				inputs.each(function(index, item) { 
					// Use quotes in case the string contains commas
					// Convert quote in string to double quote
					s += '"';
					s += $(this).val().replace(/"/g, '""');
					s += '"';
					
					if (index != (length - 1)) {
						s += ",";	
					} 
				});
				s += "\n"; 
			});
			$("textArea").val(s);
        }

		writeCSV();
</script>
 
 