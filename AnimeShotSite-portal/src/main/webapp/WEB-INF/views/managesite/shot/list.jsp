<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<title>动画剧集截图一览</title>

<script>
	function publicLike(id) {
		var param = {};
		param.id = id;
		$.homePost("/shot/publicLike", param, function(data) {
			if (data) {
				$("#publicLike_" + id).text(data.newCount);
			}
		});
	}

	function masterLike(id) {
		var param = {};
		param.id = id;
		$.homePost("${MANAGE_URL_STR}/shot/masterLike", param, function(data) {
			if (data) {
				$("#masterLike_" + id).text(data.newMasterCount);
				$("#publicLike_" + id).text(data.newPublicCount);
			}
		});
	}
</script>

<div>
	<div class="blockTitle">
		<c:out value="${animeInfo.name}" />
		<small><c:out value="${animeEpisode.name}" /></small>
	</div>
	<div class="row">
		<c:forEach items="${ shotInfoPage.content }" var="shotInfo">
			<div style="min-height: 100px;" class="col-lg-2 col-sm-3 col-xs-4 thumbnail">
				<a href="${ctx}/shot/view/${shotInfo.id}">
					<img src="${shotInfo.urlS}" class="img-responsive">
					<div style="margin-top: 5px;">
						${shotInfo.formatedMinSec}<span style="display: none; color: lightgray;">:${shotInfo.formatedMicroSec}</span>
					</div>
					<div class="btn btn-primary btn-xs" href="#" onclick="publicLike('${shotInfo.id}');">
						<span class="glyphicon glyphicon-star"></span>喜欢 <span id="publicLike_${shotInfo.id}" class="badge">${shotInfo.publicLikeCount}</span>
					</div>
					<div class="btn btn-primary btn-xs" onclick="masterLike('${shotInfo.id}');">
						<span class="glyphicon glyphicon-star"></span>推荐 <span id="masterLike_${shotInfo.id}" class="badge">${shotInfo.masterRecommendRank}</span>
					</div>
				</a>
			</div>
		</c:forEach>
	</div>
</div>

<div>
	<tags:pagination page="${shotInfoPage}" paginationSize="10" />
</div>


<a href="${ctx}${MANAGE_URL_STR}/animeEpisode/view/${animeEpisode.id}">返回上层</a>
