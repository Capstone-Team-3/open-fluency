<%@ page import="com.openfluency.deck.PreviewDeck" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'previewDeck.label', default: 'PreviewDeck')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		  <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
		<div id="show-previewDeck" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list previewDeck">
			
				<g:if test="${previewDeckInstance?.description}">
				<li class="fieldcontain">
					<span id="description-label" class="property-label"><g:message code="previewDeck.description.label" default="Description" /></span>
					
						<span class="property-value" aria-labelledby="description-label"><g:fieldValue bean="${previewDeckInstance}" field="description"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${previewDeckInstance?.filename}">
				<li class="fieldcontain">
					<span id="filename-label" class="property-label"><g:message code="previewDeck.filename.label" default="Filename" /></span>
					
						<span class="property-value" aria-labelledby="filename-label"><g:fieldValue bean="${previewDeckInstance}" field="filename"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${previewDeckInstance?.name}">
				<li class="fieldcontain">
					<span id="name-label" class="property-label"><g:message code="previewDeck.name.label" default="Name" /></span>
					
						<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${previewDeckInstance}" field="name"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${previewDeckInstance?.owner}">
				<li class="fieldcontain">
					<span id="owner-label" class="property-label"><g:message code="previewDeck.owner.label" default="Owner" /></span>
					
						<span class="property-value" aria-labelledby="owner-label"><g:link controller="user" action="show" id="${previewDeckInstance?.owner?.id}">${previewDeckInstance?.owner?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
			</ol>
				
				<g:render template="select" />

	             

		</div>
	</body>
</html>
