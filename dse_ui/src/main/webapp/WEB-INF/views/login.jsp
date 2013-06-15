<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Login</title>
<link rel="stylesheet" href="/static/css/main.css" type="text/css"></link>
<link rel="stylesheet" href="/static/css/colors.css" type="text/css"></link>
<link rel="stylesheet" href="/static/css/custom.css" type="text/css"></link>
<link href="/static/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen"></link>
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<script src="/static/bootstrap/js/bootstrap.min.js"></script>
</head>
<body>
	<div id="page">
		<div id="header">
			<div id="name-and-company">
				<div id='site-name'>
					<a href="" title="Login" rel="home">Login</a>
				</div>
			</div>
		</div>
		<!-- /header -->
		<div id="container">
			<div id="content" class="no-side-nav">

					<form class="form-horizontal" method="post" action="/login/authenticate">
						<fieldset>
							<div class="control-group">
								<!-- Username -->
								<label class="control-label" for="username">Username</label>
								<div class="controls">
									<input type="text" id="username" name="username" class="input-xlarge"/>
								</div>
							</div>

							<div class="control-group">
								<!-- Password-->
								<label class="control-label" for="password">Password</label>
								<div class="controls">
									<input type="password" id="password" name="password" class="input-xlarge"/>
								</div>
							</div>

							<div class="control-group">
								<!-- Type-->
								<label class="control-label" for="type">Einloggen als</label>
								<div class="controls">
									<label class="radio inline" for="radios-0">
										<input type="radio" name="usertype" value="PATIENT" checked="checked" />
										Patient
									</label>
									<label class="radio inline" for="radios-1">
										<input type="radio" name="usertype" value="DOCTOR" />
										Doktor
									</label>
									<label class="radio inline" for="radios-2">
										<input type="radio" name="usertype" value="HOSPITAL" />
										Krankenhaus
									</label>
								</div>
							</div>

							<div class="control-group">
								<!-- Button -->
								<div class="controls">
									<button class="btn btn-success">Login</button>
								</div>
							</div>
						</fieldset>
					</form>
				</div>

			</div>
		</div>
	</div>
</body>
</html>
