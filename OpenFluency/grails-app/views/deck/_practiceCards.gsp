<%@ page import="com.openfluency.Constants" %>

    <div class="row">
        <div class="col-lg-12">
            <div class="deck-header text-center">
                <h3>Practice by ${Constants.CARD_ELEMENTS[rankingType as Integer]}</h3>
                <g:if test="${chapterInstance}">                    
                    <h1>${chapterInstance?.title}</h1>
                </g:if>
                <g:else>
                    <h1>${practiceDeckInstance.title}</h1>
                </g:else>

            </div><!-- end deck-heaer -->

            <g:render template="/customization/customize" model="[flashcardInstance: cardUsageInstance?.flashcard]"/>
        </div><!-- end col-leg-12 -->
    </div><!-- end row -->

    <div class="practice-body row">
        <div class="col-lg-4 col-lg-offset-4">
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

            %{-- <div class="history">
                <h4>History</h4>
                <p>Last time you ranked this card as:</p>
                <p>Meaning: ${cardRankingInstance?.meaningRanking ? Constants.DIFFICULTIES[cardRankingInstance?.meaningRanking] : "Unranked"}</p>
                <p>Pronunciation: ${cardRankingInstance?.pronunciationRanking ? Constants.DIFFICULTIES[cardRankingInstance?.pronunciationRanking] : "Unranked"}</p>
            </div> --}%
        </div><!-- end col-lg-4 -->
    </div><!-- end row -->

    <div class="progress-container row">
        <div class="col-lg-2">
            <h4>Progress:</h4>
        </div>
        <div class="col-lg-5">
            <strong>Practice by Meaning</strong>
            <g:render template="/deck/progress" model="[progress: practiceDeckInstance.progress[Constants.MEANING]]"/>
        </div>
        <div class="col-lg-5">
            <strong>Practice by Pronunciation</strong>
            <g:render template="/deck/progress" model="[progress: practiceDeckInstance.progress[Constants.PRONUNCIATION]]"/>
        </div>
    </div>