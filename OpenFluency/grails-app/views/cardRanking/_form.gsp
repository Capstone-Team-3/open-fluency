<%@ page import="com.openfluency.flashcard.CardRanking" %>



<div class="fieldcontain ${hasErrors(bean: cardRankingInstance, field: 'meaningRanking', 'error')} ">
	<label for="meaningRanking">
		<g:message code="cardRanking.meaningRanking.label" default="Meaning Ranking" />
		
	</label>
	<g:field name="meaningRanking" type="number" value="${cardRankingInstance.meaningRanking}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: cardRankingInstance, field: 'pronunciationRanking', 'error')} ">
	<label for="pronunciationRanking">
		<g:message code="cardRanking.pronunciationRanking.label" default="Pronunciation Ranking" />
		
	</label>
	<g:field name="pronunciationRanking" type="number" value="${cardRankingInstance.pronunciationRanking}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: cardRankingInstance, field: 'symbolRanking', 'error')} ">
	<label for="symbolRanking">
		<g:message code="cardRanking.symbolRanking.label" default="Symbol Ranking" />
		
	</label>
	<g:field name="symbolRanking" type="number" value="${cardRankingInstance.symbolRanking}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: cardRankingInstance, field: 'flashcard', 'error')} required">
	<label for="flashcard">
		<g:message code="cardRanking.flashcard.label" default="Flashcard" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="flashcard" name="flashcard.id" from="${com.openfluency.flashcard.Flashcard.list()}" optionKey="id" required="" value="${cardRankingInstance?.flashcard?.id}" class="many-to-one"/>

</div>

<div class="fieldcontain ${hasErrors(bean: cardRankingInstance, field: 'user', 'error')} required">
	<label for="user">
		<g:message code="cardRanking.user.label" default="User" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="user" name="user.id" from="${com.openfluency.auth.User.list()}" optionKey="id" required="" value="${cardRankingInstance?.user?.id}" class="many-to-one"/>

</div>

