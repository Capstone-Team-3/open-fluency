<%@ page import="com.openfluency.Constants" %>
<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"/>
	
	
	<style>
	
	
	d change the background color on hover.


h2 {
  font: 400 40px/1.5 Helvetica, Verdana, sans-serif;
  margin: 0;
  padding: 0;
}

#deck-list-ul {
  list-style-type: none;
  margin: 0;
  padding: 0;
}

.deck-list-li {
  font: 200 20px/1.5 Helvetica, Verdana, sans-serif;
  border-bottom: 1px solid #ccc;
}

.deck-list-li:last-child {
  border: none;
}

.deck-list-li {
  text-decoration: none;
  color: #000;
  display: block;

  -webkit-transition: font-size 0.3s ease, background-color 0.3s ease;
  -moz-transition: font-size 0.3s ease, background-color 0.3s ease;
  -o-transition: font-size 0.3s ease, background-color 0.3s ease;
  -ms-transition: font-size 0.3s ease, background-color 0.3s ease;
  transition: font-size 0.3s ease, background-color 0.3s ease;
}

.deck-list-li:hover {
  font-size: 21px;
  background: #b3e6ff;
}


.card-selected {
	border-right: 1px solid black;
	border-left: 1px solid black;
	background: lavender;
}

#selected-cards-display {
	background: lavender;
	display: none;
}

#selected-cards-display h4{
	text-align: center;
}

#selectedCardsMenu {
	list-style-type: none;
	font-size: 24px;
	padding: 10px;
}

#selectedCardsMenu li {
	text-align: center;
}

.deck-actions-2 {
	margin-top: 15px;
}


	</style>
	
</head>
<body>

	<div class="container deck-show">

		<ul class="breadcrumb" id="breadcrumb">
			<li>
				<a href="${createLink(uri:'/') }">Home</a>
			</li>
			<li>Decks</li>
			<li>
				<g:link action="search" controller="deck" >Search for Decks</g:link>
			</li>
			<li>
				<a href="#">${deckInstance?.title}</a>
			</li>
		</ul>

		<div class="row">
			<div class="col-lg-12">
				<div class="deck-header text-center center-block">
					<h1 class="deck-title">
						${deckInstance?.title}
						<g:if test="${isOwner}">
							<g:link action="edit" id="${deckInstance.id}" elementId="edit-deck" class="btn btn-warning">
								<span class="glyphicon glyphicon-pencil"></span>
							</g:link>
							<g:link action="delete" id="${deckInstance.id}" class="btn btn-danger">
								<span class="glyphicon glyphicon-trash"></span>
							</g:link>
						</g:if>
					</h1>
					<p class="deck-description">${deckInstance?.description}</p>
					
				</div>
			</div>

			<div class="col-lg-4 col-lg-offset-4" id="show-display">
				<g:render template="/deck/allProgress" model="[deckInstance: deckInstance, progress: deckInstance.progress, id: deckInstance.id]"/>
				<g:render template="/deck/selectedCardsDisplay" />
				
				
				<br/>
				
				<div class="deck-actions center">
					<g:if test="${flashcardCount}">
						<div class="btn-group text-left">
							<button type="button" class="btn btn-success dropdown-toggle" data-toggle="dropdown">
								Practice Flashcards
								<span class="caret"></span>
							</button>

							<ul class="dropdown-menu" role="menu">
								<li>
									<g:link action="practice" id="${deckInstance.id}" controller="deck" params="[rankingType: Constants.MEANING]">
										Practice ${deckInstance?.language} to ${deckInstance?.sourceLanguage}
									</g:link>
								</li>
								<li>
									<g:link action="practice" id="${deckInstance.id}" controller="deck" params="[rankingType: Constants.SYMBOL]">
										Practice ${deckInstance?.sourceLanguage} to ${deckInstance?.language}
									</g:link>
								</li>
								<li>
									<g:link action="practice" id="${deckInstance.id}" controller="deck" params="[rankingType: Constants.PRONUNCIATION]">
										Practice pronunciations of ${deckInstance?.language} words/characters
									</g:link>
								</li>
							</ul>
						</div>
						<!-- end btn-group -->
					</g:if>
					<div class="btn-group text-left">
						<g:if test="${isOwner}">
							<button type="button" class="btn btn-info dropdown-toggle" data-toggle="dropdown">
								Add Flashcards
								<span class="caret"></span>
							</button>
						</g:if>
						<ul class="text-left dropdown-menu" role="menu">
							<li>
								<g:link action="search" controller="unit" params="${['languageId': deckInstance.language.id, 'deckId': deckInstance.id]}">Search Flashcards</g:link>
							</li>
							<li>
								<a data-toggle="modal" data-target="#myModal">Load from CSV file</a>
							</li>
						</ul>
					</div>
					<div class="deck-actions center deck-actions-2">
						<div class="btn-group text-left">
							<button type="button" class="btn btn-success" id="move-cards-button">
								Move Cards
							</button>
						</div>
					</div>
				</div>
				
			</div>
		</div>

		<br/>
		<!-- end deck-header -->

		<div class="row">
			<g:each in="${flashcardInstanceList}">
				<div class="col-lg-3 flashcard-result">
					<g:render template="/flashcard/flashcard" model="[flashcardInstance: it, isOwner: isOwner]"/>
				</div>
				<!-- end col-lg-3 -->
			</g:each>
		</div>
		<!-- end row -->

		<!-- this is the panel that indicates progress through the deck, perhaps this should move to a template for decks too -->
		<div class="pagination center-block text-center">
			<g:paginate controller="deck" action="show" id="${deckInstance.id}" total="${flashcardCount ?: 0}" />
		</div>

		<div class="modal csv-modal fade" id="myModal">
			<div class="modal-dialog">
				<g:form action="loadFlashcardsFromCSV" id="${deckInstance.id}" enctype="multipart/form-data">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
							<h4 class="modal-title">Load Flashcards from CSV File</h4>
						</div>
						<div class="modal-body">
							<p>
								Upload a CSV file with your flashcard definitions. (You can download a sample CSV to see how the file is structured
								<a href="../../resources/testDeck.csv">here</a>
								.)
							</p>
							<input name="csvData" type="file" name="csvData"/>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
							<button type="submit" class="btn btn-primary">Submit</button>
						</div>
					</div>
				</g:form>
			</div>
		</div>
	</div>
<!-- Modal -->
<div id="myModal2" class="modal fade" role="dialog">
 	<div class="modal-dialog">
	
		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal">&times;</button>
			<h4 class="modal-title">Reassign Flashcard to deck</h4>
			</div>
			<div class="modal-body">
			    <div id='ul-container'>
			        <h2>
			            My Decks
			            <g:link action="create" controller="deck" class="btn btn-info">Create New Deck</g:link>
			        </h2>
			       <g:each in="${deckInstanceList}">
			        <div class="radio">
			              <label> <input type="radio" name="decks" class="decks-rb"  value="${it.id}"> ${it} </label>
			        </div>
			        </g:each>
			    </div>
			</div>
			<div class="modal-footer">
			<button type="button" class="btn btn-lg btn-default btn-info" data-dismiss="modal" id="reassign-submit">Submit</button>
			</div>
		</div>
	</div>
</div>

	<g:javascript>initializeAudio();initializeDonuts();of2FlashcardFontSize();</g:javascript>
	<g:javascript src="reassignCard.js" />
	<g:javascript src="jquery.sticky.js" />
</body>
</html>
