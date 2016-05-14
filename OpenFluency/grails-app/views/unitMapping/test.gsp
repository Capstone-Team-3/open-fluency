<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"/>

	<style>
	.ui-state-hover {
		border: 3px dashed black;
	}
</style>
		
	
</head>
<body>
<%@ page import="com.openfluency.media.Audio" %>

<!-- <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css"> -->


<g:render template="select" test="${test}" />

	

<g:javascript src="jquery-ui.min.js"/>
<g:javascript>
    initializeUnitMappingDraggable();
</g:javascript>

</body>
</html>