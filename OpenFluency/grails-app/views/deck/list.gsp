<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"/>

</head>
<body>
    
	<div class="container course-search">
		<ul class="breadcrumb">
            <li><a href="dashboard.html">Home</a></li>
            <li><a href="#">Decks</a></li>
            <li><a href="#">My Decks</a></li>
        </ul>
        <h2>My Decks  <g:link action="create" controller="deck" class="btn btn-primary">Create</g:link></h2>
		
		 <table class="table">
			<thead>
				<tr>
					<th>Title</th>
					<th>Description</th>
					<th>Flashcards</th>
				</tr>
			</thead>
			<g:each in="${deckInstanceList}">
				<tr>
					<td><g:link action="show" controller="deck" id="${it.id}">${it.title}</g:link></td>
					<td>${it.description}</td>
					<td>${it.flashcards.size()}</td>
				</tr>
			</g:each>
		 </table>
	</div>
			
	
</body>
</html>