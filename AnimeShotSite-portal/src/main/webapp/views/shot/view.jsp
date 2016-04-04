<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<title>动画剧集截图</title>

<caption>动画名：${animeEpisode.fullName}</caption>
<br>
<caption>时间：${shotInfo.timeStamp}</caption>
<br>
<caption>
	时间：
	<fmt:formatNumber value="${shotInfo.timeStamp / 1000 / 60}" pattern="#" />
	:
	<fmt:formatNumber value="${shotInfo.timeStamp / 1000 % 60}" pattern="#" />
</caption>
<br>
<div align="center">

	<div>
		<img alt="" src="${shotInfo.tietukuOUrl}" width="1000px">
	</div>
</div>
<div style="padding: 5px;">
	<c:if test="${!empty previousShotInfo.id}">
		<a style="margin: 10px;" class="btn btn-primary" href="${ctx}/shot/view/${previousShotInfo.id}">上一张</a>
		<img alt="" src="${previousShotInfo.tietukuOUrl}" style="display: none;">
	</c:if>
	<c:if test="${!empty nextShotInfo.id}">
		<a style="margin: 10px;" class="btn btn-primary" href="${ctx}/shot/view/${nextShotInfo.id}">下一张</a>
		<img alt="" src="${nextShotInfo.tietukuOUrl}" style="display: none;">
	</c:if>
</div>

<div>
	<a class="btn btn-primary" href="${ctx}/shot/list/${shotInfo.animeEpisodeId}">返回截图一览</a>
</div>