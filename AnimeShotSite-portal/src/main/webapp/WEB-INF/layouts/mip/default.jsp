<%@page import="xie.common.web.util.ConstantsWeb"%>
<%@ page language="java" contentType="text/html; UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html mip>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width,minimum-scale=1,initial-scale=1">
<link rel="stylesheet" type="text/css" href="https://mipcache.bdstatic.com/static/v1/mip.css">
<link rel="canonical" href="${thisPageOriginalUrl}">

<title><decorator:title default="动画截图网" /></title>

<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta http-equiv="Cache-Control" content="max-age=6" />
<meta http-equiv="Content-Language" content="zh-cn" />
<link rel="shortcut icon" type="image/x-icon" href="//www.acgimage.com/static/img/logo.png" media="screen" />


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

<style mip-custom>

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

#bs-navbar {
	height: 0;
}

.page-content {
	max-width: 1280px;
}

/* 菜单高度 */
.mip-nav-wrapper #bs-navbar .navbar-nav li {
	line-height: 30px !important;
}

.languageChangeLi {
	margin: auto;
}

.languageChangeLi td:FIRST-CHILD {
	vertical-align: top;
}

.languageChangeLi td:FIRST-CHILD a {
	margin-right: 10px;
}

.languageChangeBlock {
	display: inline-block;
}

.languageChangeLink {
	
}

.languageChangeLink li {
	
}

.languageChangeLink li a, .languageChangeLink li span {
	line-height: 25px;
	font-size: 14px !important;
	text-align: left !important;
}

/* 左上角图标 */
.navbar-brand {
	white-space: nowrap;
}

/*******************
网站用css 
*******************/
.btn {
	margin: 5px;
	display: inline-block;
}

/* 图片根据尺寸自动改变大小 */
/* 
<c:set var="ImageAspectRatio" value="${(not empty animeEpisode.height and not empty animeEpisode.width) ? animeEpisode.height/animeEpisode.width : 9/16}" />
<c:set var="shotImgHeight1" value="${ImageAspectRatio * 46.5}vw" />
<c:set var="shotImgHeightStyle1" value="height: ${shotImgHeight1};" />
<c:set var="shotImgHeight2" value="${ImageAspectRatio * 31}vw" />
<c:set var="shotImgHeightStyle2" value="height: ${shotImgHeight2};" />
<c:set var="shotImgHeight3" value="${ImageAspectRatio * 23.5}vw" />
<c:set var="shotImgHeightStyle3" value="height: ${shotImgHeight3};" />
<c:set var="shotImgHeight4" value="${ImageAspectRatio * 18.5}vw" />
<c:set var="shotImgHeightStyle4" value="height: ${shotImgHeight4};" />
<c:set var="shotImgHeight5" value="${ImageAspectRatio * 15}vw" />
<c:set var="shotImgHeightStyle5" value="height: ${shotImgHeight5};" />
 */
@media ( min-width : 1px) {
	.listImg {
		width: 46.5vw;
	}
	.listImg mip-img { ${shotImgHeightStyle1
		
	}
}

}
@media ( min-width : 768px) {
	.listImg {
		width: 31vw;
	}
	.listImg mip-img { ${shotImgHeightStyle2
		
	}
}

}
@media ( min-width : 1024px) {
	.listImg {
		width: 23.5vw;
	}
	.listImg mip-img { ${shotImgHeightStyle3
		
	}
}

}
@media ( min-width : 1400px) {
	.listImg {
		width: 18.5vw;
	}
	.listImg mip-img { ${shotImgHeightStyle4
		
	}
}

}
@media ( min-width : 1800px) {
	.listImg {
		width: 15vw;
	}
	.listImg mip-img { ${shotImgHeightStyle5
		
	}
}

}
.listImg {
	display: inline-block;
	padding: 0.5vw;
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

.blockTitle .count {
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

.shotList-blockTitle h1 {
	font-size: 20px;
	display: inline;
}

.shotList-blockTitle h1 small {
	font-weight: normal;
	color: gray;
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
/* 
<c:set var="shotImgHeight" value="${ImageAspectRatio * 98}vw" />
<c:set var="shotImgHeightStyle" value="height: ${shotImgHeight};" />
 */
.shotImgDivStyle {
	position: relative;
	margin-bottom: 10px;
}

.shotImgDivStyle .shotImg {
	
}

.shotImgDivStyle .shotImg { ${shotImgHeightStyle
	
}

}
.shotImgHotPoint {
	height: 100%;
	width: 40%;
	position: absolute;
	z-index: 2;
}

.shotSubtitle {
	margin-top: 0px;
	margin-bottom: 10px;
}

.shotSubtitle .subtitleText {
	padding-left: 5px;
}

.left-0 {
	left: 0;
}

.right-0 {
	right: 0;
}

.top-0 {
	top: 0;
}

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

.shotTitleInfo {
	white-space: nowrap;
	word-break: keep-all;
}

.shotTitleInfo .textLine {
	display: inline-block;
	margin: 5px;
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
	
}

.pagination>li>a, .pagination>li>span {
	position: relative;
	float: left;
	margin-left: -1px;
	line-height: 1.42857143;
	border: 1px solid #ddd;
	color: #337ab7;
	text-decoration: none;
	background-color: #fff;
	padding: 6px 12px;
}

.pagination .disabled {
	color: black;
}

.pagination .disabled a, .pagination .disabled span {
	color: black;
}

.pageInfoLine {
	margin-top: -20px;
	margin-bottom: 20px;
	font-size: 8px;
}

/* 各种自定义css */
.text-align-left {
	text-align: left;
}

.bold {
	font-weight: bold;
}

.margin-0 {
	margin: 0;
}

.margin-5px {
	margin: 5px;
}

.margin-top-5px {
	margin-top: 5px;
}

.margin-top-10px {
	margin-top: 10px;
}

.margin-top-50px {
	margin-top: 50px;
}

.margin-left-0px {
	margin-left: 0px;
}

.margin-bottom-3px {
	margin-bottom: 3px;
}

.padding-top-0px {
	padding-top: 0px;
}

.min-height-100px {
	min-height: 100px;
}

.font-size-10px {
	font-size: 10px;
}

.font-size-14px {
	font-size: 14px;
}

.font-size-16px {
	font-size: 16px;
}

.font-size-28px {
	font-size: 28px;
}

.bottomBlockLine {
	height: 60px;
	background-color: black;
	margin-top: 20px;
}

.display-none {
	display: none;
}

.color-lightgray {
	color: lightgray;
}

.width-95 {
	width: 95%;
}

.width-96 {
	width: 96%;
}

.width-98 {
	width: 98%;
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

	<div id="section" align="center">
		<div class="page-content">
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
	<script src="https://mipcache.bdstatic.com/static/v1/mip-stats-baidu/mip-stats-baidu.js"></script>
	<script src="https://mipcache.bdstatic.com/static/v1/mip-link/mip-link.js"></script>
	<script src="https://mipcache.bdstatic.com/static/v1/mip-accordion/mip-accordion.js"></script>
	<script src="https://mipcache.bdstatic.com/static/v1/mip-list/mip-list.js"></script>
	<script src="https://mipcache.bdstatic.com/static/v1/mip-mustache/mip-mustache.js"></script>
	<script src="https://mipcache.bdstatic.com/static/v1/mip-form/mip-form.js"></script>
</body>
</html>