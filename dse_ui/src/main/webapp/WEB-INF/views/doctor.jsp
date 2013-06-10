<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html;charset=iso-8859-15" /> <!-- for correctly displayed German -->

<title>�rztebereich</title>
<link rel="stylesheet" href="/static/css/main.css" type="text/css"></link>
<link rel="stylesheet" href="/static/css/colors.css" type="text/css"></link>
<link rel="stylesheet" href="/static/css/local.css" type="text/css"></link>
<link rel="stylesheet" href="/static/css/custom.css" type="text/css"></link>
<link rel="stylesheet" href="/static/css/datepicker.css" type="text/css" />
<script type="text/javascript" src="/static/js/sorttable.js"></script>
<script type="text/javascript"
	src="http://code.jquery.com/jquery-1.9.0.min.js"></script>
<script type="text/javascript"
	src="http://code.jquery.com/ui/1.10.0/jquery-ui.min.js"></script>
<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/i18n/jquery-ui-i18n.min.js"></script>
<script type="text/javascript" src="static/js/jquery.timepicker.js"></script>
<script>
	jQuery(function($) {
		$.datepicker.setDefaults({
			showOtherMonths : true,
			selectOtherMonths : true
		});
		$(".date").datepicker($.datepicker.regional['de']);
	});
</script>
</head>
<body>
	<div id="page">
		<div id="header">
			<div id="name-and-company">
				<div id='site-name'>
					<a href="" title="Doctor" rel="home">�rztebereich</a>
				</div>
			</div>
		</div>
		<!-- /header -->
		
		<div id="container">
			<div id="content" class="no-side-nav">
			
			<div class="elem_wrapper">
				<h2>Pers�nliche OP-Slot Liste</h2>

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
							<td><fmt:formatDate value="${op_slots_this_doctor.date}" pattern="dd.MM.yyyy HH:mm" /></td>
							<td>${op_slots_this_doctor.length}</td>
							<td>${op_slots_this_doctor.type}</td>
							<td>${op_slots_this_doctor.hospital.name}</td>
							<td>${op_slots_this_doctor.reservation.doctor.lastName}</td>
							<td>${op_slots_this_doctor.reservation.patient.lastName}</td>
							<td><a href="/remove_reservation/?id=${op_slots_this_doctor.id}">X</a></td>
						</tr>
					</c:forEach>

				</table>
				
				<h2>Reservierung f�r einen Patienten vornehmen</h2>
				
				<c:if test="${sent_reservation == true}">
				    <p class="success">Eine Reservierungsanfrage wurde gesendet f�r:
				    PatientID: ${sent_dto.patientID},
				    Fr�hester Zeitpunkt: ${sent_dto.dateStart}",
				    Sp�tester Zeitpunkt: ${sent_dto.dateEnd}",
				    Minimale Dauer in Minuten: ${sent_dto.minTime},
				    Typ: ${sent_dto.type}
				    </p>
				</c:if>
				
				<div id="stylized" class="searchForm">
					<form id="form" name="form" method="post" action="/doctor/reserve/">
						<input type="hidden" name="doctor_id" value="${doctorID}" />
						<table>
							<tbody>
							<tr>
								<td>
									<label>Fr�hester Zeitpunkt</label> 
									<input type="text" class="date" name="date_start"/>
								</td>
								<td>
									<label>Sp�tester Zeitpunkt</label> 
									<input type="text" class="date" name="date_end"/>
								</td>
							</tr>
							<tr>
								<td>
									<label>ID des Patienten</label> 
									<input type="text" name="patient_id"/>
								</td>
								<td>
									<label>ben�tigter Typ</label> 
										<select name="type">
											<option value="AUGEN">Augen</option>
											<option value="KARDIO">Kardio</option>
											<option value="OTHER">Andere</option>
										</select>
								</td>
								
							</tr>
							<tr>
								<td>
									<label>Dauer der Operation in Min</label> 
									<input type="number" name="min_time" min="0" max="300" />
								</td>
								<td>
									<label>Maximale Entfernung vom Heimatort des Patienten</label> 
									<input type="number" name="max_distance" min="0" max="40000" />
								</td>
								
							</tr>
							<tr>
								<td><label>&nbsp;</label>
									<button type="submit">Versuche OP-Slot zu finden</button></td>
							</tr>
						</tbody></table>

					</form>
				</div>
				
				
				
			<div class="elem_wrapper">
				<h2>Liste aller Patienten</h2>

				<table id='gradient-style' class='sortable'>
					<tr>
						<th>ID</th>
						<th>Username</th>
						<th>Vorname</th>
						<th>Nachname</th>
						<th>Geo-Koordinaten</th>
						<th>Patient l�schen</th>
					</tr>

					<c:forEach items="${patients}" var="patients">
						<tr>
							<td>${patients.id}</td>
							<td>${patients.username}</td>
							<td>${patients.firstName}</td>
							<td>${patients.lastName}</td>
							<td>Lat. ${patients.location[0]}, Long. ${patients.location[1]}</td>
							<td><a href="/admin/patient/delete/?id=${patients.id}">X</a></td>
						</tr>
					</c:forEach>

				</table>
			</div><!-- elem_wrapper close -->

								
				
				</div><!-- elem_wrapper close -->		
		</div>
	</div>
</div>
</body>
</html>