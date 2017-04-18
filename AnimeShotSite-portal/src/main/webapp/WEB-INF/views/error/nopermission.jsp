<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<c:set var="ctx" value="${pageContext.request.contextPath}"/>     
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>no permission</title>
<link href="${ctx}/static/media/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
<link href="${ctx}/static/media/css/error.css" rel="stylesheet" type="text/css"/>
</head>
<body  class="page-500-full-page">
<div class="row-fluid">

		<div class="span12 page-500">

			<div class=" number">

				Wrong!

			</div>

			<div class=" details">

				<h3>You have no permission</h3>

				<p>

					Please contact the administrator.<br /><br />

				</p>

			</div>

		</div>

	</div>
</body>
</html>