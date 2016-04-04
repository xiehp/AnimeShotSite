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
<title>角色列表</title>
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
								<a href="javascript:void();">角色列表</a>
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
						<div class="portlet box blue">
							<div class="portlet-title">
								<div class="caption"><i class="icon-globe"></i></div>
							</div>
							<div class="portlet-body">
								<div id="sample_1_wrapper" class="dataTables_wrapper form-inline" role="grid">
									<div class="row-fluid">
										<!-- 搜索条件  -->
										<div class="dataTables_filter" id="sample_1_filter">
											<form action="${ ctx }/role/list" method="post">
											<div class="controls">

													<input name="search_LIKE_name" value="${ param.search_LIKE_name }" class="span1 m-wrap" type="text" placeholder="role name">
													<i>&nbsp;</i>
													<button type="submit" class="btn red"><i class="icon-search"></i>  搜索</button>
													<shiro:hasPermission name="roleList:add">
														<a class="btn blue" id="add_role_btn" ><i class="icon-plus"></i>  新增</a>
													</shiro:hasPermission>
												</div>
											</form>
										</div>
									</div>
									<table class="table table-striped table-bordered table-hover dataTable" aria-describedby="sample_1_info">
									<thead>
										<tr role="row">
											<th style="width: 48px;" class="sorting_disabled" >
												序号
											</th>
											<th class="hidden-480" style="width: 157px;">
												角色名称
											</th>
											<th class="hidden-480" style="width: 128px;">
												描述
											</th>
											<th class="sorting_disabled" style="width: 164px;">
												操作
											</th>
										</tr>
									</thead>
								<tbody role="alert" aria-live="polite" aria-relevant="all">
									<c:forEach items="${ roleList.content }" var="role" varStatus="status">
										<tr class="gradeX odd">
											<td class=" sorting_1">
												${ status.index + 1}
											</td>
											<td class="hidden-480">${ role.name }</td>
											<td class="hidden-480">${ role.description }</td>
											<td class="hidden-480">
												<shiro:hasPermission name="roleList:authorize">
													<a href="${ ctx }/role/permissionList/${ role.id }" class="btn mini blue role-modify-btn" title="Set permissions"> 设置权限</a>
												</shiro:hasPermission>
												<shiro:hasPermission name="roleList:delete">
													<a href="javascript:;" data-roleid="${ role.id }" class="btn mini red role-del-btn"><i class="icon-trash"></i> 删除</a>
												</shiro:hasPermission>
											</td>
										</tr>
									</c:forEach>
										</tbody></table>
										<div class="span6 pull-right">
											<tags:pagination page="${roleList}" paginationSize="10"/>
										</div>
									</div>
							</div>
						</div>
						<!-- END EXAMPLE TABLE PORTLET-->
					</div>
				</div>
				<!-- END PAGE CONTENT-->
			</div>
			
			<!-- 用户列表所需javascript -->
			<script type="text/javascript">
			$('body').on('click', '.role-del-btn', function() {
				var roleid = $(this).data('roleid');
				showComfirmAlert('确定要删除该角色吗?', function() {
					$.homeGet('/role/delete/' + roleid, function(data){
						showMessage(data);
					});
				})
			});
			
			$('body').on('click', '#saveRoleBtn', function() {
				$('#roleAddForm').submit();
			});

			$('body').on('click', '#add_role_btn', function() {
				clearForm('roleAddForm');
				$('#addRoleDiv').modal('show');
				validateForm();
			});

			function validateForm() {
				$('#roleAddForm').validate({
					errorElement : 'span', // default input error message container
					errorClass : 'help-block', // default input error message class
					rules : {
						name : {
							required : true
						},
						role : {
							required : true
						}
					},
					messages : {
						name : {
							required : "请填写角色名称"
						},
						role : {
							required : "请填写角色标识"
						}
					},
					highlight : function(element) { // hightlight error inputs
						$(element).closest('.control-group').addClass('error'); // set error
					},
					submitHandler : function(form) {
						// 将提交按钮变为不可用
						$('#saveRoleBtn').attr('disabled', true);
						var formData = $('#roleAddForm').serializeJSON();
						var isExists = false;
						$.homePost('/role/add', formData, function(data) {
							$('#addRoleDiv').modal('hide');
							showMessage(data);
						});
					}
				});
			}
			</script>
					
			<!-- 弹出层  BEGIN -->
			<div id="addRoleDiv" class="modal hide fade" tabindex="-1" data-backdrop="static" data-width="560">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
						<h3>新建角色</h3>
					</div>
					<form id="roleAddForm" class="form-horizontal">
					<div class="modal-body">
						<div class="portlet-body form">
							<!-- BEGIN FORM-->
								<input name="id" type="hidden" id="userId" />
								<div class="row-fluid">
									<div class="span9">
										<div class="control-group">
											<label class="control-label">角色名称</label>
											<div class="controls">
												<input type="text" id="name" name="name" class="m-wrap span12" placeholder="角色名称">
												<span class="help-block"></span>
											</div>
										</div>
									</div>
								</div>
								<div class="row-fluid">
									<div class="span9">
										<div class="control-group">
											<label class="control-label">角色标识</label>
											<div class="controls">
												<input type="text" id="role" name="role" class="m-wrap span12" placeholder="英文字母">
												<span class="help-block"></span>
											</div>
										</div>
									</div>
								</div>
								<div class="row-fluid">
									<div class="span9">
										<div class="control-group">
											<label class="control-label">描述</label>
											<div class="controls">
												<input type="text" id="description" name="description" class="m-wrap span12" placeholder="描述">
												<span class="help-block"></span>
											</div>
										</div>
									</div>
								</div>
								
							</div>
								<!--/row-->
							<!-- END FORM-->                
					</div>
					<div class="modal-footer">
						<button type="button" data-dismiss="modal" class="btn">关闭</button>
						<button type="button" class="btn blue" id="saveRoleBtn">提交</button>
					</div>
					</form>
				</div>
			<!-- 弹出层  END -->
</body>
</html>