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
	<label for="alphabet" class="tooltiper control-label"  data-toggle="tooltip"  data-placement="left" title="The language you already know" >
		Source Language 
		<span class="required-indicator">*</span>
	</label>
	<g:select from="${Language.list()}" optionValue="name" optionKey="id" value="${deckInstance?.sourceLanguage?.id}" class="form-control" name="sourceLanguage.id"/>
</div>

<div class="form-group">
	<label for="alphabet" class="tooltiper control-label"  data-toggle="tooltip"  data-placement="left" title="The language you're learning">
		Language 
		<span class="required-indicator">*</span>
	</label>
	<g:select from="${Language.list()}" optionValue="name" optionKey="id" value="${deckInstance?.language?.id}" class="form-control" name="language.id"/>
</div>

<div class="form-group">
	<label for="Repetition Algorithm" class="tooltiper control-label"  data-toggle="tooltip"  data-placement="left" title="The Repetition Algorithm you'd like to use (SM2SpacedRepetion is recommended)">
		Repetition Algorithm 
		<span class="required-indicator">*</span>
	</label>
	<g:select from="${cardServerAlgos}" name="cardServerAlgo" class="form-control" value="${deckInstance?.cardServerName}"/>
</div>