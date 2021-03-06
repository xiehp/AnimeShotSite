<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<head>
<title>动画截图网</title>
<meta name="keywords" content="动画截图网,动画截图,动漫截图,动漫图片,动画图片,acg截图,acg图片,新番截图,截图字幕,截图台词,字幕截图,高清截图,画像,截屏,图片外链,Gif动态图片,スクリーンショット,anime shot,anime image" />
<meta name="description" content="本站提供动画完整的动漫图片，动画图片以及字幕台词。截图图片一般以5秒为间隔，图片尽量为1080P，不包含字幕台词。字幕台词尽量为中日双语字幕，可以简繁切换，并且以外挂的形式显示。同时本站提供获取Gif动态图片的功能。" />
<meta name='yandex-verification' content='64ecf3910c1154ef' />
<meta name="google-site-verification" content="xgQXjnyjdOEL3cepdnkjkgfsegefTyyOGKuSSSanTjI" />
</head>

<div>
	<div id="最新动画剧集一览">
		<div class="blockTitle">
			<a href="${siteBaseUrl}/mip/anime">
				<span><spring:message code='最新动画剧集一览' /></span> <span class="count"> <spring:message code='当前剧集总数' /> ：${animeEpisodeCount}
				</span>
			</a>
		</div>
		<div>
			<c:forEach items="${ animeEpisodeList }" var="animeEpisode" end="35">
				<div class="listImg">
					<a href="${siteBaseUrl}/mip/shot/list/${animeEpisode.id}" title="<c:out value='${animeEpisode.animeInfo.fullName}' /> <c:out value='${animeEpisode.animeInfo.secondName}' /> <c:out value='${animeEpisode.divisionName}' /> <c:out value='${animeEpisode.title}' />">
						<mip-img src="${animeEpisode.titleUrl.urlS}" data-original="${animeEpisode.titleUrl.urlS}" class="img-responsive imagelazy" alt="<c:out value='${animeEpisode.fullName}' />"></mip-img>
						<div class="wordKeepLine">
							<c:out value='${animeEpisode.animeInfo.fullName}' />
							<c:out value='${animeEpisode.animeInfo.secondName}' />
						</div>
						<div class="wordKeepLine">
							<c:out value='${animeEpisode.divisionName}' />
							<c:out value='${animeEpisode.title}' />
						</div>
					</a>
				</div>
			</c:forEach>
		</div>
	</div>

	<div id="推荐动漫图片">
		<div class="blockTitle">
			<a href="${siteBaseUrl}/mip/shot/recommend">
				<span><spring:message code='推荐动漫图片' /></span>
			</a>
		</div>
		<div>
			<c:forEach items="${ masterRecommandShotList }" var="shot" varStatus="status" end="29">
				<div class="listImg">
					<a href="${siteBaseUrl}/mip/shot/view/${shot.id}" title="<c:out value='${shot.animeEpisode.fullName}' /> <c:out value='${shot.animeInfo.secondName}' /> ${shot.formatedTimeChina}">
						<mip-img src="${shot.urlS}" data-original="${shot.urlS}" class="img-responsive imagelazy"></mip-img>
						<div class="wordKeepLine">
							<c:out value="${shot.animeInfo.fullName}" />
							<c:out value='${shot.animeInfo.secondName}' />
						</div>
						<div class="wordKeepLine">
							<c:out value="${shot.animeEpisode.divisionName}" />
							${shot.formatedTimeChina}
						</div>
					</a>
				</div>
			</c:forEach>
		</div>
	</div>

	<c:if test="${false}">
		<div id="点击热度图片">
			<div class="blockTitle">
				<span><spring:message code='点击热度图片' /></span>
			</div>
			<div>
				<c:forEach items="${ publicLikeShotList }" var="shot" varStatus="status" end="23">
					<div class="listImg">
						<a href="${siteBaseUrl}/mip/shot/view/${shot.id}" title="<c:out value='${shot.animeEpisode.fullName}' /> <c:out value='${shot.animeInfo.secondName}' /> ${shot.formatedTimeChina}">
							<mip-img src="${shot.urlS}" data-original="${shot.urlS}" class="img-responsive imagelazy"></mip-img>
							<div class="wordKeepLine">
								<c:out value="${shot.animeInfo.fullName}" />
								<c:out value='${shot.animeInfo.secondName}' />
							</div>
							<div class="wordKeepLine">
								<c:out value="${shot.animeEpisode.divisionName}" />
								${shot.formatedTimeChina}
							</div>
						</a>
					</div>
				</c:forEach>
			</div>
		</div>
	</c:if>

	<div id="最新截图展示">
		<div class="blockTitle">
			<span><spring:message code='最新截图展示' /></span> <span class="count"><spring:message code='当前截图总数' />：${shotCount}</span>
		</div>
		<div>
			<c:forEach items="${ newestShotList }" var="shot" varStatus="status" end="11">
				<div class="listImg">
					<a href="${siteBaseUrl}/mip/shot/view/${shot.id}" title="<c:out value='${shot.animeEpisode.fullName}' /> <c:out value='${shot.animeInfo.secondName}' /> ${shot.formatedTimeChina}">
						<mip-img src="${shot.urlS}" data-original="${shot.urlS}" class="img-responsive imagelazy"></mip-img>
						<div class="wordKeepLine">
							<c:out value="${shot.animeInfo.fullName}" />
							<c:out value='${shot.animeInfo.secondName}' />
						</div>
						<div class="wordKeepLine">
							<c:out value="${shot.animeEpisode.divisionName}" />
							${shot.formatedTimeChina}
						</div>
					</a>
				</div>
			</c:forEach>
		</div>
	</div>

	<div id="最新截图展示">
		<div class="blockTitle">
			<a href="${siteBaseUrl}/mip/anime">
				<span><spring:message code='动画列表一览' /></span>
			</a>
		</div>
		<div>
			<c:forEach items="${ animeInfoPage.content }" var="anime">
				<div class="listImg">
					<a href="${siteBaseUrl}/mip/episode/list/${anime.id}" title="<c:out value='${anime.fullName}' /> <c:out value='${anime.secondName}' />">
						<mip-img src="${anime.titleUrl.urlS}" data-original="${anime.titleUrl.urlS}" class="img-responsive imagelazy"></mip-img>
						<div class="wordKeepLine margin-top-5px">
							<c:out value='${anime.fullName}' />
							<c:out value='${anime.secondName}' />
						</div>
					</a>
				</div>
			</c:forEach>
		</div>
	</div>

	<div align="left">
		<h1 class="font-size-16px">
			<spring:message code='动画截图网' />
			<spring:message code='本站说明' />
		</h1>
		<p class="font-size-14px">
			<spring:message code='本站说明具体内容' />
		</p>
	</div>

</div>