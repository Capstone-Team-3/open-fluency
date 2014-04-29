<div class="form-group">
	<div class="${hasErrors(bean: chapterInstance, field: 'title', 'error')} required">
		<label for="title" class="control-label">
			<g:message code="chapter.title.label" default="Title" />
			<span class="required-indicator">*</span>
		</label>
		<input class="form-control" type="text" name="title"  required="" value="${chapterInstance?.title}"/>
	</div>
</div>

<div class="form-group">
	<div class="${hasErrors(bean: chapterInstance, field: 'description', 'error')} required">
		<label for="description" class="control-label">
			<g:message code="chapter.description.label" default="Description" />
			<span class="required-indicator">*</span>
		</label>
		<textarea class="form-control" name="description" required="">${chapterInstance?.description}</textarea>
	</div>
</div>

<div class="form-group">
	<div class="${hasErrors(bean: chapterInstance, field: 'Choose a deck', 'error')} required">
		<label for="choose a course" class="control-label">
			<g:message code="chapter.choose a deck.label" default="Choose a deck" />
			<span class="required-indicator">*</span>
		</label>
		<g:select class="form-control" name="deckId" required="" from="${userDecks}" value="${chapterInstance?.deck?.id}" noSelection="['':'-Choose a deck-']" optionKey="id" optionValue="title"/>
	</div>
</div>