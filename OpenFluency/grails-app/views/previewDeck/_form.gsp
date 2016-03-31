<%@ page import="com.openfluency.deck.PreviewDeck" %>



<div class="fieldcontain ${hasErrors(bean: previewDeckInstance, field: 'description', 'error')} ">
	<label for="description">
		<g:message code="previewDeck.description.label" default="Description" />
		
	</label>
	<g:textField name="description" value="${previewDeckInstance?.description}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: previewDeckInstance, field: 'filename', 'error')} ">
	<label for="filename">
		<g:message code="previewDeck.filename.label" default="Filename" />
		
	</label>
	<g:textField name="filename" value="${previewDeckInstance?.filename}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: previewDeckInstance, field: 'name', 'error')} ">
	<label for="name">
		<g:message code="previewDeck.name.label" default="Name" />
		
	</label>
	<g:textField name="name" value="${previewDeckInstance?.name}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: previewDeckInstance, field: 'owner', 'error')} required">
	<label for="owner">
		<g:message code="previewDeck.owner.label" default="Owner" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="owner" name="owner.id" from="${com.openfluency.auth.User.list()}" optionKey="id" required="" value="${previewDeckInstance?.owner?.id}" class="many-to-one"/>

</div>

