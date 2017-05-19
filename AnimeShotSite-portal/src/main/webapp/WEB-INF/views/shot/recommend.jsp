<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<title>动漫截图推荐 第${shotInfoPage.number + 1}页 - 动画截图网</title>
<head>
<meta name="keywords" content="动漫 动画 截图 推荐 第${shotInfoPage.number + 1}页 - 动画截图网,动画截图网,动画截图,动漫截图,动漫图片,动画图片,截图字幕" />
<meta name="description" content="动漫 动画 截图 推荐 第${shotInfoPage.number + 1}页 - 动画截图网,动漫截图,动漫图片,动画图片,截图字幕" />

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
		home.masterLike("${MANAGE_URL_STR}/shot/masterLike", id, "#masterLike_" + id, "#publicLike_" + id);
	}
	</c:if>
</script>

<div>
	<!-- 标题 -->
	<div class="blockTitle">
		<spring:message code='推荐动漫截图' />${shotInfoPage.numberOfElements}张
		共${shotInfoPage.totalElements}张图片
	</div>

	<!-- 截图一览 -->
	<div class="row">
		<c:forEach items="${ shotInfoPage.content }" var="shotInfo">
			<div style="min-height: 100px; font-size: 10px;" class="col-lg-2 col-sm-3 col-xs-4 thumbnail">
				<a href="${ctx}/shot/view/${shotInfo.id}" title="<c:out value='${shotInfo.animeEpisode.fullName}' /> <c:out value='${shotInfo.formatedTimeChina}' />">
					<img src="${ctx}/static/img/imageLoading_mini.jpg" data-original="${shotInfo.urlS}" class="img-responsive imagelazy">
					<div class="wordKeepLine">
						<c:out value="${shotInfo.animeInfo.fullName}" />
						<c:out value='${shotInfo.animeInfo.secondName}' />
					</div>
					<div>
						<c:out value="${shotInfo.animeEpisode.divisionName}" />
						${shotInfo.formatedMinSec}<span style="color: lightgray;${shotInfo.formatedMicroSec > 0 ? '' : ' display: none;'}">:${shotInfo.formatedMicroSec}</span>
					</div>
				</a>
				<c:if test="${IS_MASTER}">
					<div class="btn btn-primary btn-xs" onclick="home.publicLike('${shotInfo.id}');">
						<span class="glyphicon glyphicon-star"></span>
						<spring:message code='喜欢' />
						<div id="publicLike_${shotInfo.id}" class="badge" style="padding-top: 0px;">${shotInfo.publicLikeCount}</div>
					</div>
					<div class="btn btn-primary btn-xs" onclick="home.masterLike('${MANAGE_URL_STR}/shot/masterLike', '${shotInfo.id}');">
						<span class="glyphicon glyphicon-star"></span>
						<spring:message code='推荐' />
						<div id="masterLike_${shotInfo.id}" class="badge" style="padding-top: 0px;">${shotInfo.masterRecommendRank}</div>
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
	<a class="btn btn-primary" href="${ctx}/">
		<spring:message code='返回首页' />
	</a>
</div>

