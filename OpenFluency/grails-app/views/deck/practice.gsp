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
                <h1>
                    Deck: ${deckInstance.title}
                </h1>
                <g:link action="index" id="1" controller="unit">Add Flashcards</g:link>
            </div><!-- end col-lg-6 -->
        </div><!-- end row -->

        <div class="col-lg-3">
            <g:render template="/flashcard/flashcard"/>
        </div><!-- end col-lg-3 -->
        
        <div class="pagination">
            <g:paginate controller="deck" action="practice" id="${deckInstance.id}" total="${flashcardCount ?: 0}" />
        </div>
    </div><!-- end container -->
</body>
</html>