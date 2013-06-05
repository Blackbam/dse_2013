<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Krankenhausbereich</title>
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
					<a href="" title="Patientenbereich" rel="home">Krankenhausbereich</a>
				</div>
			</div>
		</div>
		<!-- /header -->
		<div id="container">
			<div id="content" class="no-side-nav">

				<h2>Über dieses Krankenhaus</h2>

				<table>
					<tr>
						<td align="right">Name</td>
						<td>${hospital.name}</td>
					</tr>
					<tr>
						<td align="right">Latitude:</td>
						<td>${hospital.location[0]}</td>
					</tr>
					<tr>
						<td align="right">Longitude</td>
						<td>${hospital.location[1]}</td>
					</tr>
				</table>
				
			<div class="elem_wrapper">
				<h2>OP Slots</h2>

				<h3>OP Slot anlegen</h3>
				
				<c:if test="${true == add_opslot}">
				    <p class="success">Ein neuer OP Slot wurde angelegt:
				    ID: ${new_opslot.id},
				    In Krankenhaus: ${new_opslot.hospital.name},
				    Datum: ${new_opslot.date},
				    Dauer in Minuten: ${new_opslot.length},
				    Typ: ${new_opslot.type}
				    </p>
				</c:if>
				<c:if test="${false == add_opslot}">
				    <p class="error">Der OP-Slot konnten nicht angelegt werden. Bitte überprüfen sie die Parameter.</p>
				</c:if>

				<c:if test="${true == delete_opslot}">
				    <p class="success">OP-Slot erfolgreich gelöscht.</p>
				</c:if>
				<c:if test="${false == delete_hospital}">
				    <p class="error">Der OP-Slot konnte nicht gelöscht werden. Wahrscheinlich ist eine Reservierung aktiv?</p>
				</c:if>
				
				<div id="opslot_anlegen">
					<form method="post" action="/hospital/op_slot/create/">
						<input type="hidden" name="hospital_id" value="${hospital.id}" />
						<label>Date (yyyy-mm-dd hh:ii):</label><input type="text" name="date" /><div style="clear:both;"></div><br/>
						<label>Length in Minutes:</label><input type="text" name="length" /><div style="clear:both;"></div><br/>
						<label>Typ:</label>
							<select name="type">
								<option value="AUGEN">Augen</option>
								<option value="KARDIO">Kardio</option>
								<option value="OTHER">Andere</option>
							</select>
						<div style="clear:both;"></div><br/>
						<input type="submit" value="OP-Slot anlegen" />
					</form>
				</div>
				
				<h3>Liste aller OP-Slots</h3>

				<table id='gradient-style' class='sortable'>
					<tr>
						<th>ID</th>
						<th>Krankenhaus</th>
						<th>Datum</th>
						<th>Dauer</th>
						<th>Typ</th>
						<th>Reservierung</th>
						<th>Löschen</th>
					</tr>

					<c:forEach items="${opslots}" var="opslots">
						<tr>
							<td>${opslots.id}</td>
							<td>${opslots.hospital.name}</td>
							<td>${opslots.date}</td>
							<td>${opslots.length}</td>
							<td>${opslots.type}</td>
							<td>
								<c:if test="${not empty opslots.reservation}">
								    reserviert von Arzt ${opslots.reservation.doctor.title}  ${opslots.reservation.doctor.firstName} ${opslots.reservation.doctor.lastName}
								    für Patient ${opslots.reservation.patient.firstName} ${opslots.reservation.patient.lastName}
								</c:if>
								<c:if test="${empty opslots.reservation}">
								    Nein
								</c:if>
							</td>
							<td><a href="/hospital/op_slot/delete/?id=${opslots.id}">X</a></td>
						</tr>
					</c:forEach>

				</table>
			</div><!-- elem_wrapper close -->
				
				
			</div>
		</div>
	</div>
</body>
</html>