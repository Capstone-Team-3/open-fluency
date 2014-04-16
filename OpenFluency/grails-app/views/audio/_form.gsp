<%@ page import="com.openfluency.media.Audio" %>

<div class="panel panel-default">
	<div class="panel-body">
		<div class="fieldcontain ${hasErrors(bean: audioInstance, field: 'pronunciation', 'error')} required">
			<label for="pronunciation">
				<g:message code="audio.pronunciation.label" default="Pronunciation" />
				<span class="required-indicator">*</span>
			</label>
			<g:select id="pronunciation" name="pronunciation.id" from="${com.openfluency.language.Pronunciation.list()}" optionKey="id" required="" value="${audioInstance?.pronunciation?.id}" class="many-to-one"/>
		
		</div>
		
		<div class="fieldcontain ${hasErrors(bean: audioInstance, field: 'url', 'error')} required">
			<label for="url">
				<g:message code="audio.url.label" default="Url" />
				
			</label>
			<g:textField id="url" name="url" value="${audioInstance?.url}"/>
		
		</div>
	</div>
</div>


