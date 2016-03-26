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

 <div class="form-group">
 <textArea class="form-control" name="questions">
 Type in the questions in csv format here:
 The format is: question_type, question, correct_answer, wrong_answer1, wrong_answer2, ...
 </textArea>
 </div>
 
 