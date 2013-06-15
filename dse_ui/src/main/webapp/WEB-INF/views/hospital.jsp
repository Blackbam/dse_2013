<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Krankenhausbereich</title>
<link rel="stylesheet" href="/static/css/main.css" type="text/css"></link>
<link rel="stylesheet" href="/static/css/colors.css" type="text/css"></link>
<link rel="stylesheet" href="/static/css/local.css" type="text/css"></link>
<link rel="stylesheet" href="/static/css/normalize.css" type="text/css" />
<link rel="stylesheet" href="/static/css/datepicker.css" type="text/css" />
<link rel="stylesheet" href="/static/css/jquery.timepicker.css" type="text/css" />
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
					<a href="" title="Krankenhausbereich" rel="home">Krankenhausbereich</a>
				</div>
			</div>
		</div>
		<!-- /header -->
		<div id="container">
			<div id="content" class="no-side-nav">

				<h2>Krankenhaus Daten</h2>

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
						<p class="success">
							Ein neuer OP Slot wurde angelegt: ID: ${new_opslot.id}, In
							Krankenhaus: ${new_opslot.hospital.name}, Datum:
							<fmt:formatDate value="${new_opslot.date}"
								pattern="dd.MM.yyyy HH:mm" />
							, Dauer in Minuten: ${new_opslot.length}, Typ: ${new_opslot.type}
						</p>
					</c:if>
					<c:if test="${false == add_opslot}">
						<p class="error">Der OP-Slot konnten nicht angelegt werden.
							Bitte überprüfen sie die Parameter.</p>
					</c:if>

					<c:if test="${true == delete_opslot}">
						<p class="success">OP-Slot erfolgreich gelöscht.</p>
					</c:if>
					<c:if test="${false == delete_hospital}">
						<p class="error">Der OP-Slot konnte nicht gelöscht werden.
							Wahrscheinlich ist eine Reservierung aktiv?</p>
					</c:if>

					<div id="opslot_anlegen">
						<form method="post" action="/hospital/op_slot/create/">
							<input type="hidden" name="hospital_id" value="${hospital.id}" />
							<label style="width: 160px;">Date (yyyy-mm-dd hh:ii)</label><input type="text"
								name="date" />
							<div style="clear: both;"></div>
							<br /> <label style="width: 160px;">Dauer (in Minuten)</label><input type="text"
								name="length" />
							<div style="clear: both;"></div>
							<br /> <label style="width: 160px;">Typ</label> <select name="type">
								<option value="AUGEN">Augen</option>
								<option value="KARDIO">Kardio</option>
								<option value="OTHER">Andere</option>
							</select>
							<div style="clear: both;"></div>
							<br /> <input type="submit" value="OP-Slot anlegen" />
						</form>
					</div>

					<h2>Liste von Operation-Slots</h2>

					<table id='gradient-style' class='sortable' style="width: 100%">
						<thead>
							<tr>
								<th>Datum & Uhrzeit</th>
								<th>Dauer<br />(in Minuten)
								</th>
								<th>Typ</th>
								<th>Patient</th>
								<th>Arzt</th>
							</tr>
						</thead>
						<tfoot>
							<tr>
								<td colspan=6 style="text-align: left">${slotCount} <c:choose>
										<c:when test="${slotCount == '1'}">Slot
										</c:when>
										<c:otherwise>Slots
										</c:otherwise>
									</c:choose> gefunden
								</td>
							</tr>
						</tfoot>

						<c:forEach items="${opSlots}" var="opSlots">
							<tr>
								<td><fmt:formatDate value="${opSlots.date}"
										pattern="dd.MM.yyyy HH:mm" /></td>
								<td>${opSlots.length}</td>
								<td>${opSlots.type}</td>
								<td><c:choose>
										<c:when test="${opSlots.reservation.patient.lastName != null}">${opSlots.reservation.patient.lastName}
										</c:when>
										<c:otherwise>-
										</c:otherwise>
									</c:choose></td>
								<td><c:choose>
										<c:when test="${opSlots.reservation.doctor.lastName != null}">${opSlots.reservation.doctor.lastName}
										</c:when>
										<c:otherwise>-
										</c:otherwise>
									</c:choose></td>
							</tr>
						</c:forEach>

					</table>

					<h2>Suche</h2>

					<div id="stylized" class="searchForm">
						<form id="form" name="form" method="post" action="/hospital">
							<input type="hidden" name="id" value="${hospitalId}" />
							<table>
								<tr>
									<td><label>Datum<span class="small">Tag</span>
									</label> <input type="text" class="date" name="date" /></td>
									<td><label>Patient<span class="small">Name
												des Patienten</span>
									</label> <input type="text" name="patient" class="genericInput" /></td>
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
									<td><label>Typ<span class="small">Art der
												Operation</span>
									</label> <input type="text" name="type" class="genericInput" /></td>
									<td><label>Status<span class="small">Verfügbarkeit</span>
									</label><select name="status">
											<option value="unset">-</option>
											<option value="available">frei</option>
											<option value="reserved">reserviert</option>
									</select></td>
								</tr>
								<tr>
									<td><label>&nbsp;</label>
										<button type="submit">suchen</button></td>
								</tr>
							</table>
						</form>
					</div>
				</div>
				<!-- elem_wrapper close -->


			</div>
		</div>
	</div>
</body>
</html>