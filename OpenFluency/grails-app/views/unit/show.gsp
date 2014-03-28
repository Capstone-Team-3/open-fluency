<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="layout" content="main"/>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-lg-6 col-lg-offset-3">
				<div class="panel panel-default">
					<h1 class="unit-large">${unitInstance?.print}</h1>
					<table class="table">
						<tr>
							<th>Alphabet</th>
							<td>${unitInstance?.alphabet.name}</td>
						</tr>
						<tr>
							<th>Literal Encoded</th>
							<td>${unitInstance?.print}</td>
						</tr>
						<tr>
							<th>Literal</th>
							<td>${unitInstance?.literal}</td>
						</tr>
						<tr>
							<th>Grade</th>
							<td>${unitInstance?.grade}</td>
						</tr>
						<tr>
							<th>Frequency</th>
							<td>${unitInstance?.frequency}</td>
						</tr>
						<tr>
							<th>Stroke Count</th>
							<td>${unitInstance?.strokeCount}</td>
						</tr>
					</table>
					<div class="panel-footer center">
						<g:form action="create" controller="flashcard" name="createFlashcardForm">
							<input type="hidden" name="unit" value="${unitInstance.id}"/>
							<button class="btn btn-info" type="submit">Create Flashcard</button>
						</g:form>
					</div>
				</div>
			</div>
			<div class="col-lg-12">
				<g:each in="${unitInstance.mappings}">
					<div class="col-lg-4">
						<div class="panel panel-default">
							<g:link action="show" id="${it.id}" controller="unit">
								<h1 class="unit-large">${it?.print}</h1>
							</g:link>
							<table class="table">
								<tr>
									<th>Alphabet</th>
									<td>${it?.alphabet.name}</td>
								</tr>
								<tr>
									<th>Literal Encoded</th>
									<td>${it?.print}</td>
								</tr>
								<tr>
									<th>Literal</th>
									<td>${it?.literal}</td>
								</tr>
								<tr>
									<th>Grade</th>
									<td>${it?.grade}</td>
								</tr>
								<tr>
									<th>Frequency</th>
									<td>${it?.frequency}</td>
								</tr>
								<tr>
									<th>Stroke Count</th>
									<td>${it?.strokeCount}</td>
								</tr>
							</table>
						</div>
					</div>
				</g:each>
			</div>
			<div class="col-lg-12">
				<h2>Pronunciations</h2>
				<g:each in="${unitInstance.pronunciations}">
					<div class="col-lg-4">
						<div class="panel panel-default">
							<table class="table">
								<tr>
									<th>Alphabet</th>
									<td>${it?.alphabet.name}</td>
								</tr>
								<tr>
									<th>Literal</th>
									<td>${it?.literal}</td>
								</tr>
							</table>
						</div>
					</div>
				</g:each>
			</div>
		</div>
	</div>
</body>
</html>