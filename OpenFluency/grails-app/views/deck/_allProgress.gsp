<%@ page import="com.openfluency.Constants" %>
<div class="donut-container">
    <div class="col-lg-4 progress-donut center" data-progress="${progress[Constants.MEANING]}" id="meaning-progress-${id}">
        <p>Meaning</p>
    </div>
    <div class="col-lg-4 progress-donut center" data-progress="${progress[Constants.PRONUNCIATION]}" id="pronunciation-progress-${id}">
        <p>Pronunciation</p>
    </div>
    <div class="col-lg-4 progress-donut center" data-progress="${progress[Constants.SYMBOL]}" id="symbol-progress-${id}">
        <p>Symbol</p>
    </div>
</div>