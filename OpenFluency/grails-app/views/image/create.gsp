<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'image.label', default: 'Image')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#create-image" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="create-image" class="content scaffold-create" role="main">
			<h1><g:message code="default.create.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${imageInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${imageInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>
			<g:form url="[resource:imageInstance, action:'save']" >
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>
				<fieldset class="buttons">
					<g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
				</fieldset>
			</g:form>
		</div>

		<div id="wrapper">

			<h1>Flickr Search Tester (Ajax)</h1>

			<g:form id="my_form">
				<label for="query">Query: </label>
				<input id="query" name="query" type="text" size="60" /><br /><br />
				<input id="flickr_search" name="flickr_search" type="button" value="Search" />
			</g:form>
			<!-- Results will be placed into the following container. -->
			<div id="results"></div>

		</div>

	</body>

	<g:javascript>
		$(function(){
			<!-- set helpful and need variables-->
			var apiKey = "ec50db25dd7a2b1d0c5d7b3ec404cce6";
			var sMethod = "flickr.photos.search";
			var respFormat = "&format=json&jsoncallback=?"
			var numPics = "5";
			var src;
			<!-- build the base of the query string-->
			var baseUrl = "https://api.flickr.com/services/rest/?api_key=" + apiKey + "&method=" + sMethod + "&sort=relevance&per_page=" + numPics + "&text=";
			
			<!-- add search buttin click handler -->
			$("#flickr_search").click(function(){
				<!--remove previous pics -->
				$("#results").empty();
				<!-- build query url-->
				var queryStr = baseUrl + $("#query").val() + respFormat;
				<!-- query the flickr api-->
				$.getJSON(queryStr, function(data){
					<!-- iterate through responses and display them -->
					$.each(data.photos.photo, function(i,item){
        				src = "http://farm" + item.farm + ".static.flickr.com/" + item.server + "/" + item.id + "_" + item.secret + "_m.jpg";
        				$("<img/>").attr("src", src).appendTo("#results");
        				<!-- click handler on the image -->
        				$("<img/>").click(function(){
        					$("#url").val(src);
        				});
        				if ( i >= numPics ) return false;
    				});
				});
			});
		});
	</g:javascript>

</html>
