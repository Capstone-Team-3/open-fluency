<!DOCTYPE html> 
<html>
<head>
    <meta name="layout" content="main"/>
    <title>OpenFluency</title>
</head>
<body>
    <div class="container deck-practice">
        
        <div class="deck-header text-center">
            <h1>
                Deck: ${deckInstance.title}
            </h1>
        </div><!-- end deck-header -->

        <g:render template="/flashcard/flashcard"/>
        
        <div class="pagination center-block text-center">
            <g:paginate controller="deck" action="practice" maxsteps="0" id="${deckInstance.id}" total="${flashcardCount ?: 0}" />
        </div>
    </div><!-- end container -->
</body>
</html>