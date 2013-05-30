<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Administration Area</title>
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
					<a href="" title="Admin" rel="home">Administrator Access</a>
				</div>
			</div>
		</div>
		<!-- /header -->
		
		<div id="container">
			<div id="content" class="no-side-nav">
			
			<div class="elem_wrapper">
				<h2>Krankenhäuser</h2>

				<h3>Krankenhaus anlegen</h3>
				
				<c:if test="${true == add_hospital}">
				    <p class="success">Ein neues Krankenhaus wurde angelegt:
				    ID: ${new_hospital.id},
				    Name: ${new_hospital.name},
				    Latitude: ${new_hospital.location[0]},
				    Longitude: ${new_hospital.location[1]}</p>
				</c:if>
				<c:if test="${false == add_hospital}">
				    <p class="error">Das Krankenhaus konnte nicht angelegt werden.</p>
				</c:if>

				<c:if test="${true == delete_hospital}">
				    <p class="success">Krankenhaus erfolgreich gelöscht.</p>
				</c:if>
				<c:if test="${false == delete_hospital}">
				    <p class="error">Krankenhaus konnte nicht gelöscht werden (bietet noch OP_Slots an?)</p>
				</c:if>
				
				<div id="krankenhaus_anlegen">
					<form method="post" action="/admin/hospital/create/">
						<label>Name</label><input type="text" name="name" /><br/>
						<label>Latitude</label><input type="text" name="latitude" /><br/>
						<label>Longitude</label><input type="text" name="longitude" /><br/>
						<input type="submit" value="Krankenhaus anlegen" />
					</form>
				</div>
				
				<h3>Liste aller Krankenhäuser</h3>

				<table id='gradient-style' class='sortable'>
					<tr>
						<th>ID</th>
						<th>Name</th>
						<th>Geo-Koordinaten</th>
						<th>Krankenhaus löschen</th>
					</tr>

					<c:forEach items="${hospitals}" var="hospitals">
						<tr>
							<td>${hospitals.id}</td>
							<td>${hospitals.name}</td>
							<td>Lat. ${hospitals.location[0]}, Long. ${hospitals.location[1]}</td>
							<td><a href="/admin/hospital/delete/?id=${hospitals.id}">X</a></td>
						</tr>
					</c:forEach>

				</table>
			</div><!-- elem_wrapper close -->
			
			<div class="elem_wrapper">
				<h2>Doktoren</h2>

				<h3>Doktor anlegen</h3>
				
				<c:if test="${true == add_doctor}">
				    <p class="success">Ein neuer Doktor wurde angelegt:
				    ID: ${new_doctor.id},
				    Titel: ${new_doctor.title},
				    Vorname: ${new_doctor.firstName},
				    Nachname: ${new_doctor.lastName}</p>
				</c:if>
				<c:if test="${false == add_doctor}">
				    <p class="error">Der Doktor konnte nicht angelegt werden.</p>
				</c:if>

				<c:if test="${true == delete_doctor}">
				    <p class="success">Doktor erfolgreich gelöscht.</p>
				</c:if>
				<c:if test="${false == delete_doctor}">
				    <p class="error">Der Doktor konnte nicht gelöscht werden (Offene Reservierungen?)</p>
				</c:if>
				
				<div id="doktor_anlegen">
					<form method="post" action="/admin/doctor/create/">
						<label>Titel</label><input type="text" name="title" /><br/>
						<label>Vorname</label><input type="text" name="firstName" /><br/>
						<label>Nachname</label><input type="text" name="lastName" /><br/>
						<input type="submit" value="Doktor anlegen" />
					</form>
				</div>
				
				<h3>Liste aller Doktoren</h3>

				<table id='gradient-style' class='sortable'>
					<tr>
						<th>ID</th>
						<th>Titel</th>
						<th>Vorname</th>
						<th>Nachname</th>
						<th>Doktor löschen</th>
					</tr>

					<c:forEach items="${doctors}" var="doctors">
						<tr>
							<td>${doctors.id}</td>
							<td>${doctors.title}</td>
							<td>${doctors.firstName}</td>
							<td>${doctors.lastName}</td>
							<td><a href="/admin/doctor/delete/?id=${doctors.id}">X</a></td>
						</tr>
					</c:forEach>

				</table>
			</div><!-- elem_wrapper close -->
			
			
			<div class="elem_wrapper">
				<h2>Patienten</h2>

				<h3>Patient anlegen</h3>
				
				<c:if test="${true == add_patient}">
				    <p class="success">Ein neuer Patient wurde angelegt:
				    ID: ${new_patient.id},
				    Vorname: ${new_patient.firstName},
				    Nachname: ${new_patient.lastName},
				    Latitude: ${new_patient.location[0]},
				    Longitude: ${new_patient.location[1]}</p>
				</c:if>
				<c:if test="${false == add_patient}">
				    <p class="error">Der Patient konnte nicht angelegt werden.</p>
				</c:if>

				<c:if test="${true == delete_patient}">
				    <p class="success">Patient erfolgreich gelöscht.</p>
				</c:if>
				<c:if test="${false == delete_patient}">
				    <p class="error">Der Patient konnte nicht gelöscht werden (Offene Reservierungen?)</p>
				</c:if>
				
				<div id="patient_anlegen">
					<form method="post" action="/admin/patient/create/">
						<label>Vorname</label><input type="text" name="firstName" /><br/>
						<label>Nachname</label><input type="text" name="lastName" /><br/>
						<label>Latitude</label><input type="text" name="latitude" /><br/>
						<label>Longitude</label><input type="text" name="longitude" /><br/>
						<input type="submit" value="Patient anlegen" />
					</form>
				</div>
				
				<h3>Liste aller Patienten</h3>

				<table id='gradient-style' class='sortable'>
					<tr>
						<th>ID</th>
						<th>Vorname</th>
						<th>Nachname</th>
						<th>Geo-Koordinaten</th>
						<th>Patient löschen</th>
					</tr>

					<c:forEach items="${patients}" var="patients">
						<tr>
							<td>${patients.id}</td>
							<td>${patients.firstName}</td>
							<td>${patients.lastName}</td>
							<td>Lat. ${patients.location[0]}, Long. ${patients.location[1]}</td>
							<td><a href="/admin/patient/delete/?id=${patients.id}">X</a></td>
						</tr>
					</c:forEach>

				</table>
			</div><!-- elem_wrapper close -->
			
			
		</div>
	</div>
</div>
	
</body>
</html>