<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false"%>
<html>
<head>
<title>Simple RabbitMQ Application</title>
</head>
<body>
	<h1>Simple RabbitMQ Application</h1>


	<h2>Publish a message to messenger queue:</h2>

	<form:form modelAttribute="message" action="/sendTo_messenger"
		method="post">
		<form:label for="value" path="value">Message to publish:</form:label>
		<form:input path="value" type="text" />

		<input type="submit" value="Publish" />
	</form:form>

	<h2>Publish a message to your own (allocator) queue:</h2>

	<form:form modelAttribute="message" action="/sendTo_allocator"
		method="post">
		<form:label for="value" path="value">Message to publish:</form:label>
		<form:input path="value" type="text" />

		<input type="submit" value="Publish" />
	</form:form>

	<c:if test="${publishedAllocator || publishedMessenger}">
		<p>Published a message!</p>
	</c:if>

	<h2>Get allocator queue message</h2>

	<form:form action="/get" method="post">
		<input type="submit" value="Get one" />
	</form:form>

	<c:choose>
		<c:when test="${got_queue_empty}">
			<p>Queue empty</p>
		</c:when>
		<c:when test="${got != null}">
			<p>
				Got message:
				<c:out value="${got}" />
			</p>
		</c:when>
	</c:choose>


</body>
</html>