<%@ page import="com.openfluency.language.Language" %>
<%@ page import="com.openfluency.language.Proficiency" %>

<div class="panel panel-default">
	<div class="panel-body">
		<label for="language" class="control-label">
			<g:message code="proficiency.language.label" default="Language" />
			<span class="required-indicator">*</span>
		</label>
		<g:select id="language" class="form-control" name="language.id" from="${Language.list()}" optionKey="id" required="" value="${languages?.id}"/>

		<label for="proficiencies" class="control-label">
			<g:message code="proficiency.proficiency.label" default="Proficiency" />
			<span class="required-indicator">*</span>
		</label>

		<g:select id="proficiency" class="form-control" name="proficiency.id" from="${Proficiency.list()}" optionKey="id" required="" optionValue="${proficiency}" />
	</div>
</div>