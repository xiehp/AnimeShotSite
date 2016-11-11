<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<title>动画详情</title>

<form class="form-horizontal" action="${ctx}${MANAGE_URL_STR}/anime/submit" method="post">
	<div class="form-group">
		<label class="col-sm-2 control-label">ID</label>
		<div class="col-sm-10">
			<input class="form-control" name="id" value="${animeInfo.id}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">名称</label>
		<div class="col-sm-10">
			<input class="form-control" name="name" value="${animeInfo.name}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">版本</label>
		<div class="col-sm-10">
			<input class="form-control" name="version" value="${empty animeInfo.version ? 0 : animeInfo.version}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">type</label>
		<div class="col-sm-10">
			<input class="form-control" name="type" value="${animeInfo.type}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">系列</label>
		<div class="col-sm-10">
			<input class="form-control" name="series" value="${animeInfo.series}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">截图状态 =</label>
		<div class="col-sm-10">
			<input class="form-control" name="shotStatus" value="${animeInfo.shotStatus}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">处理动作</label>
		<div class="col-sm-10">
			<input class="form-control" name="processAction" value="${animeInfo.processAction}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">本地所在根路径</label>
		<div class="col-sm-10">
			<input class="form-control" name="localRootPath" value="${animeInfo.localRootPath}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">本地所在相对路径</label>
		<div class="col-sm-10">
			<input class="form-control" name="localDetailPath" value="${animeInfo.localDetailPath}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">图片ID</label>
		<div class="col-sm-10">
			<input class="form-control" name="titleUrlId" value="${animeInfo.titleUrlId}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">图片</label>
		<div class="col-sm-10">
			<img alt="${animeInfo.titleUrl.urlS}" src="${animeInfo.titleUrl.urlS}"> ${animeInfo.titleUrl.urlS}
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">status</label>
		<div class="col-sm-10">
			<input class="form-control" name="status" value="${animeInfo.status}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">deleteFlag</label>
		<div class="col-sm-10">
			<input class="form-control" name="deleteFlag" value="${animeInfo.deleteFlag}" />
		</div>
	</div>

	<input type="submit" />

	<a href="${ctx}${MANAGE_URL_STR}/anime/list">返回上层</a>
</form>

<div>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="row">
				<c:forEach items="${ animeEpisodeList }" var="animeEpisode">
					<div class="col-lg-3 col-sm-4 col-xs-6 thumbnail">
						<a href="${ctx}${MANAGE_URL_STR}/animeEpisode/view/${animeEpisode.id}">
							<img src="${ctx}/static/img/imageLoading_mini.jpg" data-original="${animeEpisode.titleUrl.urlS}" class="img-responsive imagelazy">
							<div style="margin-top: 5px;">
								<c:out value="${animeEpisode.name}" />
							</div>
						</a>
					</div>
				</c:forEach>
			</div>
		</div>
	</div>
</div>

<div>
	<a href="${ctx}${MANAGE_URL_STR}/animeEpisode/new"> 增加剧集信息 </a>
</div>

