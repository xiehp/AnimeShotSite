<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<title><c:out value='${animeInfo.fullName}' /> <c:out value='${animeInfo.secondName}' /> - 动画截图网</title>
<head>
<meta name="keywords" content="<c:out value='${animeInfo.fullName}' />,<c:out value='${animeInfo.secondName}' />,动画截图网,动画截图,动漫截图,动漫图片,动画图片,截图字幕" />
<meta name="description" content="<c:out value='${fn:substring(animeInfo.summaryCleanHtml, 0, 200)}' />" />

<style>
.AnimeSummary li>p {
	margin-bottom: 0px;
}
</style>
</head>
<div class="container-fluid">
	<div class="row-fluid">
		<div class="row">
			<!-- 图片 -->
			<div class="col-sm-4 col-xs-12">
				<img src="${animeInfo.titleUrl.urlL}" width="100%" />
			</div>

			<!-- 信息 -->
			<div class="col-sm-8 col-xs-12">
				<!-- 名称 -->
				<div class="blockTitle" style="margin-left: 0px;">
					<h1 style="font-size: 28px;"><c:out value='${animeInfo.fullName}' /> <c:out value='${animeInfo.secondName}' /></h1>
				</div>

				<!-- 动画简介 -->
				<c:if test="${!empty animeInfo.summary}">
					<div class="AnimeSummary" style="text-align: left;">
						<div style="font-weight: 700;"><spring:message code='动画简介' /></div>
						<div>${animeInfo.summary}</div>
					</div>
				</c:if>
			</div>
		</div>

		<!-- 列表 -->
		<div class="blockTitle">
			<span><spring:message code='动画剧集一览' /></span>
		</div>
		<div class="row" style="margin-top: 10px;">
			<c:forEach items="${ animeEpisodePage.content }" var="animeEpisode">
				<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4 thumbnail">
					<a href="${ctx}/shot/list/${animeEpisode.id}" title="<c:out value='${animeInfo.fullName}' /> <c:out value='${animeInfo.secondName}' /> <c:out value='${animeEpisode.divisionName}' /> <c:out value='${animeEpisode.title}' />">
						<img data-original="${animeEpisode.titleUrl.urlS}" class="img-responsive imagelazy">
						<div class="wordKeepLine" style="margin-top: 5px;">
							<c:out value='${animeEpisode.divisionName}' /> <c:out value='${animeEpisode.title}' />
						</div>
					</a>
				</div>
			</c:forEach>
		</div>
	</div>
</div>

<div>
	<tags:paginationRestPage page="${animeEpisodePage}" paginationSize="5" />
</div>

<div>
	<a class="btn btn-primary" href="${ctx}/anime"><spring:message code='返回动画列表' /></a>
</div>

