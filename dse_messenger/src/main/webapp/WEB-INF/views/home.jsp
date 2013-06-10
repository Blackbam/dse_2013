<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html;charset=iso-8859-15" />
<!-- for correctly displayed German -->
<title>Hello Spring</title>
<link rel="stylesheet" href="static/css/main.css" type="text/css"></link>
<link rel="stylesheet" href="static/css/colors.css" type="text/css"></link>
<link rel="stylesheet" href="static/css/local.css" type="text/css"></link>
</head>
<body>
	<div id="page">
		<div id="header">
			<div id="name-and-company">
				<div id='site-name'>
					<a href="" title="Hello Spring ${environmentName}" rel="home">Hello
						Spring ${environmentName}</a>
				</div>
				<div id='company-name'>
					<a href="http://www.springsource.com" title="SpringSource">SpringSource
						Home</a>
				</div>
			</div>
			<!-- /name-and-company -->
		</div>
		<!-- /header -->
		<div id="container">
			<div id="content" class="no-side-nav">

				<h2>The following services are bound to this application:</h2>
				<ul>
					<c:forEach items="${services}" var="service">
						<li><p>${service}</p></li>
					</c:forEach>
				</ul>

				<h2>Debug view of ALL stored notifications</h2>

				<c:forEach items="${nots}" var="nots">

					<h4>
						User: ${nots.user.username}: <br /> ${nots.title} [
						<fmt:formatDate value="${nots.date}" pattern="dd.MM.yyyy HH:mm" />
						]
					</h4>
					<p>${nots.content}</p>
					wurde gelesen: ${nots.read} <br />
					____________________________________________________________________
						
					</c:forEach>


			</div>
		</div>
	</div>
</body>
</html>
