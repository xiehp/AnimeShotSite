<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>   
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改用户资料</title>
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
				<a href="javascript:void(0);">个人中心</a> 
				<i class="icon-angle-right"></i>
			</li>
			<li>
				<a href="javascript:void(0);">个人资料</a>
			</li>
		</ul>
		<!-- END PAGE TITLE & BREADCRUMB-->
		<div class="portlet ">
			<div class="portlet-body span6">
				<c:if test="${ !empty param.success }">
					<div class="alert alert-success">
						<button class="close" data-dismiss="alert"></button>
						<strong>操作成功!</strong> 
					</div>
				</c:if>
				<c:if test="${ !empty param.error }">
					<div class="alert alert-error">
						<button class="close" data-dismiss="alert"></button>
						<strong>${ param.error }</strong> 
					</div>
				</c:if>
			</div>
		</div>
	</div>
</div>
<!-- END PAGE HEADER-->

<div class="row-fluid">
		<div class="span6">
			<!-- BEGIN SAMPLE FORM PORTLET-->   
			<div class="portlet box blue">
				<div class="portlet-title">
					<div class="caption"><i class="icon-reorder"></i>更新个人资料</div>
				</div>
				<div class="portlet-body form">
					<!-- BEGIN FORM-->
					<form action="${ ctx }/user/updateSetting" class="form-horizontal" id="user-setting-form" method="post">
						<input type="hidden" name="id" value="${user.id }" />
						<div class="control-group">
							<label class="control-label">用户姓名</label>
							<div class="controls">
								<input type="text" name="userName" class="span7 m-wrap" value="${ user.userName }">
							</div>
						</div>
						<div class="control-group">
							<label class="control-label">手机号</label>
							<div class="controls">
								<input class="span7 m-wrap" name="mobile" type="text" placeholder="" value="${ user.mobile }">
							</div>
						</div>
						<div class="control-group">
							<label class="control-label">email</label>
							<div class="controls">
								<input class="span6 m-wrap" name="email" type="text" placeholder="" value="${ user.email }">
							</div>
						</div>
						<div class="control-group">
							<label class="control-label">密码</label>
							<div class="controls">
								<input id="plainPassword" name="plainPassword" class="span6 m-wrap" type="password" placeholder="密码">
							</div>
						</div>
						<div class="control-group">
							<label class="control-label">重复密码</label>
							<div class="controls">
								<input name="rePwd" class="span6 m-wrap" type="password" placeholder="重复密码">
							</div>
						</div>
						<hr>
						<div class="control-group">
							<label class="control-label">显示侧边栏</label>
							<div class="controls">
								<label class="radio">
									<div class="radio"><span><input <c:if test="${ userSetting.showSidebar==1 }">checked="checked"</c:if> type="radio" name="showSidebar" value=1></span></div>
									显示
								</label>
								<label class="radio">
									<div class="radio"><span><input <c:if test="${ userSetting.showSidebar==0 }">checked="checked"</c:if>  type="radio" name="showSidebar" value="0"></span></div>
									隐藏
								</label>
							</div>
						</div>
						<div class="control-group">
							<label class="control-label">皮肤</label>
							<div class="controls">
								<select name="skinPath">
									<option  value="">请选择...</option>
									<c:forEach items="${ skinMap }" var="skin">
										<option <c:if test="${ skin.key==userSetting.skinPath }">selected</c:if> value="${ skin.key }">${ skin.value }</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="form-actions">
							<button type="submit" class="btn blue">提交</button>
							<button type="button" class="btn">取消</button>                            
						</div>
					</form>
					<!-- END FORM-->       
				</div>
			</div>
			<!-- END SAMPLE FORM PORTLET-->
		</div>
	</div>
</div>
<script type="text/javascript">
$(function(){
	$('#user-setting-form').validate({
		errorElement : 'span', // default input error message container
		errorClass : 'help-block', // default input error message class
		rules : {			
			userName : {
				required : true
			},
			plainPassword : {
				rangelength : [ 4, 10 ]
			},
			rePwd : {
				equalTo : "#plainPassword"
			},
			mobile : {
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
			plainPassword : {
				rangelength : $.format("密码最小长度:{0}, 最大长度:{1}。")
			},
			rePwd : {
				equalTo : "两次密码输入不一致"
			},
			mobile : {
				required : "手机号不能为空"
			},
			email : {
				email : "email格式不正确"
			}
		}
	});
})
</script>
</body>
</html>