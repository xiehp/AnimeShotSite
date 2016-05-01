<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<title>动画截图网 <c:out value="${animeEpisode.fullName}" /></title>
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
		<c:out value="${animeInfo.fullName}" />
		<small><c:out value="${animeEpisode.divisionName}" /></small> <small><c:out value="${animeEpisode.title}" /></small>
		<c:if test="${!empty animeEpisode.summary}">
			<button type="button" class="btn btn-rimary btn-xs" data-toggle="collapse" data-target="#episodeSmmary">显示剧集简介</button>
		</c:if>
	</div>

	<div class="row" style="text-align: left;">
		<!-- 剧集简介 -->
		<div class="col-md-12">
			<div id="episodeSmmary" class="collapse">${animeEpisode.summary}</div>
		</div>
	</div>

	<!-- 截图一览 -->
	<div class="row">
		<c:forEach items="${ shotInfoPage.content }" var="shotInfo">
			<div style="min-height: 100px;" class="col-lg-2 col-sm-3 col-xs-4 thumbnail">
				<a href="${ctx}/shot/view/${shotInfo.id}">
					<img data-original="${shotInfo.urlS}" class="img-responsive imagelazy">
					<div style="margin-top: 5px;">
						${shotInfo.formatedMinSec}<span style="color: lightgray;">:${shotInfo.formatedMicroSec}</span>
						<div style="margin-bottom: 10px;">
							<a class="btn btn-primary btn-xs" onclick="home.publicLike('${shotInfo.id}');">
								<span class="glyphicon glyphicon-star"></span>喜欢
								<div id="publicLike_${shotInfo.id}" class="badge">${shotInfo.publicLikeCount}</div>
							</a>
							<c:if test="${IS_MASTER}">
								<a class="btn btn-primary btn-xs" onclick="home.masterLike('${MANAGE_URL_STR}/shot/masterLike', '${shotInfo.id}');">
									<span class="glyphicon glyphicon-star"></span>推荐
									<div id="masterLike_${shotInfo.id}" class="badge">${shotInfo.masterRecommendRank}</div>
								</a>
							</c:if>
						</div>

					</div>
				</a>
			</div>
		</c:forEach>
	</div>
</div>

<div>
	<tags:pagination page="${shotInfoPage}" paginationSize="10" />
</div>

<div>
	<a class="btn btn-primary" href="${ctx}/episode/list/${animeEpisode.animeInfoId}">返回剧集列表</a>
</div>

<div style="margin-top: 50px;">
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="row">
				<c:if test="${!empty subtitleLineList}">字幕一览</c:if>
				<table>
					<c:forEach items="${ subtitleLineList }" var="subtitleLine">
						<tr>
							<td style="padding-right: 5px; font-size: 10px;">${subtitleLine.startTimeMinSecMicro}</td>
							<td style="padding-right: 5px; font-size: 10px;">${subtitleLine.endTimeMinSecMicro}</td>
							<td style="font-size: 10px;">${subtitleLine.text}</td>
						</tr>
					</c:forEach>
				</table>
			</div>
		</div>
	</div>
</div>
