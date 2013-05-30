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

				<p><a href="/login">User login</a></p>
				
				<h2>OP-Slot Suche</h2>

				<h2>Liste aller OP-Slots</h2>

				<table id='gradient-style' class='sortable'>
					<tr>
						<th>Datum</th>
						<th>Dauer</th>
						<th>Typ</th>
						<th>Krankenhaus</th>
						<th>Reservierung</th>
					</tr>

					<c:forEach items="${opSlots}" var="opSlots">
						<tr>
							<td>${opSlots.date}</td>
							<th>${opSlots.length}</th>
							<td>${opSlots.type}</td>
							<td>${opSlots.hospital.name}</td>
							<td>
								<c:if test="${not empty opSlots.reservation}">
								    ${opSlots.reservation.id} von Arzt ${opSlots.reservation.doctor.name} 
								    für Patient ${opSlots.reservation.patient.name}
								</c:if>
								<c:if test="${empty opSlots.reservation}">
								    Nein
								</c:if>
							</td>
						</tr>
					</c:forEach>

				</table>

			</div>
		</div>
	</div>
</body>
</html>
