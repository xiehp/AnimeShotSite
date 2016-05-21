<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<title>动画截图网 <c:out value='${animeEpisode.fullName}' /> 第${shotInfoPage.number + 1}页</title>
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

.subtitleTable {
	padding-right: 5px;
	font-size: 10px;
	text-align: left;
}

pre {
	white-space: pre-wrap; /*css-3*/
	white-space: -moz-pre-wrap; /*Mozilla,since1999*/
	white-space: -pre-wrap; /*Opera4-6*/
	white-space: -o-pre-wrap; /*Opera7*/
	word-wrap: break-word; /*InternetExplorer5.5+*/
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
						${shotInfo.formatedMinSec}<span style="display: none; color: lightgray;">:${shotInfo.formatedMicroSec}</span>
					</div>
				</a>
				<div class="btn btn-primary btn-xs" onclick="home.publicLike('${shotInfo.id}');">
					<span class="glyphicon glyphicon-star"></span>喜欢
					<div id="publicLike_${shotInfo.id}" class="badge">${shotInfo.publicLikeCount}</div>
				</div>
				<c:if test="${IS_MASTER}">
					<div class="btn btn-primary btn-xs" onclick="home.masterLike('${MANAGE_URL_STR}/shot/masterLike', '${shotInfo.id}');">
						<span class="glyphicon glyphicon-star"></span>推荐
						<div id="masterLike_${shotInfo.id}" class="badge">${shotInfo.masterRecommendRank}</div>
					</div>
				</c:if>
			</div>
		</c:forEach>
	</div>
</div>

<div>
	<tags:paginationRestPage page="${shotInfoPage}" paginationSize="6" />
</div>

<div>
	<a class="btn btn-primary" href="${ctx}/episode/list/${animeEpisode.animeInfoId}" title="<c:out value='${animeInfo.fullName}' /> <c:out value='${animeInfo.secondName}' />">返回剧集列表</a>
</div>

<div style="margin-top: 50px;">
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="row">
				<c:if test="${!empty subtitleLineList}">
					<div style="font-weight: 700;">字幕一览</div>
					<div>
						<pre class="subtitleTable">
<c:forEach items="${ subtitleLineList }" var="subtitleLine">${subtitleLine.startTimeMinSecMicro} ${subtitleLine.endTimeMinSecMicro} <c:out value='${subtitleLine.text}' />
</c:forEach>
						</pre>
					</div>
				</c:if>
			</div>
		</div>
	</div>
</div>
