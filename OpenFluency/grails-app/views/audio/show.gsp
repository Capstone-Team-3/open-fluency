
<%@ page import="com.openfluency.media.Audio" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'audio.label', default: 'Audio')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-audio" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-audio" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<ol class="property-list audio">
			
				<g:if test="${audioInstance?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="audio.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${audioInstance?.dateCreated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${audioInstance?.lastUpdated}">
				<li class="fieldcontain">
					<span id="lastUpdated-label" class="property-label"><g:message code="audio.lastUpdated.label" default="Last Updated" /></span>
					
						<span class="property-value" aria-labelledby="lastUpdated-label"><g:formatDate date="${audioInstance?.lastUpdated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${audioInstance?.owner}">
				<li class="fieldcontain">
					<span id="owner-label" class="property-label"><g:message code="audio.owner.label" default="Owner" /></span>
					
						<span class="property-value" aria-labelledby="owner-label"><g:link controller="user" action="show" id="${audioInstance?.owner?.id}">${audioInstance?.owner?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${audioInstance?.pronunciation}">
				<li class="fieldcontain">
					<span id="pronunciation-label" class="property-label"><g:message code="audio.pronunciation.label" default="Pronunciation" /></span>
					
						<span class="property-value" aria-labelledby="pronunciation-label"><g:link controller="pronunciation" action="show" id="${audioInstance?.pronunciation?.id}">${audioInstance?.pronunciation?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${audioInstance?.url}">
				<li class="fieldcontain">
					<span id="url-label" class="property-label"><g:message code="audio.url.label" default="Url" /></span>
					
						<span class="property-value" aria-labelledby="url-label"><g:fieldValue bean="${audioInstance}" field="url"/></span>
					
				</li>
				</g:if>

				<g:if test="${audioInstance?.audioWAV}">
				<li class="fieldcontain">
					</br>
					<audio id="audioClip" src="${audioInstance?.audioWAV}" type="audio/wav"></audio>
					</br>
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:audioInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${audioInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
