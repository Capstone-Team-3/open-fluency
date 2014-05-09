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
                    <g:if test="${Constants.CARD_ELEMENTS[rankingType as Integer] == "Meaning"}">
                        <a href="#">Practice ${deckInstance?.language} to ${deckInstance?.sourceLanguage}</a>
                    </g:if>
                    <g:elseif test="${Constants.CARD_ELEMENTS[rankingType as Integer] == "Symbol"}">
                        <a href="#">Practice ${deckInstance?.sourceLanguage} to ${deckInstance?.language}</a>
                    </g:elseif>
                    <g:else>
                        <a href="#">Practice ${deckInstance?.language} Pronunciation</a>
                    </g:else>
                </li>
            </ul>
        </div>
        <g:render template="/deck/practiceCards" model="[id: deckInstance.id, practiceDeckInstance: deckInstance, cardUsageInstance: cardUsageInstance, controller: 'deck', imageURL: imageURL, audioSysId: audioSysId]"/>
    </div>
    <!-- end container -->
    <g:javascript>
        initializePracticeCards();
    </g:javascript>
</body>
</html>