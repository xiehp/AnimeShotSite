<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<title>动态图片gif 第${gifInfoPage.number + 1}页 - 动画截图网</title>
<head>
<meta name="keywords" content="动态图片gif 第${gifInfoPage.number + 1}页 - 动画截图网,动画截图网,动画截图,动漫截图,动漫图片,动画图片,截图字幕" />
<meta name="description" content="动态图片gif 第${gifInfoPage.number + 1}页 - 动画截图网,动漫截图,动漫图片,动画图片,截图字幕" />

<style>
.blockTitle {
	margin-top: 10px;
	margin-left: 10px;
	margin-bottom: 10px;
	font-size: 21px;
	text-align: left;
}

.blockTitle .count {
	margin-left: 10px;
	font-size: 12px;
}
</style>
</head>

<script>
	<c:if test="${IS_MANAGER}">
	function masterLike(id) {
		home.masterLike("${MANAGE_URL_STR}/gif/masterLike", id, "#masterLike_" + id, "#publicLike_" + id);
	}
	</c:if>
</script>

<div>
	<!-- 标题 -->
	<div class="blockTitle">动态图片${gifInfoPage.numberOfElements}张 共${gifInfoPage.totalElements}张图片</div>

	<!-- 截图一览 -->
	<div class="row">
		<c:forEach items="${ gifInfoPage.content }" var="gifInfo">
			<div style="min-height: 100px; font-size: 10px;" class="col-lg-2 col-sm-3 col-xs-4 thumbnail">
				<a href="${ctx}/gif/view/${gifInfo.id}" title="<c:out value='${gifInfo.animeEpisode.fullName}' /> <c:out value='${gifInfo.formatedTimeChina}' />">
					<img data-original="${gifInfo.urlS}" class="img-responsive imagelazy">
					<div class="wordKeepLine">
						<c:out value="${gifInfo.animeInfo.fullName}" />
						<c:out value='${gifInfo.animeInfo.secondName}' />
						<c:out value='1' />
					</div>
					<div>
						<c:out value="${gifInfo.animeEpisode.divisionName}" />
						${gifInfo.formatedMinSec}<span style="color: lightgray;${gifInfo.formatedMicroSec > 0 ? '' : ' display: none;'}">:${gifInfo.formatedMicroSec}</span>
					</div>
				</a>
				<c:if test="${IS_MASTER}">
					<div class="btn btn-primary btn-xs" onclick="home.publicLike('${gifInfo.id}');">
						<span class="glyphicon glyphicon-star"></span><span style="font-size: 10px;">喜欢</span>
						<div id="publicLike_${gifInfo.id}" class="badge" style="padding-top: 0px;">${gifInfo.publicLikeCount}</div>
					</div>
					<div class="btn btn-primary btn-xs" onclick="home.masterLike('${MANAGE_URL_STR}/gif/masterLike', '${gifInfo.id}');">
						<span class="glyphicon glyphicon-star"></span><span style="font-size: 10px;">推荐</span>
						<div id="masterLike_${gifInfo.id}" class="badge" style="padding-top: 0px;">${gifInfo.masterRecommendRank}</div>
					</div>
				</c:if>
			</div>
		</c:forEach>
	</div>
</div>

<div>
	<tags:paginationRestPage page="${gifInfoPage}" paginationSize="5" />
</div>

<div>
	<a class="btn btn-primary" href="${ctx}/">返回首页</a>
	<a class="btn btn-primary" href="${ctx}/gif/task">制作动态图片</a>
</div>

