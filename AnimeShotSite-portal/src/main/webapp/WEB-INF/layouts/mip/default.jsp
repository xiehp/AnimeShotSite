<%@ page language="java" contentType="text/html; UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html mip>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
<link rel="stylesheet" type="text/css" href="https://mipcache.bdstatic.com/static/v1/mip.css">
<link rel="canonical" href="${canonicalUrl}">

<title><decorator:title default="动画截图网" /></title>

<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta http-equiv="Cache-Control" content="max-age=6" />
<meta http-equiv="Content-Language" content="zh-cn" />
<link rel="shortcut icon" type="image/x-icon" href="${ ctx }/static/img/logo.png" media="screen" />


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

<decorator:head />

<style type="text/css">

/*******************
覆盖mip用css 
*******************/
@media ( max-width : 768px) {
	.navbar-nav {
		margin: 5px !important;
	}
}

/* 右上角弹出菜单的div层 */
@media ( max-width : 767px) {
	#bs-navbar {
		max-width: 300px !important;
		right: 0 !important;
	}
}

/* 左上角图标 */
.navbar-brand {
	white-space: nowrap;
}

/*******************
网站用css 
*******************/

/* 图片根据尺寸自动改变大小 */
@media ( min-width : 1px) {
	.listImg {
		width: 48%;
	}
}

@media ( min-width : 768px) {
	.listImg {
		width: 31%;
	}
}

@media ( min-width : 1024px) {
	.listImg {
		width: 23.5%;
	}
}

@media ( min-width : 1400px) {
	.listImg {
		width: 18.5%;
	}
}

@media ( min-width : 1800px) {
	.listImg {
		width: 15%;
	}
}

.listImg {
	display: inline-block;
	padding: 0.5%;
	padding-top: 20px;
}

.blockTitle {
	margin-top: 10px;
	margin-left: 10px;
	margin-bottom: 10px;
	font-size: 20px;
	text-align: left;
}

.blockTitle {
	margin-top: 50px;
}

.blockTitle>.count {
	margin-left: 10px;
	font-size: 12px;
}

.wordKeepLine {
	width: 90%;
	white-space: nowrap;
	word-break: keep-all;
	overflow: hidden;
	text-overflow: ellipsis;
}

.noBreak {
	white-space: nowrap;
	word-break: keep-all;
}

/* 翻译菜单部分 */
.dropdown-menu .menuTitle a, .dropdown-menu .menuTitle a:FOCUS, .dropdown-menu .menuTitle a:HOVER, .dropdown-menu .menuTitle a:ACTIVE {
	font-weight: bold;
	cursor: default;
	background-color: gray;
	color: white;
}

.menuSelected {
	background-color: highlight;
}

.tranLanColorLi {
	width: 100px;
}

/* 剧集列表页面的css */
.AnimeSummary li>p {
	margin-bottom: 0px;
}

/* 截图列表页面的css */
.shotList-blockTitle {
	margin-top: 10px;
	margin-left: 10px;
	margin-bottom: 10px;
	font-size: 21px;
	text-align: left;
}

.shotList-blockTitle .count {
	margin-left: 10px;
	font-size: 12px;
}

.subtitleTable {
	padding-right: 5px;
	font-size: 10px;
	text-align: left;
	white-space: pre-wrap; /*css-3*/
	white-space: -moz-pre-wrap; /*Mozilla,since1999*/
	white-space: -pre-wrap; /*Opera4-6*/
	white-space: -o-pre-wrap; /*Opera7*/
	word-wrap: break-word; /*InternetExplorer5.5+*/
}

/* 截图详细页面的css */
.ShareLinkDiv {
	margin-top: 20px;
	max-width: 800px;
}

.ShareLinkItem {
	margin-top: 10px;
}

.ShareLinkItem>div {
	padding-right: 3px;
	padding-left: 3px;
}

@media ( min-width : 768px) {
	.ShareLinkLabel {
		text-align: right;
	}
	.ShareLinkButton {
		text-align: left;
	}
	.shotTitleInfo {
		width: 400px;
	}
}

@media ( max-width : 768px) {
	.noLeftRightPadding {
		padding-right: 1px !important;
		padding-left: 1px !important;
	}
}

.subtitleTranslatedText {
	font-size: <c:out value="${subtitleTranslatedTextFontsize}"></c:out>;
	color: <c:out value="${subtitleTranslatedTextColor}"></c:out>;
}

/* 翻页用css */
.pagination {
	display: inline-block;
	padding-left: 0;
	margin: 20px 0;
	border-radius: 4px;
}

.pagination>li {
	display: inline;
}

.pagination>li {
	position: relative;
	float: left;
	padding: 6px 12px;
	margin-left: -1px;
	line-height: 1.42857143;
	border: 1px solid #ddd;
}

.pagination>li>a, .pagination>li>span {
	color: #337ab7;
	text-decoration: none;
	background-color: #fff;
}
}
</style>

</head>

<body class="<shiro:principal property="showSidebar"></shiro:principal>">
	<div id="header">
		<%@ include file="header.jsp"%>
	</div>

	<c:if test="${IS_MASTER}">
		<input value="${siteBaseUrl}" size="50">
		<input value="${thisPageUrl}" size="100">
	</c:if>

	<div class="page-container row-fluid" align="center">
		<div id="section" class="page-content">
			<decorator:body />
		</div>
	</div>

	<div id="footer">
		<%@ include file="footer.jsp"%>
	</div>

	<!-- js -->
	<script src="https://mipcache.bdstatic.com/static/v1/mip.js"></script>
	<script src="https://mipcache.bdstatic.com/static/v1/mip-nav-slidedown/mip-nav-slidedown.js"></script>
	<script src="https://mipcache.bdstatic.com/static/v1/mip-gototop/mip-gototop.js"></script>

</body>
</html>