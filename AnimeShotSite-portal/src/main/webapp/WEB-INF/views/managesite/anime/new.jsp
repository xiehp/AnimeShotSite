<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="MANAGE_URL_STR" value="${MANAGE_URL_STR}" />

<title>动画详情 <c:out value='${animeInfo.fullName}' /></title>

<script type="text/javascript">
	lazyRun(function () {
		$("#mainForm").find("input[name]").each(function () {
			var params = {};
			params.name = $(this).attr("name");
			var buttonHtml = $("#updateOneColumnButtonTemplate").render(params);
			$(this).parent().after(buttonHtml);
		}, -1);

		// 编辑器
		var changesCount = 0;
		var summaryEditor = CKEDITOR.replace('summaryEditor', {});
		summaryEditor.on('change', function (ev) {
			$("input[name=summary]").val(summaryEditor.getData());
			document.getElementById('summaryCount').innerHTML = summaryEditor.getData().length;
		});
	});

	function updateOneColumn(columnName) {
		var param = {};
		param.id = "${animeInfo.id}";
		param.columnName = columnName;
		param.columnValue = $("#mainForm").find("input[name=" + columnName + "]").val();
		$.homePost("${MANAGE_URL_STR}/anime/updateOneColumn", param, function (data) {
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


	/** 显示剧集 */
	function updateToShow(animeEpisodeInfoId, domLink) {
		if (confirm("是否继续？")) {
			var param = {};
			param.id = animeEpisodeInfoId;
			param.columnName = "showFlg";
			param.columnValue = 1;
			$.homePost("${MANAGE_URL_STR}/animeEpisode/updateOneColumn", param, function (data) {
				if (data.success) {
					domLink.innerHTML = "已展示";
				} else {
					$.showMessageModal(data.message);
				}
			});
		}
	}

	function doShotTask(id, thisObj, startTime) {
		if (confirm("是否继续？")) {
			var param = {};
			param.taskType = 1;
			param.id = id;
			param.timeInterval = 5000;
			if (startTime != null) {
				param.startTime = startTime;
			}
			//$("#mainForm").attr("action", "${ctx}${MANAGE_URL_STR}/animeEpisode/addShotTask?taskType=1&id=" + id + "&timeInterval="+5000);
			$.homePost("${MANAGE_URL_STR}/animeEpisode/addShotTaskAjax", param, function (data) {
				if (data.success) {
					$.showMessageModal(data.message);
				} else {
					$.showMessageModal(data.message);
				}
			});
		}
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
		<div class="col-sm-9" style="min-height: 300px;">
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
		<label class="col-sm-2 control-label">获取简介的url</label>
		<div class="col-sm-5">
			<input class="form-control" name="summaryCollectUrl" value="<c:out value="${animeInfo.summaryCollectUrl}"></c:out>" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">获取简介标题的正则: .*?\s+(.*)</label>
		<div class="col-sm-5">
			<input class="form-control" name="summaryCollectTitleExp" value="<c:out value="${animeInfo.summaryCollectTitleExp}"></c:out>" />
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
		<label class="col-sm-2 control-label">showDate(新增时格式yyyy/MM/dd HH:mm:ss)</label>
		<div class="col-sm-5">
			<input class="form-control" name="showDate" value="<fmt:formatDate value='${animeInfo.showDate}' pattern='yyyy-MM-dd HH:mm:ss'/>" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">deleteFlag</label>
		<div class="col-sm-5">
			<input class="form-control" name="deleteFlag" value="${animeInfo.deleteFlag}" />
		</div>
	</div>

	<input type="submit" />

	<a href="${ctx}${MANAGE_URL_STR}/anime/list" class="btn btn-primary">返回上层</a>
</form>

<div style="margin-top: 20px;">
	是否强制更新
	<input id="updateEpisodeTitleAndSummaryForceUpdate" />
	<a href="javascript:void(0);" onclick="updateEpisodeTitleAndSummary('${animeInfo.id}', '', $('#updateEpisodeTitleAndSummaryForceUpdate').val());" class="btn btn-primary">获取百度title</a>

	<div class="row">
		<c:forEach items="${ animeEpisodeList }" var="animeEpisode">
			<div class="col-lg-3 col-sm-4 col-xs-6 thumbnail">
				<a href="${ctx}${MANAGE_URL_STR}/animeEpisode/view/${animeEpisode.id}">
					<img src="${ctx}/static/img/imageLoading_mini.jpg" data-original="${animeEpisode.titleUrl.urlS}" class="img-responsive imagelazy">
					<div style="margin-top: 5px;">
						<c:out value='${animeEpisode.divisionName}' />
						<c:out value='${animeEpisode.title}' />
					</div>
					<div style="margin-top: 5px;">
						<a href="javascript:void(0);" onclick="doShotTask('<c:out value='${animeEpisode.id}'/>', this);" style="color: blue;">从头截图</a>
						<c:if test="${!empty maxTimestampMap[animeEpisode.id]}">
							<a href="javascript:void(0);" onclick="doShotTask('${animeEpisode.id}', this, '${maxTimestampMap[animeEpisode.id]}');" style="color: blue;"> ${maxTimestampMap[animeEpisode.id]}开始截图 </a>
						</c:if>
						<c:if test="${animeEpisode.showFlg eq '0'}">
							<a href="javascript:void(0);" onclick="updateToShow('<c:out value='${animeEpisode.id}'/>', this);" style="color: red;">展示</a>
						</c:if>
						<c:if test="${animeEpisode.showFlg eq '1'}">
							<span style="${animeEpisode.showFlg == "1" ? "" :"color:red;"}"> ${animeEpisode.showFlg == "1" ? "已展示" :"未展示"} </span>
						</c:if>
						${empty shotCountMap[animeEpisode.id] ? 0 : shotCountMap[animeEpisode.id]}张
					</div>
					<div>
						<input type="button" class="stopEpisodeMonitor" data-animeepisodeid="${animeEpisode.id}" value="停止监视">
					</div>
				</a>
			</div>
		</c:forEach>
	</div>
</div>

<div>
	<a href="${ctx}${MANAGE_URL_STR}/animeEpisode/new?animeInfoId=${animeInfo.id}"> 增加剧集信息 </a>
</div>


<div style="margin-top: 20px;">
	<a href="javascript:void(0);" onclick="createSubtitleTask('<c:out value='${animeInfo.id}'/>');" style="color: blue;">生成字幕</a>
	<div class="row">
		<table>
			<c:forEach items="${ subtitleInfoList }" var="subtitleInfo">
				<tr>
					<td style="font-size: 10px; padding: 3px;">
						<a href="${ctx}${MANAGE_URL_STR}/subtitle/view/${subtitleInfo.id}">${subtitleInfo.id}</a>
					</td>
					<td style="font-size: 10px; padding: 3px;">${animeEpisode.divisionName}</td>
					<td style="padding: 5px;">${subtitleInfo.subInStatus == 1 ? '已录入' : ''}</td>
					<td style="font-size: 10px; padding: 3px;">${subtitleInfo.localFileName}</td>
				</tr>
			</c:forEach>
		</table>
	</div>
</div>

<div>
	<a href="${ctx}${MANAGE_URL_STR}/subtitle/new?animeInfoId=${animeInfo.id}"> 增加字幕信息 </a>
</div>

<div style="height: 100px;"></div>


<div>
	<div style="font-size: 24px;">自动化配置</div>
	<script id="autoRunRow" type="text/x-jsrender">
		<div class="form-group">
			<label class="col-sm-2 control-label">{{>name}}({{>key}})</label>
			<div class="col-sm-5">
				<input class="form-control" name="id" value="{{>value}}" />
			</div>
		</div>
	</script>

	<script id="updateOneAutoRunParamButtonTemplate" type="text/x-jsrender">
		<div class="col-md-1">
			<a class="btn btn-primary updateOneAutoRunParam" data-key="{{>key}}">更新</a>
		</div>
	</script>

	<script type="text/javascript">
		lazyRun(function () {
			// 创建更新按钮
			$("#autoRunForm").find("input[name=key]").each(function () {
				var params = {};
				params.key = $(this).val();
				var buttonHtml = $("#updateOneAutoRunParamButtonTemplate").render(params);
				$(this).parent().parent().append(buttonHtml);
			});

			//绑定点击事件
			$("a.updateOneAutoRunParam").on("click", function () {
				var key = $(this).data("key");
				updateOneAutoRunParam(key);
			})

			$("#beginMonitor").on("click", function () {
				var param = {};
				param.animeInfoId = $(this).data("animeinfoid");
				$.homePost("${MANAGE_URL_STR}/autorun/beginMonitor", param);
			})

			$("#stopMonitor").on("click", function () {
				var param = {};
				param.animeInfoId = $(this).data("animeinfoid");
				$.homePost("${MANAGE_URL_STR}/autorun/stopMonitor", param);
			})

			$(".stopEpisodeMonitor").on("click", function () {
				var param = {};
				param.animeEpisodeId = $(this).data("animeepisodeid");
				$.homePost("${MANAGE_URL_STR}/autorun/stopMonitor", param);
			})
		});

		function addAutoRunParam() {
			var paramsTemplate = {};
			paramsTemplate.name = "";
			paramsTemplate.key = "";
			paramsTemplate.value = "";
			var htmlStr = $("#autoRunRow").render(paramsTemplate);
			$("#autoRunFormInputDiv").after(htmlStr);
		}

		function updateOneAutoRunParam(key) {
			var $auto_param_div = $("#auto_param_" + key);
			var param = {};
			param.id = $auto_param_div.find("[name=id]").val();
			param.name = $auto_param_div.find("[name=name]").val();
			param.key = key;
			param.value = $auto_param_div.find("[name=value]").val();
			param.animeInfoId = $auto_param_div.find("[name=animeInfoId]").val();
			param.animeEpisodeId = $auto_param_div.find("[name=animeEpisodeId]").val();
			param.refId = $auto_param_div.find("[name=refId]").val();
			param.refType = $auto_param_div.find("[name=refType]").val();
			$.homePost("${MANAGE_URL_STR}/autorun/updateOneAutoRunParam", param);
		}
	</script>
	<div style="margin: 20px;">
		<input type="button" id="addAutoRunBtn" value="增加一行">
		<input type="button" id="beginMonitor" data-animeinfoid="${animeInfo.id}" value="开始监视">
		<input type="button" id="stopMonitor" data-animeinfoid="${animeInfo.id}" value="停止监视">
	</div>
	<form id="autoRunForm" class="form-horizontal" action="${ctx}${MANAGE_URL_STR}/anime/submit" method="post">
		<div id="autoRunFormInputDiv">
			<c:forEach items="${autoRunParamList}" var="autoRunParam">
				<div id="auto_param_${autoRunParam.key}" class="form-group">
					<div class="col-sm-3">
						<input class="form-control" name="id" value="${autoRunParam.id}">
						<br>
						<input class="form-control" name="animeInfoId" value="${animeInfo.id}">
						<br>
						<input class="form-control" name="name" value="${autoRunParam.name}">
						<br>
						<input class="form-control" name="key" value="${autoRunParam.key}">
						<br>
					</div>
					<div class="col-sm-5">
						<input class="form-control" name="value" value="${autoRunParam.value}" />
					</div>
				</div>
				<hr />
			</c:forEach>
		</div>
		<input type="submit" />

		<a href="${ctx}${MANAGE_URL_STR}/anime/list" class="btn btn-primary">返回上层</a>
	</form>
</div>




