<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>欢迎使用</title>
</head>
<body class="login">
	<!-- login page do not delete -->
	<!-- BEGIN LOGO -->

	<div class="logo">

		<img src="${ctx }/static/media/image/logo-big.png" alt="" />
		<!-- <div style="font-weight: 25px; color: #fff;"><h4>Warehouse Manage System</h4></div> -->
	</div>

	<!-- END LOGO -->

	<!-- BEGIN LOGIN -->

	<div class="content">

		<!-- BEGIN LOGIN FORM -->

		<form id="login-form" class="form-vertical" method="post">

			<h3 class="form-title">后台管理</h3>

			<div id="sysAlert" class="alert alert-error hide">

				<span id="errorContent"></span>

			</div>

			<div class="control-group">

				<!--ie8, ie9 does not support html5 placeholder, so we just show field title for that-->

				<label class="control-label visible-ie8 visible-ie9">Username</label>

				<div class="controls">

					<div class="input-icon left">

						<i class="icon-user"></i>

						<input class="m-wrap placeholder-no-fix" type="text" placeholder="Username" id="username" name="username" />

					</div>

				</div>

			</div>

			<div class="control-group">

				<label class="control-label visible-ie8 visible-ie9">Password</label>

				<div class="controls">

					<div class="input-icon left">

						<i class="icon-lock"></i>

						<input class="m-wrap placeholder-no-fix" type="password" placeholder="Password" id="password" name="password" />

					</div>

				</div>

			</div>

			<div class="form-actions">

				<!-- <label class="checkbox">

				<input type="checkbox" name="remember" value="1"/> Remember me

				</label> -->


			</div>

			<div class="forget-password"></div>

			<div class="create-account"></div>

			<input type="submit" value="form登录">
		</form>

		<!-- END LOGIN FORM -->

		<button onclick="loginSubmitAjax();">ajax登录</button>

	</div>

	<!-- END LOGIN -->

	<!-- BEGIN COPYRIGHT -->

	<div></div>
	<script>
		$(document).ready(function() {
			/*
			$('#username').focus();
			$('#login-form').validate({
				errorElement : 'label', //default input error message container
				errorClass : 'help-inline', // default input error message class
				rules : {
					username : {
						required : true
					},
					password : {
						required : true
					}
				},
				messages : {
					username : {
						required : "Username is required."
					},
					password : {
						required : "Password is required."
					}
				},
				highlight : function(element) { // hightlight error inputs
					$(element).closest('.control-group').addClass('error'); // set error class to the control group
				},
				errorPlacement : function(error, element) {
					error.addClass('help-inline help-small no-left-padding').insertAfter(element.closest('.input-icon'));
				},
				submitHandler : function(form) {
					loginSubmit();
				}
			});

			$('#loginButton').keypress(function(e) {
				if (e.which == 13) {
					if ($('#login-form').validate().form()) {
						loginSubmit();
					}
					return false;
				}
			});
			 */
		});

		function loginSubmitAjax() {
			var password = $.trim($('#password').val());
			var param = {
				"loginName" : $.trim($('#username').val()),
				"password" : password
			}
			$.ajax({
				url : 'webLoginAjax',
				contentType : 'application/json',
				dataType : "json",
				type : "post",
				data : JSON.stringify(param),
				success : function(data) {
					if (data && data.code == '10000') {
						window.location.href = '${ctx}';
					} else {
						alert(data.message);
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					console.log('XMLHttpRequest: ' + XMLHttpRequest + ',textStatus:' + textStatus + ',errorThrown:' + errorThrown);
				}
			});
		}
	</script>

	<!-- END JAVASCRIPTS -->
</body>
</html>