<%@ page import="com.openfluency.auth.User" %>
<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"></head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-lg-6 col-lg-offset-3">
				<h1>Edit User</h1>
				<g:hasErrors bean="${userInstance}">
					<ul class="errors" role="alert">
						<g:eachError bean="${userInstance}" var="error">
							<li>
								<g:message error="${error}"/>
							</li>
						</g:eachError>
					</ul>
				</g:hasErrors>
				<g:form url="[resource:userInstance, action:'update']" method="PUT" >
					<g:render template="form"/>

					<g:each in="${userInstance.languageProficiencies}">
						<g:render template="/proficiency/languageProficiency" model="[languageProficiency: it]"/>
					</g:each>

					<div class="proficiencies">
						<label class="control-label">
							Language Proficiencies
							<a id="addproficiency" class="btn btn-success btn-xs">Add</a>
						</label>
					</div>
					<br>
					<g:submitButton name="update" class="btn btn-primary" value="Update" />
				</g:form>
			</div>
		</div>
	</div>
</div>

<g:javascript>
	initializeProficiencyAdder();
initializeProficiencyRemovers();
</g:javascript>
</body>
</html>