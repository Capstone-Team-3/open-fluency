<html>
<head>
	<meta name='layout' content='main'/>
	<title><g:message code="springSecurity.login.title"/></title> 
</head>

<body>
	<div id="login" class="container signin">
		<div class="row">
			<div class="col-lg-4 col-lg-offset-4">
				<form action='reset' method='POST' id='loginForm' class='cssform' autocomplete='off'>

					<label for='email' class="control-label">
						Email:
					</label>
					<input type='text' class='text_ form-control' name='email' id='email' placeholder="Enter your email"/>
					<br>
					<input type="submit" name="reset" id="sign-in" class="btn btn-primary btn-block" value="Reset Password" />
				</form>
			</div>
		</div>
	</div>

	<script type='text/javascript'>
		(function() {
			document.forms['loginForm'].elements['j_username'].focus();
		});
	</script>
</body>
</html>