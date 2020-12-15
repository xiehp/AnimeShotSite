<%@ page language="java" contentType="text/html; UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<title><decorator:title default="动画截图网" /></title>
<script type="text/javascript">window._MM_HS=+new Date;</script>
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
		baseUrl = global.baseUrl;
		ctx = global.ctx;
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
<link href="${ staticResourceUrl }bootstrap-switch/${bootstrapSwitch}/css/bootstrap3/bootstrap-switch.min.css" rel="stylesheet" type="text/css" />

<!-- jquery -->
<script src="${ staticResourceUrl }jquery/${jqueryVersion}/jquery.min.js" type="text/javascript"></script>
<script src="${ staticResourceUrl }jquery.form/${jqueryFormVersion}/jquery.form.min.js" type="text/javascript"></script>
<script src="${ staticResourceUrl }jquery-cookie/${jqueryCookieVersion}/jquery.cookie.min.js" type="text/javascript"></script>
<script src="${ staticResourceUrl }jqueryui/${jqueryUiVersion}/jquery-ui.min.js" type="text/javascript"></script>

<!-- other 
<script src="${ staticResourceUrl }bootstrap-switch/${bootstrapSwitch}/js/bootstrap-switch.min.js" type="text/javascript"></script>
-->

<script src="${ ctx }/static/js/homeBase.js" type="text/javascript"></script>

<script type="text/javascript">
	window._MM_CID = "1", window._MM_TP = "pc", function (a) {
	"use strict";
	a.JSMON = {
		_head_end: +new Date,
		_head_start: a._MM_HS || 0,
		_customer_id: a._MM_CID,
		_part2_url: "https://s1.mmtrix.com/jm/v1/" + a._MM_CID + "/p.js",
		_part2_expire_minutes: 60,
		_tp: a._MM_TP,
		_time_stamps: {},
		_fs_marks: [],
		_waiting_list: a._MM_WL && a._MM_WL.split(",") || [],
		_mark: function (a) {
			this._time_stamps[a] = +new Date
		},
		mark: function (a, b) {
			var c, d = +b || +new Date, e = this._time_stamps, f = this._waiting_list, g = [];
			for ((!e[a] || e[a] < d) && (e[a] = d), c = f.length - 1; c >= 0; c--) f[c] !== a && g.push(f[c]);
			g.length < f.length && (this._waiting_list = g, this.goahead && this.goahead())
		}
	};
	var b = function (a, b) {
		var c = !1, d = !0, e = a.document, f = e.documentElement, g = !!e.addEventListener, h = g ? "addEventListener" : "attachEvent", i = g ? "removeEventListener" : "detachEvent", j = g ? "" : "on", k = function (d) {
			("readystatechange" != d.type || /complete|interactive/.test(e.readyState)) && (d.type && ("load" == d.type ? a : e)[i](j + d.type, k, !1), !c && (c = !0) && b.call(a, d.type || d))
		}, l = function () {
			try {
				f.doScroll("left")
			} catch (a) {
				return void setTimeout(l, 50)
			}
			k("poll")
		};
		if ("complete" == e.readyState) b.call(a, "lazy"); else {
			if (!g && f.doScroll) {
				try {
					d = !a.frameElement
				} catch (m) {
				}
				d && l()
			}
			e[h](j + "DOMContentLoaded", k, !1), e[h](j + "readystatechange", k, !1), a[h](j + "load", k, !1)
		}
	};
	b(a, function (a) {
		window.JSMON && window.JSMON._mark && window.JSMON._mark("_dc2");
		var b, c, d, e, f, g = window, h = g.document, i = h.getElementsByTagName("img"), j = h.getElementsByTagName("IFRAME");
		for (f = function () {
			g.JSMON._fs_marks.push({img: this, time: +new Date}), this.removeEventListener ? this.removeEventListener("load", f, !1) : this.detachEvent && this.detachEvent("IFRAME" === this.nodeName ? "onload" : "onreadystatechange", f)
		}, b = 0, c = i.length; c > b; b++) d = i[b], d.addEventListener ? !d.complete && d.addEventListener("load", f, !1) : d.attachEvent && d.attachEvent("onreadystatechange", function () {
			"complete" == d.readyState && f.call(d)
		});
		for (b = 0, c = j.length; c > b; b++) e = j[b], e.addEventListener ? e.addEventListener("load", f, !1) : e.attachEvent && e.attachEvent("onload", function () {
			f.call(e)
		})
	});
	var c = function () {
		window.JSMON && window.JSMON._mark && window.JSMON._mark("_lt"), setTimeout(function () {
			var a, b = document.createElement("script"), c = window.JSMON;
			b.type = "text/javascript", a = c._part2_url, b.src = a, document.getElementsByTagName("body")[0].appendChild(b)
		}, 0)
	};
	document.addEventListener ? a.addEventListener("load", c, !1) : a.attachEvent("onload", c)
}(window);
</script>
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