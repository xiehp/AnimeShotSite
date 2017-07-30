<%@page import="xie.common.utils.XSSHttpUtil"%>
<%@page import="xie.animeshotsite.db.vo.ShotInfoVO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="EpisodeFullName" value="${animeInfo.fullName} ${animeInfo.secondName} ${animeEpisode.divisionName}" />
<c:set var="EpisodeFullNameWithTime" value="${EpisodeFullName} ${shotInfo.formatedTimeChina}" />
<%
	//ShotInfoVO vo = request.getAttribute("shotInfo");
	//if (vo.getUrlL().contains("05bc88b3992add5d")){
	//	vo.setTietukuUrlPrefix("http://sdfsdfsdfsdf3254354.com/ddd/")
	//}
%>
<c:set var="FullImageUrl" value="${shotInfo.urlL}" />
<c:set var="PreFullImageUrl" value="${previousShotInfo.urlL}" />
<c:set var="NextFullImageUrl" value="${nextShotInfo.urlL}" />
<c:set var="ThisPageUrl" value="${siteBaseUrl}/shot/view/${shotInfo.id}" />
<c:set var="DivPaddingBorderWidth" value="10" />
<c:set var="DivPaddingBorderHeight" value="10" />
<c:set var="ImageAspectRatio" value="${(!empty animeEpisode.height && !empty animeEpisode.width) ? animeEpisode.height/animeEpisode.width : 9/16}" />

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

<style>
body {
	overflow: scroll;
}

.ShareLinkDiv {
	margin-top: 20px;
	max-width: 800px;
}

.ShareLinkItem {
	margin-top: 10px;
}

.ShareLinkItem>div {
	padding-right: 3px;
	padding-left: 3px;
}

@media ( min-width : 768px) {
	.ShareLinkLabel {
		text-align: right;
	}
	.ShareLinkButton {
		text-align: left;
	}
	.shotTitleInfo {
		width: 400px;
	}
}

@media ( max-width : 768px) {
	.noLeftRightPadding {
		padding-right: 1px !important;
		padding-left: 1px !important;
	}
}

.subtitleTranslatedText {
	font-size: <c:out value="${subtitleTranslatedTextFontsize}"></c:out>;
	color: <c:out value="${subtitleTranslatedTextColor}"></c:out>;
}

.shotImgDivStyle {
	position: relative;
	margin-bottom: 10px;
}

.shotImgDivStyle #shotImg {
	
}

.shotImgHotPoint {
	height: 100%;
	width: 40%;
	position: absolute;
	z-index: 2;
}
</style>
</head>

<script src="${ ctx }/static/js/shotView.js" type="text/javascript"></script>
<script src="${ ctx }/static/plugins/subtitle/ass.js" type="text/javascript"></script>
<!-- 
<script src="${ ctx }/static/plugins/subtitle/[Kamigami] Sailor Moon Crystal - 01 [1920x1080 x264 AAC Sub(Chs,Cht,Jap)]_track3_und.ass" type="text/javascript"></script>
 -->

<script type="text/javascript">
	var originalImg = null; // 原始图片
	lazyRun(function() {
		// 将cookie放到input中
		readCookieAndSetWidth();

		// 显示当前图片尺寸
		originalImg = new Image(); // 原始图片
		originalImg.src = $("#shotImg").attr("src");
		$.log("开始显示当前图片尺寸");
		checkImgSize(originalImg, 100);

		// 百度翻译
		var baiduT = new BaiduT("${translateParam.appId}");
		$(".subtitleText").each(function(index, element) {
			<c:forEach items="${translateParam.toLangList}" var="toLang" varStatus="status">
			var fromLang = "${translateParam.fromLang}";
			fromLang = "auto"; // TODO 是否需要自动
			var doSubtitleLang = "${translateParam.doSubtitleLang}";

			var thisSubtitleLang = $(element).attr("data-lang");
			if (thisSubtitleLang == doSubtitleLang) {
				baiduT.translate(fromLang, "${toLang}", "${translateParam.salt}", $(element).attr("data-sign"), $(element).attr("data-text"), function(data) {
					if (data.trans_result != null) {
						//var oldStr = $(element).text();
						//var newStr = $.escapeHtml(oldStr) + "<br>" + $.escapeHtml(data.trans_result[0].dst);
						//$(element).html(newStr);
						$(element).append("<br>");
						$(element).append("<span class='subtitleTranslatedText'>" + $.escapeHtml(data.trans_result[0].dst) + "<span>");
					}
				});
			}
			</c:forEach>
		});
	});

	// 加载前后图片
	<c:if test="${not empty previousShotInfo.id}">
	lazyRun(function() {
		loadImg("${PreFullImageUrl}");
	}, 100);
	</c:if>

	// 加载前后图片
	<c:if test="${not empty nextShotInfo.id}">
	lazyRun(function() {
		loadImg("${NextFullImageUrl}");
	}, 200);
	</c:if>
</script>

<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 noLeftRightPadding">
	<div style="margin-bottom: 3px;">
		<h1 style="font-size: 14px; margin: 0px;">
			<c:out value='${EpisodeFullNameWithTime}' />
		</h1>
	</div>
	<div style="margin-bottom: 3px;">
		<div class="shotTitleInfo" style="white-space: nowrap; word-break: keep-all;">
			<div class="col-sm-4 col-xs-12">
				<label><spring:message code='时间戳' />：${shotInfo.timeStamp}</label>
			</div>
			<div class="col-sm-8 col-xs-12">
				<label><spring:message code='尺寸' />：</label><span id="imgWidth">${animeEpisode.width}</span>×<span id="imgHeight">${animeEpisode.height}</span> <span style="font-size: xx-small;"><spring:message code='设置图片宽度' /></span>
				<input id="ShotViewImgWidth" type="text" value="${ShotViewImgWidth}" style="width: 50px; font-size: xx-small;" onchange="changeShotViewImgWidth(true);">
				<input id="ShotImgDivWidth" type="hidden" value="${ShotImgDivWidth}">
			</div>
		</div>
	</div>

	<input type="hidden" id="scorllTop" name="scorllTop" value="<c:out value="${scorllTop}" />">
	<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 noLeftRightPadding">
		<!-- 图片 -->
		<div id="shotImgDiv" class="thumbnail shotImgDivStyle" data-ImageAspectRatio="${ImageAspectRatio}" style="margin-bottom: 10px;<c:if test="${ShotImgDivWidth > 0}">width: ${ShotImgDivWidth}px; height: ${DivPaddingBorderHeight + (ShotImgDivWidth-DivPaddingBorderWidth) * ImageAspectRatio}px;</c:if>">
			<img id="shotImg" style="width: 100%;" src="${FullImageUrl}" alt="<c:out value='${EpisodeFullNameWithTime}' /> <c:out value="${fn:substring(subtitleLineTextStr100, 0, 50)}" />">

			<c:if test="${not empty previousShotInfo.id}">
				<a class="shotImgHotPoint left-0 top-0" href="${ctx}/shot/view/${previousShotInfo.id}"></a>
			</c:if>
			<c:if test="${empty previousShotInfo.id}">
				<a class="shotImgHotPoint left-0 top-0" href="javascript:Message.msg('<spring:message code='没有上一张' />');" title="<spring:message code='没有上一张' />"></a>
			</c:if>
			<c:if test="${not empty nextShotInfo.id}">
				<a class="shotImgHotPoint right-0 top-0" href="${ctx}/shot/view/${nextShotInfo.id}"></a>
			</c:if>
			<c:if test="${empty nextShotInfo.id}">
				<a class="shotImgHotPoint right-0 top-0" href="javascript:Message.msg('<spring:message code='已经是最后一张了' />');" title="<spring:message code='已经是最后一张了' />"></a>
			</c:if>
		</div>
		<!-- 字幕 -->
		<c:if test="${!empty subtitleLineList}">
			<table class="shotSubtitle" style="margin-top: 0px; margin-bottom: 10px;">
				<c:forEach items="${subtitleLineList}" var="subtitleLine" varStatus="status">
					<tr>
						<td style="font-size: 10px;" class="noBreak" title="${subtitleLine.startTimeMinSecMicro}-${subtitleLine.endTimeMinSecMicro}">${subtitleLine.startTimeMinSec}-${subtitleLine.endTimeMinSec}</td>
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
	<a class="btn btn-primary btn-sm" onclick="home.publicLike('${shotInfo.id}');">
		<span class="glyphicon glyphicon-star"></span>
		<spring:message code='喜欢' />
		<div id="publicLike_${shotInfo.id}" class="badge">${shotInfo.publicLikeCount}</div>
	</a>
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
	<a class="btn btn-primary btn-sm" href="javascript:void(0);" onclick="openWhatanimeSite(this, '${FullImageUrl}');" target="_blank">
		<spring:message code='查看短视频' />
	</a>
</div>

<div class="buttonDiv">
	<c:if test="${!empty previousShotInfo.id}">
		<a class="btn btn-primary btn-md postByFromXXX" href="${ctx}/shot/view/${previousShotInfo.id}" title="<c:out value='${EpisodeFullName}' /> <c:out value='${previousShotInfo.formatedTimeChina}' />">
			<spring:message code='上一张' />
		</a>
	</c:if>
	<c:if test="${!empty nextShotInfo.id}">
		<a class="btn btn-primary btn-md postByFromXXX" href="${ctx}/shot/view/${nextShotInfo.id}" title="<c:out value='${EpisodeFullName}' /> <c:out value='${nextShotInfo.formatedTimeChina}' />">
			<spring:message code='下一张' />
		</a>
	</c:if>
</div>

<div id="createShotResultDiv" class="buttonDiv">
	<spring:message code='获取新截图' />
	<c:if test="${!empty previousShotInfo.id}">
		<a class="btn btn-primary btn-xs postByFromXXX" href="javascript:doCreateShot('${shotInfo.id}', 2000, true);">-2</a>
	</c:if>
	<c:if test="${!empty previousShotInfo.id}">
		<a class="btn btn-primary btn-xs postByFromXXX" href="javascript:doCreateShot('${shotInfo.id}', 1000, true);">-1</a>
	</c:if>
	<c:if test="${!empty nextShotInfo.id}">
		<a class="btn btn-primary btn-xs postByFromXXX" href="javascript:doCreateShot('${shotInfo.id}', 1000, false);">1</a>
	</c:if>
	<c:if test="${!empty nextShotInfo.id}">
		<a class="btn btn-primary btn-xs postByFromXXX" href="javascript:doCreateShot('${shotInfo.id}', 2000, false);">2</a>
	</c:if>
</div>

<div class="buttonDiv">
	<a class="btn btn-primary btn-md" href="${ctx}/shot/list/${shotInfo.animeEpisodeId}${pageNumberUrl}" title="<c:out value='${EpisodeFullName}' />">
		<spring:message code='返回截图一览' />
	</a>
</div>

<div id="链接地址" class="row ShareLinkDiv" style="margin-left: 0; margin-right: 0;">
	<div class="col-sm-12 ShareLinkItem noLeftRightPadding">
		<div class="col-sm-3 ShareLinkLabel">
			<label><spring:message code='动漫图片链接' />：</label>
		</div>
		<div class="col-sm-6">
			<input id="imageLink" readonly="readonly" class="form-control input-sm" style="cursor: text;" value="${FullImageUrl}">
		</div>
		<div class="col-sm-3 ShareLinkButton">
			<input type="button" class="btn btn-sm btn-primary ZeroClipboardButton" data-clipboard-target="imageLink" value="<spring:message code='复制' />" />
		</div>
	</div>
	<div class="col-sm-12 ShareLinkItem noLeftRightPadding">
		<div class="col-sm-3 ShareLinkLabel">
			<label><spring:message code='本页链接' />：</label>
		</div>
		<div class="col-sm-6">
			<input id="pageLink" readonly="readonly" class="form-control input-sm" style="cursor: text;" value="${ThisPageUrl}">
		</div>
		<div class="col-sm-3 ShareLinkButton">
			<input type="button" class="btn btn-sm btn-primary ZeroClipboardButton" data-clipboard-target="pageLink" value="<spring:message code='复制' />" />
		</div>
	</div>
	<div class="col-sm-12 ShareLinkItem noLeftRightPadding quickShareDivClass">
		<div class="col-sm-3 ShareLinkLabel">
			<label><spring:message code='分享本页' />：</label>
		</div>
		<div style="height: 24px;" align="center" class="col-sm-6">
			<div style="display: block; width: 200px;">
				<div style="margin-top: -4px;" class="bdsharebuttonbox bdshare-button-style0-16">
					<a href="#" class="bds_mshare" data-cmd="mshare" title="百度一键分享"></a>
					<a href="#" class="bds_tieba" data-cmd="tieba" title="分享到百度贴吧"></a>
					<a href="#" class="bds_sqq" data-cmd="sqq" title="分享到QQ好友"></a>
					<a href="#" class="bds_weixin" data-cmd="weixin" title="分享到微信"></a>
					<a href="#" class="bds_bdxc" data-cmd="bdxc" title="分享到百度相册"></a>
					<a href="#" class="bds_duitang" data-cmd="duitang" title="分享到堆糖"></a>
					<a href="#" class="bds_evernotecn" data-cmd="evernotecn" title="分享到印象笔记"></a>
					<a href="#" class="bds_more" data-cmd="more" title="更多"></a>
				</div>
			</div>
		</div>
		<div class="col-sm-3 ShareLinkButton"></div>
	</div>
</div>