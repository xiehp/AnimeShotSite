<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/> 
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<!-- BEGIN HEADER -->

<style type="text/css">
	.page-title small{
		font-size: 22px;
		font-weight: 600;
	}
</style>

<div class="header navbar navbar-inverse navbar-fixed-top">

	<!-- BEGIN TOP NAVIGATION BAR -->

	<div class="navbar-inner">

		<div class="container-fluid">

			<!-- BEGIN LOGO -->

			<a class="brand" href="${ ctx }/index"> <img
				src="${ ctx }/static/img/logo.png" alt="logo" width="20" />
				望盛金融
			</a>

			<!-- END LOGO -->

			<!-- BEGIN RESPONSIVE MENU TOGGLER -->

			<a href="javascript:;" class="btn-navbar collapsed"
				data-toggle="collapse" data-target=".nav-collapse"> <img
				src="${ ctx }/static/media/image/menu-toggler.png" alt="" />

			</a>

			<!-- END RESPONSIVE MENU TOGGLER -->

			<!-- BEGIN TOP NAVIGATION MENU -->

			<ul class="nav pull-right">

				<!-- BEGIN USER LOGIN DROPDOWN -->

				<li class="dropdown user"><a href="#" class="dropdown-toggle"
					data-toggle="dropdown"> <img alt=""
						src="${ ctx }/static/media/image/avatar1_small.jpg" /> <span class="username"><shiro:principal property="userName"/></span> <i class="icon-angle-down"></i>

				</a>

					<ul class="dropdown-menu">
						<li><a href="${ ctx }/user/setting"><i class="icon-user"></i> 修改资料</a></li>
						<li><a href="${ ctx }/logout"><i class="icon-key"></i> Log Out</a></li>
					</ul></li>

				<!-- END USER LOGIN DROPDOWN -->

			</ul>

			<!-- END TOP NAVIGATION MENU -->

		</div>

	</div>

	<!-- END TOP NAVIGATION BAR -->

</div>
<!-- END HEADER -->