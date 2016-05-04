
<%@ page import="com.openfluency.deck.PreviewDeck" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'previewDeck.label', default: 'PreviewDeck')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
        <!--
		<a href="#list-previewDeck" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        -->
		<div id="list-previewDeck" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
The following decks are in the process of import. To finish the import, click on the link and assign 
appropiate fields in each record to a flash card.
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table width="100%">
			<thead>
					<tr>
						<g:sortableColumn property="filename" title="${message(code: 'previewDeck.filename.label', default: 'Filename')}" />

						<g:sortableColumn property="description" title="${message(code: 'previewDeck.description.label', default: 'Description')}" />
					
						<g:sortableColumn property="name" title="${message(code: 'previewDeck.name.label', default: 'Name')}" />
					
						<!--th><g:message code="previewDeck.owner.label" default="Owner" /></th-->
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${previewDeckInstanceList}" status="i" var="previewDeckInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="map" id="${previewDeckInstance.id}">${fieldValue(bean: previewDeckInstance, field: "filename")}</g:link></td>
					
						<td>${fieldValue(bean: previewDeckInstance, field: "description")}</td>
					
						<td>${fieldValue(bean: previewDeckInstance, field: "name")}</td>
					
						<td>${fieldValue(bean: previewDeckInstance, field: "owner")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${previewDeckInstanceCount ?: 0}" />
			</div>
		</div>
	</body>
</html>
