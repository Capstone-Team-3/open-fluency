<%@ page import="com.openfluency.media.Customization" %>



<div class="fieldcontain ${hasErrors(bean: customizationInstance, field: 'owner', 'error')} required">
	<label for="owner">
		<g:message code="customization.owner.label" default="Owner" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="owner" name="owner.id" from="${com.openfluency.auth.User.list()}" optionKey="id" required="" value="${customizationInstance?.owner?.id}" class="many-to-one"/>

</div>

<div class="fieldcontain ${hasErrors(bean: customizationInstance, field: 'card', 'error')} required">
	<label for="card">
		<g:message code="customization.card.label" default="Card" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="card" name="card.id" from="${com.openfluency.flashcard.Flashcard.list()}" optionKey="id" required="" value="${customizationInstance?.card?.id}" class="many-to-one"/>

</div>

<div class="fieldcontain ${hasErrors(bean: customizationInstance, field: 'audioAssoc', 'error')} ">
	<label for="audioAssoc">
		<g:message code="customization.audioAssoc.label" default="Audio Assoc" />
		
	</label>
	<g:select id="audioAssoc" name="audioAssoc.id" from="${com.openfluency.media.Audio.list()}" optionKey="id" value="${customizationInstance?.audioAssoc?.id}" class="many-to-one" noSelection="['null': '']"/>

</div>

<div class="fieldcontain ${hasErrors(bean: customizationInstance, field: 'imageAssoc', 'error')} ">
	<label for="imageAssoc">
		<g:message code="customization.imageAssoc.label" default="Image Assoc" />
		
	</label>
	<g:select id="imageAssoc" name="imageAssoc.id" from="${com.openfluency.media.Image.list()}" optionKey="id" value="${customizationInstance?.imageAssoc?.id}" class="many-to-one" noSelection="['null': '']"/>

</div>

