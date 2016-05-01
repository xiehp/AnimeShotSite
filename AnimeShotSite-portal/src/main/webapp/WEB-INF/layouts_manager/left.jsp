<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@  taglib  uri="http://java.sun.com/jsp/jstl/functions"   prefix="fn" %>
<!-- BEGIN SIDEBAR -->
		<div class="page-sidebar nav-collapse collapse">
			<!-- BEGIN SIDEBAR MENU -->        
			<ul class="page-sidebar-menu" id="menu-sidebar">
			</ul>
			<!-- END SIDEBAR MENU -->
		</div>
<script id="menu-list" type="text/html">
<li>
<div class="sidebar-toggler hidden-phone"></div>
</li>
<li>
<i>&nbsp;</i>
</li>
<li>
<a href="{{baseUrl}}">
<i class="icon-home"></i> 
<span class="title">首页</span>
</a>
</li>
{{each menuList as menu i}}  
<li class="{{if activeMenuName == menu.identity}}active open{{/if}}">
	<!-- 一级菜单 -->
	<a class="active" href="javascript:;">
	<i class="{{ menu.menuIcon  ? menu.menuIcon :　'icon-sitemap' }}"></i> 
	<span class="title">{{ menu.name }}</span>
	<span class="arrow {{if menu.hasChildren && activeMenuName == menu.identity}}open{{/if}}"></span>
	</a>	
	{{if menu.children}}
		<ul class="sub-menu">
			{{each menu.children as subMenu i}}
				<li {{if secondActiveMenuName == subMenu.identity}} class="active" {{/if}}>
					{{if subMenu.url}}
						<a href="{{baseUrl}}{{subMenu.url}}">
					{{else}}
						<a href="javascript:;">
					{{/if}}
					{{ subMenu.name }}
					<span class="{{if subMenu.hasChildren}}arrow {{/if}}{{if secondActiveMenuName == subMenu.identity}}open{{/if}}"></span>
					</a>
					{{if subMenu.hasChildren}}
						<ul class="sub-menu">
						{{each subMenu.children as thirdMenu t}}
								<li {{if thirdActiveMenuName == thirdMenu.identity}}class="active"{{/if}}><a href="{{baseUrl}}{{thirdMenu.url}}">{{thirdMenu.name}}</a></li>
							{{/each}}
						</ul>
					{{/if}}
				</li>
			{{/each}}
		</ul>
	{{/if}}	
</li>	
{{/each}}	
</script>			
<script type="text/javascript">
//加载菜单
$(document).ready(function(){
	$.ajax({
		url : '${ctx}/menu/init',
		contentType : 'application/json',
		dataType : "json",
		type : "GET",
		success : function(data) {
			var activeMenuName = '${SECOND_MENU_STATUA_KEY}';
			var secondActiveMenuName = '${THIRD_MENU_STATUA_KEY}';
			var thirdActiveMenuName = '${FOURTH_MENU_STATUA_KEY}';
			var menuData = {
				menuList : data,
				activeMenuName : activeMenuName,
				secondActiveMenuName : secondActiveMenuName,
				thirdActiveMenuName : thirdActiveMenuName,
				baseUrl : baseUrl
			};
			var html = template('menu-list', menuData);
			$('#menu-sidebar').html(html);
		}
	});
});
</script>		

	
			