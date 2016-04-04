<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<title>动画截图网 首页</title>

<div class="container-fluid">
	<div class="row-fluid">

		<div>
			<div class="blockTitle">
				<span>最新动画一览</span> <span class="count">当前总剧集数：${animeEpisodeCount}</span>
			</div>
			<div>

				<div class="row">
					<c:forEach items="${ animeEpisodeList }" var="animeEpisode">
						<div class="col-lg-3 col-sm-4 col-xs-6 thumbnail">
							<a href="${ctx}/shot/list/${animeEpisode.id}">
								<img data-original="${animeEpisode.titleUrl.urlS}" class="img-responsive imagelazy">
								<div style="margin-top: 5px;">
									<c:out value="${animeEpisode.animeInfo.fullName}" />
								</div>
								<div style="margin-top: 5px;">
									<c:out value="${animeEpisode.divisionName}" />
								</div>
							</a>
						</div>
					</c:forEach>
				</div>
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
							<div class="wordKeepLine" title="<c:out value="${shot.animeInfo.fullName}" />">
								<c:out value="${shot.animeInfo.fullName}" />
							</div>
							<c:out value="${shot.animeEpisode.divisionName}" />
							<br>
							<div style="margin-top: 5px;">${shot.formatedMinSec}<span style="color: lightgray;">:${shot.formatedMicroSec}</span>
							</div>
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
							<div class="wordKeepLine" title="<c:out value="${shot.animeInfo.fullName}" />">
								<c:out value="${shot.animeInfo.fullName}" />
							</div>
							<c:out value="${shot.animeEpisode.divisionName}" />
							<br>
							<div style="margin-top: 5px;">${shot.formatedMinSec}<span style="color: lightgray;">:${shot.formatedMicroSec}</span>
							</div>
						</a>
					</div>
				</c:forEach>
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
							<div class="wordKeepLine" title="<c:out value="${shot.animeInfo.fullName}" />">
								<c:out value="${shot.animeInfo.fullName}" />
							</div>
							<c:out value="${shot.animeEpisode.divisionName}" />
							<br>
							<div style="margin-top: 5px;">${shot.formatedMinSec}<span style="color: lightgray;">:${shot.formatedMicroSec}</span>
							</div>
						</a>
					</div>
				</c:forEach>
			</div>
		</div>
	</div>

</div>