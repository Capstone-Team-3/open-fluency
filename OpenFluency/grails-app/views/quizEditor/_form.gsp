<%@ page import="com.openfluency.Constants" %>
<div class="form-group">
	<label for="title">Title:</label>
	<g:textField name="title" value="${quizInstance?.title}" class="form-control"/>
</div>

<div class="form-group">
	<label for="maxCardTime">
		Maximum seconds allowed per card (if 0, the quiz will not be timed):
	</label>
	<g:field type="number" min="0" max="60" name="maxCardTime" value="${quizInstance?.maxCardTime}" class="form-control"/>
</div>

<div class="form-group live-time-group">
	<label for="liveTime">Available starting:</label>
	<g:datePicker name="liveTime" value="${quizInstance ? quizInstance.liveTime : new Date()}" class="form-control"/>
</div>

<div class="btn btn-info" onclick="addQuestion(); writeCSV();"> Add a Question</div>

<div id="questionList"></div>

<div class="question panel panel-default question-panel">

		<label>Question</label>
		<input name="question" type="text" onchange="writeCSV();" onkeyup="writeCSV();"></input>
		<br/>

		<label>Correct Answer:</label>
		<input name="correctAnswer" type="text" onchange="writeCSV();" onkeyup="writeCSV();"></input>
		<br/>

		<label>Wrong Answer 1:</label>
		<input name="wrongAnswer" type="text" onchange="writeCSV();" onkeyup="writeCSV();"></input>
		<br/>

		<label>Wrong Answer 2:</label>
		<input name="wrongAnswer" type="text" onchange="writeCSV();" onkeyup="writeCSV();"></input>
		<br/>

		<label>Wrong Answer 3:</label>
		<input name="wrongAnswer" type="text" onchange="writeCSV();" onkeyup="writeCSV();"></input>
		<br/>
		
		<div class="btn btn-danger" onclick="$(this).parent().remove(); writeCSV();">Remove Question</div>
</div>

 <div class="form-group">
 <div id="showCSV" class="btn btn-info" onclick="showCSV()">Show Raw Quiz CSV</div>
 <textArea class="form-control" name="questions" style="display:none">
 </textArea>
 </div>
 
 <script type="text/javascript">

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
			questionHtml += "<label>Question</label>";
			questionHtml += "<input name=\"question\" type=\"text\" onchange=\"writeCSV();\" onkeyup=\"writeCSV();\"></input>";
			questionHtml += "<br/>";
			questionHtml += "<label>Correct Answer:</label>";
			questionHtml += "<input name=\"correctAnswer\" type=\"text\" onchange=\"writeCSV();\" onkeyup=\"writeCSV();\"></input>";
			questionHtml += "<br/>";
			questionHtml += "<label>Wrong Answer 1:</label>";
			questionHtml += "<input name=\"wrongAnswer\" type=\"text\" onchange=\"writeCSV();\" onkeyup=\"writeCSV();\"></input>";
			questionHtml += "<br/>";
			questionHtml += "<label>Wrong Answer 2:</label>";
			questionHtml += "<input name=\"wrongAnswer\" type=\"text\" onchange=\"writeCSV();\" onkeyup=\"writeCSV();\"></input>";
			questionHtml += "<br/>";
			questionHtml += "<label>Wrong Answer 3:</label>";
			questionHtml += "<input name=\"wrongAnswer\" type=\"text\" onchange=\"writeCSV();\" onkeyup=\"writeCSV();\"></input>";
			questionHtml += "<br/>";
			questionHtml += "<div class=\"btn btn-danger\" onclick=\"$(this).parent().remove(); writeCSV();\">Remove Question</div>";
			questionHtml += "</div>";

 			var questionList = $("#questionList");

 			var html = questionList.append(questionHtml);
 	 	}	
 
 		// TODO what if one of the inputs contains a comma
        function writeCSV() {
            var s = "";
			$(".question").each(function(index,item) {

				s += "MANUAL,"
				 
				var inputs = $(this).find("input");
				var length = inputs.length;
				inputs.each(function(index, item) { 
					s += $(this).val();
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
 
 