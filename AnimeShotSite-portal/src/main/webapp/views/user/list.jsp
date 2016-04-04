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
<title>用户列表</title>
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
								<a href="javascript:void();">用户管理</a> 
								<i class="icon-angle-right"></i>
							</li>
							<li>
								<a href="javascript:void();">用户列表</a>
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
											<form action="${ ctx }/user/list" method="post">
											<div class="controls">

													<input name="search_LIKE_userName" value="${ param.search_LIKE_userName }" class="span1 m-wrap" type="text" placeholder="real name">
													<input name="search_LIKE_loginName" value="${ param.search_LIKE_loginName }" class="span1 m-wrap" type="text" placeholder="login id">
													<i>&nbsp;</i>
													<button type="submit" class="btn red"><i class="icon-search"></i>  搜索</button>
													<shiro:hasPermission name="userList:add">
														<a class="btn blue" id="add_user_btn" ><i class="icon-plus"></i>  新增</a>
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
												用户姓名
											</th>
											<th class="hidden-480" style="width: 128px;">
												登录账号
											</th>
											<th class="hidden-480" style="width: 108px;">
												手机号码
											</th>
											<th class="hidden-480" style="width: 103px;">
												电子邮件
											</th>
											<th class="hidden-480" style="width: 53px;">
												用户状态
											</th>
											<th class="sorting_disabled" style="width: 164px;">
												操作
											</th>
										</tr>
									</thead>
								<tbody role="alert" aria-live="polite" aria-relevant="all">
									<c:forEach items="${ userList.content }" var="user" varStatus="status">
										<tr class="gradeX odd">
											<td class=" sorting_1">
												${ status.index + 1}
											</td>
											<td class="hidden-480">${ user.userName }</td>
											<td class="hidden-480">${ user.loginName }</td>
											<td class="hidden-480">${ user.mobile }</td>
											<td class="hidden-480"><a href="mailto:${ user.email }">${ user.email }</a></td>
											<td>${ user.userStatus }</td>
											<c:choose>
												<c:when test="${ !user.adminFlag }">
													<td class="hidden-480">
														<shiro:hasPermission name="userList:update">
															<a href="javascript:;" data-userid="${ user.id }" class="btn mini blue user-modify-btn"><i class="icon-wrench"></i> 更新</a>
															<c:if test="${ user.status == 1 }">
																<a href="javascript:;" data-userid="${ user.id }" class="btn mini blue user-lock-btn"><i class="icon-lock"></i> 锁定</a>
															</c:if>
															<c:if test="${ user.status == 2 }">
																<a href="javascript:;" data-userid="${ user.id }" class="btn mini blue user-unlock-btn"><i class="icon-unlock"></i> 解锁</a>
															</c:if>
															<a href="javascript:;" data-userid="${ user.id }" class="btn mini red user-del-btn"><i class="icon-trash"></i> 删除</a>
														</shiro:hasPermission>
													</td>
												</c:when>
												<c:otherwise>
													<td class="hidden-480"></td>
												</c:otherwise>
											</c:choose>
										</tr>
									</c:forEach>
										</tbody></table>
										<div class="span6 pull-right">
											<tags:pagination page="${userList}" paginationSize="10"/>
										</div>
									</div>
							</div>
						</div>
						<!-- END EXAMPLE TABLE PORTLET-->
					</div>
				</div>
				<!-- END PAGE CONTENT-->
			</div>
			<script type="text/javascript">
			$('body').on('click', '.user-lock-btn', function() {
				var userid = $(this).data('userid');
				showComfirmAlert('确定要锁定该用户吗?', function() {
					$.homeGet('/user/lock/' + userid, function(data){
						showMessage(data);
					})
				})
			});

			$('body').on('click', '.user-unlock-btn', function() {
				var userid = $(this).data('userid');
				showComfirmAlert('确定要解锁该用户吗?', function() {
					$.homeGet('/user/unlock/' + userid, function(data){
						showMessage(data);
					})
				})
			});

			$('body').on('click', '.user-del-btn', function() {
				var userid = $(this).data('userid');
				showComfirmAlert('确定要删除该用户吗?', function() {
					$.homeGet('/user/delete/' + userid, function(data){
						showMessage(data);
					})
				})
			});

			$('body').on('click', '.user-modify-btn', function() {
				clearForm('userAddForm');
				var userid = $(this).data('userid');
				$('#userId', '#userAddForm').val(userid);
				$('#addUserFormTitle').html('更新用户');
				$('#addOrUpdateUserDiv').modal({show: true, keyboard: false});
				validateUpdateForm();
				setTimeout(function(){
					// 获取用户信息并填充表单
					viewUser(userid);
				}, 1000);
			});

			function validateUpdateForm() {
				$('#userAddForm').validate({
					errorElement : 'span', // default input error message container
					errorClass : 'help-block', // default input error message class
					rules : {
						userName : {
							required : true
						},
						email : {
							email : true
						}
					},
					messages : {
						userName : {
							required : "用户姓名不能为空"
						},
						email : {
							email : "email格式不正确"
						}
					},
					submitHandler : function(form) {
						// 将提交按钮变为不可用
						$('#saveUserBtn').attr('disabled', true);
						var formData = $('#userAddForm').serializeJSON();
						formData.roleList = $('#role-list').val();
						var isExists = false;
						$.homePost('/user/update', formData, function(data){
							$('#addOrUpdateUserDiv').modal('hide');
							showMessage(data);
						})
					}
				});
			}

			function viewUser(userid) {
				$.homeGet('/user/view/' + userid, function(data){
					var user = data.user;
					// 登陆账号不可改
					$('#loginName', '#userAddForm').val(user.loginName).removeAttr(
							'name').attr('disabled', true);
					$('#userName', '#userAddForm').val(user.userName);
					$('#mobile', '#userAddForm').val(user.mobile);
					$('#email', '#userAddForm').val(user.email);
					
					var selectObj = $('#role-list');
					
					var roleList = data.roleList;
					for(var i=0;i<roleList.length;i++){
						var checkStr = roleList[i].isChecked ? 'selected="true"' : '';
						selectObj.append("<option " + checkStr + " value='"+roleList[i].id+"'>"+roleList[i].name+"</option>");
					}
					$('#role-list').chosen({});
				})
			}

			$('body').on('click', '#saveUserBtn', function() {
				$('#userAddForm').submit();
			});

			$('body').on('click', '#add_user_btn', function() {
				clearForm('userAddForm');
				$('#role-list').empty().trigger('chosen:updated');
				
				$('#addUserFormTitle').html('创建用户');
				$('#addOrUpdateUserDiv').modal({show: true, keyboard: false});
				//表单验证
				validateForm();
				//此处需要延迟1秒,否则无法正常加载
				setTimeout(function(){
					//角色列表
					initRoleList();
				}, 1000);
			});
			
			function initRoleList(){
				$.homeGet('/role/initRoles', function(data){
					if(data && data.length > 0){
						
						var selectObj = $('#role-list');
						
						for(var i=0;i<data.length;i++){
							selectObj.append("<option value='"+data[i].id+"'>"+data[i].name+"</option>");
						}
						$('#role-list').chosen({});
						$('#role-list').trigger('chosen:updated');
					}
				});
			}

			function validateForm() {
				jQuery.validator.addMethod("userNameExists", function(value, element) {
					var formData = {
						id : $('#userId').val(),
						loginName : value
					};
					var isExists = false;
					
					$.homePostNotAsync('/user/validateUserName', formData, function(data){
						isExists = data.success;
					});
					
					return this.optional(element) || (!isExists);
				}, "用户账号已经存在");
				$('#userAddForm').validate({
					errorElement : 'span', // default input error message container
					errorClass : 'help-block', // default input error message class
					rules : {
						loginName : {
							required : true,
							rangelength : [ 4, 15 ],
							userNameExists : true
						},
						userName : {
							required : true
						},
						plainPassword : {
							required : true,
							rangelength : [ 4, 10 ]
						},
						rePwd : {
							equalTo : "#plainPassword"
						},
						email : {
							email : true
						}
					},
					messages : {
						loginName : {
							required : "请填写登陆账号",
							rangelength : "账号长度为{0}~{1}个字符",
							userNameExists : "用户账号已经存在"
						},
						userName : {
							required : "用户姓名不能为空"
						},
						plainPassword : {
							required : "密码不能为空",
							rangelength : $.format("密码最小长度:{0}, 最大长度:{1}。")
						},
						rePwd : {
							equalTo : "两次密码输入不一致"
						},
						email : {
							email : "email格式不正确"
						}
					},
					submitHandler : function(form) {
						// 将提交按钮变为不可用
						$('#saveUserBtn').attr('disabled', true);
						var formData = $('#userAddForm').serializeJSON();
						formData.roleList = $('#role-list').val();
						var isExists = false;
						$.homePost('/user/add', formData, function(data){
							if(data.success == 'true'){
								$('#addOrUpdateUserDiv').modal('hide');
								showMessage(data);
							}else{
								showMessageWithoutRefresh(data);
							}
						});
					}
				});
			}
			</script>
					
			<!-- 弹出层  BEGIN -->
			<div id="addOrUpdateUserDiv" class="modal hide fade" tabindex="-1" data-backdrop="static" data-width="560" data-height="450">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
						<h3 id="addUserFormTitle"></h3>
					</div>
					<form id="userAddForm" class="form-horizontal">
					<div class="modal-body">
						<div class="portlet-body form">
							<!-- BEGIN FORM-->
								<input name="id" type="hidden" id="userId" />
								<div class="row-fluid">
									<div class="span9">
										<div class="control-group">
											<label class="control-label">登陆名称</label>
											<div class="controls">
												<input type="text" id="loginName" name="loginName" class="m-wrap span12" placeholder="登陆名称">
												<span class="help-block"></span>
											</div>
										</div>
									</div>
								</div>
								<div class="row-fluid">
									<div class="span9">
										<div class="control-group">
											<label class="control-label">用户姓名</label>
											<div class="controls">
												<input type="text" id="userName" name="userName" class="m-wrap span12" placeholder="用户姓名">
												<span class="help-block"></span>
											</div>
										</div>
									</div>
								</div>
								<div class="row-fluid">
									<div class="span9">
										<div class="control-group">
											<label class="control-label">密码</label>
											<div class="controls">
												<input type="password" id="plainPassword" name="plainPassword" class="m-wrap span12" placeholder="密码">
												<span class="help-block"></span>
											</div>
										</div>
									</div>
								</div>
								<div class="row-fluid">
									<div class="span9">
										<div class="control-group">
											<label class="control-label">重复密码</label>
											<div class="controls">
												<input type="password" id="rePwd" name="rePwd" class="m-wrap span12" placeholder="重复密码">
												<span class="help-block"></span>
											</div>
										</div>
									</div>
								</div>
								<div class="row-fluid">
									<div class="span9">
										<div class="control-group">
											<label class="control-label">用户角色</label>
											<div class="controls">
												<select id="role-list" data-placeholder="请选择角色" name="roleList" class="chosen-select" multiple="true" style="width:220px;">
												</select>
											</div>
										</div>
									</div>
								</div>
								<div class="row-fluid">
									<div class="span9">
										<div class="control-group">
											<label class="control-label">联系电话</label>
											<div class="controls">
												<input type="text" id="mobile" name="mobile" class="m-wrap span12" placeholder="联系电话">
												<span class="help-block"></span>
											</div>
										</div>
									</div>
								</div>
								<div class="row-fluid">
									<div class="span9">
										<div class="control-group">
											<label class="control-label">email</label>
											<div class="controls">
												<input type="text" id="email" name="email" class="m-wrap span12" placeholder="email">
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
						<button type="button" class="btn blue" id="saveUserBtn">提交</button>
					</div>
					</form>
				</div>
			<!-- 弹出层  END -->
</body>
</html>