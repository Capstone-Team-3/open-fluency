<%@ page import="com.openfluency.Constants" %>

<div class="row">
    <g:render template="/customization/customize" model="[flashcardInstance: cardUsageInstance?.flashcard]"/>
    <button id="customizationBtn" class="btn btn-info">Customize?</button>
</div>
<g:javascript>
    $('#customizationBtn').click(function(){ 
        $('#customize-container').show();
        $('#customizationBtn').hide();
    });
    $('#customizationCreate').click(function(){
        $('#customize-container').hide();
        $('#customizationBtn').show();
    });
</g:javascript>

<div class="row">
    <div class="col-lg-12">
        <div class="deck-header text-center">
            <h1>${deckInstance.title}</h1>
        </div>
    </div>
</div>

<div class="row">
    <div class="col-lg-4 col-lg-offset-4">
        <g:render template="/deck/progress" model="[progress: deckInstance.progress]"/>
    </div>
</div>

<div class="row">
    <div class="col-lg-12">
        <g:render template="/flashcard/flashcard" model="[flashcardInstance: cardUsageInstance?.flashcard, practicing: true]"/>

        <g:form id="${id}" controller="${controller}" action="practice" name="rankCardForm" class="rankCardForm form">
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

