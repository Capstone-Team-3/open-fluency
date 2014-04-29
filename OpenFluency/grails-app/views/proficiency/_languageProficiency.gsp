<%@ page import="com.openfluency.language.Language" %>
<%@ page import="com.openfluency.language.Proficiency" %>

<div class="panel panel-default">
	<div class="panel-body">
		<div class="control-group">
			<button class="remove-proficiency pull-right btn btn-danger btn-xs"><strong>Remove</strong></button>

			<label for="language" class="pull-left control-label">
				Language
				<span class="required-indicator">*</span>
			</label>

			<g:select id="language" class="form-control" name="language.id" from="${Language.list()}" optionKey="id" required="" optionValue="name" value="${languageProficiency?.language?.id}"/>
		</div>

		<div class="control-group">
			<label for="proficiencies" class="control-label">
				Proficiency Level
				<span class="required-indicator">*</span>
			</label>
			<g:select id="proficiency" class="form-control" name="proficiency.id" from="${Proficiency.list()}" optionKey="id" required="" optionValue="${proficiency}" value="${languageProficiency?.proficiency?.id}"/>
		</div>
	</div>
</div>