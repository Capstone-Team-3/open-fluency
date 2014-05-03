<%@ page import="com.openfluency.Constants" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>OpenFluency</title>
</head>
<body>
    <div class="container deck-practice" id="practice-container" data-rank-type="${Constants.CARD_ELEMENTS[rankingType as Integer]}">
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
                    <a href="#">Practice - ${Constants.CARD_ELEMENTS[rankingType as Integer]}</a>
                </li>
            </ul>
        </div>
        <g:render template="/deck/practiceCards" model="[id: deckInstance.id, practiceDeckInstance: deckInstance, cardUsageInstance: cardUsageInstance, controller: 'deck', imageURL: imageURL, audioSysId: audioSysId]"/>
    </div>
    <!-- end container -->
    <g:javascript>initializePracticeCards();</g:javascript>
</body>
</html>