
<%@ page import="com.openfluency.flashcard.CardRanking" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'cardRanking.label', default: 'CardRanking')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-cardRanking" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-cardRanking" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			<thead>
					<tr>
					
						<g:sortableColumn property="meaningRanking" title="${message(code: 'cardRanking.meaningRanking.label', default: 'Meaning Ranking')}" />
					
						<g:sortableColumn property="pronunciationRanking" title="${message(code: 'cardRanking.pronunciationRanking.label', default: 'Pronunciation Ranking')}" />
					
						<g:sortableColumn property="symbolRanking" title="${message(code: 'cardRanking.symbolRanking.label', default: 'Symbol Ranking')}" />
					
						<g:sortableColumn property="dateCreated" title="${message(code: 'cardRanking.dateCreated.label', default: 'Date Created')}" />
					
						<th><g:message code="cardRanking.flashcard.label" default="Flashcard" /></th>
					
						<g:sortableColumn property="lastUpdated" title="${message(code: 'cardRanking.lastUpdated.label', default: 'Last Updated')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${cardRankingInstanceList}" status="i" var="cardRankingInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${cardRankingInstance.id}">${fieldValue(bean: cardRankingInstance, field: "meaningRanking")}</g:link></td>
					
						<td>${fieldValue(bean: cardRankingInstance, field: "pronunciationRanking")}</td>
					
						<td>${fieldValue(bean: cardRankingInstance, field: "symbolRanking")}</td>
					
						<td><g:formatDate date="${cardRankingInstance.dateCreated}" /></td>
					
						<td>${fieldValue(bean: cardRankingInstance, field: "flashcard")}</td>
					
						<td><g:formatDate date="${cardRankingInstance.lastUpdated}" /></td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${cardRankingInstanceCount}" />
			</div>
		</div>
	</body>
</html>
