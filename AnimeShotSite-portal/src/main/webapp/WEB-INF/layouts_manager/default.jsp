<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<title><sitemesh:title/></title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<link rel="shortcut icon" type="image/x-icon" href="${ ctx }/static/img/logo.png" media="screen" /> 
<!-- BEGIN GLOBAL MANDATORY STYLES -->

<!-- jquery -->
<script src="${ BAIDU_STATIC_URL }jquery/2.1.4/jquery.js" type="text/javascript"></script>
<link href="${ ctx }/static/media/css/jquery-ui-1.10.1.custom.min.css" rel="stylesheet" type="text/css"/>

<!-- bootstrap -->
<script src="${ BAIDU_STATIC_URL }bootstrap/3.3.4/js/bootstrap.js" type="text/javascript"></script>
<link href="${ BAIDU_STATIC_URL }bootstrap/3.3.4/css/bootstrap.css" rel="stylesheet" type="text/css"/>
<link href="${ ctx }/static/media/css/bootstrap-responsive.min.css" rel="stylesheet" type="text/css"/>



<!-- bootstrap -->
<link href="${ ctx }/static/media/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
<link href="${ ctx }/static/media/css/style-metro.css" rel="stylesheet" type="text/css"/>
<link href="${ ctx }/static/media/css/style.css" rel="stylesheet" type="text/css"/>
<link href="${ ctx }/static/media/css/style-responsive.css" rel="stylesheet" type="text/css"/>
<%
//<link href="${ ctx }/static/media/css/<shiro:principal property="skinPath"></shiro:principal>.css" rel="stylesheet" type="text/css" id="style_color"/>
%>
<link href="${ ctx }/static/media/css/uniform.default.css" rel="stylesheet" type="text/css"/>
<link href="${ ctx }/static/media/css/select2_metro.css" rel="stylesheet" type="text/css" />
<link href="${ ctx }/static/media/css/DT_bootstrap.css" rel="stylesheet"  />
<link href="${ ctx }/static/media/css/bootstrap-modal.css" rel="stylesheet" type="text/css"/>
<link href="${ ctx }/static/css/chosen/chosen.min.css" rel="stylesheet" type="text/css"/>
<link href="${ ctx }/static/css/jquery.ui/jquery-ui.min.css" rel="stylesheet" type="text/css"/>
<link href="${ ctx }/static/css/jquery.ui/jquery-ui.structure.min.css" rel="stylesheet" type="text/css"/>
<link href="${ ctx }/static/css/jquery.ui/jquery-ui.theme.min.css" rel="stylesheet" type="text/css"/>
<link href="${ ctx }/static/css/style.css" rel="stylesheet" type="text/css"/>
<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
<!-- BEGIN CORE PLUGINS -->
<script src="${ ctx }/static/media/js/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>
<!-- IMPORTANT! Load jquery-ui-1.10.1.custom.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->
<script src="${ ctx }/static/media/js/jquery-ui-1.10.1.custom.min.js" type="text/javascript"></script>      
<!--[if lt IE 9]>
<script src="${ ctx }/static/media/js/excanvas.min.js"></script>
<script src="${ ctx }/static/media/js/respond.min.js"></script>  
<![endif]-->   
<script src="${ ctx }/static/media/js/jquery.slimscroll.min.js" type="text/javascript"></script>
<script src="${ ctx }/static/media/js/jquery.blockui.min.js" type="text/javascript"></script>  
<script src="${ ctx }/static/media/js/jquery.cookie.min.js" type="text/javascript"></script>
<script src="${ ctx }/static/media/js/jquery.uniform.min.js" type="text/javascript" ></script>
<script src="${ ctx }/static/js/template/template.js" type="text/javascript" ></script>
<script src="${ ctx }/static/js/bootstrap-paginator/bootstrap-paginator.min.js" type="text/javascript" ></script>
<script src="${ctx }/static/media/js/bootstrap-modal.js" type="text/javascript" ></script>
<script src="${ctx }/static/media/js/bootstrap-modalmanager.js" type="text/javascript" ></script>
<script src="${ctx }/static/media/js/ui-modals.js"></script>
<script src="${ctx }/static/media/js/jquery.validate.min.js"></script>
<script src="${ctx }/static/js/jquery.serializeJSON/jquery.serializeJSON.js"></script>
<script src="${ctx }/static/js/jquery.ui/jquery-ui.min.js"></script>
<script src="${ctx }/static/js/jquery.ui/jquery-ui-i18n.js"></script>
<script src="${ctx }/static/js/chosen/chosen.jquery.min.js"></script>
<script src="${ctx }/static/js/chosen/chosen.proto.min.js"></script>
<script src="${ctx }/static/js/base/base-yjy.js"></script>
<!-- END CORE PLUGINS -->
<!-- END JAVASCRIPTS -->

<!-- END GLOBAL MANDATORY STYLES -->
<script>
    !function (global) {
        global.baseUrl= '${ctx}';
    }(window);
</script>
<sitemesh:head />
</head>
<body class="page-header-fixed page-sidebar-fixed <shiro:principal property="showSidebar"></shiro:principal>">
	<%@ include file="/WEB-INF/layouts/header.jsp"%>
	<div class="page-container row-fluid">
		<%@ include file="/WEB-INF/layouts/left.jsp"%>
		<div id="main" class="page-content">
			<sitemesh:body />
		</div>
	</div>
	<%@ include file="/WEB-INF/layouts/footer.jsp"%>
	<script src="${ctx }/static/media/js/app.js"></script>
</body>
</html>