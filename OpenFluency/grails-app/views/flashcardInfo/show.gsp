
<%@ page import="com.openfluency.flashcard.FlashcardInfo" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'flashcardInfo.label', default: 'FlashcardInfo')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-flashcardInfo" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-flashcardInfo" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list flashcardInfo">
			
				<g:if test="${flashcardInfoInstance?.flashcard}">
				<li class="fieldcontain">
					<span id="flashcard-label" class="property-label"><g:message code="flashcardInfo.flashcard.label" default="Flashcard" /></span>
					
						<span class="property-value" aria-labelledby="flashcard-label"><g:link controller="flashcard" action="show" id="${flashcardInfoInstance?.flashcard?.id}">${flashcardInfoInstance?.flashcard?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${flashcardInfoInstance?.user}">
				<li class="fieldcontain">
					<span id="user-label" class="property-label"><g:message code="flashcardInfo.user.label" default="User" /></span>
					
						<span class="property-value" aria-labelledby="user-label"><g:link controller="user" action="show" id="${flashcardInfoInstance?.user?.id}">${flashcardInfoInstance?.user?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${flashcardInfoInstance?.deck}">
				<li class="fieldcontain">
					<span id="deck-label" class="property-label"><g:message code="flashcardInfo.deck.label" default="Deck" /></span>
					
						<span class="property-value" aria-labelledby="deck-label"><g:link controller="deck" action="show" id="${flashcardInfoInstance?.deck?.id}">${flashcardInfoInstance?.deck?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${flashcardInfoInstance?.queue}">
				<li class="fieldcontain">
					<span id="queue-label" class="property-label"><g:message code="flashcardInfo.queue.label" default="Queue" /></span>
					
						<span class="property-value" aria-labelledby="queue-label"><g:fieldValue bean="${flashcardInfoInstance}" field="queue"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${flashcardInfoInstance?.viewPriority}">
				<li class="fieldcontain">
					<span id="viewPriority-label" class="property-label"><g:message code="flashcardInfo.viewPriority.label" default="View Priority" /></span>
					
						<span class="property-value" aria-labelledby="viewPriority-label"><g:fieldValue bean="${flashcardInfoInstance}" field="viewPriority"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${flashcardInfoInstance?.numberOfRepetitions}">
				<li class="fieldcontain">
					<span id="numberOfRepetitions-label" class="property-label"><g:message code="flashcardInfo.numberOfRepetitions.label" default="Number Of Repetitions" /></span>
					
						<span class="property-value" aria-labelledby="numberOfRepetitions-label"><g:fieldValue bean="${flashcardInfoInstance}" field="numberOfRepetitions"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${flashcardInfoInstance?.easinessFactor}">
				<li class="fieldcontain">
					<span id="easinessFactor-label" class="property-label"><g:message code="flashcardInfo.easinessFactor.label" default="Easiness Factor" /></span>
					
						<span class="property-value" aria-labelledby="easinessFactor-label"><g:fieldValue bean="${flashcardInfoInstance}" field="easinessFactor"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${flashcardInfoInstance?.interval}">
				<li class="fieldcontain">
					<span id="interval-label" class="property-label"><g:message code="flashcardInfo.interval.label" default="Interval" /></span>
					
						<span class="property-value" aria-labelledby="interval-label"><g:fieldValue bean="${flashcardInfoInstance}" field="interval"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${flashcardInfoInstance?.data1}">
				<li class="fieldcontain">
					<span id="data1-label" class="property-label"><g:message code="flashcardInfo.data1.label" default="Data1" /></span>
					
						<span class="property-value" aria-labelledby="data1-label"><g:fieldValue bean="${flashcardInfoInstance}" field="data1"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${flashcardInfoInstance?.data2}">
				<li class="fieldcontain">
					<span id="data2-label" class="property-label"><g:message code="flashcardInfo.data2.label" default="Data2" /></span>
					
						<span class="property-value" aria-labelledby="data2-label"><g:fieldValue bean="${flashcardInfoInstance}" field="data2"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${flashcardInfoInstance?.data3}">
				<li class="fieldcontain">
					<span id="data3-label" class="property-label"><g:message code="flashcardInfo.data3.label" default="Data3" /></span>
					
						<span class="property-value" aria-labelledby="data3-label"><g:fieldValue bean="${flashcardInfoInstance}" field="data3"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:flashcardInfoInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${flashcardInfoInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
