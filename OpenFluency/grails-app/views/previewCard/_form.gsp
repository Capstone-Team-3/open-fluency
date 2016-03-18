<%@ page import="com.openfluency.deck.PreviewCard" %>



<div class="fieldcontain ${hasErrors(bean: previewCardInstance, field: 'units', 'error')} ">
	<label for="units">
		<g:message code="previewCard.units.label" default="Units" />
		
	</label>
	

</div>

<div class="fieldcontain ${hasErrors(bean: previewCardInstance, field: 'types', 'error')} ">
	<label for="types">
		<g:message code="previewCard.types.label" default="Types" />
		
	</label>
	

</div>

<div class="fieldcontain ${hasErrors(bean: previewCardInstance, field: 'fields', 'error')} ">
	<label for="fields">
		<g:message code="previewCard.fields.label" default="Fields" />
		
	</label>
	

</div>

<div class="fieldcontain ${hasErrors(bean: previewCardInstance, field: 'deck', 'error')} required">
	<label for="deck">
		<g:message code="previewCard.deck.label" default="Deck" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="deck" name="deck.id" from="${com.openfluency.deck.PreviewDeck.list()}" optionKey="id" required="" value="${previewCardInstance?.deck?.id}" class="many-to-one"/>

</div>

