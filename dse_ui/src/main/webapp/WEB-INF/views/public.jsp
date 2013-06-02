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
<link rel="stylesheet" href="static/css/jquery.timepicker.css"
	type="text/css" />
<script type="text/javascript" src="static/js/sorttable.js"></script>
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
<script>
	jQuery(function($) {
		$('.time').timepicker({
			'minTime' : '07:00',
			'maxTime' : '17:00',
			'timeFormat' : 'H:i'
		});
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

				<table id='gradient-style' class='sortable' style="width: 100%">
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
				
				<h2>Suche</h2>

				<div id="stylized" class="searchForm">
					<form id="form" name="form" method="post" action="/public">
						<table>
							<tr>
								<td><label>Datum<span class="small">Tag des
											OP-Slots</span>
								</label> <input type="text" class="date" name="date"
									style="width: 181px" /></td>
								<td><label>Krankenhaus<span class="small">Name
											des Krankenhauses</span>
								</label> <input type="text" name="hospital" style="width: 181px" /></td>
							</tr>
							<tr>
								<td><label>Uhrzeit<span class="small">todo</span>
								</label><input type="text" class="time" name="from" style="width: 75px" />
									<span style="margin-top: 5px; margin-right: 5px;">bis</span><input
									type="text" class="time" name="to" style="width: 75px" /></td>
								<td><label>Arzt<span class="small">Name des
											Arztes</span>
								</label> <input type="text" name="doctor" style="width: 181px" /></td>
							</tr>
							<tr>
								<td><label>Status<span class="small">todo</span>
								</label><select name="status">
										<option value="unset">-</option>
										<option value="available">frei</option>
										<option value="reserved">reserviert</option>
								</select></td>
								<td><label>Type<span class="small">Art der
											OP</span>
								</label> <input type="text" name="type" style="width: 181px" /></td>
							</tr>
							<tr>
								<td><label>&nbsp;</label><button type="submit">suchen</button></td>
							</tr>
						</table>

					</form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
