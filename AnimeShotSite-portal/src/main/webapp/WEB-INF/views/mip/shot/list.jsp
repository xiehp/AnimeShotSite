<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<title><c:out value='${animeInfo.fullName}' /> <c:out value='${animeInfo.secondName}' /> <c:out value='${animeEpisode.divisionName}' /> <c:out value='${animeEpisode.title}' /> 第${shotInfoPage.number + 1}页 - 动画截图网</title>
<head>
<c:if test="${shotInfoPage.number == 0}">
	<meta name="keywords" content="<c:out value='${animeEpisode.fullName}' />,<c:out value='${animeInfo.secondName}' />,第${shotInfoPage.number + 1}页,动画截图网,动画截图,动漫截图,动漫图片,动画图片,截图字幕" />
	<meta name="description" content="${fn:substring(shotInfoPage.number == 0 ? animeEpisode.summaryCleanHtml : '', 0, 200)}" />
</c:if>
<c:if test="${shotInfoPage.number > 0}">
	<meta name="robots" content="noindex,follow" />
</c:if>

</head>

<c:if test="${IS_MANAGER}">
	<script>
		function masterLike(id) {
			home.masterLike("${MANAGE_URL_STR}/shot/masterLike", id, "#masterLike_" + id, "#publicLike_" + id);
		}
	</script>
</c:if>

<div>
	<!-- 标题 -->
	<div class="shotList-blockTitle">
		<h1>
			<c:out value='${animeInfo.fullName}' />
			<c:out value='${animeInfo.secondName}' />
			<c:out value='${animeEpisode.divisionName}' />
			<small><c:out value='${animeEpisode.title}' /></small>
		</h1>
		<c:if test="${1 != 1 and not empty animeEpisode.summary and shotInfoPage.number == 0}">
			<button type="button" class="btn btn-rimary btn-xs" data-toggle="collapse" data-target="#episodeSmmary">
				<spring:message code='显示剧集简介' />
			</button>
		</c:if>
	</div>

	<div class="text-align-left width-95">
		<!-- 剧集简介 -->
		<div id="episodeSmmary" class="collapse">
			<c:if test="${not empty animeEpisode.summary and shotInfoPage.number == 0}">
				<spring:message code='剧情介绍' />：
					${animeEpisode.summary}
				</c:if>
		</div>
	</div>

	<!-- 截图一览 -->
	<div>
		<c:forEach items="${ shotInfoPage.content }" var="shotInfo">
			<div class="listImg min-height-100px">
				<a href="${siteBaseUrl}/mip/shot/view/${shotInfo.id}" title="<c:out value='${animeEpisode.fullName}' /> <c:out value='${shotInfo.formatedTimeChina}' />">
					<mip-img src="${shotInfo.urlS}" data-original="${shotInfo.urlS}" class="img-responsive imagelazy"></mip-img>
					<div class="margin-top-5px">
						${shotInfo.formatedMinSec}<span class="color-lightgray ${shotInfo.formatedMicroSec > 0 ? '' : ' display-none'}">:${shotInfo.formatedMicroSec}</span>
					</div>
				</a>
				<c:if test="${IS_MASTER}">
					<div class="btn btn-primary btn-xs" onclick="home.publicLike('${shotInfo.id}');">
						<span class="glyphicon glyphicon-star"></span>
						<spring:message code='喜欢' />
						<div id="publicLike_${shotInfo.id}" class="badge">${shotInfo.publicLikeCount}</div>
					</div>
					<div class="btn btn-primary btn-xs" onclick="home.masterLike('${MANAGE_URL_STR}/shot/masterLike', '${shotInfo.id}');">
						<span class="glyphicon glyphicon-star"></span>
						<spring:message code='推荐' />
						<div id="masterLike_${shotInfo.id}" class="badge">${shotInfo.masterRecommendRank}</div>
					</div>
				</c:if>
			</div>
		</c:forEach>
	</div>
</div>

<div>
	<tags:paginationRestPage page="${shotInfoPage}" paginationSize="5" />
</div>

<div>
	<a class="btn btn-primary" href="${siteBaseUrl}/mip/episode/list/${animeEpisode.animeInfoId}" title="<c:out value='${animeInfo.fullName}' /> <c:out value='${animeInfo.secondName}' />">
		<spring:message code='返回剧集列表' />
	</a>
</div>

<div class="margin-top-50px width-98">
	<c:if test="${!empty subtitleLineList}">
		<div class="blod">
			<spring:message code='动画字幕台词一览' />
		</div>
		<div>
			<pre class="subtitleTable">
<c:out value="${animeEpisode.fullName}" /> <c:out value='${animeEpisode.title}' />
<c:forEach items="${ subtitleLineList }" var="subtitleLine">${subtitleLine.startTimeMinSecMicro} ${subtitleLine.endTimeMinSecMicro} <c:out value='${subtitleLine.text}' />
</c:forEach>
						</pre>
		</div>
	</c:if>
</div>
