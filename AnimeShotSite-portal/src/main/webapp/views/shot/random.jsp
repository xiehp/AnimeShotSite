<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<title>动画剧集截图一览</title>
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
	function publicLike(id) {
		var param = {};
		param.id = id;
		$.homePost("/shot/publicLike", param, function(data) {
			if (data) {
				$("#publicLike_" + id).text(data.newCount);
			}
		});
	}
</script>
<div>
	<div class="blockTitle">
		帮你随机选出了${ shotInfoList.size() }张图
	</div>
	<div class="row">
		<c:forEach items="${ shotInfoList }" var="shotInfo">
			<div style="min-height: 100px;" class="col-lg-2 col-sm-3 col-xs-4 thumbnail">
				<a href="${ctx}/shot/view/${shotInfo.id}">
					<img data-original="${shotInfo.urlS}" class="img-responsive imagelazy">
					<div style="margin-top: 5px;">
						${shotInfo.formatedMinSec}<span style="color: lightgray;">:${shotInfo.formatedMicroSec}</span>
						<div style="margin-bottom: 10px;">
							<a class="btn btn-primary btn-xs" onclick="publicLike('${shotInfo.id}');">
								<span class="glyphicon glyphicon-star"></span>喜欢
								<div id="publicLike_${shotInfo.id}" class="badge">${shotInfo.publicLikeCount}</div>
							</a>
						</div>

					</div>
				</a>
			</div>
		</c:forEach>
	</div>
</div>

<div>
	<a href="${ctx}/episode/list/${animeEpisode.animeInfoId}">返回剧集列表</a>
</div>
