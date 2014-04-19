
<%@ page import="com.openfluency.media.Customization" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'customization.label', default: 'Customization')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-customization" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-customization" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			<thead>
					<tr>
					
						<th><g:message code="customization.owner.label" default="Owner" /></th>
					
						<th><g:message code="customization.card.label" default="Card" /></th>
					
						<th><g:message code="customization.audioAssoc.label" default="Audio Assoc" /></th>
					
						<th><g:message code="customization.imageAssoc.label" default="Image Assoc" /></th>
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${customizationInstanceList}" status="i" var="customizationInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${customizationInstance.id}">${fieldValue(bean: customizationInstance, field: "owner")}</g:link></td>
					
						<td>${fieldValue(bean: customizationInstance, field: "card")}</td>
					
						<td>${fieldValue(bean: customizationInstance, field: "audioAssoc")}</td>
					
						<td>${fieldValue(bean: customizationInstance, field: "imageAssoc")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${customizationInstanceCount ?: 0}" />
			</div>
		</div>
	</body>
</html>
