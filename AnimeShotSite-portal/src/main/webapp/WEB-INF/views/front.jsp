<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<head>
<title>动画截图网</title>
<meta name="keywords" content="动画截图网,动画截图,动漫截图,acg截图,动画图片,动漫图片,acg图片,新番截图,截图字幕,截图台词,字幕截图,截图时间,截图,截屏,图片,anime shot,anime image" />
<meta name="description" content="本站提供动画截图以及每张截图对应的字幕。截图一般以5秒为间隔，字幕尽量以外挂的形式显示。" />
</head>

<div class="container-fluid">
	<div class="row-fluid">
		<div align="left"><h1>本站提供动画截图以及每张截图对应的字幕。截图一般以5秒为间隔，字幕尽量以外挂的形式显示。</h1></div>
		<div align="left">PS：最近由于图片服务商硬盘崩溃，导致某些图片暂时无法读取。</div>

		<div>
			<div class="blockTitle">
				<span>最新动画剧集一览</span> <span class="count"><a href="${ctx}/anime/list">当前总剧集数：${animeEpisodeCount}</a></span>
			</div>
			<div>
				<div class="row">
					<c:forEach items="${ animeEpisodeList }" var="animeEpisode">
						<div class="col-lg-2 col-md-3 col-sm-4 col-xs-4 thumbnail">
							<a href="${ctx}/shot/list/${animeEpisode.id}">
								<img data-original="${animeEpisode.titleUrl.urlS}" class="img-responsive imagelazy" alt="<c:out value="${animeEpisode.animeInfo.fullName}" />">
								<div class="wordKeepLine" title="<c:out value='${animeEpisode.animeInfo.fullName}' />">
									<c:out value="${animeEpisode.animeInfo.fullName}" />
								</div>
								<div>
									<c:out value="${animeEpisode.divisionName}" />
								</div>
							</a>
						</div>
					</c:forEach>
				</div>
			</div>
		</div>

		<div id="站长推荐图片">
			<div class="blockTitle">
				<span>站长推荐图片</span>
			</div>
			<div class="row">
				<c:forEach items="${ masterRecommandShotList }" var="shot" varStatus="status" end="41">
					<div class="col-lg-2 col-sm-3 col-xs-4 thumbnail">
						<a href="${ctx}/shot/view/${shot.id}">
							<img data-original="${shot.urlS}" class="img-responsive imagelazy">
							<div class="wordKeepLine" title="<c:out value='${shot.animeInfo.fullName}' />">
								<c:out value="${shot.animeInfo.fullName}" />
							</div>
							<c:out value="${shot.animeEpisode.divisionName}" />
							<br>
							<div style="margin-top: 5px;">${shot.formatedMinSec}</div>
						</a>
					</div>
				</c:forEach>
			</div>
		</div>

		<div id="点击热度图片">
			<div class="blockTitle">
				<span>点击热度图片</span>
			</div>
			<div class="row">
				<c:forEach items="${ publicLikeShotList }" var="shot" varStatus="status" end="41">
					<div class="col-lg-2 col-sm-3 col-xs-4 thumbnail">
						<a href="${ctx}/shot/view/${shot.id}">
							<img data-original="${shot.urlS}" class="img-responsive imagelazy">
							<div class="wordKeepLine" title="<c:out value='${shot.animeInfo.fullName}' />">
								<c:out value="${shot.animeInfo.fullName}" />
							</div>
							<c:out value="${shot.animeEpisode.divisionName}" />
							<br>
							<div style="margin-top: 5px;">${shot.formatedMinSec}</div>
						</a>
					</div>
				</c:forEach>
			</div>
		</div>

		<div id="最新图片展示">
			<div class="blockTitle">
				<span>最新图片展示</span> <span class="count">当前总截图数：${shotCount}</span>
			</div>
			<div class="row">
				<c:forEach items="${ newestShotList }" var="shot" varStatus="status" end="41">
					<div class="col-lg-2 col-sm-3 col-xs-4 thumbnail">
						<a href="${ctx}/shot/view/${shot.id}">
							<img data-original="${shot.urlS}" class="img-responsive imagelazy">
							<div class="wordKeepLine" title="<c:out value='${shot.animeInfo.fullName}' />">
								<c:out value="${shot.animeInfo.fullName}" />
							</div>
							<c:out value="${shot.animeEpisode.divisionName}" />
							<br>
							<div style="margin-top: 5px;">${shot.formatedMinSec}</div>
						</a>
					</div>
				</c:forEach>
			</div>
		</div>
	</div>
</div>