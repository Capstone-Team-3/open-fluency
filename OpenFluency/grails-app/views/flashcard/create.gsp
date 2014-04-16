<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"/>
	<title>OpenFluency</title>
</head>
<body>
	<div class="container flashcard-create">
		<ul class="breadcrumb">
            <li><a href="${createLink(uri:'/') }">Home</a></li>
            <li>Decks</li>
            <li><a href="${createLink(uri:'/unit/search?filter-alph=1') }">Flashcard Search</a></li>
            <li><a href="#">Create Flashcard </a></li>
        </ul>
		<div class="row">
			<div class="col-lg-5">
				<g:hasErrors bean="${flashcardInstance}">
					<ul class="errors" role="alert">
						<g:eachError bean="${flashcardInstance}" var="error">
							<li>
								<g:message error="${error}"/>
							</li>
						</g:eachError>
					</ul>
				</g:hasErrors>

				<h1>Create flashcard for ${unitInstance?.print}</h1>

				<g:form action="save" controller="flashcard" name="createFlashcardForm">

					<input type="hidden" name="unit" value="${unitInstance.id}"/>

					<div class="form-group">
						<label class="control-label">What meaning do you want to use?</label>
						<select name="unitMapping" class="form-control">
							<g:each in="${unitInstance.unitMappings}">
								<option value="${it.id}">
									${it.unit1.id == unitInstance.id ? it.unit2.print : it.unit1.print}
								</option>
							</g:each>
						</select>
					</div>

					<div class="form-group">
						<label class="control-label">What pronunciation do you want to use?</label>
						<g:select class="form-control" name="pronunciation" from="${unitInstance.pronunciations}" optionKey="id" optionValue="literal"/>
					</div>

					<div class="form-group">
						<label class="control-label">What deck should this card go into?</label>
						<g:select class="form-control" name="deck" from="${userDecks}" optionKey="id" optionValue="title"/>
					</div>

					<div class="form-group">
						<label class="control-label">What image should be associated with this card (optional)?</label>
						<g:textField class="form-control" id="imageLink" name="imageLink" value="${flashcardInstance?.image}"/>
					</div>
					<g:if test="${flashcardInstance?.audio}">
						<div class="form-group">
							<label class="control-label">
								What audio clip provides pronunciation for this card (optional)?
							</label>
							<g:textField class="form-control" name="audio" value="${flashcardInstance?.audio}"/>
						</div>
					</g:if>
					<div class="audio">
						<label class="control-label">
							Create your own audio clip (optional)
							<a id="addaudio" class="btn btn-default">create new audio</a>
						</label>
					</div>
					<br></br>		
					<button class="center btn btn-success">Create it!</button>
				</g:form>
			</div>
			<div class="col-lg-7">
				<h1>Flickr Search</h1>
				<label for="query">Query:</label>
				<input id="query" name="query" type="text" size="60" placeholder="Type here to find your photo" />
				<button id="flickr_search">Search</button>
				<div id="results"></div>
				<button id="flickr_back">Back</button>
				<label id="flickr_page_number"></label>
				<button id="flickr_next">Next</button>
			</div>
		</div>
	</div>

	<g:javascript>
		$(function() {
		$("#flickr_search").click(function(){
			searchImage("#query", "#results", "#imageLink");
		});
	})
	</g:javascript>
	<g:javascript><!-- just an example of how to integrate the audio part -->
		$(function() {
		$('#addaudio').click(function(){
			$.ajax({
				url: "${g.createLink(action:'create', controller: 'audio')}",
					context: document.body
				}).done(function(data) 
				{
					$('.audio').append(data);
					$('.audio').append('<input id="start_button" name="start_button" type="button" value="Start Recording" />');
							
					$('.remove-audio').unbind('click').click(function()
						{
							$(this).parents('.panel').remove();
							return false;
						});
				});
			});
		})
</g:javascript>

	<g:javascript src="create_flashcard.js"/>


</body>
</html> 
