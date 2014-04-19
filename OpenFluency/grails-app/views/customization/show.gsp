
<%@ page import="com.openfluency.media.Customization" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'customization.label', default: 'Customization')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-customization" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-customization" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list customization">
			
				<g:if test="${customizationInstance?.owner}">
				<li class="fieldcontain">
					<span id="owner-label" class="property-label"><g:message code="customization.owner.label" default="Owner" /></span>
					
						<span class="property-value" aria-labelledby="owner-label"><g:link controller="user" action="show" id="${customizationInstance?.owner?.id}">${customizationInstance?.owner?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${customizationInstance?.card}">
				<li class="fieldcontain">
					<span id="card-label" class="property-label"><g:message code="customization.card.label" default="Card" /></span>
					
						<span class="property-value" aria-labelledby="card-label"><g:link controller="flashcard" action="show" id="${customizationInstance?.card?.id}">${customizationInstance?.card?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${customizationInstance?.audioAssoc}">
				<li class="fieldcontain">
					<span id="audioAssoc-label" class="property-label"><g:message code="customization.audioAssoc.label" default="Audio Assoc" /></span>
					
						<span class="property-value" aria-labelledby="audioAssoc-label"><g:link controller="audio" action="show" id="${customizationInstance?.audioAssoc?.id}">${customizationInstance?.audioAssoc?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${customizationInstance?.imageAssoc}">
				<li class="fieldcontain">
					<span id="imageAssoc-label" class="property-label"><g:message code="customization.imageAssoc.label" default="Image Assoc" /></span>
					
						<span class="property-value" aria-labelledby="imageAssoc-label"><g:link controller="image" action="show" id="${customizationInstance?.imageAssoc?.id}">${customizationInstance?.imageAssoc?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:customizationInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${customizationInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
