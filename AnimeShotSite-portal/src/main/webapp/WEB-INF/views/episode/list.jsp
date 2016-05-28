<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<title><c:out value='${animeInfo.fullName}' /> <c:out value='${animeInfo.secondName}' /> - 动画截图网</title>

<div class="container-fluid">
	<div class="row-fluid">
		<div class="row">
			<!-- 图片 -->
			<div class="col-sm-4 col-xs-12">
				<img src="${animeInfo.titleUrl.urlS}" width="100%" />
			</div>
			<!-- 信息 -->
			<div class="col-sm-8 col-xs-12">
				<!-- 名称 -->
				<div class="blockTitle" style="margin-left: 0px;">
					<span><c:out value='${animeInfo.fullName}' /></span> <span><c:out value='${animeInfo.secondName}' /></span>
				</div>

				<!-- 动画简介 -->
				<div style="text-align: left;">
					<div style="font-weight: 700;">动画简介</div>
					<div>
						${animeInfo.summary}
					</div>
				</div>
			</div>
		</div>

		<!-- 列表 -->
		<div class="row" style="margin-top: 10px;">
			<c:forEach items="${ animeEpisodePage.content }" var="animeEpisode">
				<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4 thumbnail">
					<a href="${ctx}/shot/list/${animeEpisode.id}">
						<img data-original="${animeEpisode.titleUrl.urlS}" class="img-responsive imagelazy">
						<div style="margin-top: 5px;">
							<c:out value='${animeEpisode.divisionName}' />
						</div>
					</a>
				</div>
			</c:forEach>
		</div>
	</div>
</div>

<div>
	<tags:pagination page="${animeEpisodePage}" paginationSize="10" />
</div>

<div>
	<a class="btn btn-primary" href="${ctx}/anime/list/${animeEpisode.animeInfoId}">返回动画列表</a>
</div>

