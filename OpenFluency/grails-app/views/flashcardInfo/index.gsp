
<%@ page import="com.openfluency.flashcard.FlashcardInfo" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'flashcardInfo.label', default: 'FlashcardInfo')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-flashcardInfo" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-flashcardInfo" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			<thead>
					<tr>
					
						<th><g:message code="flashcardInfo.flashcard.label" default="Flashcard" /></th>
					
						<th><g:message code="flashcardInfo.user.label" default="User" /></th>
					
						<th><g:message code="flashcardInfo.deck.label" default="Deck" /></th>
					
						<g:sortableColumn property="queue" title="${message(code: 'flashcardInfo.queue.label', default: 'Queue')}" />
					
						<g:sortableColumn property="viewPriority" title="${message(code: 'flashcardInfo.viewPriority.label', default: 'View Priority')}" />
					
						<g:sortableColumn property="numberOfRepetitions" title="${message(code: 'flashcardInfo.numberOfRepetitions.label', default: 'Number Of Repetitions')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${flashcardInfoInstanceList}" status="i" var="flashcardInfoInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${flashcardInfoInstance.id}">${fieldValue(bean: flashcardInfoInstance, field: "flashcard")}</g:link></td>
					
						<td>${fieldValue(bean: flashcardInfoInstance, field: "user")}</td>
					
						<td>${fieldValue(bean: flashcardInfoInstance, field: "deck")}</td>
					
						<td>${fieldValue(bean: flashcardInfoInstance, field: "queue")}</td>
					
						<td>${fieldValue(bean: flashcardInfoInstance, field: "viewPriority")}</td>
					
						<td>${fieldValue(bean: flashcardInfoInstance, field: "numberOfRepetitions")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${flashcardInfoInstanceCount ?: 0}" />
			</div>
		</div>
	</body>
</html>
