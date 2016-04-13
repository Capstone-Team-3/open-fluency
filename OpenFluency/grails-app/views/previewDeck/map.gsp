<%@ page import="com.openfluency.deck.PreviewDeck" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'previewDeck.label', default: 'PreviewDeck')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
		
		<style>
		
		#show-previewDeck {	
		    width: 25%;
		    float: left;
		    word-wrap: break-word;
		    word-break: break-all;
		    margin-left: 20px;
		}
		
		#show-previewDeck ul {
			list-style: none;
			padding: 0px;
		}
		
		#show-previewDeck ul li {
			margin-bottom: 20px;
			border-left: 5px solid #b3ff99;
			padding-left: 5px;
		}
		
		.map-edit-submit-button {
			margin-top: 15px;	
		}
		
		
		</style>
		
		
	</head>
	<body>
		  <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
		<div id="show-previewDeck" class="content scaffold-show" role="main">
			<h1>Preview Deck</h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ul class="property-list previewDeck">
			
				<li class="fieldcontain">
					<h4><strong>Deck Name</strong> <button class="btn btn-xs btn-info" id="edit-preview-deck-name">Edit</button></h4>
					<span id="map-preview-deck-name" class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${previewDeckInstance}" field="name"/></span>
				</li>
			
				<li class="fieldcontain">
					<h4><strong>Description</strong> <button class="btn btn-xs btn-info" id="edit-preview-deck-description">Edit</button></h4>
					<span id="map-preview-deck-description" class="property-value" aria-labelledby="description-label"><g:fieldValue bean="${previewDeckInstance}" field="description"/></span>
				</li>
			
				<li class="fieldcontain">
					<h4><strong>Filename</strong></h4>
					<span class="property-value" aria-labelledby="filename-label"><g:fieldValue bean="${previewDeckInstance}" field="filename"/></span>
				</li>
						
				<li class="fieldcontain">
					<h4><strong>Owner</strong></h4>
					<span class="property-value" aria-labelledby="owner-label"><g:link controller="user" action="show" id="${previewDeckInstance?.owner?.id}">${previewDeckInstance?.owner?.encodeAsHTML()}</g:link></span>
				</li>
			
			</ul>
		</div>
		
		
		<!-- Edit name modal -->
		<div id="map-edit-name-modal" class="modal fade" role="dialog">
		  <div class="modal-dialog modal-sm">
		
		    <!-- Modal content-->
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal">&times;</button>
		        <h4 class="modal-title">Edit Deck Name</h4>
		      </div>
		      <div class="modal-body">
		        <label for="map-input-deck-name">Name:</label>
				<input type="text" class="form-control" id="map-input-deck-name">
				<button class="btn btn-sm btn-success map-edit-submit-button" id="map-change-deck-name-submit" data-dismiss="modal">Submit</button>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
		      </div>
		    </div>
		
		  </div>
		</div>
		
		
				
		<!-- Edit description modal -->
		<div id="map-edit-description-modal" class="modal fade" role="dialog">
		  <div class="modal-dialog modal-sm">
		
		    <!-- Modal content-->
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal">&times;</button>
		        <h4 class="modal-title">Edit Deck Description</h4>
		      </div>
		      <div class="modal-body">
	        	 	<label for="map-input-deck-name">Description:</label>
					<input type="text" class="form-control" id="map-input-deck-description">
					<button class="btn btn-sm btn-success map-edit-submit-button" id="map-change-deck-description-submit" data-dismiss="modal">Submit</button>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
		      </div>
		    </div>
		
		  </div>
		</div>
		
		<g:javascript>
			window.unitMappingPreviewDeckName = '${previewDeckInstance.name }';
			window.unitMappingPreviewDeckDescription = '${previewDeckInstance.description }';
		</g:javascript>
		
		<g:render template="select" />  <!--  renders units + card display -->
		<div class="spinner"></div>
	</body>
</html>
