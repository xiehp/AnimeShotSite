<%@ page language="java" contentType="text/html; UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="ctxManage" value="${ctx}${MANAGE_URL_STR}" />

<div class="mip-nav-wrapper">
	<mip-nav-slidedown data-id="bs-navbar" class="mip-element-sidebar container" data-showbrand="1" data-brandhref="${ctx}/mip" data-brandname="<spring:message code='动画截图网' />">
	<nav id="bs-navbar" class="navbar-collapse collapse navbar navbar-static-top">
		<ul class="nav navbar-nav navbar-right">
			<li>
				<a href="${ctx}/mip">
					<spring:message code='首页' />
				</a>
				<hr class="hr-xs">
			</li>
			<li class="doc-body">
				<a href="${ctx}/mip/anime">
					<spring:message code='动画列表' />
				</a>
				<hr class="hr-xs">
			</li>
			<li class="timeline-body">
				<a href="${ctx}/mip/shot/recommend">
					<spring:message code='截图推荐' />
				</a>
			</li>
			<li class="">
				<a href="${ctx}/mip/gif/list">
					<spring:message code='动态图片gif' />
				</a>
				<hr class="hr-xs">
			</li>
			<li>
				<a href="${ctx}/mip/search">
					<spring:message code='字幕搜索' />
				</a>
				<hr class="hr-xs">
			</li>
			<li>
				<a href="${ctx}/mip/shot/random">
					<spring:message code='随便看' />
				</a>
				<hr class="hr-xs">
			</li>
			<li class="navbar-wise-close">
				<span id="navbar-wise-close-btn"></span>
			</li>
		</ul>
	</nav>
	</mip-nav-slidedown>
</div>