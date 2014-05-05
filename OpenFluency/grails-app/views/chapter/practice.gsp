<%@ page import="com.openfluency.Constants" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>OpenFluency</title>
</head>
<body>
    <div class="container chapter-practice" id="practice-container" data-rank-type="${Constants.CARD_ELEMENTS[rankingType as Integer]}">
        <div class="row">
            <ul class="breadcrumb">
                <li>
                    <a href="${createLink(uri:'/') }">Home</a>
                </li>
                <li>
                    <g:link action="search" controller="course">Courses</g:link>
                </li>
                <li>
                    <g:link action="search" controller="course">Search for Course</g:link>
                </li>
                <li>
                    <g:link action="show" controller="course" id="${chapterInstance.course.id}">
                        ${chapterInstance.course.getCourseNumber()}: ${chapterInstance.course.title}
                    </g:link>
                </li>
                <li>
                    <g:link action="show" controller="chapter" id="${chapterInstance.id}">${chapterInstance.title}</g:link>
                </li>
                <li>
                    <g:if test="${Constants.CARD_ELEMENTS[rankingType as Integer] == "Meaning"}">
                        <a href="#">Practice ${chapterInstance?.deck?.language} to ${chapterInstance?.deck?.sourceLanguage}</a>
                    </g:if>
                    <g:elseif test="${Constants.CARD_ELEMENTS[rankingType as Integer] == "Symbol"}">
                        <a href="#">Practice ${chapterInstance?.deck?.sourceLanguage} to ${chapterInstance?.deck?.language}</a>
                    </g:elseif>
                    <g:else>
                        <a href="#">Practice ${chapterInstance?.deck?.language} Pronunciation</a>
                    </g:else>
                </li>
            </ul>
        </div>
        <g:render template="/deck/practiceCards" model="[id: chapterInstance.id, practiceDeckInstance: chapterInstance.deck, cardUsageInstance: cardUsageInstance, controller: 'chapter', imageURL: imageURL, audioSysId: audioSysId]"/>
    </div>
    
    <!-- end container -->
    <g:javascript>initializePracticeCards();</g:javascript>

</body>
</html>