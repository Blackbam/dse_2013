<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Patientenbereich</title>
<link rel="stylesheet" href="/static/css/main.css" type="text/css"></link>
<link rel="stylesheet" href="/static/css/colors.css" type="text/css"></link>
<link rel="stylesheet" href="/static/css/local.css" type="text/css"></link>
<link rel="stylesheet" href="/static/css/custom.css" type="text/css"></link>
<link rel="stylesheet" href="/static/css/datepicker.css" type="text/css" />
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
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
            url: "/notification/?id=${patientID}",
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
					<a href="" title="Patientenbereich" rel="home">Patientenbereich</a>
				</div>
			</div>
		</div>
		<!-- /header -->
		
		<div id="notifications">
			<div id="notifications_header">
				Notifications
			</div>
			<div id="notifications_content">
				
			</div>
		</div>
		
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
