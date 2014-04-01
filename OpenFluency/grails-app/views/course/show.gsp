<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"/>
</head>
<body>
	<div class="container">

		<div class="row">
			<div class="col-lg-6 col-lg-offset-3">
				<g:if test="${flash.message}">
					<div class="alert alert-info" style="display: block">${flash.message}</div>
				</g:if>
				<h1>${courseInstance.title}</h1>
				<p>${courseInstance.description}</p>
			</div>
			<!-- end col-lg-6 -->
		</div>
		<!-- end row -->

		<div class="row">
			<div class="col-lg-12">
				<g:each in="${courseInstance.chapters}">
					<div class="col-lg-3">
						<div class="panel panel-default">
							<div class="panel-body">
								<h3>
									<g:link action="show" id="${it.id}" controller="chapter">${it.title}</g:link>
								</h3>
								<p>${it.deck.flashcards.size()} Flashcards</p>
							</div>
						</div>
					</div>
					<!-- end col-lg-3 -->
				</g:each>
			</div>
		</div>
		<!-- end row -->

		<g:if test="${isOwner}">
			<!-- This is only displayed for the owner of the course -->
			<div class="row">
				<div class="col-lg-12">
					<g:link action="create" controller="chapter" id="${courseInstance.id}">Add Chapters</g:link>
				</div>
			</div>
		</g:if>

		<sec:ifAllGranted roles="ROLE_STUDENT">
			<!-- Displayed for students - need to check if the student is already enrolled in this course -->
			<div class="row">
				<div class="col-lg-12">
					<g:link class="btn btn-info" action="enroll" controller="course" id="${courseInstance.id}">Enroll!</g:link>
				</div>
			</div>
		</sec:ifAllGranted>

	</div>
	<!-- end container -->
</body>
</html>