<%@ page import="com.openfluency.Constants" %>

<div class="row">
    <div class="deck-header text-center">
        <g:if test="${chapterInstance}">
            <h1>${chapterInstance?.title}</h1>
        </g:if>
        <g:else>
            <h1>${practiceDeckInstance.title}</h1>
        </g:else>

        <g:if test="${Constants.CARD_ELEMENTS[rankingType as Integer] == "Meaning"}">
            <h2 class="h4">Practice ${practiceDeckInstance?.language} to ${practiceDeckInstance?.sourceLanguage}</h2>
        </g:if>
        <g:elseif test="${Constants.CARD_ELEMENTS[rankingType as Integer] == "Symbol"}">
            <h2 class="h4">Practice ${practiceDeckInstance?.language} to ${practiceDeckInstance?.sourceLanguage}</h2>
        </g:elseif>
        <g:else>
            <h2 class="h4">Practice ${practiceDeckInstance?.language} Pronunciation</h2>
        </g:else>
    </div>
    <!-- end deck-heaer -->

    <g:render template="/customization/customize" model="[flashcardInstance: cardUsageInstance?.flashcard]"/>
</div>
<!-- end row -->

<div class="practice-body row">

<g:render template="/dictionary/dictionaryTable"/>


    <div class="practice-flashcard center-block">
        <g:render template="/flashcard/flashcard" model="[flashcardInstance: cardUsageInstance?.flashcard, practicing: true, imageURL: imageURL, audioSysId: audioSysId]"/>

        <g:form id="${id}" controller="${controller}" action="practice" name="rankCardForm" class="rankCardForm form">
            <input type="hidden" name="cardUsageId" value="${cardUsageInstance?.id}"/>
            <input type="hidden" id="ranking" name="ranking" value="1"/>
            <input type="hidden" name="rankingType" value="${rankingType}"/>
        </g:form>

        <div class="ranking-container btn-group center-block">
            <button id="easy" class="btn btn-success ranker" data-value="${Constants.EASY}">Easy</button>
            <button id="medium" class="btn btn-warning ranker" data-value="${Constants.MEDIUM}">Medium</button>
            <button id="hard" class="btn btn-danger ranker" data-value="${Constants.HARD}">Hard</button>
        </div>

    </div>
    <!-- end col-lg-4 -->
</div>
<!-- end row -->


<div class="progress-container row">
    <div class="col-lg-12">
        <p class="col-lg-1"><strong>Progress:</strong></p>
        <g:if test="${rankingType == Constants.MEANING.toString()}">
            <g:render class="col-lg-11" template="/deck/progress" model="[progress: practiceDeckInstance.progress[Constants.MEANING]]"/>
        </g:if>
        <g:elseif test="${rankingType == Constants.PRONUNCIATION.toString()}">
            <g:render template="/deck/progress" model="[progress: practiceDeckInstance.progress[Constants.PRONUNCIATION]]"/>
        </g:elseif>
        <g:elseif test="${rankingType == Constants.SYMBOL.toString()}">
            <g:render template="/deck/progress" model="[progress: practiceDeckInstance.progress[Constants.SYMBOL]]"/>
        </g:elseif>
    </div>
</div> 


<script>
// set height for dictionary table to match height of card
$(document).ready(function() {
	$('#dictionary-results-table').css('max-height', $('.panel-body')[0].clientHeight);
});


</script>


