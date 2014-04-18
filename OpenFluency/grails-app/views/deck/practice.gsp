<%@ page import="com.openfluency.Constants" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>OpenFluency</title>
</head>
<body>
    <div class="container deck-practice">
        <div class="row">
            <ul class="breadcrumb">
                <li>
                    <a href="${createLink(uri:'/') }">Home</a>
                </li>
                <li>Decks</li>
                <li>
                    <g:link action="search" controller="deck" >Search for Decks</g:link>
                </li>
                <li>
                    <g:link action="show" controller="deck" id="${deckInstance.id}">${deckInstance.title}</g:link>
                </li>
                <li>
                    <a href="#">Practice</a>
                </li>
            </ul>

            <div class="col-lg-12">
                <div class="deck-header text-center">
                    <h1>Deck: ${deckInstance.title}</h1>
                </div>
            </div>
            <!-- end deck-header --> </div>

        <div class="row">
            <div class="col-lg-4 col-lg-offset-4">
                <div class="progress">
                    <div class="progress-bar" role="progressbar" aria-valuenow="${deckInstance.progress}" aria-valuemin="0" aria-valuemax="100" style="width:${deckInstance.progress}%">${deckInstance.progress}%</div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-lg-12">
                <g:render template="/flashcard/flashcard" model="[flashcardInstance: cardUsageInstance?.flashcard]"/>

                <g:form id="${deckInstance?.id}" controller="deck" action="practice" name="rankCardForm" class="rankCardForm form">
                    <input type="hidden" name="cardUsageId" value="${cardUsageInstance?.id}"/>
                    <input type="hidden" id="ranking" name="ranking" value="1"/>
                </g:form>

                <div class="center">
                    <button id="easy" class="btn btn-success ranker" data-value="${Constants.EASY}">Easy</button>
                    <button id="medium" class="btn btn-warning ranker" data-value="${Constants.MEDIUM}">Medium</button>
                    <button id="hard" class="btn btn-danger ranker" data-value="${Constants.HARD}">Hard</button>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- end container -->

<g:javascript>
    $(function(){
    var $meaningContainer = $('.meaning');
    var meaning = $meaningContainer.html();
    $meaningContainer.html('<button class="btn" id="show-meaning">Show Meaning</button>');
    $('#show-meaning').on('click', function() { 
        $meaningContainer.html(meaning);
    });

    $(".ranker").click(function() {
        // Set the ranking value in the form and submit it
        $("#ranking").val($(this).data('value'));
        $('.rankCardForm').submit();
    });
})
</g:javascript>
</body>
</html>