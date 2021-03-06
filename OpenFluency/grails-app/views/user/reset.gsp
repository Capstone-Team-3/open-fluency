<html>
<head>
	<meta name='layout' content='main'/>
	<title><g:message code="springSecurity.login.title"/></title> 
</head>

<body>
	<div id="login" class="container forgot-password">
		<div class="row">
			<div class="col-lg-12">
				<form action='reset' method='POST' id='loginForm' class='cssform' autocomplete='off'>

					<h1>Forget your password?</h1>
					<p class="instructions">Enter your email address below and we'll send you a new one!</p>
					
					<div class="row">
						<div class="col-lg-4">
							<div class="form-group">
								<label for='email' class="control-label">
									Email:
								</label>
								<input type='text' class='text_ form-control' name='email' id='email' placeholder="Enter your email"/>
							</div>
							<input type="submit" name="reset" id="sign-in" class="btn btn-primary" value="Reset Password" />
						</div>
					</div>
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