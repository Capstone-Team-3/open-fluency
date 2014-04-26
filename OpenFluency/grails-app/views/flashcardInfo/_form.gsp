<%@ page import="com.openfluency.flashcard.FlashcardInfo" %>



<div class="fieldcontain ${hasErrors(bean: flashcardInfoInstance, field: 'flashcard', 'error')} required">
	<label for="flashcard">
		<g:message code="flashcardInfo.flashcard.label" default="Flashcard" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="flashcard" name="flashcard.id" from="${com.openfluency.flashcard.Flashcard.list()}" optionKey="id" required="" value="${flashcardInfoInstance?.flashcard?.id}" class="many-to-one"/>

</div>

<div class="fieldcontain ${hasErrors(bean: flashcardInfoInstance, field: 'user', 'error')} required">
	<label for="user">
		<g:message code="flashcardInfo.user.label" default="User" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="user" name="user.id" from="${com.openfluency.auth.User.list()}" optionKey="id" required="" value="${flashcardInfoInstance?.user?.id}" class="many-to-one"/>

</div>

<div class="fieldcontain ${hasErrors(bean: flashcardInfoInstance, field: 'deck', 'error')} required">
	<label for="deck">
		<g:message code="flashcardInfo.deck.label" default="Deck" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="deck" name="deck.id" from="${com.openfluency.flashcard.Deck.list()}" optionKey="id" required="" value="${flashcardInfoInstance?.deck?.id}" class="many-to-one"/>

</div>

<div class="fieldcontain ${hasErrors(bean: flashcardInfoInstance, field: 'queue', 'error')} required">
	<label for="queue">
		<g:message code="flashcardInfo.queue.label" default="Queue" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="queue" type="number" value="${flashcardInfoInstance.queue}" required=""/>

</div>

<div class="fieldcontain ${hasErrors(bean: flashcardInfoInstance, field: 'viewPriority', 'error')} required">
	<label for="viewPriority">
		<g:message code="flashcardInfo.viewPriority.label" default="View Priority" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="viewPriority" value="${fieldValue(bean: flashcardInfoInstance, field: 'viewPriority')}" required=""/>

</div>

<div class="fieldcontain ${hasErrors(bean: flashcardInfoInstance, field: 'numberOfRepetitions', 'error')} required">
	<label for="numberOfRepetitions">
		<g:message code="flashcardInfo.numberOfRepetitions.label" default="Number Of Repetitions" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="numberOfRepetitions" type="number" value="${flashcardInfoInstance.numberOfRepetitions}" required=""/>

</div>

<div class="fieldcontain ${hasErrors(bean: flashcardInfoInstance, field: 'easinessFactor', 'error')} required">
	<label for="easinessFactor">
		<g:message code="flashcardInfo.easinessFactor.label" default="Easiness Factor" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="easinessFactor" value="${fieldValue(bean: flashcardInfoInstance, field: 'easinessFactor')}" required=""/>

</div>

<div class="fieldcontain ${hasErrors(bean: flashcardInfoInstance, field: 'interval', 'error')} required">
	<label for="interval">
		<g:message code="flashcardInfo.interval.label" default="Interval" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="interval" value="${fieldValue(bean: flashcardInfoInstance, field: 'interval')}" required=""/>

</div>

<div class="fieldcontain ${hasErrors(bean: flashcardInfoInstance, field: 'data1', 'error')} ">
	<label for="data1">
		<g:message code="flashcardInfo.data1.label" default="Data1" />
		
	</label>
	<g:textField name="data1" value="${flashcardInfoInstance?.data1}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: flashcardInfoInstance, field: 'data2', 'error')} ">
	<label for="data2">
		<g:message code="flashcardInfo.data2.label" default="Data2" />
		
	</label>
	<g:textField name="data2" value="${flashcardInfoInstance?.data2}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: flashcardInfoInstance, field: 'data3', 'error')} ">
	<label for="data3">
		<g:message code="flashcardInfo.data3.label" default="Data3" />
		
	</label>
	<g:textField name="data3" value="${flashcardInfoInstance?.data3}"/>

</div>

