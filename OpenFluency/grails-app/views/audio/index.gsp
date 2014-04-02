
<%@ page import="com.openfluency.media.Audio" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'audio.label', default: 'Audio')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-audio" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-audio" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			<thead>
					<tr>
					
						<g:sortableColumn property="dateCreated" title="${message(code: 'audio.dateCreated.label', default: 'Date Created')}" />
					
						<g:sortableColumn property="lastUpdated" title="${message(code: 'audio.lastUpdated.label', default: 'Last Updated')}" />
					
						<th><g:message code="audio.owner.label" default="Owner" /></th>
					
						<th><g:message code="audio.pronunciation.label" default="Pronunciation" /></th>
					
						<g:sortableColumn property="url" title="${message(code: 'audio.url.label', default: 'Url')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${audioInstanceList}" status="i" var="audioInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${audioInstance.id}">${fieldValue(bean: audioInstance, field: "dateCreated")}</g:link></td>
					
						<td><g:formatDate date="${audioInstance.lastUpdated}" /></td>
					
						<td>${fieldValue(bean: audioInstance, field: "owner")}</td>
					
						<td>${fieldValue(bean: audioInstance, field: "pronunciation")}</td>
					
						<td>${fieldValue(bean: audioInstance, field: "url")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${audioInstanceCount ?: 0}" />
			</div>
		</div>
	</body>
</html>
