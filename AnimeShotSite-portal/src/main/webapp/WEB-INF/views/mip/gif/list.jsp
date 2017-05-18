<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<title>动态图片gif 第${gifInfoPage.number + 1}页 - 动画截图网</title>
<head>
<meta name="keywords" content="动态图片gif 第${gifInfoPage.number + 1}页 - 动画截图网,动画截图网,动画截图,动漫截图,动漫图片,动画图片,截图字幕" />
<meta name="description" content="动态图片gif 第${gifInfoPage.number + 1}页 - 动画截图网,动漫截图,动漫图片,动画图片,截图字幕" />

</head>

	<c:if test="${IS_MANAGER}">
<script>
	function masterLike(id) {
		home.masterLike("${MANAGE_URL_STR}/gif/masterLike", id, "#masterLike_" + id, "#publicLike_" + id);
	}
</script>
	</c:if>

<div>
	<!-- 标题 -->
	<div class="blockTitle">
		<spring:message code='动态图片Gif一览' />
	</div>

	<!-- 截图一览 -->
	<div class="row">
		<c:forEach items="${ gifInfoPage.content }" var="gifInfo">
			<div class="listImg min-height-100px  font-size-10px">
				<a href="${siteBaseUrl}/mip/gif/view/${gifInfo.id}" title="<c:out value='${gifInfo.animeEpisode.fullName}' /> <c:out value='${gifInfo.formatedTimeChina}' />">
					<mip-img src="${gifInfo.urlS}" data-original="${gifInfo.urlS}" class="img-responsive imagelazy"></mip-img>
					<div class="wordKeepLine">
						<c:out value="${gifInfo.animeInfo.fullName}" />
						<c:out value='${gifInfo.animeInfo.secondName}' />
					</div>
					<div>
						<c:out value="${gifInfo.animeEpisode.divisionName}" />
						${gifInfo.formatedMinSec}<span class="color-lightgray ${gifInfo.formatedMicroSec > 0 ? '' : ' display-none'}">:${gifInfo.formatedMicroSec}</span>
					</div>
				</a>
				<c:if test="${IS_MASTER}">
					<div class="btn btn-primary btn-xs" onclick="home.publicLike('${gifInfo.id}');">
						<span class="glyphicon glyphicon-star"></span>
						<spring:message code='喜欢' />
						<div id="publicLike_${gifInfo.id}" class="badge" style="padding-top: 0px;">${gifInfo.publicLikeCount}</div>
					</div>
					<div class="btn btn-primary btn-xs" onclick="home.masterLike('${MANAGE_URL_STR}/gif/masterLike', '${gifInfo.id}');">
						<span class="glyphicon glyphicon-star"></span>
						<spring:message code='推荐' />
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
	<a class="btn btn-primary" href="${siteBaseUrl}/mip">
		<spring:message code='返回首页' />
	</a>
	<a class="btn btn-primary" href="${siteBaseUrl}/mip/gif/task">
		<spring:message code='制作动态图片' />
	</a>
</div>

