<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<title><c:out value='${animeEpisode.fullName}' /> <c:out value='${shotInfo.formatedTime}' /></title>
<head>
<meta property="og:title" content="<c:out value='${animeEpisode.fullName}' /> <c:out value='${shotInfo.formatedTime}' />" />
<meta property="og:type" content="photo" />
<meta property="og:url" content="${ctx}/shot/view/${shotInfo.id}" />
<meta property="og:image" content="${shotInfo.tietukuOUrlChangeDomain}" />
<meta property="og:image:type" content="image/jpeg" />

<script>
	// 读取cookie，并且设置图片尺寸
	function readCookieAndSetWidth(isCreateDom) {
		var ShotViewImgWidth = $.cookie("ShotViewImgWidth");
		$.log("读取设置图片尺寸cookie:" + ShotViewImgWidth);

		if (!isNaN(ShotViewImgWidth) && ShotViewImgWidth > 0) {
			// 有cookie
			if (isCreateDom) {
				var setHeight = ShotViewImgWidth * 9 / 16 + 4 + 1;
				$("#shotImgDiv").css("width", ShotViewImgWidth);
				$("#shotImgDiv").css("height", setHeight);
				//$("#shotImg").css("width", ShotViewImgWidth);
				$.log("设置图片div尺寸:" + ShotViewImgWidth + "," + setHeight);
			} else {
				$("#ShotViewImgWidth").val(ShotViewImgWidth);
			}
		} else {
			// 无cookie，直接根据当前div宽度设定宽高
			if (isCreateDom) {
				var divWidth = $("#shotImgDiv").css("width");
				divWidth = parseFloat(divWidth);
				$.log("当前div宽度:" + divWidth);
				if (!isNaN(divWidth) && divWidth > 0) {
					var setHeight = divWidth * 9 / 16 + 4 + 1;
					$("#shotImgDiv").css("height", setHeight);
					$.log("设置图片div尺寸:" + divWidth + "," + setHeight);
				}
			}
		}
	}
</script>

<style>
body {
overflow: scroll;
}
</style>
</head>

<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
	<span><c:out value='${animeEpisode.fullName}' /></span>
</div>
<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
	<div style="width: 360px; white-space: nowrap; word-break: keep-all;">
		<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
			时间：${shotInfo.formatedMinSec}<span style="color: lightgray;">.${shotInfo.formatedMicroSec} (${shotInfo.timeStamp})</span>
		</div>
		<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
			尺寸：<span id="imgWidth">${animeEpisode.width}</span>×<span id="imgHeight">${animeEpisode.height}</span> <span style="font-size: xx-small;">设置图片宽度</span>
			<input id="ShotViewImgWidth" type="text" value="${ShotViewImgWidth}" style="width: 50px; height: 18px; font-size: xx-small;" onchange="changeShotViewImgWidth();">
			<input id="ShotImgDivWidth" type="hidden" value="${ShotImgDivWidth}">
		</div>
	</div>
</div>

<form action="/anime/list" method="post">
	<input type="hidden" id="scorllTop" name="scorllTop" value="<c:out value="${scorllTop}" />">
	<div align="center">
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
			<div id="shotImgDiv" class="thumbnail shotImgDivStyle" style="margin-bottom: 10px;<c:if test="${ShotImgDivWidth > 0}">width: ${ShotImgDivWidth}px; height: ${ShotImgDivWidth * 9 / 16}px;</c:if>">
				<script>
					readCookieAndSetWidth(true);
				</script>
				<img id="shotImg" style="margin-left: 0px;" src="${shotInfo.tietukuOUrlChangeDomain}" alt="<c:out value='${animeEpisode.fullName}' /> ${shotInfo.formatedMinSec}" title="<c:out value='${animeEpisode.fullName}' /> ${shotInfo.formatedTime}" usemap="#planetmap">
			</div>
			<c:if test="${!empty subtitleLineList}">
				<table class="shotSubtitle" style="margin-top: 0px;margin-bottom: 10px;">
					<c:forEach items="${subtitleLineList}" var="subtitleLine">
						<tr>
							<td style="font-size: 10px;" class="noBreak" title="${subtitleLine.startTimeMinSecMicro}-${subtitleLine.endTimeMinSecMicro}">${subtitleLine.startTimeMinSec}-${subtitleLine.endTimeMinSec}</td>
							<td><c:out value="${subtitleLine.text}" /></td>
						</tr>
					</c:forEach>
				</table>
			</c:if>
		</div>

		<map id="planetmap" name="planetmap">
			<c:if test="${!empty previousShotInfo.id}">
				<area id="areaPrev" class="postByFrom" shape="rect" coords="0,0,200,5000" href="${ctx}/shot/view/${previousShotInfo.id}" title="上一张" alt="上一张" />
				<img alt="" src="${previousShotInfo.tietukuOUrl}" style="display: none;">
			</c:if>
			<c:if test="${!empty nextShotInfo.id}">
				<area id="areaNext" class="postByFrom" shape="rect" coords="500,0,5000,5000" href="${ctx}/shot/view/${nextShotInfo.id}" title="下一张" alt="下一张" />
				<img alt="" src="${nextShotInfo.tietukuOUrl}" style="display: none;">
			</c:if>
		</map>

		<script type="text/javascript">
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

			// 定时执行获取宽高
			var checkImgSize = function(imgDomObject, maxCheckCount) {
				// console.log(maxCheckCount);
				if (maxCheckCount <= 0) {
					return;
				}
				if (imgDomObject == null) {
					return;
				}

				if (imgDomObject.width > 0) {
					$.log("读取到图片宽度:" + imgDomObject.width);

					// 设置标题栏图片大小
					$("#imgWidth").text(imgDomObject.width);
					$("#imgHeight").text(imgDomObject.height);

					// 重新设置图片div高宽
					$.log("重新设置图片div高宽");
					changeShotViewImgWidth()

					// 设置初始滚动条值
					var scorllTop = document.getElementById("scorllTop").value;
					if (scorllTop != null && scorllTop > 0) {
						scrollTo(0, scorllTop);
						$.log("设置初始滚动条高度:" + scorllTop);
					}

					// 追加滚动事件
					setTimeout(function() {
						window.onscroll = function() {
							var nowScrollTop = document.body.scrollTop;
							document.getElementById("scorllTop").value = nowScrollTop;
							$.log("滚动事件:" + nowScrollTop);
						};
						$.log("追加滚动事件");
					});

					//动态生成左右图片热点
					window.onresize = reCreateImgHotLink;
					reCreateImgHotLink();

					$.log("读取到图片宽度后处理结束。");
					return;
				}

				maxCheckCount = maxCheckCount - 1;
				setTimeout(function() {
					checkImgSize(imgDomObject, maxCheckCount);
				}, 200);
			};

			//动态生成左右图片热点链接
			function reCreateImgHotLink(width, height) {
				var jqueryShotImg = $("#shotImg");
				var shotImg = jqueryShotImg[0];
				var shotImgMapWidth = shotImg.width;
				var shotImgMapHeight = shotImg.height;
				if (width != null && height != null) {
					shotImgMapWidth = width;
					shotImgMapHeight = height;
				}
				$("#areaPrev").attr("coords", "0,0," + shotImgMapWidth / 3 + "," + shotImgMapHeight);
				$("#areaNext").attr("coords", shotImgMapWidth / 3 * 2 + ",0," + shotImgMapWidth + "," + shotImgMapHeight);
				$.log("图片左热点设置：" + $("#areaPrev").attr("coords"));
				$.log("图片右热点设置：" + $("#areaNext").attr("coords"));
			}

			function changeShotViewImgWidth() {
				var ShotViewImgWidth = $("#ShotViewImgWidth").val();
				if (ShotViewImgWidth != null && isNaN(ShotViewImgWidth)) {
					$("#ShotViewImgWidth").val("");
					$.showMessageModal("请输入数字");
					return;
				}

				if (ShotViewImgWidth <= 0) {
					HomeCookie.removeCookie("ShotViewImgWidth");
					// 设置图片尺寸
					var width = '';
					var height = '';
					$("#shotImgDiv").css("width", width);
					$("#shotImgDiv").css("height", height);
					$.log("改变图片div尺寸：" + width + ", " + height);
					setShotImgDivWidthCookie($("#shotImgDiv").css("width"));
					//$("#shotImg").css("width", '');
				} else {
					if (originalImg != null && originalImg.width > 0 && ShotViewImgWidth > originalImg.width) {
						ShotViewImgWidth = originalImg.width;
						$("#ShotViewImgWidth").val(ShotViewImgWidth);
					}
					HomeCookie.setCookie("ShotViewImgWidth", ShotViewImgWidth);
					// 设置图片尺寸
					var width = ShotViewImgWidth;
					var height = '';
					$("#shotImgDiv").css("width", width);
					$("#shotImgDiv").css("height", height);
					//$("#shotImg").css("width", ShotViewImgWidth);
					$.log("改变图片div尺寸：" + width + ", " + height);
				}

				reCreateImgHotLink();
			}

			function setShotImgDivWidthCookie(value) {
				value = parseFloat(value);
				$.log("设置cookie, ShotImgDivWidth：" + value);
				if (value == null || value == "") {
					HomeCookie.removeCookie("ShotImgDivWidth");
				} else {
					HomeCookie.setCookie("ShotImgDivWidth", value);
				}
			}
		</script>
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
			<a style="margin: 10px;" class="btn btn-primary btn-sm postByFrom" href="${ctx}/shot/view/${previousShotInfo.id}" title="<c:out value='${animeEpisode.fullName}' /> <c:out value='${previousShotInfo.formatedTime}' />">上一张</a>
		</c:if>
		<c:if test="${!empty nextShotInfo.id}">
			<a style="margin: 10px;" class="btn btn-primary btn-sm postByFrom" href="${ctx}/shot/view/${nextShotInfo.id}" title="<c:out value='${animeEpisode.fullName}' /> <c:out value='${nextShotInfo.formatedTime}' />">下一张</a>
		</c:if>
	</div>
</form>

<div>
	<a class="btn btn-primary btn-sm" href="${ctx}/shot/list/${shotInfo.animeEpisodeId}${pageNumberUrl}" title="<c:out value='${animeEpisode.fullName}' />">返回截图一览</a>
</div>

