<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<head>
<c:if test="${not isSinglePage}">
	<title><c:out value='乖离性百万亚瑟王 伤害计算器 乖离性MA 计算公式 乖離性ミリオンアーサー ダメージ電卓' /></title>
	<meta name="keywords" content="乖离性百万亚瑟王 伤害计算器 乖离性MA 计算公式 乖離性ミリオンアーサー ダメージ電卓" />
	<meta name="description" content="乖离性百万亚瑟王 伤害计算器 乖离性MA 计算公式 乖離性ミリオンアーサー ダメージ電卓" />
</c:if>
<c:if test="${isSinglePage}">
	<title><c:out value='${commonRecord.name}' /> - <c:out value='${commonRecord.userName}' /> - 乖离性MA 伤害计算器</title>
	<meta name="keywords" content="<c:out value='${commonRecord.name}' /> - <c:out value='${commonRecord.userName}' /> - 乖离性MA 伤害计算器" />
	<meta name="description" content="<c:out value='${commonRecord.name}' /> - <c:out value='${commonRecord.userName}' /> - 乖离性MA 伤害计算器" />
</c:if>

<style>
body {
	overflow: scroll;
}

.advancedSettingDiv {
	display: none;
}
</style>
</head>

<script type="text/javascript">

	function ajax() {
		home.post();
	}
	
	
	function post4() {
		$.homePost("/testController/goPageResult4");
	}
</script>

<div class="row">
	<input type="button" onclick="$.homePost('${ctx}/testController/test1')" class="btn btn-sm btn-primary" value="test1" />

	<form action="${ctx}/testController/test2" method="post">
		<input type="submit" class="btn btn-sm btn-primary" value="test2" />
	</form>

	<form action="${ctx}/testController/goPageResult1" method="post">
		<a href="#" onclick="$.homePost('/testController/goPageResult1');" class="btn btn-sm btn-primary"  >goPageResult1</a>
	</form>

	<form action="${ctx}/testController/goPageResult2" method="post">
		<a href="#" onclick="$.homePost('/testController/goPageResult2');" class="btn btn-sm btn-primary" >goPageResult2</a>
	</form>

	<form action="${ctx}/testController/goPageResult3" method="post">
		<a href="#" onclick="$.homePost('/testController/goPageResult3');" class="btn btn-sm btn-primary" >goPageResult3 异常抛出</a>
	</form>

	<form action="${ctx}/testController/goPageResult4" method="post">
		<a href="#" onclick="$.homePost('/testController/goPageResult4');" class="btn btn-sm btn-primary">goPageResult4 异常抛出codeApplicationException</a>
		
	</form>
</div>
