<%@ page import="com.openfluency.Constants" %>
<%@ page import="com.openfluency.course.Registration" %>
<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"/>
</head>
<body>
	<div class="container chapter-show">
		<ul class="breadcrumb">
			<li>
				<a href="${createLink(uri:'/') }">Home</a>
			</li>
			<li>
				<g:link action="search" controller="course" >Courses</g:link>
			</li>
			<li>
				<g:link action="search" controller="course" >Search for Course</g:link>
			</li>
			<li>
				<g:link action="show" controller="course" id="${chapterInstance.course.id}">
					${chapterInstance.course.getCourseNumber()}: ${chapterInstance.course.title}
				</g:link>
			</li>
			<li>
				<a href="#">${chapterInstance.title}</a>
			</li>
		</ul>

		<div class="row">
			<div class="col-lg-12">
				<div class="chapter-header">
					<h1 class="chapter-title">
						${chapterInstance.title}
						<g:if test="${isOwner}">
							<g:link action="edit" id="${chapterInstance?.id}" class="btn btn-warning">
								<span class="glyphicon glyphicon-pencil"></span>
							</g:link>
						</g:if>
					</h1>
					<p class="chapter-description">${chapterInstance.description}</p>
				</div>
			</div>

			<div class="col-lg-4 col-lg-offset-4">
				<g:render template="/deck/allProgress" model="[deckInstance: chapterInstance.deck, progress: chapterInstance.deck.progress, id: chapterInstance.deck.id]"/>
				<br/>
				<div class="center">
					<g:if test="${chapterInstance.deck.flashcardCount >
						0 && Registration.findAllByCourseAndUser(chapterInstance.course, userInstance)}">
						<div class="btn-group text-left">
							<button type="button" class="btn btn-success dropdown-toggle" data-toggle="dropdown">
								Practice Flashcards
								<span class="caret"></span>
							</button>

							<ul class="dropdown-menu" role="menu">
								<li>
									<g:link action="practice" id="${chapterInstance.id}" controller="chapter" params="[rankingType: Constants.MEANING]">
										Practice ${chapterInstance.deck.language} to ${chapterInstance.deck.sourceLanguage}
									</g:link>
								</li>
								<li>
									<g:link action="practice" id="${chapterInstance.id}" controller="chapter" params="[rankingType: Constants.SYMBOL]">
										Practice ${chapterInstance.deck.sourceLanguage} to ${chapterInstance.deck.language}
									</g:link>
								</li>
								<li>
									<g:link action="practice" id="${chapterInstance.id}" controller="chapter" params="[rankingType: Constants.PRONUNCIATION]">
										Practice pronunciations of ${chapterInstance.deck.language} words/characters
									</g:link>
								</li>
							</ul>
						</div>
						<!-- end btn-group -->
					</g:if>
					<g:if test="${isOwner}">
						<g:link class="btn btn-info add-flashcard" action="search" controller="unit" params="${[deckId: chapterInstance.deck.id, 'filter-alph': chapterInstance.deck.language.id]}">Add Flashcards</g:link>
					</g:if>
				</div>
			</div>
			<!-- end chapter-header -->
		</div>
		<br/>

		<div class="row">
			<g:each in="${flashcardInstanceList}">
				<div class="col-lg-3 flashcard-result">
					<g:render template="/flashcard/flashcard" model="[flashcardInstance: it]"/>
				</div>
				<!-- end col-lg-3 -->
			</g:each>
		</div>
		<!-- end row -->

		<div class="pagination center-block text-center">
			<g:paginate controller="chapter" action="show"  id="${chapterInstance.id}" total="${flashcardCount ?: 0}" />
		</div>
	</div>
	<!-- end container -->
	<g:javascript>initializeAudio(); initializeDonuts();</g:javascript>
	

</body>
</html>