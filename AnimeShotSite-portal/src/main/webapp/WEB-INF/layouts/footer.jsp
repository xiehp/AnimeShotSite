<%@ page language="java" contentType="text/html; UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
	function backToLogin() {
		window.location.href = baseUrl;
	}

	function backToIndex() {
		window.location.href = baseUrl;
	}
</script>

<div style="height: 60px; background-color: black; margin-top: 20px;">
	<div style="padding-top: 20px;" align="center">
		<a href="http://www.miitbeian.gov.cn" style="color: white;">沪ICP备16008261号</a>
	</div>
</div>

<%@ include file="/WEB-INF/layouts/modal.jsp" %>