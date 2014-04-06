<!DOCTYPE html> 
<html>
<head>
    <meta name="layout" content="main"/>
    <title>OpenFluency</title>
</head>
<body>
    <div class="container chapter-practice">
        
        <div class="chapter-header text-center">
            <h1>
                Deck: ${chapterInstance.title}
            </h1>
        </div><!-- end deck-header -->

        <g:render template="/flashcard/flashcard"/>
        
        <div class="pagination center-block text-center">
            <g:paginate controller="chapter" action="practice" maxsteps="0" id="${chapterInstance.id}" total="${flashcardCount ?: 0}" />
        </div>
    </div><!-- end container -->
</body>
</html>