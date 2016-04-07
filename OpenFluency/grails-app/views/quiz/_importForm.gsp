<%@ page import="com.openfluency.Constants"%>
 	<link rel="stylesheet" href="http://code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
    <script src="http://code.jquery.com/jquery-1.10.2.js"></script>
    <script src="http://code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
  <link rel="stylesheet" href="/resources/demos/style.css">
  <script>
     $(function() {
     var newDate = new Date();   
    $("#liveTime_id").datepicker();
    $("#liveTime_id").datepicker("setDate", newDate);
     $( "#endTime_id" ).datepicker({ defaultDate: new Date() });
     
  });
    function test2(){
    document.getElementById("liveTime_id").innerHTML = new Date();
    }
  </script>
<div class="form-group">
	<label for="title">Title:</label>
	<g:textField name="title" value="${quizInstance?.title}"
		class="form-control" />
</div>

<div class="form-group">
	<label for="maxCardTime"> Maximum seconds allowed per card
		(if 0, the quiz will not be timed): </label>
	<g:field type="number" min="0" max="60" name="maxCardTime"
		value="${quizInstance?.maxCardTime}" class="form-control" />
</div>

<div class="form-group live-time-group">
	<label for="liveTime">Available starting:</label>
	    <p><input type="text" name="liveTime" id="liveTime_id"</p> 
</div>
<g:hiddenField name="courseInstanceId" value="${courseInstance.id}" />

<div class="form-group end-time-group">
	<label for="endTime">Available until:</label>
		<p><input type="text" name="endTime" id="endTime_id"> </p> 
</div>

<div class="btn-group text-left">
		<button type="button" class="btn btn-default"
			data-toggle="modal" data-target="#myModal">
			File Upload 
		</button>
</div>
<div class="modal csv-modal fade" id="myModal">
	<div class="modal-dialog">
		<g:form action="loadQuizFromCSV" id="${courseInstance.id}"
			enctype="multipart/form-data">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title">Load Quiz CSV File</h4>
				</div>
				<div class="modal-body">
					<p>
						Upload a CSV file with your Quiz definitions. 
					</p>
					<input name="csvData" type="file" name="csvData" />
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
					<button type="submit" class="btn btn-primary" model="[quizInstance: quizInstance, courseInstance: courseInstance]">Submit</button>
				</div>

			</div>
		</g:form>
	</div>
</div>
