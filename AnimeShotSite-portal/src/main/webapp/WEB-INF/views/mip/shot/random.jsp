<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<title><spring:message code='随便看帮你随机挑选20张动漫图片' /> - 动画截图网</title>

<div>
	<!-- 标题 -->
	<div class="blockTitle">
		<spring:message code='帮你随机选出了' />${shotInfoList.size()}<spring:message code='张图' />
	</div>

	<!-- 截图一览 -->
	<div>
		<c:forEach items="${ shotInfoList }" var="shotInfo">
			<div style="min-height: 100px;" class="listImg">
				<a href="${ctx}/mip/shot/view/${shotInfo.id}">
					<mip-img src="${shotInfo.urlS}" data-original="${shotInfo.urlS}" class="img-responsive imagelazy"></mip-img>
					<div style="margin-top: 5px;">
						<div class="wordKeepLine" title="<c:out value='${shotInfo.animeEpisode.fullName}' />">
							<c:out value="${shotInfo.animeEpisode.fullName}" />
						</div>
						${shotInfo.formatedMinSec}<span style="color: lightgray;">:${shotInfo.formatedMicroSec}</span>
					</div>
				</a>
			</div>
		</c:forEach>
	</div>
</div>

<div>
	<a class="btn btn-primary" href="${ctx}/mip">返回首页</a>
</div>
