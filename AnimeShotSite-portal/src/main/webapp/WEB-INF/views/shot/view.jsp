<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<head>
<title><c:out value='${animeInfo.fullName}' /> <c:out value='${animeInfo.secondName}' /> <c:out value='${animeEpisode.divisionName}' /> <c:out value='${shotInfo.formatedTimeChina}' /></title>
<meta name="keywords" content="<c:out value='${subtitleLineTextStr100}' />" />
<meta name="description" content="<c:out value='${subtitleLineTextStr200}' />" />

<meta property="og:title" content="<c:out value='${animeInfo.fullName}' /> <c:out value='${animeInfo.secondName}' /> <c:out value='${animeEpisode.divisionName}' /> <c:out value='${shotInfo.formatedTimeChina}' />" />
<meta property="og:type" content="photo" />
<meta property="og:url" content="${ctx}/shot/view/${shotInfo.id}" />
<meta property="og:image" content="${shotInfo.tietukuOUrlChangeDomain}" />
<meta property="og:image:type" content="image/jpeg" />

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
}
</style>

<script src="${ ctx }/static/js/shotView.js" type="text/javascript"></script>

</head>

<script type="text/javascript">
	// 复制按钮
	$(function() {
		$(".ZeroClipboardButton").each(function() {
			initZeroClipboard(this);
		});
	});

	var originalImg = null; // 原始图片

	$(function() {
		// 将cookie放到input中
		readCookieAndSetWidth();

		// 显示当前图片尺寸
		originalImg = new Image(); // 原始图片
		originalImg.src = $("#shotImg").attr("src");
		$.log("开始显示当前图片尺寸");
		checkImgSize(originalImg, 100);
	});
</script>

<div>
	<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
		<h1 style="font-size: 14px; margin: 0px;"><c:out value='${animeInfo.fullName}' /> <c:out value='${animeInfo.secondName}' /> <c:out value='${animeEpisode.divisionName}' /> ${shotInfo.formatedTimeChina}</h1>
	</div>
	<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
		<div style="width: 400px; white-space: nowrap; word-break: keep-all;">
			<div class="col-sm-4 col-xs-12">
				时间戳：${shotInfo.timeStamp}</span>
			</div>
			<div class="col-sm-8 col-xs-12">
				尺寸：<span id="imgWidth">${animeEpisode.width}</span>×<span id="imgHeight">${animeEpisode.height}</span> <span style="font-size: xx-small;">设置图片宽度</span>
				<input id="ShotViewImgWidth" type="text" value="${ShotViewImgWidth}" style="width: 50px; height: 18px; font-size: xx-small;" onchange="changeShotViewImgWidth();">
				<input id="ShotImgDivWidth" type="hidden" value="${ShotImgDivWidth}">
			</div>
		</div>
	</div>

	<!-- <form action="${ctx}/shot/view/${shotInfo.id}" method="post"> -->
	<input type="hidden" id="scorllTop" name="scorllTop" value="<c:out value="${scorllTop}" />">
	<div align="center">
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
			<div id="shotImgDiv" class="thumbnail shotImgDivStyle" style="margin-bottom: 10px;<c:if test="${ShotImgDivWidth > 0}">width: ${ShotImgDivWidth}px; height: ${ShotImgDivWidth * 9 / 16}px;</c:if>">
				<script>
					readCookieAndSetWidth(true);
				</script>
				<img id="shotImg" src="${shotInfo.tietukuOUrlChangeDomain}" alt="<c:out value='${animeInfo.fullName}' /> <c:out value='${animeInfo.secondName}' /> <c:out value='${animeEpisode.divisionName}' /> <c:out value='${shotInfo.formatedTimeChina}' />"
					title="<c:out value='${animeInfo.fullName}' /> <c:out value='${animeInfo.secondName}' /> <c:out value='${animeEpisode.divisionName}' /> <c:out value='${shotInfo.formatedTimeChina}' />" usemap="#planetmap">
			</div>
			<c:if test="${!empty subtitleLineList}">
				<table class="shotSubtitle" style="margin-top: 0px; margin-bottom: 10px;">
					<c:forEach items="${subtitleLineList}" var="subtitleLine">
						<tr>
							<td style="font-size: 10px;" class="noBreak" title="${subtitleLine.startTimeMinSecMicro}-${subtitleLine.endTimeMinSecMicro}">${subtitleLine.startTimeMinSec}-${subtitleLine.endTimeMinSec}</td>
							<td>
								<c:out value="${subtitleLine.text}" />
							</td>
						</tr>
					</c:forEach>
				</table>
			</c:if>
		</div>

		<map id="planetmap" name="planetmap">
			<c:if test="${!empty previousShotInfo.id}">
				<area id="areaPrev" class="postByFromXXX" shape="rect" coords="0,0,200,5000" href="${ctx}/shot/view/${previousShotInfo.id}" title="上一张"
					alt="<c:out value='${animeInfo.fullName}' /> <c:out value='${animeInfo.secondName}' /> <c:out value='${animeEpisode.divisionName}' /> <c:out value='${previousShotInfo.formatedTimeChina}' />" />
				<img alt="" src="${previousShotInfo.tietukuOUrlChangeDomain}" style="display: none;">
			</c:if>
			<c:if test="${!empty nextShotInfo.id}">
				<area id="areaNext" class="postByFromXXX" shape="rect" coords="500,0,5000,5000" href="${ctx}/shot/view/${nextShotInfo.id}" title="下一张"
					alt="<c:out value='${animeInfo.fullName}' /> <c:out value='${animeInfo.secondName}' /> <c:out value='${animeEpisode.divisionName}' /> <c:out value='${nextShotInfo.formatedTimeChina}' />" />
				<img alt="" src="${nextShotInfo.tietukuOUrlChangeDomain}" style="display: none;">
			</c:if>
		</map>
	</div>
</div>

<div style="padding: 5px;">
	<a class="btn btn-primary btn-sm" onclick="home.publicLike('${shotInfo.id}');">
		<span class="glyphicon glyphicon-star"></span>喜欢
		<div id="publicLike_${shotInfo.id}" class="badge">${shotInfo.publicLikeCount}</div>
	</a>
	<c:if test="${IS_MASTER}">
		<a class="btn btn-primary btn-sm" onclick="home.masterLike('${MANAGE_URL_STR}/shot/masterLike', '${shotInfo.id}');">
			<span class="glyphicon glyphicon-star"></span>推荐
			<div id="masterLike_${shotInfo.id}" class="badge">${shotInfo.masterRecommendRank}</div>
		</a>
		<a class="btn btn-primary btn-sm" onclick="home.setAnimeTitleImage('/tool/setAnimeTitleImage', '${animeInfo.id}', null, '${shotInfo.id}');"> 动画图片 </a>
		<a class="btn btn-primary btn-sm" onclick="home.setAnimeTitleImage('/tool/setAnimeTitleImage', null, '${animeEpisode.id}', '${shotInfo.id}');"> 剧集图片 </a>
	</c:if>
</div>

<div style="padding: 5px;">
	<c:if test="${!empty previousShotInfo.id}">
		<a style="margin: 10px;" class="btn btn-primary btn-sm postByFromXXX" href="${ctx}/shot/view/${previousShotInfo.id}"
			title="<c:out value='${animeInfo.fullName}' /> <c:out value='${animeInfo.secondName}' /> <c:out value='${animeEpisode.divisionName}' /> <c:out value='${previousShotInfo.formatedTimeChina}' />">上一张</a>
	</c:if>
	<c:if test="${!empty nextShotInfo.id}">
		<a style="margin: 10px;" class="btn btn-primary btn-sm postByFromXXX" href="${ctx}/shot/view/${nextShotInfo.id}"
			title="<c:out value='${animeInfo.fullName}' /> <c:out value='${animeInfo.secondName}' /> <c:out value='${animeEpisode.divisionName}' /> <c:out value='${nextShotInfo.formatedTimeChina}' />">下一张</a>
	</c:if>
</div>
<!-- </form>  -->

<div>
	<a class="btn btn-primary btn-sm" href="${ctx}/shot/list/${shotInfo.animeEpisodeId}${pageNumberUrl}" title="<c:out value='${animeInfo.fullName}' /> <c:out value='${animeInfo.secondName}' /> <c:out value='${animeEpisode.divisionName}' />">返回截图一览</a>
</div>

<%
	request.getRequestURL();
	//request.setAttribute("requestURI", request.getRequestURI());
	//request.setAttribute("requestURL", request.getRequestURL());
%>


<div id="链接地址" class="row ShareLinkDiv">
	<div class="col-sm-12 ShareLinkItem quickShareDivClass">
		<div class="col-sm-3 ShareLinkLabel">分享本页：</div>
		<div style="height: 24px;" align="center" class="col-sm-6">
			<div style="display: block;width: 200px;">
				<div style="margin-top: -4px;" class="bdsharebuttonbox">
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
		<div class="col-sm-3 ShareLinkButton">
		</div>
	</div>
	<div class="col-sm-12 ShareLinkItem">
		<div class="col-sm-3 ShareLinkLabel">本页链接：</div>
		<div class="col-sm-6">
			<input id="pageLink" readonly="readonly" class="form-control input-sm" style="cursor: text;" value="http://www.acgimage.com${requestURI}">
		</div>
		<div class="col-sm-3 ShareLinkButton">
			<input type="button" class="btn btn-sm btn-primary ZeroClipboardButton" data-clipboard-text="http://www.acgimage.com${requestURI}" value="复制" />
		</div>
	</div>
	<div class="col-sm-12 ShareLinkItem">
		<div class="col-sm-3 ShareLinkLabel">动漫图片链接：</div>
		<div class="col-sm-6">
			<input id="imageLink" readonly="readonly" class="form-control input-sm" style="cursor: text;" value="${shotInfo.tietukuOUrlChangeDomain}">
		</div>
		<div class="col-sm-3 ShareLinkButton">
			<input type="button" class="btn btn-sm btn-primary ZeroClipboardButton" data-clipboard-text="${shotInfo.tietukuOUrlChangeDomain}" value="复制" />
		</div>
	</div>
</div>
