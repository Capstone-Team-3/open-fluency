<%@ page import="grails.plugin.springsecurity.SpringSecurityUtils" %>

<!DOCTYPE html>
<html lang="en" class="no-js">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>OpenFluency</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">

	<!-- CSS includes -->
	<link rel="stylesheet" href="${resource(dir: 'css', file: 'bootstrap.min.css')}" type="text/css">
	<link rel="stylesheet" href="${resource(dir: 'css', file: 'main.css')}" type="text/css">

	<!-- JS libraries -->
	<g:javascript src="jquery.min.js"/>
	<g:javascript src="bootstrap.min.js"/>
	<g:javascript library="application"/>

	<g:layoutHead/>
	<r:layoutResources />
</head>
<body>
	<nav class="navbar navbar-default" role="navigation">
		<div class="container">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar-collapse-1">
					<span class="sr-only">Toggle navigation</span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="${createLink(uri: '/')}">OpenFluency</a>
			</div>

			<div class="collapse navbar-collapse" id="navbar-collapse-1">
				<ul class="nav navbar-nav">
					<li>
						<g:link action="list" controller="alphabet">Alphabets</g:link>
					</li>
					<li>
						<g:link action="list" controller="deck">Decks</g:link>
					</li>
				</ul>

				<!-- LOGGED IN -->
				<sec:ifLoggedIn>
					<ul class="gandalf nav navbar-nav navbar-right">
						<li>
							<g:link controller="user" action="profile"><span class="glyphicon glyphicon-user"></span>
								<sec:username/>
							</g:link>
						</li>
						<li>
							<g:link controller="logout">Logout</g:link>
						</li>
					</ul>
				</sec:ifLoggedIn>

				<!-- NOT LOGGED IN -->
				<sec:ifNotLoggedIn>
					<form class="navbar-form navbar-right" role="search" action="${request.contextPath}${SpringSecurityUtils.securityConfig.apf.filterProcessesUrl}" method="POST">
						<div class="form-group">
							<input type="text" name="j_username" placeholder="username" class="form-control"/>
							<input type="password" name="j_password" placeholder="password" class="form-control"/>
							<input type="hidden" name="spring-security-redirect" value="${request.requestURL}"/>
							<button type="submit" class="btn btn-inverse">Sign in</button>
						</div>
					</form>
				</sec:ifNotLoggedIn>
			
			</div><!-- end navbar-collapse -->
		</div><!-- end container -->
	</nav>

	<g:layoutBody/>
	<r:layoutResources />
</body>
</html>