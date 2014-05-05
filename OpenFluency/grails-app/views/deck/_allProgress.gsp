<%@ page import="com.openfluency.Constants" %>
<div class="donut-container">
    <div class="col-lg-4 progress-donut center" data-progress="${progress[Constants.MEANING]}" id="meaning-progress-${id}">
        <p>${deckInstance.language} to ${deckInstance.sourceLanguage}</p>
    </div>
    <div class="col-lg-4 progress-donut center" data-progress="${progress[Constants.SYMBOL]}" id="symbol-progress-${id}">
        <p>${deckInstance.sourceLanguage} to ${deckInstance.language}</p>
    </div>
    <div class="col-lg-4 progress-donut center" data-progress="${progress[Constants.PRONUNCIATION]}" id="pronunciation-progress-${id}">
        <p>Pronunciations in ${deckInstance.language}</p>
    </div>
</div>