<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>500</title>
</head>
<body class="page-500-full-page">
	<div class="row-fluid">
		<p></p>
		<div class="span12 page-500">

			<div class=" number">500</div>

			<div class=" details">

				<h3>Opps, Something went wrong.</h3>

				<p>

					We are fixing it!
					<br />

					Please come back in a while.
					<br />
					<br />

				</p>

				<p>
					Error message:
					<br />
					<c:out value="${exception} "/>
				</p>

			</div>

		</div>

	</div>
</body>
</html>