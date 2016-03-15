<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<title>剧集详情</title>

<script>
	function doSubmit(newFlg) {
		if (newFlg) {
			$("input[name=id]").val("");
		}
		$("#animeEpisodeForm").attr("action", "${ctx}/managesite/animeEpisode/submit");
		$("#animeEpisodeForm").submit();
	}
	function doSubmitMuti() {
		$("#animeEpisodeForm").attr("action", "${ctx}/managesite/animeEpisode/submitMuti");
		$("#animeEpisodeForm").submit();
	}
</script>

<form id="animeEpisodeForm" class="form-horizontal" method="post">
	<div class="form-group">
		<label class="col-sm-2 control-label">ID</label>
		<div class="col-sm-10">
			<input class="form-control" name="id" value="${animeEpisodeInfo.id}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">animeInfoId</label>
		<div class="col-sm-10">
			<input class="form-control" name="animeInfoId" value="${animeEpisodeInfo.animeInfoId}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">名称</label>
		<div class="col-sm-10">
			<input class="form-control" name="name" value="${animeEpisodeInfo.name}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">版本</label>
		<div class="col-sm-10">
			<input class="form-control" name="version" value="${empty animeEpisodeInfo.version ? 0 : animeEpisodeInfo.version}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">type</label>
		<div class="col-sm-10">
			<input class="form-control" name="type" value="${animeEpisodeInfo.type}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">截图状态 =</label>
		<div class="col-sm-10">
			<input class="form-control" name="shotStatus" value="${animeEpisodeInfo.shotStatus}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">处理动作</label>
		<div class="col-sm-10">
			<input class="form-control" name="processAction" value="${animeEpisodeInfo.processAction}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">本地所在根路径</label>
		<div class="col-sm-10">
			<input class="form-control" name="localRootPath" value="${animeEpisodeInfo.localRootPath}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">本地所在相对路径</label>
		<div class="col-sm-10">
			<input class="form-control" name="localDetailPath" value="${animeEpisodeInfo.localDetailPath}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">本地所在文件名</label>
		<div class="col-sm-10">
			<input class="form-control" name="localFileName" value="${animeEpisodeInfo.localFileName}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">截图的本地根路径</label>
		<div class="col-sm-10">
			<input class="form-control" name="shotLocalRootPath" value="${animeEpisodeInfo.shotLocalRootPath}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">截图的本地相对路径</label>
		<div class="col-sm-10">
			<input class="form-control" name="shotLocalDetailPath" value="${animeEpisodeInfo.shotLocalDetailPath}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">序号，每个剧集唯一</label>
		<div class="col-sm-10">
			<input class="form-control" name="number" value='${empty animeEpisodeInfo.number ? "[[0]]" : animeEpisodeInfo.number}' />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">图片ID</label>
		<div class="col-sm-10">
			<input class="form-control" name="titleUrlId" value="${animeEpisodeInfo.titleUrlId}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">图片</label>
		<div class="col-sm-10">
			<img alt="${animeEpisodeInfo.titleUrl.urlS}" src="${animeEpisodeInfo.titleUrl.urlS}"> ${animeEpisodeInfo.titleUrl.urlS}
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">sort</label>
		<div class="col-sm-10">
			<input class="form-control" name="sort" value="${animeEpisodeInfo.sort}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">status</label>
		<div class="col-sm-10">
			<input class="form-control" name="status" value="${animeEpisodeInfo.status}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">deleteFlag</label>
		<div class="col-sm-10">
			<input class="form-control" name="deleteFlag" value="${animeEpisodeInfo.deleteFlag}" />
		</div>
	</div>

	<div class="form-group">
		<label class="col-sm-2 control-label">开始</label>
		<div class="col-sm-10">
			<input class="form-control" name="start" value="${empty start ? 1 : start}" />
		</div>
	</div>

	<div class="form-group">
		<label class="col-sm-2 control-label">结束</label>
		<div class="col-sm-10">
			<input class="form-control" name="end" value="${empty end ? 13 : end}" />
		</div>
	</div>

	<div class="form-group">
		<label class="col-sm-2 control-label">位数扩展</label>
		<div class="col-sm-10">
			<input class="form-control" name="extention" value="${extention}" />
		</div>
	</div>

	<div class="form-group">
		<label class="col-sm-2 control-label">其他需要替换参数</label>
		<div class="col-sm-10">
			<input class="form-control" name="param" value="${param}" />
		</div>
	</div>

	<input type="submit" value="更新" onclick="doSubmit(false);" />
	<input type="button" value="新建一个" onclick="doSubmit(true);" />
	<input type="button" value="新建或更新多个" onclick="doSubmitMuti();" />

</form>

	<a href="${ctx}/managesite/anime/view/${animeEpisodeInfo.animeInfoId}">返回上层</a>
	
<br />
<br />
	<a href="${ctx}/managesite/shot/list/${animeEpisodeInfo.id}">截图一览</a>

<br />
<br />
<br />
<br />
<br />
<br />
