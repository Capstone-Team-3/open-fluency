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
            <g:paginate controller="deck" action="practice" maxsteps="1" max="1" id="${deckInstance.id}" total="${flashcardCount ?: 0}" />
        </div>
    </div><!-- end container -->

    <script>
        var $meaningContainer = $('.meaning');
        var meaning = $meaningContainer.html();
        $meaningContainer.html('<button class="btn" id="show-meaning">Show Meaning</button>');
        $('#show-meaning').on('click', function() { 
            $meaningContainer.html(meaning);
        });
    </script>
</body>
</html>