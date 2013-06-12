<%-- Set the content type header with the JSP directive --%>
<%@ page contentType="application/json" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false"%>

<%-- Set the content disposition header --%>
<%
   // Returns all employees (active and terminated) as json.
   response.setContentType("application/json");
   response.setHeader("Content-Disposition", "inline");
%>

<%@ page language="java"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="javax.servlet.http.*"%>

[
<c:forEach items="${notifications}" var="notifications" varStatus="status">
		${status.first ? '' : ','} {"id":"${notifications.id}", "title":"${notifications.title}", "content":"${notifications.content}","date":"<fmt:formatDate value="${notifications.date}" pattern="dd.MM.yyyy HH:mm" />","read":"${notifications.read}"}
</c:forEach>
]