<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>权限列表</title>
</head>
<body>
	<div class="container-fluid">
		<!-- BEGIN PAGE HEADER-->
		<div class="row-fluid">
			<div class="span12">
				<!-- BEGIN PAGE TITLE & BREADCRUMB-->
				<h4 class="page-title">
				</h4>
				<ul class="breadcrumb">
					<li>
						<i class="icon-home"></i>
						<a href="javascript:void();">角色管理</a> 
						<i class="icon-angle-right"></i>
					</li>
					<li>
						<a href="javascript:void();">权限列表</a>
					</li>
				</ul>
				<!-- END PAGE TITLE & BREADCRUMB-->
			</div>
		</div>
		<!-- END PAGE HEADER-->
		<!-- BEGIN PAGE CONTENT-->
		<div class="row-fluid">
			<div class="span12">
				<!-- BEGIN EXAMPLE TABLE PORTLET-->
				<div class="portlet box blue span6">
					<div class="portlet-title">
						<div class="caption"><i class="icon-globe"></i></div>
					</div>
					<div class="portlet-body">
						<div id="sample_1_wrapper" class="dataTables_wrapper form-inline" role="grid">
							<div class="row-fluid">
								<!-- 搜索条件  -->
								<div class="dataTables_filter" id="sample_1_filter">
									<div class="controls pull-right">
										<button id="modify-permission-btn" type="button" class="btn blue"><i class="icon-save"></i>  保存</button>
									</div>
									<div class="controls pull-left">
										<label class="control-label">${ rolePermissionList.role.name }</label>
										<input id="roleId" type="hidden" value="${ rolePermissionList.role.id }" />
									</div>
								</div>
							</div>
							<table class="table table-striped table-bordered table-hover dataTable" aria-describedby="sample_1_info">
							<thead>
								<tr role="row">
									<th style="width: 88px;" class="sorting_disabled" >
										资源名称
									</th>
									<th class="hidden-480" style="width: 157px;" colspan="2">
										可用权限
									</th>
								</tr>
							</thead>
						<tbody role="alert" aria-live="polite" aria-relevant="all">
							<c:forEach items="${ rolePermissionList.resourceList }" var="resource">
								<tr class="gradeX odd">
									<td class=" sorting_1">
										${ resource.name }
									</td>
									<td class="hidden-480" colspan="2">
										<c:forEach items="${ resource.permissionList }" var="permission">
											<label class="checkbox"><input name="permssionId" <c:if test="${ permission.isPermitted }">checked="checked"</c:if> type="checkbox" value="${ permission.id }"> ${ permission.name }
											</label>
										</c:forEach>
									</td>
								</tr>
							</c:forEach>
								</tbody></table>
							</div>
					</div>
				</div>
				<!-- END EXAMPLE TABLE PORTLET-->
			</div>
		</div>
		<!-- END PAGE CONTENT-->
	</div>
	<script type="text/javascript">
	$('#modify-permission-btn').on('click', function() {
		var permissionIds = new Array();
		$('input[name="permssionId"]:checked').each(function () {
		    permissionIds.push(this.value);
		});
		
		var rolePermissionData = {
				permissionIds : permissionIds,
				id : $('#roleId').val()
		}
		
		$('#modify-permission-btn').attr('disabled', true);

		
		$.homePost('/role/setRoleResourcePermission', rolePermissionData, function(data) {
			if(data.success=='true'){
				showMessage(data);
			}else{
				showMessageWithoutClose(data);
				$('#modify-permission-btn').removeAttr('disabled');
			}
		});
	});
	</script>
</body>
</html>