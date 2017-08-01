<%@ page language="java" contentType="text/html; UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<title><decorator:title default="动画截图网" /></title>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta http-equiv="Cache-Control" content="public, max-age=1" />
<meta http-equiv="Content-Language" content="zh-cn" />
<link rel="shortcut icon" type="image/x-icon" href="${ ctx }/static/img/logo.png" media="screen" />
<link rel="miphtml" href="${thisPageMipUrl}">

<decorator:head />

<!-- 初始化所有参数 -->
<script>
	var _speedMark = new Date();
	var global = {};
	var baseUrl;
	var ctx;
	if (global.ctx == null) {
		global.baseUrl = '${siteBaseUrl}';
		global.baseUrl = global.baseUrl.replace("http://", "//");
		global.baseUrl = global.baseUrl.replace("https://", "//");
		global.ctx = '${ctx}';
		baseUrl = '${siteBaseUrl}';
		ctx = '${ctx}';
	}
	var IS_MASTER = false;
	var MANAGE_URL_STR = "";
	<c:if test="${IS_MASTER}">
	IS_MASTER = "${IS_MASTER}";
	MANAGE_URL_STR = "${MANAGE_URL_STR}";
	</c:if>
	var IS_JS_DEBUG = false;
	<c:if test="${IS_JS_DEBUG}">
	IS_JS_DEBUG = "${IS_JS_DEBUG}";
	</c:if>

	// 是否进行网站统计
	var canBaiduRecord = false;
	// 是否让搜索引擎索引
	var canBaiduIndex = false;
	<c:if test="${canBaiduRecord eq true}">
	canBaiduRecord = "${canBaiduRecord}";
	</c:if>
	<c:if test="${canBaiduIndex eq true}">
	canBaiduIndex = "${canBaiduIndex}";
	</c:if>

	var resetRowMaxHeightBySelectorDoneFlag = false;
</script>

<!-- 定义使用的资源版本 -->
<c:set var="staticResourceUrl" value="${ ctx }/static/plugin/" />
<c:set var="jqueryVersion" value="2.2.1" />
<c:set var="jqueryFormVersion" value="3.51" />
<c:set var="bootstrapVersion" value="3.3.6" />
<c:set var="jqueryLazyloadVersion" value="jquery_lazyload/1.9.7" />
<c:set var="jqueryCookieVersion" value="1.4.1" />
<c:set var="zeroClipboardVersion" value="2.2.0" />
<c:set var="jqueryUiVersion" value="1.12.1" />
<c:set var="bootstrapSwitch" value="3.3.2" />

<c:set var="useCdnStatic" value="bootcss" />
<c:if test="${ useCdnStatic eq 'baidu' }">
	<c:set var="staticResourceUrl" value="${ BAIDU_STATIC_URL }" />
	<c:set var="jqueryVersion" value="2.1.4" />
	<c:set var="bootstrapVersion" value="3.3.4" />
	<c:set var="jqueryLazyloadVersion" value="jquery-lazyload/1.9.5" />
</c:if>
<c:if test="${ useCdnStatic eq 'bootcss' }">
	<c:set var="staticResourceUrl" value="//cdn.bootcss.com/" />
</c:if>

<!-- css -->
<link href="${ staticResourceUrl }bootstrap/${bootstrapVersion}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="${ staticResourceUrl }jqueryui/${jqueryUiVersion}/jquery-ui.min.css" rel="stylesheet" type="text/css" />
<link href="${ ctx }/static/css/style.css" rel="stylesheet" type="text/css" />
<link href="${ ctx }/static/js/layui/css/layui.css" rel="stylesheet" type="text/css" />
<link href="${ staticResourceUrl }bootstrap-switch/${bootstrapSwitch}//css/bootstrap3/bootstrap-switch.min.css" rel="stylesheet" type="text/css" />

<!-- jquery -->
<script src="${ staticResourceUrl }jquery/${jqueryVersion}/jquery.min.js" type="text/javascript"></script>
<script src="${ staticResourceUrl }jquery.form/${jqueryFormVersion}/jquery.form.min.js" type="text/javascript"></script>
<script src="${ staticResourceUrl }jquery-cookie/${jqueryCookieVersion}/jquery.cookie.min.js" type="text/javascript"></script>
<script src="${ staticResourceUrl }jqueryui/${jqueryUiVersion}/jquery-ui.min.js" type="text/javascript"></script>

<!-- other 
<script src="${ staticResourceUrl }bootstrap-switch/${bootstrapSwitch}/js/bootstrap-switch.min.js" type="text/javascript"></script>
-->

<script src="${ ctx }/static/js/homeBase.js" type="text/javascript"></script>
</head>

<body class="<shiro:principal property="showSidebar"></shiro:principal>">
	<div id="header">
		<%@ include file="/WEB-INF/layouts/header.jsp"%>
	</div>

	<c:if test="${IS_MASTER}">
		<div>
			<input id="aaaaa">
			<script type="text/javascript">
				document.getElementById("aaaaa").value = document.documentElement.clientWidth + "," + window.innerWidth;
			</script>
		</div>
		<div>
			<c:out value="${thisPageUrl}"></c:out>
		</div>
		<div>
			<c:out value="${thisPageUrl}"></c:out>
		</div>
	</c:if>

	<div id="section" align="center">
		<div class="page-content container-fluid">
			<decorator:body />
		</div>
	</div>

	<div id="footer">
		<%@ include file="/WEB-INF/layouts/footer.jsp"%>

		<!-- jquery -->
		<script src="${ staticResourceUrl }${jqueryLazyloadVersion}/jquery.lazyload.min.js" type="text/javascript"></script>

		<!-- bootstrap -->
		<script src="${ staticResourceUrl }bootstrap/${bootstrapVersion}/js/bootstrap.min.js" type="text/javascript"></script>

		<!-- other -->
		<!-- <script src="${ staticResourceUrl }zeroclipboard/${zeroClipboardVersion}/ZeroClipboard.min.js" type="text/javascript"></script> -->

		<!-- local js -->
		<script src="${ ctx }/static/js/template/jsrender.min.js" type="text/javascript"></script>
		<script src="${ ctx }/static/js/layui/layui.js" type="text/javascript"></script>
		<script src="${ ctx }/static/js/layui/lay/dest/layui.all.js" type="text/javascript"></script>

		<!-- self js -->
		<script src="${ ctx }/static/js/message.js" type="text/javascript"></script>
		<script src="${ ctx }/static/js/homeBussness.js" type="text/javascript"></script>
		<script src="${ ctx }/static/js/homeInit.js" type="text/javascript"></script>
	</div>
</body>

</html>