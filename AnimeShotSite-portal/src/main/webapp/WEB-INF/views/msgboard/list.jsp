<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<title><spring:message code='留言板' /> - 动画截图网</title>
<style>
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


<!-- 留言 -->
<div id="留言" align="center" class="row ShareLinkDiv" style="margin-left: 0; margin-right: 0;">
	<c:if test="${!empty commentPage}">
		<div align="left" class="col-lg-9 col-md-9 col-sm-10 col-xs-11" style="float: none;">
			<c:if test="${commentPage.content.size() > 0}">
				<spring:message code='网友留言' />
				<c:forEach items="${ commentPage.content }" var="commentRecord">
					<div style="margin-top: 10px;">
						<!--<fmt:formatDate value="${commentRecord.commentDate}" pattern="yyyy-MM-dd HH:mm:ss" />-->
						<div style="margin-left: 10px; display: inline-block;">
							<c:if test="${empty commentRecord.userName}">
								<spring:message code='某网友' />
							</c:if>
							<c:if test="${not empty commentRecord.userName}">
								<c:out value='${commentRecord.userName}' />
							</c:if>
						</div>
						<pre><c:out value='${commentRecord.content}' /></pre>
					</div>
				</c:forEach>
			</c:if>
		</div>
	</c:if>
</div>
<div id="创建留言" align="center" class="row ShareLinkDiv" style="margin-left: 0; margin-right: 0;margin-top: 15px;">
	<div align="left" class="col-sm-9" style="float: none;">
		<form id="createCommentForm" data-action="${ctx}/comment/createComment" method="post">

			<input type="hidden" name="class1" value="msgboard" />

			<div class="col-sm-10">
				<spring:message code='输入留言' />
				:
			</div>
			<div class="col-sm-10">
				<textarea name="content" rows="5" style="width: 100%;"></textarea>
			</div>
			<div class="col-sm-10">
				<spring:message code='您的昵称' />
				:
				<input type="text" name="userName" value="<c:out value='${cookieUserName}' />" />
			</div>
			<div class="col-sm-10">
				<input type="button" value="<spring:message code='发表' />" onclick="createCommentRecord('createCommentForm');" class="btn btn-sm btn-primary" />
			</div>
		</form>
	</div>
</div>
