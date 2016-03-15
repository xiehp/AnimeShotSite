<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<title>剧集列表</title>

所有动画一览
<div class="row">
	<c:forEach items="${ animeEpisodeList }" var="anime">
		<div class="col-lg-6 col-sm-6 col-xs-6">
			<a href="#">
				<img src="${animeEpisode.name}" class="thumbnail img-responsive">
			</a>
			<a href="${ctx}/managesite/animeEpisode/view/${animeEpisode.id}">${animeEpisode.name}</a>
		</div>
	</c:forEach>
</div>