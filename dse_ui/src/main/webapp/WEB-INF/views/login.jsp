<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Public Access</title>
<link rel="stylesheet" href="static/css/main.css" type="text/css"></link>
<link rel="stylesheet" href="static/css/colors.css" type="text/css"></link>
<link rel="stylesheet" href="static/css/local.css" type="text/css"></link>
<script type="text/javascript" src="static/js/sorttable.js"></script>
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

				<h2>Login</h2>

				<div id="login">
					<form method="post" action="/login/authenticate">
						<label>Username:</label><input type="text" name="username" /><br />
						<label>Passwort</label><input type="text" name="password" /><br />
						<input type="submit" value="Einloggen" />
					</form>
				</div>

			</div>
		</div>
	</div>
</body>
</html>
