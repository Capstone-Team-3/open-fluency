
<%@ page import="com.openfluency.deck.PreviewDeck" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'previewDeck.label', default: 'PreviewDeck')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-previewDeck" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
                <!--
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
                -->
			</ul>
		</div>
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
                <g:each in="${previewCardInstanceList}" status="i" var="previewCardInstance">
                    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                        
                   <td><g:link action="show" id="${previewCardInstance.id}">${fieldValue(bean: previewCardInstance, field: "id")}</g:link></td>        
                    
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
            
                </ol>
                    </tr>
                </g:each>

			<g:form url="[resource:previewDeckInstance, action:'importDeck']" method="POST">
				<fieldset class="buttons">
					<g:actionSubmit class="edit" action="importDeck" value="${message(code: 'default.button.save.label', default: 'Import')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
			<g:form url="[resource:previewDeckInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${previewDeckInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
