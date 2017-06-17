<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>上传案例演示</title>
<link href="${ctx}/static/js/uploadify/uploadify.css" rel="stylesheet" type="text/css" />
</head>
<body>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12">
				<!-- BEGIN PAGE TITLE & BREADCRUMB-->
				<h4 class="page-title">
				</h4>
				<ul class="breadcrumb">
					<li>
						<i class="icon-home"></i>
						<a href="javascript:void(0);">案例管理</a>
						<i class="icon-angle-right"></i>
					</li>
					<li>
						<a href="javascript:void(0);">上传功能案例</a>
					</li>
				</ul>
				<!-- END PAGE TITLE & BREADCRUMB-->
			</div>
		</div>

		<!-- BEGIN PAGE CONTENT-->
		<div class="row-fluid">
			<div class="portlet-body form">
				<!-- BEGIN FORM-->
				<input type="file" name="fileUpload" id="fileUpload" />
			</div>
			<div>上传结果：</div>
			<div id="uploadResult"></div>
			<div></div>
			<!-- END PAGE CONTENT-->
		</div>
	</div>
	<script type="text/javascript" src="${ctx}/static/js/uploadify/jquery.uploadify.min.js"></script>
	<script type="text/javascript">
		lazyRun(function() {
			$('#fileUpload').uploadify({
				swf : '${ctx}/static/js/uploadify/uploadify.swf',
				fileObjName : "fileUpload",
				uploader : "${ctx}/sample/uploadFile",
				multi : false,
				fileTypeDesc : '请选择文件',
				fileTypeExts : '*.xls;*.xlsx;',
				fileSizeLimit : '15MB',
				width : 80,
				height : 24,
				buttonText : '选择文件',
				onUploadSuccess : function(file, data, response, attr) {
					data = JSON.parse(data);
					$('#uploadResult').html(data.filePath);
				},
				onUploadStart : function(file) {
					$('#fileUpload').uploadify("settings", "formData", {
						'params1' : '我是参数1',
						'params2' : '我是参数2',
					})
				},
			});
		})
	</script>
</body>
</html>