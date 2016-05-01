<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<title>${animeEpisode.fullName} ${shotInfo.formatedMinSec}</title>

<caption>${animeEpisode.fullName}</caption>
<br>
<caption>
	时间： ${shotInfo.formatedMinSec}<span style="color: lightgray;">.${shotInfo.formatedMicroSec} (${shotInfo.timeStamp})</span>
</caption>
<br>
<caption>
	尺寸：<span id="imgWidth">${animeEpisode.width}</span>×<span id="imgHeight">${animeEpisode.height}</span>
</caption>
<div align="center">
	<div class="col-lg-12 col-sm-12 col-xs-12 thumbnail">
		<img id="shotImg" src="${shotInfo.tietukuOUrl}" usemap="#planetmap">
		<map id="planetmap" name="planetmap">
			<c:if test="${!empty previousShotInfo.id}">
				<area id="areaPrev" shape="rect" coords="0,0,200,5000" href="${ctx}/shot/view/${previousShotInfo.id}" title="上一张" alt="上一张" />
				<img alt="" src="${previousShotInfo.tietukuOUrl}" style="display: none;">
			</c:if>
			<c:if test="${!empty nextShotInfo.id}">
				<area id="areaNext" shape="rect" coords="500,0,5000,5000" href="${ctx}/shot/view/${nextShotInfo.id}" title="下一张" alt="下一张" />
				<img alt="" src="${nextShotInfo.tietukuOUrl}" style="display: none;">
			</c:if>
		</map>
		<script type="text/javascript">
			jQuery(function() {
				//动态生成左右图片热点
				reCreateImgHotLink();
				window.onresize = reCreateImgHotLink;

				//显示当前图片尺寸
				var newImg = new Image();
				newImg.src = shotImg.src;
				checkImgSize(newImg, 100);
			});

			//动态生成左右图片热点链接
			function reCreateImgHotLink() {
				var jqueryShotImg = $("#shotImg");
				var shotImg = jqueryShotImg[0];
				var shotImgMapWidth = shotImg.width;
				var shotImgMapHeight = shotImg.height;
				$("#areaPrev").attr("coords", "0,0," + shotImgMapWidth / 3 + "," + shotImgMapHeight);
				$("#areaNext").attr("coords", shotImgMapWidth / 3 * 2 + ",0," + shotImgMapWidth + "," + shotImgMapHeight);
			}

			// 定时执行获取宽高
			var checkImgSize = function(imgDomObject, maxCheckCount) {
				console.log(maxCheckCount);
				if (maxCheckCount <= 0) {
					return;
				}
				if (imgDomObject == null) {
					return;
				}

				if (imgDomObject.width > 0) {
					$("#imgWidth").text(imgDomObject.width);
					$("#imgHeight").text(imgDomObject.height);
					return;
				}

				maxCheckCount = maxCheckCount - 1;
				setTimeout(function() {
					checkImgSize(imgDomObject, maxCheckCount);
				}, 200);
			};
		</script>
	</div>
	<div style="padding: 5px;">
		<a class="btn btn-primary btn-md" onclick="home.publicLike('${shotInfo.id}');">
			<span class="glyphicon glyphicon-star"></span>喜欢
			<div id="publicLike_${shotInfo.id}" class="badge">${shotInfo.publicLikeCount}</div>
		</a>
		<c:if test="${IS_MASTER}">
			<a class="btn btn-primary btn-md" onclick="home.masterLike('${MANAGE_URL_STR}/shot/masterLike', '${shotInfo.id}');">
				<span class="glyphicon glyphicon-star"></span>推荐
				<div id="masterLike_${shotInfo.id}" class="badge">${shotInfo.masterRecommendRank}</div>
			</a>
		</c:if>
	</div>
</div>

<!-- 字幕显示 -->
<div style="padding: 5px;">
	<table>
		<c:forEach items="${subtitleLineList}" var="subtitleLine">
			<tr>
				<td style="font-size: 10px;">
					<div>
						<span>${subtitleLine.startTimeMinSec}</span>-<span>${subtitleLine.endTimeMinSec}</span>
					</div>
				</td>
				<td style="padding-left: 10px;"><c:out value="${subtitleLine.text}"></c:out></td>
			</tr>
		</c:forEach>
	</table>
</div>

<div style="padding: 5px;">
	<c:if test="${!empty previousShotInfo.id}">
		<a style="margin: 10px;" class="btn btn-primary" href="${ctx}/shot/view/${previousShotInfo.id}">上一张</a>
	</c:if>
	<c:if test="${!empty nextShotInfo.id}">
		<a style="margin: 10px;" class="btn btn-primary" href="${ctx}/shot/view/${nextShotInfo.id}">下一张</a>
	</c:if>
</div>

<div>
	<a class="btn btn-primary" href="${ctx}/shot/list/${shotInfo.animeEpisodeId}${pageNumberUrl}">返回截图一览</a>
</div>