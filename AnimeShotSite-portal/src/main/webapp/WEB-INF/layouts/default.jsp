<%@ page language="java" contentType="text/html; UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE>
<html>
<head>

<title><decorator:title default="动画截图网" /></title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<link rel="shortcut icon" type="image/x-icon" href="${ ctx }/static/img/logo.png" media="screen" />
<!-- BEGIN GLOBAL MANDATORY STYLES -->

<!-- jquery -->
<script src="${ BAIDU_STATIC_URL }jquery/2.1.4/jquery.js" type="text/javascript"></script>
<script src="${ BAIDU_STATIC_URL }jquery-lazyload/1.9.5/jquery.lazyload.js" type="text/javascript"></script>


<!-- bootstrap -->
<link href="${ BAIDU_STATIC_URL }bootstrap/3.3.4/css/bootstrap.css" rel="stylesheet" type="text/css" />
<script src="${ BAIDU_STATIC_URL }bootstrap/3.3.4/js/bootstrap.js" type="text/javascript"></script>
<!-- 
<link href="${ ctx }/static/media/css/jquery-ui-1.10.1.custom.min.css" rel="stylesheet" type="text/css"/>
<link href="${ ctx }/static/media/css/bootstrap-responsive.min.css" rel="stylesheet" type="text/css"/>
-->

<script>
	var global = {};
	var baseUrl;
	if (global.ctx == null) {
		global.ctx = '${ctx}';
		global.baseUrl = '${ctx}';
		baseUrl = '${ctx}';
	}
</script>

<!-- self -->
<link href="${ ctx }/static/css/style.css" rel="stylesheet" type="text/css" />
<script src="${ ctx }/static/js/homeInit.js" type="text/javascript"></script>
<script src="${ ctx }/static/js/homeBase.js" type="text/javascript"></script>



<!-- END CORE PLUGINS -->
<!-- END JAVASCRIPTS -->

<!-- END GLOBAL MANDATORY STYLES -->
<decorator:head />
<style type="text/css">
.page-content {
	width: 1024px;
}
</style>
<script src="//hm.baidu.com/hm.js?ce02602c958d6f6018b4a2b08090d822"></script>
<script>
	var _hmt = _hmt || [];
	(function() {
		var hm = document.createElement("script");
		hm.src = "//hm.baidu.com/hm.js?ce02602c958d6f6018b4a2b08090d822";
		var s = document.getElementsByTagName("script")[0];
		s.parentNode.insertBefore(hm, s);
	})();
</script>
</head>
<body class="<shiro:principal property="showSidebar"></shiro:principal>">
	<%@ include file="/WEB-INF/layouts/header.jsp"%>
	<div class="page-container row-fluid" align="center">
		<div id="main" class="page-content">
			<decorator:body />
		</div>
	</div>
	<%@ include file="/WEB-INF/layouts/footer.jsp"%>
	<script src="${ctx }/static/media/js/app.js"></script>
</body>
</html>