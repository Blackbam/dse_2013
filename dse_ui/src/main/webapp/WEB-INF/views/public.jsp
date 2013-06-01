<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Public Access</title>
<link rel="stylesheet" href="static/css/main.css" type="text/css"></link>
<link rel="stylesheet" href="static/css/colors.css" type="text/css"></link>
<link rel="stylesheet" href="static/css/local.css" type="text/css"></link>
<link rel="stylesheet" href="static/css/normalize.css" type="text/css" />
<link rel="stylesheet" href="static/css/datepicker.css" type="text/css" />
<script type="text/javascript" src="static/js/sorttable.js"></script>
<script type="text/javascript"
	src="http://code.jquery.com/jquery-1.9.0.min.js"></script>
<script type="text/javascript"
	src="http://code.jquery.com/ui/1.10.0/jquery-ui.min.js"></script>
<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/i18n/jquery-ui-i18n.min.js"></script>
<script>
	jQuery(function($) {
		$(".date").datepicker($.datepicker.regional['de']);
	});
</script>
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

				<p>
					<a href="/login">User login</a>
				</p>

				<h2>OP-Slots</h2>

				<table id='gradient-style' class='sortable'>
					<tr>
						<th>Datum</th>
						<th>von</th>
						<th>bis</th>
						<th>Typ</th>
						<th>Krankenhaus</th>
						<th>Arzt</th>
						<th>Status</th>
					</tr>

					<c:forEach items="${opSlots}" var="opSlots">
						<tr>
							<td>${opSlots.dateString}</td>
							<td>${opSlots.startTimeString}</td>
							<td>${opSlots.endTimeString}</td>
							<td>${opSlots.typeString}</td>
							<td>${opSlots.hospital.name}</td>
							<td><c:if test="${not empty opSlots.reservation}">
								    ${opSlots.reservation.doctor.name}
								</c:if> <c:if test="${empty opSlots.reservation}">
								    -
								</c:if></td>
							<td><c:if test="${not empty opSlots.reservation}">
								    reserviert
								</c:if> <c:if test="${empty opSlots.reservation}">
								    frei
								</c:if></td>
						</tr>
					</c:forEach>

				</table>

				<h2>OP-Slot Suche</h2>

				<div id="filter_list" style="margin: 20px;">
					<form method="post" action="/public">
						<table>
							<tr>
								<td align="right">Datum:</td>
								<td><input type="text" class="date" name="date" /></td>
							</tr>
							<tr>
								<td align="right">Uhrzeit:</td>
								<td><input type="text" name="from" /> - <input type="text"
									name="to" /></td>
							</tr>
							<tr>
								<td align="right">Krankenhaus:</td>
								<td><input type="text" name="hospital" /></td>
							</tr>
							<tr>
								<td align="right">Arzt:</td>
								<td><input type="text" name="doctor" /></td>
							</tr>
							<tr>
								<td align="right">Typ:</td>
								<td><input type="text" name="type" /></td>
							</tr>
							<tr>
								<td align="right">Status:</td>
								<td><select name="status">
										<option value="unset">-</option>
										<option value="available">frei</option>
										<option value="reserved">reserviert</option>
								</select></td>
							</tr>
							<tr>
								<td align="right"></td>
								<td><input type="submit" value="suchen" /></td>
							</tr>
						</table>

					</form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
