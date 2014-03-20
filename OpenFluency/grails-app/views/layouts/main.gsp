<!DOCTYPE html>
<html lang="en" class="no-js">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>OpenFluency</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">

	<!-- CSS includes -->
	<link rel="stylesheet" href="${resource(dir: 'css', file: 'main.css')}" type="text/css">
	<link rel="stylesheet" href="${resource(dir: 'css', file: 'bootstrap.min.css')}" type="text/css">

	<!-- JS libraries -->
	<r:require module="jquery"/>
	<g:javascript src="bootstrap.min.js"/>
	<g:javascript library="application"/>

	<g:layoutHead/>
	<r:layoutResources />
</head>
<body>
	<nav class="navbar navbar-default" role="navigation">
		<div class="container-fluid">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<a class="navbar-brand" href="#">OpenFluency</a>
			</div>

			<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav">
					<li>
						<g:link action="list" controller="alphabet">Alphabets</g:link>
					</li>
				</ul>
			</div>
		</nav>
		<g:layoutBody/>
		<r:layoutResources />
</body>
	</html>