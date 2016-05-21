<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<title>剧集详情</title>

<script>
	function doSubmit(newFlg) {
		if (confirm("是否继续？")) {
			if (newFlg) {
				$("input[name=id]").val("");
			}
			$("#mainForm").attr("action", "${ctx}${MANAGE_URL_STR}/animeEpisode/submit");
			$("#mainForm").submit();
		}
	}
	function doSubmitMuti() {
		if (confirm("是否继续？")) {
			$("#mainForm").attr("action", "${ctx}${MANAGE_URL_STR}/animeEpisode/submitMuti");
			$("#mainForm").submit();
		}
	}

	function doShotTask(type) {
		if (confirm("是否继续？")) {
			$("#mainForm").attr("action", "${ctx}${MANAGE_URL_STR}/animeEpisode/addShotTask");
			$("#mainForm").submit();
		}
	}

	function updateOneColumn(columnName) {
		var param = {};
		param.id = "${animeEpisodeInfo.id}";
		param.columnName = columnName;
		param.columnValue = $("#mainForm").find("[name=" + columnName + "]").val();
		$.homePost("${MANAGE_URL_STR}/animeEpisode/updateOneColumn", param, function(data) {
			if (data.success) {
				$.showMessageModal(data.message);
			} else {
				$.showMessageModal(data.message);
			}
		});
	}

	$(function() {
		$("#mainForm").find("input[name]").each(function() {
			var params = {};
			params.name = $(this).attr("name");
			var buttonHtml = $("#updateOneColumnButtonTemplate").render(params);
			$(this).parent().after(buttonHtml);
		});

		// 编辑器
		var changesCount = 0;
		var summaryEditor = CKEDITOR.replace('summaryEditor', {});
		summaryEditor.on('change', function(ev) {
			$("input[name=summary]").val(summaryEditor.getData());
			document.getElementById('summaryCount').innerHTML = summaryEditor.getData().length;
		});
		
		if ($('#fullName').val() == null || $('#fullName').val() == "") {
			nameChanged();
		}
	});

	var seriesChangedFlg = false;
	function nameChanged() {
		// 全称
		$('#fullName').val($('#animeFullName').val() + ' ' + $('#divisionName').val());

		// 改变本地相对路径
		//var nowDate = new Date();
		//$('#localDetailPath').val("\\" + nowDate.getFullYear() + "\\" + $('#name').val() + "\\" + "第一季");

		// 该变系列 
		//if (!seriesChangedFlg) {
		//	$('#series').val($('#divisionName').val());
		//}

	}

	function seriesChanged() {
		seriesChangedFlg = true;
	}
</script>

<script id="updateOneColumnButtonTemplate" type="text/x-jsrender">
<div class="col-sm-1">
	<a class="btn btn-primary" value="更新" onclick="updateOneColumn('{{>name}}');">更新</a>
</div>
</script>

<!-- <script src="//cdn.ckeditor.com/4.5.8/full/ckeditor.js"></script> -->
<script src="//cdn.ckeditor.com/4.5.8/full/ckeditor.js"></script>

<form id="mainForm" class="form-horizontal" method="post">
	<div class="form-group">
		<label class="col-sm-2 control-label">ID</label>
		<div class="col-sm-5">
			<input class="form-control" id="id" name="id" value="${animeEpisodeInfo.id}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">animeInfoId</label>
		<div class="col-sm-5">
			<input class="form-control" name="animeInfoId" value="${animeEpisodeInfo.animeInfoId}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">名称</label>
		<div class="col-sm-5">
			<input class="form-control" id="name" name="name" value="${animeEpisodeInfo.name}" " />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">第几集名称</label>
		<div class="col-sm-5">
			<input class="form-control" id="divisionName" name="divisionName" value="${animeEpisodeInfo.divisionName}" onchange="nameChanged();" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">剧集标题</label>
		<div class="col-sm-5">
			<input class="form-control" name="title" value="${animeEpisodeInfo.title}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">动画全称</label>
		<div class="col-sm-5">
			<input readonly="readonly" class="form-control" id="animeFullName" name="animeFullName" value="${animeInfo.fullName}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">全称</label>
		<div class="col-sm-5">
			<input class="form-control" id="fullName" name="fullName" value="${animeEpisodeInfo.fullName}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label"></label>
		<div class="col-sm-9">
			<textarea id="summaryEditor"><c:out value="${animeEpisodeInfo.summary}"></c:out></textarea>
		</div>
		<div id="summaryCount" class="col-sm-1"></div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">简介</label>
		<div class="col-sm-5">
			<input class="form-control" name="summary" value="<c:out value="${animeEpisodeInfo.summary}"></c:out>" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">版本</label>
		<div class="col-sm-5">
			<input class="form-control" name="version" value="${empty animeEpisodeInfo.version ? 0 : animeEpisodeInfo.version}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">type</label>
		<div class="col-sm-5">
			<input class="form-control" name="type" value="${animeEpisodeInfo.type}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">截图状态 =</label>
		<div class="col-sm-5">
			<input class="form-control" name="shotStatus" value="${animeEpisodeInfo.shotStatus}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">处理动作</label>
		<div class="col-sm-5">
			<input class="form-control" name="processAction" value="${animeEpisodeInfo.processAction}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">本地所在根路径</label>
		<div class="col-sm-5">
			<input class="form-control" id="localRootPath" name="localRootPath" value="${animeEpisodeInfo.localRootPath}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">本地所在相对路径</label>
		<div class="col-sm-5">
			<input class="form-control" id="localDetailPath" name="localDetailPath" value="${animeEpisodeInfo.localDetailPath}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">本地所在文件名</label>
		<div class="col-sm-5">
			<input class="form-control" name="localFileName" value="${animeEpisodeInfo.localFileName}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">截图的本地根路径</label>
		<div class="col-sm-5">
			<input class="form-control" id="shotLocalRootPath" name="shotLocalRootPath" value="${animeEpisodeInfo.shotLocalRootPath}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">截图的本地相对路径</label>
		<div class="col-sm-5">
			<input class="form-control" id="shotLocalDetailPath" name="shotLocalDetailPath" value="${animeEpisodeInfo.shotLocalDetailPath}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">序号，每个剧集唯一</label>
		<div class="col-sm-5">
			<input class="form-control" id="number" name="number" value='${empty animeEpisodeInfo.number ? "[[0]]" : animeEpisodeInfo.number}' />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">宽度</label>
		<div class="col-sm-5">
			<input class="form-control" id="width" name="width" value='${animeEpisodeInfo.width}' />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">高度</label>
		<div class="col-sm-5">
			<input class="form-control" id="height" name="height" value='${animeEpisodeInfo.height}' />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">图片ID</label>
		<div class="col-sm-5">
			<input class="form-control" name="titleUrlId" value="${animeEpisodeInfo.titleUrlId}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">图片</label>
		<div class="col-sm-5">
			<img alt="${animeEpisodeInfo.titleUrl.urlS}" src="${animeEpisodeInfo.titleUrl.urlS}"> ${animeEpisodeInfo.titleUrl.urlS}
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">sort</label>
		<div class="col-sm-5">
			<input class="form-control" id="sort" name="sort" value="${empty animeEpisodeInfo.sort ? " [[0]]" : animeEpisodeInfo.sort}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">status</label>
		<div class="col-sm-5">
			<input class="form-control" name="status" value="${animeEpisodeInfo.status}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">showFlg</label>
		<div class="col-sm-5">
			<input class="form-control" name="showFlg" value="${animeEpisodeInfo.showFlg}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">deleteFlag</label>
		<div class="col-sm-5">
			<input class="form-control" name="deleteFlag" value="${animeEpisodeInfo.deleteFlag}" />
		</div>
	</div>

	<div class="form-group">
		<label class="col-sm-2 control-label">开始</label>
		<div class="col-sm-5">
			<input class="form-control" name="start" value="${empty start ? 1 : start}" />
		</div>
	</div>

	<div class="form-group">
		<label class="col-sm-2 control-label">结束</label>
		<div class="col-sm-5">
			<input class="form-control" name="end" value="${empty end ? 13 : end}" />
		</div>
	</div>

	<div class="form-group">
		<label class="col-sm-2 control-label">位数扩展</label>
		<div class="col-sm-5">
			<input class="form-control" name="extention" value="${empty extention ? 2 : extention}" />
		</div>
	</div>

	<div class="form-group">
		<label class="col-sm-2 control-label">其他需要替换参数</label>
		<div class="col-sm-5">
			<input class="form-control" name="param" value="${param}" />
		</div>
	</div>

	<div class="form-group">
		<input type="submit" value="更新" onclick="doSubmit(false);" />
		<input type="button" value="新建一个" onclick="doSubmit(true);" />
		<input type="button" value="新建或更新多个" onclick="doSubmitMuti();" />
	</div>


	<div class="form-group">
		<label class="col-sm-2 control-label">taskType</label>
		<div class="col-sm-5">
			<input class="form-control" name="taskType" value="1" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">scheduleTime</label>
		<div class="col-sm-5">
			<input class="form-control" name="scheduleTime" value='<fmt:formatDate value="<%=new Date()%>" pattern="yyyy/MM/dd HH:mm:ss"/>' />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">startTime</label>
		<div class="col-sm-5">
			<input class="form-control" name="startTime" value="${startTime}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">endTime</label>
		<div class="col-sm-5">
			<input class="form-control" name="endTime" value="${endTime}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">timeInterval</label>
		<div class="col-sm-5">
			<input class="form-control" name="timeInterval" value="${empty timeInterval ? 5000 : timeInterval}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">specifyTimes</label>
		<div class="col-sm-5">
			<input class="form-control" name="specifyTimes" value="${specifyTimes}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">forceUpload</label>
		<div class="col-sm-5">
			<input class="form-control" name="forceUpload" value="${forceUpload}" />
		</div>
	</div>
	<div class="form-group">
		<input type="button" value="提交定时任务" onclick="doShotTask(1);" />
		<input type="button" value="提交指定时间任务" onclick="doShotTask(2);" />
	</div>

</form>

<a href="${ctx}${MANAGE_URL_STR}/anime/view/${animeEpisodeInfo.animeInfoId}">返回上层</a>

<br />
<br />
<a href="${ctx}${MANAGE_URL_STR}/shot/list/${animeEpisodeInfo.id}">截图一览</a>

<br />
<br />
<br />
<br />
<br />
<br />
