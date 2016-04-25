<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"/>
</head>
<body>
    <g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
	</g:if>
	<div class="container">
    <h1>
        Learning Summary - Words Practiced 
    </h1>
	<form>
	<select name="course">
        <option value="">All Decks</option>
		<g:each in="${courses}">
             <g:if test="${it.course.id == params.int('course')}">
                  <option value="${it.course.id}" selected> ${it.course.title} </option>
             </g:if>
             <g:else>
                <option value="${it.course.id}">${it.course.title} </option>
             </g:else>
        </g:each>
	</select>
    <!--
	<g:select class="form control" name="course"
	  from="${courses}" value="${params.course}"
	  noSelection="['':'All Decks']"
	  optionKey="courseId" optionValue="${course?.title}"/>
    -->
	<g:submitButton name="Course" value="Filter"/>
	</form>
    <table class="summarytable" width="100%">
        <col/><col/>
        <col style='background:#f2dede;'/><col style='background:#fcf8e3;'/><col style='background:#dff0d8;'/>
        <g:set var="level" value='["Practice Mode","Unrated","hard","medium","easy"]'></g:set>
        <thead>
        <g:each in="${level}">
            <th> ${it} </th>
        </g:each>
        </thead>

        <tbody>
        <g:if test="${meaningSummary}">
          <g:render template="summarytd" model="[summaryList: meaningSummary, label: 'Meaning from Literal']"/>
        </g:if>

        <g:if test="${pronunciationSummary}">
          <g:render template="summarytd" model="[summaryList: pronunciationSummary, label: 'Pronunciation']"/>
        </g:if>

        <g:if test="${symbolSummary}">
          <g:render template="summarytd" model="[summaryList: symbolSummary, label: 'Symbol from Translation']"/>
        </g:if>
        </tbody>
    </table>

	<div id="list-cardRanking" class="content scaffold-list" role="main">
			<g:render template="table" model="[cardRankingInstanceList: cardRankingInstanceList]"/>
	</div>
	</div>
</body>
</html>
