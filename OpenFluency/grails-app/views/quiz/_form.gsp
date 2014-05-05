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
		<option value="${Constants.SYMBOL}" ${quizInstance?.testElement == Constants.SYMBOL ? "selected":""}>Meanings of words/characters (${courseInstance.chapters[0].deck.language} to ${courseInstance.chapters[0].deck.sourceLanguage})</option>
		<option value="${Constants.MEANING}" ${quizInstance?.testElement == Constants.MEANING ? "selected":""}>Meanings of words/characters (${courseInstance.chapters[0].deck.sourceLanguage} to ${courseInstance.chapters[0].deck.language})</option>
		<option value="${Constants.PRONUNCIATION}" ${quizInstance?.testElement == Constants.PRONUNCIATION ? "selected":""}>Pronunciations of ${courseInstance.chapters[0].deck.language} words/characters</option>
		<option value="${Constants.RANDOM}" ${quizInstance?.testElement == Constants.RANDOM ? "selected":""}>Any of the above will be randomly used for each question</option>
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