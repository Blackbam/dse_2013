<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Ärztebereich</title>
<link rel="stylesheet" href="/static/css/main.css" type="text/css"></link>
<link rel="stylesheet" href="/static/css/colors.css" type="text/css"></link>
<link rel="stylesheet" href="/static/css/local.css" type="text/css"></link>
<link rel="stylesheet" href="/static/css/custom.css" type="text/css"></link>
<script type="text/javascript" src="/static/js/sorttable.js"></script>
</head>
<body>
	<div id="page">
		<div id="header">
			<div id="name-and-company">
				<div id='site-name'>
					<a href="" title="Doctor" rel="home">Ärztebereich</a>
				</div>
			</div>
		</div>
		<!-- /header -->
		
		<div id="container">
			<div id="content" class="no-side-nav">
			
			<div class="elem_wrapper">
				<h2>Persönliche OP-Slot Liste</h2>

				<table id='gradient-style' class='sortable'>
					<tr>
						<th>Datum</th>
						<th>von</th>
						<th>bis</th>
						<th>Typ</th>
						<th>KH</th>
						<th>Arzt</th>
						<th>Patient</th>
						<th>Reservierung löschen</th>
					</tr>

					<c:forEach items="${op_slots}" var="op_slots">
						<tr>
							<td>${op_slots.date}</td>
							<td>todo</td>
							<td>todo</td>
							<td>${op_slots.type}</td>
							<td>${op_slots.hospital.name}</td>
							<td>${op_slots.reservation.arzt}</td>
							<td>${op_slots.reservation.patient}</td>
							<td><a href="/remove_reservation/?id=${op_slots.id}">X</a></td>
						</tr>
					</c:forEach>

				</table>
			
				<div id="reserve">
					<form method="post" action="/admin/patient/create/">
						<label>PatientenID (for debug)</label><input type="text" name="title" /><br/>
						<input type="submit" value="Reservierung für einen Patienten vornehmen]" />
					</form>
				</div>
			</div><!-- elem_wrapper close -->		
		</div>
	</div>
</div>
	
</body>
</html>