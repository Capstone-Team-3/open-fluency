<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"/>
	<g:javascript src="jquery.mousewheel.js"/>
	<g:javascript src="jquery.easing.1.3.js"/>
	<g:javascript src="jquery.contentcarousel.js"/>
	
	
</head>
<body>
	<div class="container chapter-show">

		<div class="chapter-header">
			<g:link action="show" controller="course" id="${chapterInstance.course.id}">${chapterInstance.course.title}</g:link>
			<h1>${chapterInstance.title}</h1>
			<p>${chapterInstance.description}</p>
		</div>
		
		<div id="ca-container" class="ca-container">
			<div class="ca-wrapper">
				<g:set var="counter" value="${0}" />
				<g:each  in="${chapterInstance.deck.flashcards}">
					<g:set var="counter" value="${counter + 1}" />
						<div class="ca-item ca-item-${counter}">
							<div class="col-lg-3">
								<g:render template="/flashcard/flashcard" model="[flashcardInstance: it]"/>
							</div><!-- end col-lg-3 -->
						</div><!-- ca-item -->
				</g:each>
			</div><!-- ca-wrapper -->
		</div><!-- ca-container -->
	 </div> 
	
	<g:javascript>
		$(function()
		{
			$('#ca-container').contentcarousel();
		})
	</g:javascript>
	
</body>
</html>