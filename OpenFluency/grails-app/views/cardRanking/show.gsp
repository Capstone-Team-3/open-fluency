
<%@ page import="com.openfluency.flashcard.CardRanking" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'cardRanking.label', default: 'CardRanking')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-cardRanking" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-cardRanking" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list cardRanking">
			
				<g:if test="${cardRankingInstance?.meaningRanking}">
				<li class="fieldcontain">
					<span id="meaningRanking-label" class="property-label"><g:message code="cardRanking.meaningRanking.label" default="Meaning Ranking" /></span>
					
						<span class="property-value" aria-labelledby="meaningRanking-label"><g:fieldValue bean="${cardRankingInstance}" field="meaningRanking"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${cardRankingInstance?.pronunciationRanking}">
				<li class="fieldcontain">
					<span id="pronunciationRanking-label" class="property-label"><g:message code="cardRanking.pronunciationRanking.label" default="Pronunciation Ranking" /></span>
					
						<span class="property-value" aria-labelledby="pronunciationRanking-label"><g:fieldValue bean="${cardRankingInstance}" field="pronunciationRanking"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${cardRankingInstance?.symbolRanking}">
				<li class="fieldcontain">
					<span id="symbolRanking-label" class="property-label"><g:message code="cardRanking.symbolRanking.label" default="Symbol Ranking" /></span>
					
						<span class="property-value" aria-labelledby="symbolRanking-label"><g:fieldValue bean="${cardRankingInstance}" field="symbolRanking"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${cardRankingInstance?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="cardRanking.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${cardRankingInstance?.dateCreated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${cardRankingInstance?.flashcard}">
				<li class="fieldcontain">
					<span id="flashcard-label" class="property-label"><g:message code="cardRanking.flashcard.label" default="Flashcard" /></span>
					
						<span class="property-value" aria-labelledby="flashcard-label"><g:link controller="flashcard" action="show" id="${cardRankingInstance?.flashcard?.id}">${cardRankingInstance?.flashcard?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${cardRankingInstance?.lastUpdated}">
				<li class="fieldcontain">
					<span id="lastUpdated-label" class="property-label"><g:message code="cardRanking.lastUpdated.label" default="Last Updated" /></span>
					
						<span class="property-value" aria-labelledby="lastUpdated-label"><g:formatDate date="${cardRankingInstance?.lastUpdated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${cardRankingInstance?.user}">
				<li class="fieldcontain">
					<span id="user-label" class="property-label"><g:message code="cardRanking.user.label" default="User" /></span>
					
						<span class="property-value" aria-labelledby="user-label"><g:link controller="user" action="show" id="${cardRankingInstance?.user?.id}">${cardRankingInstance?.user?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:cardRankingInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${cardRankingInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
