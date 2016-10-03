<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<title>动态图片生成任务一览 第${shotTaskPage.number + 1}页 - 动画截图网</title>
<head>
<meta name="keywords" content="动态图片生成任务一览 第${shotTaskPage.number + 1}页 - 动画截图网,动画截图网,动画截图,动漫截图,动漫图片,动画图片,截图字幕" />
<meta name="description" content="动态图片生成任务一览 第${shotTaskPage.number + 1}页 - 动画截图网,动漫截图,动漫图片,动画图片,截图字幕" />

<style>
.blockTitle {
	margin-top: 10px;
	margin-left: 10px;
	margin-bottom: 10px;
	font-size: 21px;
	text-align: left;
}

.blockTitle .count {
	margin-left: 10px;
	font-size: 12px;
}
</style>
</head>

<script>
	<c:if test="${IS_MANAGER}">
	function masterLike(id) {
		home.masterLike("${MANAGE_URL_STR}/shot/masterLike", id, "#masterLike_" + id, "#publicLike_" + id);
	}
	</c:if>

	function doGifTask() {
		if (confirm("是否继续？")) {
			$("#mainForm").submit();
		}
	}

	$(function() {
		jqueryAutoComplete("animeNameSearch", "${ctx}/ajaxAutoComplete/getAnimeName", {
			fillRelation : {
				"animeInfoId" : "code",
				"animeNameSearch" : "label"
			},
			isAutoCleanSourceObj : true,
			minLength : 0
		});

		jqueryAutoComplete("episodeNameSearch", "${ctx}/ajaxAutoComplete/getEpisodeName", {
			fillRelation : {
				"episodeInfoId" : "code",
				"episodeNameSearch" : "label"
			},
			isAutoCleanSourceObj : true,
			minLength : 0
		});
	});
</script>

<div>
	<!-- 标题 -->
	<div class="blockTitle">动态图片生成任务一览</div>

	<!-- 截图一览 -->
	<div class="row">
		<!-- 添加新任务 -->
		<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">

			<form id="mainForm" action="${ctx}/gif/addGifTask" method="post">
				<input type="hidden" name="calcFlag" value="1">
				<input type="hidden" name="saveFlag" value="1">
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
					<div class="col-sm-3">添加新任务</div>
				</div>
				<!-- <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
					<div class="col-sm-3">动画名</div>
					<div class="col-sm-6">
						<input name="animeInfoId" id="animeInfoId" type="hidden" />
						<input name="animeNameSearch" id="animeNameSearch" class="form-control input-sm" placeholder="请输入动画名称" />
					</div>
				</div> -->
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
					<div class="col-sm-3">剧集名</div>
					<div class="col-sm-6">
						<input name="episodeInfoId" id="episodeInfoId" type="hidden" />
						<input name="episodeNameSearch" id="episodeNameSearch" class="form-control input-sm" placeholder="请输入剧集名称" />
					</div>
				</div>
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
					<div class="col-sm-3">开始时间</div>
					<div class="col-sm-6">
						<input name="startTime" id="startTime" class="form-control input-sm" placeholder="请输入开始时间，单位秒" />
					</div>
				</div>
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
					<div class="col-sm-3">开始时间（分）</div>
					<div class="col-sm-6">
						<input name="startTimeMinute" id="startTimeMinute" class="form-control input-sm" placeholder="请输入开始时间，单位分" />
					</div>
				</div>
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
					<div class="col-sm-3">开始时间（秒）</div>
					<div class="col-sm-6">
						<input name="startTimeSecond" id="startTimeSecond" class="form-control input-sm" placeholder="请输入开始时间，单位秒" />
					</div>
				</div>
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
					<div class="col-sm-3">持续时间</div>
					<div class="col-sm-6">
						<input name="continueTime" id="continueTime" class="form-control input-sm" placeholder="最长10秒" />
					</div>
				</div>


				<div style="margin-top: 5px;" class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
					<div class="col-sm-3"></div>
					<div align="left" class="col-sm-9">
						您的昵称
						<input name="userName" value="<c:out value='${cookieUserName}' />" class="input-sm" placeholder="请输入您的昵称">
					</div>
				</div>
				<div style="margin-top: 5px;" class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
					<div class="col-sm-3"></div>
					<div align="left" class="col-sm-9">
						数据名称
						<input name="name" value="<c:out value='${name}' />" class="input-sm" placeholder="请给你的计算数据起个名字">
					</div>
				</div>
				<div style="margin-top: 10px;" class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
					<div class="col-sm-3"></div>
					<div align="left" class="col-sm-2">
						<input type="button" class="btn btn-sm btn-primary" onclick="doGifTask();" value="获取gif动态图片" />
					</div>
				</div>
				<div style="margin-top: 5px;" class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
					<div class="col-sm-3"></div>
					<div align="left" class="col-sm-9">
						<div style="padding-top: 1px;" class="bdsharebuttonbox quickShareDivClass">
							<a style="background-position: 0 -32px; padding-left: 0px; padding-right: 10px;" title="分享">分享</a>
							<a href="#" class="bds_mshare" data-cmd="mshare" title="百度一键分享"></a>
							<a href="#" class="bds_tieba" data-cmd="tieba" title="分享到百度贴吧"></a>
							<a href="#" class="bds_sqq" data-cmd="sqq" title="分享到QQ好友"></a>
							<a href="#" class="bds_weixin" data-cmd="weixin" title="分享到微信"></a>
							<a href="#" class="bds_bdxc" data-cmd="bdxc" title="分享到百度相册"></a>
							<a href="#" class="bds_duitang" data-cmd="duitang" title="分享到堆糖"></a>
							<a href="#" class="bds_copy" data-cmd="copy" title="复制网址"></a>
							<a href="#" class="bds_evernotecn" data-cmd="evernotecn" title="分享到印象笔记"></a>
							<a href="#" class="bds_more" data-cmd="more" title="更多"></a>
						</div>
					</div>
				</div>
			</form>
		</div>

		<!-- 任务一览 -->
		<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
			<table class="table table-bordered table-hover table-condensed table-body-scroll">
				<caption>任务一览</caption>
				<thead style="display: block; width: 100%;">
					<tr style="width: 100%;">
						<td style="width: 40%;">
							<nobr>动画名</nobr>
						</td>
						<td style="width: 40%;">
							<nobr>开始时间</nobr>
						</td>
						<td style="width: 40%;">
							<nobr>持续时间</nobr>
						</td>
						<td style="width: 40%;">
							<nobr>任务开始时间</nobr>
						</td>
						<td style="width: 20%;">
							<nobr>任务状态</nobr>
						</td>
						<td style="width: 40%;">
							<nobr>任务结束时间</nobr>
						</td>
					</tr>
				</thead>
				<tbody style="overflow-y: auto; max-height: 300px; display: block; word-break: break-all;">
					<c:forEach items="${ shotTaskPage.content }" var="shotTask">
						<tr>
							<td style="width: 40%;">
								<fmt:formatDate value="${shotTask.createDate}" pattern="yyyy-MM-dd HH:mm:ss" />
							</td>
							<td style="width: 20%;">
								<c:out value='${shotTask.taskResult }' />
							</td>
							<td style="width: 40%;">
								<fmt:formatDate value="${shotTask.endTime}" pattern="yyyy-MM-dd HH:mm:ss" />
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</div>

<div>
	<a class="btn btn-primary" href="${ctx}/">返回首页</a>
</div>

