<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<title>动画列表一览 - 动画截图网</title>

<div class="container-fluid">
	<div class="row-fluid">
		<div class="blockTitle">
			<span>动画列表一览</span>
		</div>
		<div class="row">
			<c:forEach items="${ animeInfoPage.content }" var="anime">
				<div class="col-lg-3 col-sm-4 col-xs-6 thumbnail">
					<a href="${ctx}/episode/list/${anime.id}" title="<c:out value='${anime.fullName}' />">
						<img data-original="${anime.titleUrl.urlS}" class="img-responsive imagelazy">
						<div style="margin-top: 5px;">
							<c:out value='${anime.fullName}' />
						</div>
					</a>
				</div>
			</c:forEach>
		</div>
	</div>
</div>

<div>
	<tags:pagination page="${animeInfoPage}" paginationSize="9" />
</div>


