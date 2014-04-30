<%@ page import="com.openfluency.language.Language" %>
<div class="form-group">
	<label>Title</label>
	<input class="form-control" type="text" name="title" value="${deckInstance?.title}"/>
</div>

<div class="form-group">
	<label>Description</label>
	<textarea class="form-control" name="description">${deckInstance?.description}</textarea>
</div>

<div class="form-group">
	<label for="alphabet" class="control-label">
		Source Language - The language you already know
		<span class="required-indicator">*</span>
	</label>
	<g:select from="${Language.list()}" optionValue="name" optionKey="id" value="${deckInstance?.sourceLanguage?.id}" class="form-control" name="sourceLanguage.id"/>
</div>

<div class="form-group">
	<label for="alphabet" class="control-label">
		Language - The language you're learning
		<span class="required-indicator">*</span>
	</label>
	<g:select from="${Language.list()}" optionValue="name" optionKey="id" value="${deckInstance?.language?.id}" class="form-control" name="language.id"/>
</div>

<div class="form-group">
	<label for="Repetition Algorithm" class="control-label">
		The Repetition Algorithm you'd like to use
		<br/>
		(SM2SpacedRepetion is recommended)
		<span class="required-indicator">*</span>
	</label>
	<g:select from="${cardServerAlgos}" name="cardServerAlgo" class="form-control" value="${deckInstance?.cardServerName}"/>
</div>