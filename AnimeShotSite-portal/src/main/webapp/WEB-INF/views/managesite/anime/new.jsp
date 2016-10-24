<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<title>动画详情 <c:out value='${animeInfo.fullName}' /></title>


<script type="text/javascript">
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
	});

	function updateOneColumn(columnName) {
		var param = {};
		param.id = "${animeInfo.id}";
		param.columnName = columnName;
		param.columnValue = $("#mainForm").find("input[name=" + columnName + "]").val();
		$.homePost("${MANAGE_URL_STR}/anime/updateOneColumn", param, function(data) {
			if (data.success) {
				$.showMessageModal(data.message);
			} else {
				$.showMessageModal(data.message);
			}
		});
	}

	var seriesChangedFlg = false;
	function nameChanged() {
		// 全称
		var fullName = $('#name').val();
		if ($('#divisionName').val() != "") {
			fullName = fullName + ' ' + $('#divisionName').val();
		}
		$('#fullName').val(fullName);

		// 改变本地相对路径
		var nowDate = new Date();
		$('#localDetailPath').val("\\" + nowDate.getFullYear() + "\\" + $('#name').val() + "\\" + "第一季");

		// 该变系列 
		if (!seriesChangedFlg) {
			$('#series').val($('#name').val());
		}
	}

	function seriesChanged() {
		seriesChangedFlg = true;
	}
</script>

<script id="updateOneColumnButtonTemplate" type="text/x-jsrender">
<div class="col-md-1">
	<a class="btn btn-primary" value="更新" onclick="updateOneColumn('{{>name}}');">更新</a>
</div>
</script>

<script src="//cdn.ckeditor.com/4.5.8/full/ckeditor.js"></script>

<form id="mainForm" class="form-horizontal" action="${ctx}${MANAGE_URL_STR}/anime/submit" method="post">
	<div class="form-group">
		<label class="col-sm-2 control-label">ID</label>
		<div class="col-sm-5">
			<input class="form-control" name="id" value="${animeInfo.id}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">名称</label>
		<div class="col-sm-5">
			<input class="form-control" id="name" name="name" value="${animeInfo.name}" onchange="nameChanged();" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">副标题</label>
		<div class="col-sm-5">
			<input class="form-control" name="secondName" value="${animeInfo.secondName}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">第几季名称</label>
		<div class="col-sm-5">
			<input class="form-control" id="divisionName" name="divisionName" value="${animeInfo.divisionName}" onchange="nameChanged();" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">全称</label>
		<div class="col-sm-5">
			<input class="form-control" id="fullName" name="fullName" value="${animeInfo.fullName}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label"></label>
		<div class="col-sm-9">
			<textarea id="summaryEditor"><c:out value="${animeInfo.summary}"></c:out></textarea>
		</div>
		<div id="summaryCount" class="col-sm-1"></div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">简介</label>
		<div class="col-sm-5">
			<input class="form-control" name="summary" value="<c:out value="${animeInfo.summary}"></c:out>" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">版本</label>
		<div class="col-sm-5">
			<input class="form-control" name="version" value="${empty animeInfo.version ? 0 : animeInfo.version}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">type</label>
		<div class="col-sm-5">
			<input class="form-control" name="type" value="${animeInfo.type}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">系列</label>
		<div class="col-sm-5">
			<input class="form-control" id="series" name="series" value="${animeInfo.series}" onclick="seriesChanged();" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">截图状态 =</label>
		<div class="col-sm-5">
			<input class="form-control" name="shotStatus" value="${animeInfo.shotStatus}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">处理动作</label>
		<div class="col-sm-5">
			<input class="form-control" name="processAction" value="${animeInfo.processAction}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">本地所在根路径</label>
		<div class="col-sm-5">
			<input class="form-control" name="localRootPath" value="${animeInfo.localRootPath}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">本地所在相对路径</label>
		<div class="col-sm-5">
			<input class="form-control" id="localDetailPath" name="localDetailPath" value="${animeInfo.localDetailPath}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">图片ID</label>
		<div class="col-sm-5">
			<input class="form-control" name="titleUrlId" value="${animeInfo.titleUrlId}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">图片</label>
		<div class="col-sm-5">
			<img alt="${animeInfo.titleUrl.urlS}" src="${animeInfo.titleUrl.urlS}"> ${animeInfo.titleUrl.urlS}
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">status</label>
		<div class="col-sm-5">
			<input class="form-control" name="status" value="${animeInfo.status}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">排序</label>
		<div class="col-sm-5">
			<input class="form-control" name="sort" value="${animeInfo.sort}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">showFlg</label>
		<div class="col-sm-5">
			<input class="form-control" name="showFlg" value="${animeInfo.showFlg}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">deleteFlag</label>
		<div class="col-sm-5">
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
							<img data-original="${animeEpisode.titleUrl.urlS}" class="img-responsive imagelazy">
							<div style="margin-top: 5px;">
								<c:out value='${animeEpisode.divisionName}' />
								<c:out value='${animeEpisode.title}' />
							</div>
							<div style="margin-top: 5px;">
								<span stype="${animeEpisode.showFlg == "1" ? "" :"color:red;"}"> ${animeEpisode.showFlg == "1" ? "已展示" :"未展示"} </span>
							</div>
						</a>
					</div>
				</c:forEach>
			</div>
		</div>
	</div>
</div>

<div>
	<a href="${ctx}${MANAGE_URL_STR}/animeEpisode/new?animeInfoId=${animeInfo.id}"> 增加剧集信息 </a>
</div>


<div>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="row">
				<table>
					<c:forEach items="${ subtitleInfoList }" var="subtitleInfo">
						<tr>
							<td style="font-size: 10px; padding: 3px;"><a href="${ctx}${MANAGE_URL_STR}/subtitle/view/${subtitleInfo.id}">${subtitleInfo.id}</a></td>
							<td style="font-size: 10px; padding: 3px;">${animeEpisode.divisionName}</td>
							<td style="padding: 5px;">${subtitleInfo.subInStatus == 1 ? '已录入' : ''}</td>
							<td style="font-size: 10px; padding: 3px;">${subtitleInfo.localFileName}</td>
						</tr>
					</c:forEach>
				</table>
			</div>
		</div>
	</div>
</div>

<div>
	<a href="${ctx}${MANAGE_URL_STR}/subtitle/new?animeInfoId=${animeInfo.id}"> 增加字幕信息 </a>
</div>