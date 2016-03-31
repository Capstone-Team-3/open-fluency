
<%@ page import="com.openfluency.deck.PreviewCard" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'previewCard.label', default: 'PreviewCard')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-previewCard" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-previewCard" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list previewCard">
			
				<g:if test="${previewCardInstance?.units}">
				<li class="fieldcontain">
					<span id="units-label" class="property-label"><g:message code="previewCard.units.label" default="Units" /></span>
					
						<span class="property-value" aria-labelledby="units-label"><g:fieldValue bean="${previewCardInstance}" field="units"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${previewCardInstance?.types}">
				<li class="fieldcontain">
					<span id="types-label" class="property-label"><g:message code="previewCard.types.label" default="Types" /></span>
					
						<span class="property-value" aria-labelledby="types-label"><g:fieldValue bean="${previewCardInstance}" field="types"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${previewCardInstance?.fields}">
				<li class="fieldcontain">
					<span id="fields-label" class="property-label"><g:message code="previewCard.fields.label" default="Fields" /></span>
					
						<span class="property-value" aria-labelledby="fields-label"><g:fieldValue bean="${previewCardInstance}" field="fields"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${previewCardInstance?.deck}">
				<li class="fieldcontain">
					<span id="deck-label" class="property-label"><g:message code="previewCard.deck.label" default="Deck" /></span>
					
						<span class="property-value" aria-labelledby="deck-label"><g:link controller="previewDeck" action="show" id="${previewCardInstance?.deck?.id}">${previewCardInstance?.deck?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:previewCardInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${previewCardInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
