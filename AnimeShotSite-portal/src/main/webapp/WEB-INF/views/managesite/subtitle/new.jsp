<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<title>字幕信息</title>

<script>
	function doSubmit(newFlg) {
		if (confirm("是否继续？")) {
			if (newFlg) {
				$("input[name=id]").val("");
			}
			$("#mainForm").attr("action", "${ctx}${MANAGE_URL_STR}/subtitle/submit");
			$("#mainForm").submit();
		}
	}
	function doSubmitMuti() {
		if (confirm("是否继续？")) {
			$("#mainForm").attr("action", "${ctx}${MANAGE_URL_STR}/subtitle/submitMuti");
			$("#mainForm").submit();
		}
	}
	function doSubmitMutiByEpisode() {
		if (confirm("是否继续？")) {
			$("#mainForm").attr("action", "${ctx}${MANAGE_URL_STR}/subtitle/submitMutiByEpisode");
			$("#mainForm").submit();
		}
	}

	function submitSubtitleTask(type) {
		if (confirm("是否继续？")) {
			$("#mainForm").attr("action", "${ctx}${MANAGE_URL_STR}/subtitle/createSubtitleTask");
			$("#mainForm").submit();
		}
	}

	function updateOneColumn(columnName) {
		var param = {};
		param.id = "${subtitleInfo.id}";
		param.columnName = columnName;
		param.columnValue = $("#mainForm").find("[name=" + columnName + "]").val();
		$.homePost("${MANAGE_URL_STR}/subtitle/updateOneColumn", param, function(data) {
			if (data.success) {
				$.showMessageModal(data.message);
			} else {
				$.showMessageModal(data.message);
			}
		});
	}

	lazyRun(function() {
		// 初始化input,select的更新按钮
		$("#mainForm").find("select,input[name]").each(function() {
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
			<input class="form-control" name="id" value="${subtitleInfo.id}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">animeInfoId</label>
		<div class="col-sm-5">
			<input class="form-control" name="animeInfoId" value="${subtitleInfo.animeInfoId}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">动画名</label>
		<div class="col-sm-5">
			<c:out value="${animeInfo.fullName}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">animeEpisodeId</label>
		<div class="col-sm-5">
			<input class="form-control" name="animeEpisodeId" value="${subtitleInfo.animeEpisodeId}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">剧集名</label>
		<div class="col-sm-5">
			<c:out value="${animeEpisode.fullName}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">语言</label>
		<div class="col-sm-5">
			<!-- 
			<input class="form-control" name="language" value="${subtitleInfo.language}" />
			 -->
			<select class="form-control" name="language" value="${subitleInfo.language}">
				<option value=""></option>
				<option ${subtitleInfo.language == 'sc' ? 'selected=\"selected\"' : ''} value="sc">sc:简体</option>
				<option ${subtitleInfo.language == 'jp' ? 'selected=\"selected\"' : ''} value="jp">jp:日语</option>
				<option ${subtitleInfo.language == '3' ? 'selected=\"selected\"' : ''} value="3">3:简日</option>
				<option ${subtitleInfo.language == '4' ? 'selected=\"selected\"' : ''} value="4">4:繁体</option>
				<option ${subtitleInfo.language == '5' ? 'selected=\"selected\"' : ''} value="5">5:简繁</option>
				<option ${subtitleInfo.language == '6' ? 'selected=\"selected\"' : ''} value="6">6:日繁</option>
				<option ${subtitleInfo.language == '7' ? 'selected=\"selected\"' : ''} value="7">7:简日繁</option>
				<option ${subtitleInfo.language == '8' ? 'selected=\"selected\"' : ''} value="8">8:英语</option>
				<option ${subtitleInfo.language == '11' ? 'selected=\"selected\"' : ''} value="11">11:阿拉伯语</option>
			</select>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">字幕文件类型 ass str</label>
		<div class="col-sm-5">
			<input class="form-control" name="fileType" value="${subtitleInfo.fileType}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">1:外部文件字幕（需要指明本地文件地址） 2:内嵌字幕</label>
		<div class="col-sm-5">
			<!-- 
			<input  class="form-control" name="fileLocation" value="${subtitleInfo.fileLocation}" />
			 -->
			<select class="form-control" name="fileLocation">
				<option value=""></option>
				<option ${subtitleInfo.fileLocation == '1' ? 'selected=\"selected\"' : ''} value="1">1:外部字幕</option>
				<option ${subtitleInfo.fileLocation == '2' ? 'selected=\"selected\"' : ''} value="2">2:内部字幕</option>
			</select>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">是否作为截图的字幕显示在网页上</label>
		<div class="col-sm-5">
			<input class="form-control" name="showFlg" value="${subtitleInfo.showFlg}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">本地所在根路径</label>
		<div class="col-sm-5">
			<input class="form-control" name="localRootPath" value="${subtitleInfo.localRootPath}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">本地所在相对路径</label>
		<div class="col-sm-5">
			<input class="form-control" name="localDetailPath" value="${subtitleInfo.localDetailPath}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">本地所在文件名</label>
		<div class="col-sm-5">
			<input class="form-control" name="localFileName" value="${subtitleInfo.localFileName}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">当前字幕文件需要偏移的时间，单位毫秒</label>
		<div class="col-sm-5">
			<input class="form-control" name="offsetTime" value="${subtitleInfo.offsetTime}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">哪个时间节点开始偏移，单位毫秒</label>
		<div class="col-sm-5">
			<input class="form-control" name="offsetTimeStart" value="${subtitleInfo.offsetTimeStart}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">deleteFlag</label>
		<div class="col-sm-5">
			<input class="form-control" name="deleteFlag" value="${subtitleInfo.deleteFlag}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-7 control-label">规则(针对ass格式), "#":包含, "=":相等, ",":区分，判断不区分大小写，格式为name1=value1,name2#value2，value为正则表达式</label>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">包含某种属性文字filterInclude(Style#jp)</label>
		<div class="col-sm-5">
			<input class="form-control" name="filterInclude" value="${subtitleInfo.filterInclude}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">排除某种属性文字filterRemove</label>
		<div class="col-sm-5">
			<input class="form-control" name="filterRemove" value="${subtitleInfo.filterRemove}" />
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
		<input type="button" value="自动新建多个" onclick="doSubmitMutiByEpisode();" />
		<input type="button" value="新建多个(废弃)" onclick="doSubmitMuti();" />
	</div>


	<div style="height: 100px;"></div>
	<div class="form-group">
		<label class="col-sm-2 control-label">subtitleInfoId</label>
		<div class="col-sm-5">
			<input class="form-control" name="subtitleInfoId" value="${subtitleInfo.id}" />
		</div>
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
			<input class="form-control" name="timeInterval" value="${timeInterval}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">specifyTimes</label>
		<div class="col-sm-5">
			<input class="form-control" name="specifyTimes" value="${specifyTimes}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">forceUpdate 当存在字幕，仍然会再去执行一次</label>
		<div class="col-sm-5">
			<input class="form-control" name="forceUpdate" value="${forceUpdate}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">forceDelete 执行前会先将原来的删除掉</label>
		<div class="col-sm-5">
			<input class="form-control" name="forceDelete" value="${forceDelete}" />
		</div>
	</div>
	<div class="form-group">
		<input type="button" value="生成字幕" onclick="submitSubtitleTask(1);" />
	</div>

</form>

<a href="${ctx}${MANAGE_URL_STR}/anime/view/${subtitleInfo.animeInfoId}">返回上层</a>
<br />
<a href="${ctx}${MANAGE_URL_STR}/shot/list/${subtitleInfo.id}">截图一览</a>

<br />
<br />
<br />

<div class="row">
	<table border="1" style="padding: 5px;">
		<c:forEach items="${ subtitleLineList }" var="subtitleLine">
			<tr>
				<td style="padding: 5px;">${subtitleLine.startTimeMinSecMicro}</td>
				<td style="padding: 5px;">${subtitleLine.endTimeMinSecMicro}</td>
				<td style="padding: 5px;">${subtitleLine.text}</td>
			</tr>
		</c:forEach>
	</table>
</div>

<br />
<br />

<a href="${ctx}${MANAGE_URL_STR}/anime/view/${subtitleInfo.animeInfoId}">返回上层</a>
<br />
<a href="${ctx}${MANAGE_URL_STR}/shot/list/${subtitleInfo.id}">截图一览</a>

