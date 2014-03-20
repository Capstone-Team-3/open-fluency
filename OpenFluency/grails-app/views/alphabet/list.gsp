<%@ page import="com.openfluency.language.Unit" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="layout" content="main"/>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-lg-12">
				<div class="h1">Alphabets</div>
				<table class="table">
					<thead>
						<tr>
							<th>Language</th>
							<th>Name</th>
							<th>Code</th>
							<th>Units</th>
						</tr>
					</thead>
					<tbody>
						<g:each in="${alphabetInstanceList}">
							<tr>
								<td>${it.language.name}</td>
								<td>
									<g:link action="list" controller="unit" id="${it.id}">${it?.name}</g:link>
								</td>
								<td>${it?.code}</td>
								<td>${Unit.countByAlphabet(it)}</td>
							</tr>
						</g:each>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
</html>