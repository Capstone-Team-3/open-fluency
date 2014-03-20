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
				<div class="h1">Units in ${alphabetInstance.name}</div>
				<table class="table">
					<thead>
						<tr>
							<th>Literal Encoded</th>
							<th>Literal</th>
							<th>Grade</th>
							<th>Frequency</th>
							<th>Stroke Count</th>
						</tr>
					</thead>
					<tbody>
						<g:each in="${unitInstanceList}">
							<tr>
								<td>
									<g:link action="show" controller="unit" id="${it?.id}">${it?.print}</g:link>
								</td>
								<td>${it?.literal}</td>
								<td>${it?.grade}</td>
								<td>${it?.frequency}</td>
								<td>${it?.strokeCount}</td>
							</tr>
						</g:each>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
</html>