<!DOCTYPE html> 
<html>
<head>
	<meta name="layout" content="main"/>
	<title>OpenFluency</title>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-lg-6 col-lg-offset-3">

				<g:hasErrors bean="${flashcardInstance}">
					<ul class="errors" role="alert">
						<g:eachError bean="${flashcardInstance}" var="error">
							<li>
								<g:message error="${error}"/>
							</li>
						</g:eachError>
					</ul>
				</g:hasErrors>

				<h1>
					Create flashcard for ${unitInstance?.print} (id: ${unitInstance.id})
				</h1>

				<g:form action="save" controller="flashcard" name="createFlashcardForm">

					<input type="hidden" name="unit" value="${unitInstance.id}"/>

					<div class="form-group">
						<label>What meaning do you want to use?</label>
						<select name="unitMapping" class="form-control">
							<g:each in="${unitInstance.unitMappings}">
								<option value="${it.id}">
									${it.unit1.id == unitInstance.id ? it.unit2.print : it.unit1.print}
								</option>
							</g:each>
						</select>
					</div>

					<div class="form-group">
						<label>What pronunciation do you want to use?</label>
						<g:select class="form-control" name="pronunciation" from="${unitInstance.pronunciations}" noSelection="['':'-Choose a pronunciation-']" optionKey="id" optionValue="literal"/>
					</div>

					<div class="form-group">
						<label>What deck should this card go into?</label>
						<g:select class="form-control" name="deck" from="${userDecks}" noSelection="['':'-Choose a deck-']" optionKey="id" optionValue="title"/>
					</div>

					<div class="center">
						<br>
						<button class="btn btn-info">Create it!</button>
					</div>
				</g:form>
			</div>
		</div>
	</div>
</body>
</html>