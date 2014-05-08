<%@ page import="com.openfluency.language.Language" %>
<div class="form-group">
	<label>Title</label>
	<input required class="form-control" type="text" name="title" value="${courseInstance?.title}"/>
</div>

<div class="form-group">
	<label>Description</label>
	<textarea required class="form-control" name="description">${courseInstance?.description}</textarea>
</div>

<div class="form-group">
	<label for="language" class="control-label">
		Language
		<span class="required-indicator">*</span>
	</label>
	<g:select class="form-control" name="language.id" from="${Language.list()}" optionKey="id" optionValue="name" value="${courseInstance?.language}"/>
</div>

<div class="form-group">
	<label for="language" class="control-label">
		Show in search results
		<span class="required-indicator">*</span>
	</label>
	<select class="form-control" name="visible">
		<g:if test="${courseInstance.visible}">
			<option selected value="${true}">Yes</option>
			<option value="${false}">No</option>
		</g:if>
		<g:else>
			<option value="${true}">Yes</option>
			<option selected value="${false}">No</option>
		</g:else>
	</select>
</div>

<div class="form-group">
	<label for="language" class="control-label">
		Registration
		<span class="required-indicator">*</span>
	</label>
	<select class="form-control" name="open">
		<g:if test="${courseInstance.open}">
			<option selected value="${true}">Anyone can enroll</option>
			<option value="${false}">Only invited students can enroll</option>
		</g:if>
		<g:else>
			<option value="${true}">Anyone can enroll</option>
			<option selected value="${false}">Only invited students can enroll</option>
		</g:else>
	</select>
</div>
<br>