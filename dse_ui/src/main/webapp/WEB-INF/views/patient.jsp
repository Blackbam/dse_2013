<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Patientenbereich</title>
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
					<a href="" title="Patientenbereich" rel="home">Patientenbereich</a>
				</div>
			</div>
		</div>
		<!-- /header -->
		<div id="container">
			<div id="content" class="no-side-nav">

				<h2>Persönliche Daten</h2>

				<table>
					<tr>
						<td align="right">Nachname:</td>
						<td>${patient.lastName}</td>
					</tr>
					<tr>
						<td align="right">Vorname:</td>
						<td>${patient.firstName}</td>
					</tr>
					<tr>
						<td align="right">Username:</td>
						<td>${patient.username}</td>
					</tr>
					<tr>
						<td align="right">Standort:</td>
						<td>${patient.locationString}</td>
					</tr>

				</table>

				<h2>Operationen</h2>

				<table id='gradient-style' class='sortable'>
					<tr>
						<th>Datum</th>
						<th>von</th>
						<th>bis</th>
						<th>Typ</th>
						<th>Krankenhaus</th>
						<th>Arzt</th>
					</tr>

					<c:forEach items="${opSlots}" var="opSlots">
						<tr>
							<td>${opSlots.dateString}</td>
							<td>${opSlots.startTimeString}</td>
							<td>${opSlots.endTimeString}</td>
							<td>${opSlots.typeString}</td>
							<td>${opSlots.hospital.name}</td>
							<td>${opSlots.reservation.doctor.name}</td>
						</tr>
					</c:forEach>

				</table>

			</div>
		</div>
	</div>
</body>
</html>
