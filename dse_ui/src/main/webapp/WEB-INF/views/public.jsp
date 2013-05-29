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
					<a href="" title="Public" rel="home">Public Access</a>
				</div>
			</div>
		</div>
		<!-- /header -->
		<div id="container">
			<div id="content" class="no-side-nav">

				<h2>Below you can find a list of all operation slots.</h2>

				<p>For testing purposes, reloading the page adds a new operation
					slot. Once 10 slots have been created, the next reload will remove
					them all again. DeltaX.</p>

				<table id='gradient-style' class='sortable'>
					<tr>
						<th>Date</th>
						<th>Hospital</th>
						<th>Type</th>
					</tr>

					<c:forEach items="${opSlots}" var="opSlots">
						<tr>
							<td>${opSlots.date}</td>
							<td>${opSlots.hospital.name}</td>
							<td>${opSlots.type}</td>
						</tr>
					</c:forEach>

				</table>

			</div>
		</div>
	</div>
</body>
</html>
