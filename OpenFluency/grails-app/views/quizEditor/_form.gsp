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

<div class="btn btn-info" onclick="addQuestion(); writeCSV();"> Add a Question</div>

<div id="questionList">

	<g:each var="question" in="${quizInstance?.questions}">

		<div class="question panel panel-default question-panel">
		
				<div>
					<label>Question</label>
					<input name="question" type="text" onchange="writeCSV();" onkeyup="writeCSV();" value="${question.question}"></input>
				</div>
		
				<div>
					<label>Correct Answer:</label>
					<input name="correctAnswer" type="text" onchange="writeCSV();" onkeyup="writeCSV();" value="${question.correctOption.option}"></input>
					<div class="btn btn-info" onclick="getConfusers(this);">Generate Confuser Answers</div>
				</div>
				
				<div class="btn btn-info" onclick="addWrongAnswer(this); writeCSV();">Add Wrong Answer</div>
				
				<div class="btn btn-danger" onclick="$(this).parent().remove(); writeCSV();">Remove Question</div>
		
				<g:each var="wrongOption" in="${question.wrongOptions}">
					<div>
						<label>Wrong Answer:</label>
						<input name="wrongAnswer" type="text" onchange="writeCSV();" onkeyup="writeCSV();" value="${wrongOption.option}"></input>
						<span class="btn btn-danger" onclick="$(this).parent().remove(); writeCSV();">Remove Wrong Answer</span>
					</div>
				</g:each>
		</div>
	
	</g:each>

</div>

 <div class="form-group">
 <div id="showCSV" class="btn btn-info" onclick="showCSV()">Show Raw Quiz CSV</div>
 <textArea class="form-control" name="questions" style="display:none" readonly>
 </textArea>
 </div>
 
 <script type="text/javascript">

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
 			var question = $(that).parent();

 			var wrongAnswerHtml = "<div>";
 			wrongAnswerHtml += "<label>Wrong Answer:</label>";
 			wrongAnswerHtml += "<input name=\"wrongAnswer\" type=\"text\" onchange=\"writeCSV();\" onkeyup=\"writeCSV();\"></input>";
 			wrongAnswerHtml += "<span class=\"btn btn-danger\" onclick=\"$(this).parent().remove(); writeCSV();\">Remove Wrong Answer</span>";
 			wrongAnswerHtml += "</div>";

 			question.append(wrongAnswerHtml);
 	 	}

 		function showCSV() {
	 		if ($("textArea").is(':visible')) {
		 		$("textArea").hide()
		 		$("#showCSV").html("Show Raw Quiz CSV")
		 	}
	 		else {
	 			$("textArea").show()
	 			$("#showCSV").html("Hide Raw Quiz CSV")
		 	}
	 	}
 
 		function addQuestion() {

			var questionHtml = "<div class=\"question panel panel-default question-panel\">";
			questionHtml += "<div>";
			questionHtml += "<label>Question</label>";
			questionHtml += "<input name=\"question\" type=\"text\" onchange=\"writeCSV();\" onkeyup=\"writeCSV();\"></input>";
			questionHtml += "</div>";
			questionHtml += "<div>";
			questionHtml += "<label>Correct Answer:</label>";
			questionHtml += "<input name=\"correctAnswer\" type=\"text\" onchange=\"writeCSV();\" onkeyup=\"writeCSV();\"></input>";
			questionHtml += "<div class=\"btn btn-info\" onclick=\"getConfusers(this);\">Generate Confuser Answers</div>";
			questionHtml += "</div>";
			questionHtml += "<div class=\"btn btn-info\" onclick=\"addWrongAnswer(this); writeCSV();\">Add Wrong Answer</div>";
			questionHtml += "<div class=\"btn btn-danger\" onclick=\"$(this).parent().remove(); writeCSV();\">Remove Question</div>";
			questionHtml += "<div>";
			questionHtml += "<label>Wrong Answer:</label>";
			questionHtml += "<input name=\"wrongAnswer\" type=\"text\" onchange=\"writeCSV();\" onkeyup=\"writeCSV();\"></input>";
			questionHtml += "<span class=\"btn btn-danger\" onclick=\"$(this).parent().remove(); writeCSV();\">Remove Wrong Answer</span>";
			questionHtml += "</div>";
			questionHtml += "<div>";
			questionHtml += "<label>Wrong Answer:</label>";
			questionHtml += "<input name=\"wrongAnswer\" type=\"text\" onchange=\"writeCSV();\" onkeyup=\"writeCSV();\"></input>";
			questionHtml += "<span class=\"btn btn-danger\" onclick=\"$(this).parent().remove(); writeCSV();\">Remove Wrong Answer</span>";
			questionHtml += "</div>";
			questionHtml += "<div>";
			questionHtml += "<label>Wrong Answer:</label>";
			questionHtml += "<input name=\"wrongAnswer\" type=\"text\" onchange=\"writeCSV();\" onkeyup=\"writeCSV();\"></input>";
			questionHtml += "<span class=\"btn btn-danger\" onclick=\"$(this).parent().remove(); writeCSV();\">Remove Wrong Answer</span>";
			questionHtml += "</div>";
			questionHtml += "</div>";

 			var questionList = $("#questionList");

 			var html = questionList.append(questionHtml);
 	 	}	
 
        function writeCSV() {
            var s = "";
			$(".question").each(function(index,item) {

				s += "MANUAL,"
				 
				var inputs = $(this).find("input");
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
 
 