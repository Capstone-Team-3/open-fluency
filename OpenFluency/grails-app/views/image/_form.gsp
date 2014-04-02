<%@ page import="com.openfluency.media.Image" %>



<div class="fieldcontain ${hasErrors(bean: imageInstance, field: 'owner', 'error')} required">
	<label for="owner">
		<g:message code="image.owner.label" default="Owner" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="owner" name="owner.id" from="${com.openfluency.auth.User.list()}" optionKey="id" required="" value="${imageInstance?.owner?.id}" class="many-to-one"/>

</div>

<div class="fieldcontain ${hasErrors(bean: imageInstance, field: 'unitMapping', 'error')} required">
	<label for="unitMapping">
		<g:message code="image.unitMapping.label" default="Unit Mapping" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="unitMapping" name="unitMapping.id" from="${com.openfluency.language.UnitMapping.list()}" optionKey="id" required="" value="${imageInstance?.unitMapping?.id}" class="many-to-one"/>

</div>

<div class="fieldcontain ${hasErrors(bean: imageInstance, field: 'url', 'error')} ">
	<label for="url">
		<g:message code="image.url.label" default="Url" />
		
	</label>
	<g:textField name="url" value="${imageInstance?.url}"/>

</div>

