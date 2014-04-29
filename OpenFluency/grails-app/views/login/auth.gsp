<html>
<head>
	<meta name='layout' content='main'/>
	<title><g:message code="springSecurity.login.title"/></title> 
</head>

<body>
	<div id="login" class="container signin">
		<div class="col-lg-4">

			<h2>Sign In</h2>
			<p>
				New user?
				<g:link action='create' controller='user'>Sign Up</g:link>.
			</p>

			<form action='${postUrl}' method='POST' id='loginForm' class='cssform' autocomplete='off'>

				<label for='username' class="control-label">
					<g:message code="springSecurity.login.username.label"/>
					:
				</label>
				<input type='text' class='text_ form-control' name='j_username' id='username'/>

				<label for='password' class="control-label">
					<g:message code="springSecurity.login.password.label"/>
					:
				</label>
				<input type='password' class='text_ form-control' name='j_password' id='password'/>

				<div id="remember_me_holder">
					<input type='checkbox' class='chk' name='${rememberMeParameter}' id='remember_me' <g:if test='${hasCookie}'>checked='checked'</g:if>
				/>
				<label for='remember_me' class="control-label">
					<g:message code="springSecurity.login.remember.me.label"/>
				</label>
			</div>

			<input type="submit" name="sign-in" id="sign-in" class="btn btn-primary btn-block" value="Sign In" />
			<div class="center">
				<small>
					<g:link action="reset" controller="user">Forgot password?</g:link>
				</small>
			</div>
		</form>

	</div>
	<!-- end col-lg-4 -->
</div>
<!-- end container -->

<script type='text/javascript'>
		<!--
		(function() {
			document.forms['loginForm'].elements['j_username'].focus();
		})();
		// -->
	</script>
</body>
</html>