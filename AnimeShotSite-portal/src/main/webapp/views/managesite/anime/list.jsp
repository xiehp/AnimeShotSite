<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<title>动画列表</title>

所有动画一览
<div class="row">
	<c:forEach items="${ animeInfoList }" var="anime">
		<div class="col-lg-3 col-sm-4 col-xs-6 thumbnail">
			<a href="${ctx}/managesite/anime/view/${anime.id}">
				<img data-original="${anime.titleUrl.urlS}" class="img-responsive imagelazy">
				<div style="margin-top: 5px;">
					<c:out value="${anime.name}" />
				</div>
			</a>
		</div>
	</c:forEach>
</div>