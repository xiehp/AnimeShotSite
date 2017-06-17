<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="EpisodeFullName" value="${animeInfo.fullName} ${animeInfo.secondName} ${animeEpisode.divisionName}" />
<c:set var="EpisodeFullNameWithTime" value="${EpisodeFullName} ${shotInfo.formatedTimeChina}" />
<c:set var="FullImageUrl" value="${shotInfo.urlL}" />
<c:set var="PreFullImageUrl" value="${previousShotInfo.urlL}" />
<c:set var="NextFullImageUrl" value="${nextShotInfo.urlL}" />
<c:set var="ThisPageUrl" value="${siteBaseUrl}/mip/shot/view/${shotInfo.id}" />
<c:set var="DivPaddingBorderWidth" value="10" />
<c:set var="DivPaddingBorderHeight" value="10" />
<c:set var="ImageAspectRatio" value="${(!empty animeEpisode.height && !empty animeEpisode.width) ? animeEpisode.height/animeEpisode.width : 9/16}" />
<c:set var="coordsWidth" value="${ShotImgDivWidth > 0 ? ShotImgDivWidth-DivPaddingBorderWidth : 800}" />
<c:set var="coordsHeight" value="${ShotImgDivWidth > 0 ? (ShotImgDivWidth-DivPaddingBorderWidth) * ImageAspectRatio : 5000}" />

<head>
<title><c:out value='${EpisodeFullNameWithTime}' /> - 动画截图网</title>
<meta name="keywords" content="<c:out value='${EpisodeFullNameWithTime}' />,动画截图,动漫图片,字幕台词,图片外链" />
<meta name="description" content="<c:out value='${subtitleLineTextStr200}' />" />
<c:if test="${empty subtitleLineTextStr200}">
	<meta name="robots" content="noindex,follow" />
</c:if>

<meta property="og:title" content="<c:out value='${EpisodeFullNameWithTime}' />" />
<meta property="og:type" content="photo" />
<meta property="og:url" content="${ThisPageUrl}" />
<meta property="og:image" content="${FullImageUrl}" />
<meta property="og:image:type" content="image/jpeg" />

<!-- schema.org -->
<script type="application/ld+json">
{
	"@content":"http://schema.org",
	"@type":"ImageObject",
	"author":"acgimage.com",
	"contentUrl":"${FullImageUrl}",
	"datePublished":"<fmt:formatDate value="${shotInfo.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>",
	"name":"<c:out value='${EpisodeFullNameWithTime}' />",
	"description":"<c:out value='${subtitleLineTextStr100}' />",
}
</script>

</head>

<div>
	<div class="margin-bottom-3px">
		<h1 class="font-size-14px margin-0"><c:out value='${EpisodeFullNameWithTime}' /></h1>
	</div>
	<div class="margin-bottom-3px">
		<div class="shotTitleInfo">
			<div class="col-sm-4 col-xs-12 textLine">
				<label><spring:message code='时间戳' />：${shotInfo.timeStamp}</label>
			</div>
			<div class="col-sm-8 col-xs-12 textLine">
				<label><spring:message code='尺寸' />：</label><span id="imgWidth">${animeEpisode.width}</span>×<span id="imgHeight">${animeEpisode.height}</span>
			</div>
		</div>
	</div>

	<div class="width-98">
		<!-- 图片 -->
		<div id="shotImgDiv" class="thumbnail shotImgDivStyle" data-ImageAspectRatio="${ImageAspectRatio}">
			<c:if test="${not empty previousShotInfo.id}">
				<a class="shotImgHotPoint left-0" href="${siteBaseUrl}/mip/shot/view/${previousShotInfo.id}" title="<spring:message code='上一张' />"> </a>
			</c:if>
			<c:if test="${empty previousShotInfo.id}">
				<div class="shotImgHotPoint left-0" title="<spring:message code='没有上一张' />"> </div>
			</c:if>
			<c:if test="${not empty nextShotInfo.id}">
				<a class="shotImgHotPoint right-0" href="${siteBaseUrl}/mip/shot/view/${nextShotInfo.id}" title="<spring:message code='下一张' />"></a>
			</c:if>
			<c:if test="${empty nextShotInfo.id}">
				<div class="shotImgHotPoint right-0" title="<spring:message code='已经是最后一张了' />"> </div>
			</c:if>
			<mip-img id="shotImg" class="shotImg" src="${FullImageUrl}" alt="<c:out value='${EpisodeFullNameWithTime}' />" title="<c:out value='${EpisodeFullNameWithTime}' />"></mip-img>

			<mip-img src="${PreFullImageUrl}" class="display-none"></mip-img>
			<mip-img src="${NextFullImageUrl}" class="display-none"></mip-img>
		</div>
		<!-- 字幕 -->
		<c:if test="${!empty subtitleLineList}">
			<table class="shotSubtitle">
				<c:forEach items="${subtitleLineList}" var="subtitleLine" varStatus="status">
					<tr>
						<td class="noBreak font-size-10px" title="${subtitleLine.startTimeMinSecMicro}-${subtitleLine.endTimeMinSecMicro}">${subtitleLine.startTimeMinSec}-${subtitleLine.endTimeMinSec}</td>
						<td class="subtitleText" data-lang="${subtitleLine.language}" data-text='<c:out value="${subtitleLine.text}" />' data-sign="${empty subtitleLineSignList ? '' : subtitleLineSignList[status.index]}">
							<c:out value="${subtitleLine.text}" />
						</td>
					</tr>
				</c:forEach>
			</table>
		</c:if>
	</div>
</div>

<!--按钮 -->
<div class="buttonDiv">
	<c:if test="${IS_MASTER}">
		<a class="btn btn-primary btn-sm" onclick="home.masterLike('${MANAGE_URL_STR}/shot/masterLike', '${shotInfo.id}');">
			<span class="glyphicon glyphicon-star"></span>
			<spring:message code='推荐' />
			<div id="masterLike_${shotInfo.id}" class="badge">${shotInfo.masterRecommendRank}</div>
		</a>
		<a class="btn btn-primary btn-sm" onclick="home.setAnimeTitleImage('/tool/setAnimeTitleImage', '${animeInfo.id}', null, '${shotInfo.id}');">
			<spring:message code='动画图片' />
		</a>
		<a class="btn btn-primary btn-sm" onclick="home.setAnimeTitleImage('/tool/setAnimeTitleImage', null, '${animeEpisode.id}', '${shotInfo.id}');">
			<spring:message code='剧集图片' />
		</a>
		<a class="btn btn-primary btn-sm" onclick="deleteShotById('${shotInfo.id}');">
			<spring:message code='删除图片' />
		</a>
	</c:if>
	<a class="btn btn-primary btn-sm" href="${FullImageUrl}" target="_blank">
		<spring:message code='查看原图' />
	</a>
	<a class="btn btn-primary btn-sm" href="https://whatanime.ga/?loop&url=${FullImageUrl}" target="_blank">
		<spring:message code='查看短视频' />
	</a>
</div>

<div class="buttonDiv">
	<c:if test="${not empty previousShotInfo.id}">
		<a class="btn btn-primary btn-md postByFromXXX" href="${siteBaseUrl}/mip/shot/view/${previousShotInfo.id}" title="<c:out value='${EpisodeFullName}' /> <c:out value='${previousShotInfo.formatedTimeChina}' />">
			<spring:message code='上一张' />
		</a>
	</c:if>
	<c:if test="${not empty nextShotInfo.id}">
		<a class="btn btn-primary btn-md postByFromXXX" href="${siteBaseUrl}/mip/shot/view/${nextShotInfo.id}" title="<c:out value='${EpisodeFullName}' /> <c:out value='${nextShotInfo.formatedTimeChina}' />">
			<spring:message code='下一张' />
		</a>
	</c:if>
</div>


<div class="buttonDiv margin-5px">
	<a class="btn btn-primary btn-md" href="${siteBaseUrl}/mip/shot/list/${shotInfo.animeEpisodeId}${pageNumberUrl}" title="<c:out value='${EpisodeFullName}' />">
		<spring:message code='返回截图一览' />
	</a>
</div>

