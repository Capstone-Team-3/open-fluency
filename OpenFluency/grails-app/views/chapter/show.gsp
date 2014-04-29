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

		<div class="chapter-header">
			<h1>
				${chapterInstance.title}
				<g:if test="${isOwner}">
					<g:link action="edit" id="${chapterInstance?.id}" class="btn btn-sm btn-warning">Edit</g:link>
				</g:if>
			</h1>
			<p>${chapterInstance.description}</p>
			<g:if test="${chapterInstance.deck.flashcardCount >
				0 && Registration.findAllByCourseAndUser(chapterInstance.course, userInstance)}">
				<g:link class="btn btn-success" action="practice" id="${chapterInstance.id}" controller="chapter" params="[rankingType: Constants.MEANING]">Practice Meanings</g:link>
				<g:link class="btn btn-success" action="practice" id="${chapterInstance.id}" controller="chapter" params="[rankingType: Constants.PRONUNCIATION]">Practice Pronunciations</g:link>
			</g:if>
			<g:if test="${isOwner}">
				<g:link class="btn" action="search" controller="unit" params="${[deckId: chapterInstance.deck.id, 'filter-alph': chapterInstance.deck.language.id]}">Add Flashcards</g:link>
			</g:if>
		</div>
		<!-- end chapter-header -->

		<div class="row">
			<g:each in="${flashcardInstanceList}">
				<div class="col-lg-3">
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

</body>
</html>