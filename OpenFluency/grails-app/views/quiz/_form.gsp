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

<div class="form-group">
	<label for="testElement">Test students on:</label>
	<select class="form-control" name="testElement">
		<g:each in="${Constants.CARD_ELEMENTS}" status="i" var="element">
			<option value="${i}">${element}s of the words/characters</option>
		</g:each>
	</select>
</div>

<div class="form-group live-time-group">
	<label for="liveTime">Available starting:</label>
	<g:datePicker name="liveTime" value="${quizInstance ? quizInstance.liveTime : new Date()}" class="form-control"/>
</div>

<label for="included-chapters">Chapters to include:</label>
<div class="panel panel-default">
	<div class="panel-body">
		<ul class="list-unstyled">
			<g:each in="${courseInstance.chapters}">
				<li>
					<input type="checkbox" data-chapter="${it.id}" class="chapter-selector"/>
					${it.title}
				</li>
			</g:each>
		</ul>
	</div>
</div>