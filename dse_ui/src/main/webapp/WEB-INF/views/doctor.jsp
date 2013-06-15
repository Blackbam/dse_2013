<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html;charset=iso-8859-15" />
<!-- for correctly displayed German -->

<title>Ärztebereich</title>
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
	
	$(document).ready(function() {
		remote_notifications();
		setInterval(function() { remote_notifications(); }, 5000);
	});
	
	
	function remote_notifications() {
		
        $.ajax({
            type: "GET",
            url: "/notification/?id=${doctorID}",
            data: "",
            success: function(data){
                var html = "";
                $(data).each(function(key,obj) {
                	
                	html +='<div class="notification_element"><div class="noz"><span class="title">'+obj.title+' <span class="date">'+obj.date+'</span></div>';
	                	html += '<div class="notel_mas" title="'+obj.title+' - '+obj.date+'">';
		                	html += '<div class="notel_content">'+obj.content+'</div>';
	                	html += '</div>';
	                html += '</div>';
                });
                $('#notifications_content').html(html);
                
                $(".notification_element").on("click", function (e) {
                   
                 $(this).children('.notel_mas').each(function() {
                	$(this).dialog({
                		width:400,
                		height:300
                	}); 
                 });
                });
            },
            error: function(jqXHR, textStatus, errorThrown) {
            	console.log(jqXHR);
            	console.log(textStatus);
            	console.log(errorThrown);
            }
        });
	}
</script>
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

		<div id="notifications">
			<div id="notifications_header">Notifications</div>
			<div id="notifications_content"></div>
		</div>

		<div id="container">
			<div id="content" class="no-side-nav">

				<div class="elem_wrapper">
					<h2>Persönliche OP-Slot Liste</h2>

					<table id='gradient-style' class='sortable' style="width: 100%">
						<thead>
							<tr>
								<th>Datum</th>
								<th>Dauer</br>(in Minuten)</th>
								<th>Typ</th>
								<th>Krankenhaus</th>
								<th>Patient</th>
								<th>Reservierung</br>loeschen</th>
							</tr>
						</thead>
						<tfoot>
							<tr>
								<td colspan=6 style="text-align: left">${slotCount} 
									<c:choose>
										<c:when test="${slotCount == '1'}">Slot
										</c:when>
										<c:otherwise>Slots
										</c:otherwise>
									</c:choose> gefunden
								</td>
							</tr>
						</tfoot>

						<c:forEach items="${op_slots_this_doctor}" var="op_slots_this_doctor">
							<tr>
								<td><fmt:formatDate value="${op_slots_this_doctor.date}"
										pattern="dd.MM.yyyy HH:mm" /></td>
								<td>${op_slots_this_doctor.length}</td>
								<td>${op_slots_this_doctor.type}</td>
								<td>${op_slots_this_doctor.hospital.name}</td>
								<td>${op_slots_this_doctor.reservation.patient.lastName}</td>
								<td><a
									href="/doctor/remove_reservation/?opslot_id=${op_slots_this_doctor.id}">X</a></td>
							</tr>
						</c:forEach>

					</table>
					
					<h2>Suche</h2>

				<div id="stylized" class="searchForm">
					<form id="form" name="form" method="post" action="/doctor">
						<input type="hidden" name="id" value="${doctorID}"/>
						<table>
							<tr>
								<td><label>Datum<span class="small">Tag</span>
								</label> <input type="text" class="date" name="date" /></td>
								<td><label>Krankenhaus<span class="small">Name
											des Krankenhauses</span>
								</label> <input type="text" name="hospital" class="genericInput" /></td>
							</tr>
							<tr>
								<td><label>Uhrzeit<span class="small">Zeitraum</span>
								</label><input type="text" class="time" name="from" style="width: 75px" />
									<span style="margin-top: 5px; margin-right: 5px;">bis</span><input
									type="text" class="time" name="to" style="width: 75px" /></td>
								<td><label>Arzt<span class="small">Name des
											Arztes</span>
								</label> <input type="text" name="doctor" class="genericInput" /></td>
							</tr>
							<tr>
								<td><label>Status<span class="small">Verfügbarkeit</span>
								</label><select name="status">
										<option value="unset">-</option>
										<option value="available">frei</option>
										<option value="reserved">reserviert</option>
								</select></td>
								<td><label>Typ<span class="small">Art der
											Operation</span>
								</label> <input type="text" name="type" class="genericInput" /></td>
							</tr>
							
							<tr>
								<td><label>&nbsp;</label>
									<button type="submit">suchen</button></td>
							</tr>
						</table>

					</form>
				</div>

					<h2>Reservierung für einen Patienten vornehmen</h2>

					<c:if test="${sent_reservation == true}">
						<p class="success">Eine Reservierungsanfrage wurde gesendet
							für: PatientID: ${sent_dto.patientID}, Frühester Zeitpunkt:
							${sent_dto.dateStart}", Spätester Zeitpunkt:
							${sent_dto.dateEnd}", Minimale Dauer in Minuten:
							${sent_dto.minTime}, Typ: ${sent_dto.type}</p>
					</c:if>

					<div id="stylized" class="searchForm">
						<form id="form" name="form" method="post"
							action="/doctor/reserve/">
							<input type="hidden" name="doctor_id" value="${doctorID}" />
							<table>
								<tbody>
									<tr>
										<td><label>Frühester Zeitpunkt</label> <input type="text"
											class="date" name="date_start" /></td>
										<td><label>Spätester Zeitpunkt</label> <input type="text"
											class="date" name="date_end" /></td>
									</tr>
									<tr>
										<td><label>ID des Patienten</label> <input type="text"
											name="patient_id" /></td>
										<td><label>benötigter Typ</label> <select name="type">
												<option value="AUGEN">Augen</option>
												<option value="KARDIO">Kardio</option>
												<option value="OTHER">Andere</option>
										</select></td>

									</tr>
									<tr>
										<td><label>Dauer der Operation in Min</label> <input
											type="number" name="min_time" min="0" max="300" /></td>
										<td><label>Maximale Entfernung vom Heimatort des
												Patienten</label> <input type="number" name="max_distance" min="0"
											max="40000" /></td>

									</tr>
									<tr>
										<td><label>&nbsp;</label>
											<button type="submit">Versuche OP-Slot zu finden</button></td>
									</tr>
								</tbody>
							</table>

						</form>
					</div>

					<div class="elem_wrapper">
						<h2>Liste aller Patienten</h2>

						<table id='gradient-style' class='sortable' style="width: 100%">
							<thead>
								<tr>
									<th>ID</th>
									<th>Username</th>
									<th>Vorname</th>
									<th>Nachname</th>
									<th>Geo-Koordinaten</th>
									<th>Patient löschen</th>
								</tr>
							</thead>

							<c:forEach items="${patients}" var="patients">
								<tr>
									<td>${patients.id}</td>
									<td>${patients.username}</td>
									<td>${patients.firstName}</td>
									<td>${patients.lastName}</td>
									<td>Lat. ${patients.location[0]}, Long.
										${patients.location[1]}</td>
								</tr>
							</c:forEach>

						</table>
					</div>
					<!-- elem_wrapper close -->

				</div>
				<!-- elem_wrapper close -->
			</div>
		</div>
	</div>
</body>
</html>