<%@ page import="grails.plugin.springsecurity.SpringSecurityUtils" %>

<!DOCTYPE html>
<html lang="en" class="no-js">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>OpenFluency</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">

	<!-- CSS includes -->
	<link rel="stylesheet" href="${resource(dir: 'css', file: 'main.css')}" type="text/css">

	<!-- JS libraries -->
	<g:javascript src="jquery.min.js"/>
	<g:javascript src="jquery.mousewheel.js"/>
	<g:javascript src="jquery.easing.1.3.js"/>
	<g:javascript src="jquery.contentcarousel.js"/>
	
	<r:require modules="bootstrap"/>
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
				<!-- LOGGED IN -->
				<sec:ifLoggedIn>
					<ul class="nav navbar-nav">
						<li>
							<g:link class="dropdown-toggle" data-toggle="dropdown" action="list" controller="deck">Decks<b class="caret"></b></g:link>
							<ul class="dropdown-menu">
                            	<li><g:link action="list" controller="deck">My decks</g:link></li>
                            	<li><g:link action="create" controller="deck">Create new deck</g:link></li>
                            	<li><a href="#">Search for decks</a></li>
                        	</ul>
						</li>
						<li>
							<g:link class="dropdown-toggle" data-toggle="dropdown" action="list" controller="course">Courses<b class="caret"></b></g:link>
							
							<ul class="dropdown-menu">
	                            <sec:ifAllGranted roles="ROLE_STUDENT">
	                            	<li><g:link action="list" controller="course">Enrolled courses</g:link></li>
                            	</sec:ifAllGranted>
                            	<sec:ifAllGranted roles="ROLE_INSTRUCTOR">
	                            	<li><g:link action="list" controller="course">My courses</g:link></li>
	                            	<li><g:link action="create" controller="course">Create new course</g:link></li>
                            	</sec:ifAllGranted>
                            	<li><g:link action="search" controller="course">Search for courses</g:link></li>
	                        </ul>
						</li>
                    </li>
					</ul>
				
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