<%@ page import="com.openfluency.Constants" %>
<div class="form-group">
	<label for="title">Title:</label>
	<input required name="title" type="text" value="${quizInstance?.title}" class="form-control"/>
</div>

<div class="form-group">
	<label for="maxCardTime">
		Maximum seconds allowed per card (if 0, the quiz will not be timed):
	</label>
	<g:field type="number" required min="0" max="60" name="maxCardTime" value="${quizInstance?.maxCardTime ? quizInstance?.maxCardTime : 0 }" class="form-control"/>
</div>

<div class="form-group">
	<label for="testElement">Test students on:</label>
	<select class="form-control" name="testElement">
		
		<g:set var="language" value="${courseInstance.chapters[0]?.deck?.language ? courseInstance.chapters[0].deck.language : courseInstance.language }"/>
		<g:set var="sourceLanguage" value="${courseInstance.chapters[0]?.deck?.sourceLanguage ? courseInstance.chapters[0].deck.sourceLanguage : 'English' }"/>
	
		<option value="${Constants.SYMBOL}">Meanings of words/characters (${language} to ${sourceLanguage})</option>
		<option value="${Constants.MEANING}">Meanings of words/characters (${sourceLanguage} to ${language})</option>
		<option value="${Constants.PRONUNCIATION}">Pronunciations of ${language} words/characters</option>
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