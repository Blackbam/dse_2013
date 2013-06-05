<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
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
				<h2>Persoenliche OP-Slot Liste</h2>

				<table id='gradient-style' class='sortable'>
					<tr>
						<th>Datum</th>
						<th>Dauer</th>
						<th>Typ</th>
						<th>KH</th>
						<th>Arzt</th>
						<th>Patient</th>
						<th>Reservierung loeschen</th>
					</tr>

					<c:forEach items="${op_slots_this_doctor}" var="op_slots_this_doctor">
						<tr>
							<td>${op_slots_this_doctor.date}</td>
							<td>${op_slots_this_doctor.length}</td>
							<td>${op_slots_this_doctor.type}</td>
							<td>${op_slots_this_doctor.hospital.name}</td>
							<td>${op_slots_this_doctor.reservation.doctor.lastName}</td>
							<td>${op_slots_this_doctor.reservation.patient.lastName}</td>
							<td><a href="/remove_reservation/?id=${op_slots_this_doctor.id}">X</a></td>
						</tr>
					</c:forEach>

				</table>
				
				<h2>Reservierung für einen Patienten vornehmen</h2>
				<p>benötigt Name des Patienten</p>
				<p>benötigt Typ der Operation</p>
				<p>benötigt maximaler Umkreis</p>
				<p>benötigt frühestes Datum</p>
				<p>benötigt spätestes Datum</p>
				<p>--&gt; der Allocator sucht ein passendes Krankenhaus</p>
				
				<div id="reserve">
					<form  method="post"  action="/doctor/reserve/">
						<label>PatientenID (for debug)</label><input type="text" name="patientID" /><br/>				
						<input type="submit" value="Reservierung fuer einen Patienten vornehmen" />
					</form>	
				</div>

								
				
				</div><!-- elem_wrapper close -->		
		</div>
	</div>
</div>
	
</body>
</html>