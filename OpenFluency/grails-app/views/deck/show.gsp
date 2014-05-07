<%@ page import="com.openfluency.Constants" %>
<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"/>
</head>
<body>
	<div class="container deck-show">

		<ul class="breadcrumb">
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
							<g:link action="edit" id="${deckInstance.id}" class="btn btn-warning">
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

			<div class="col-lg-4 col-lg-offset-4">
				<g:render template="/deck/allProgress" model="[deckInstance: deckInstance, progress: deckInstance.progress, id: deckInstance.id]"/>
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
								<a href="https://s3.amazonaws.com/OpenFluency/resources/testDeck.csv">here</a>
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
	<g:javascript>initializeAudio();initializeDonuts();</g:javascript>
</body>
</html>