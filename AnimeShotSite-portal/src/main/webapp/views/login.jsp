<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>欢迎使用</title>
<link rel="shortcut icon" type="image/x-icon" href="${ ctx }/static/img/logo.png" media="screen" />
<link href="${ctx }/static/media/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="${ctx }/static/media/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
<link href="${ctx }/static/media/css/style-metro.css" rel="stylesheet" type="text/css" />
<link href="${ctx }/static/media/css/style.css" rel="stylesheet" type="text/css" />
<link href="${ctx }/static/media/css/uniform.default.css" rel="stylesheet" type="text/css" />
<link href="${ctx }/static/media/css/login.css" rel="stylesheet" type="text/css" />
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

		<form id="login-form" class="form-vertical">

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

						<input class="m-wrap placeholder-no-fix" type="text" placeholder="Username" id="username" name="username" value="${ username }" />

					</div>

				</div>

			</div>

			<div class="control-group">

				<label class="control-label visible-ie8 visible-ie9">Password</label>

				<div class="controls">

					<div class="input-icon left">

						<i class="icon-lock"></i>

						<input class="m-wrap placeholder-no-fix" type="password" placeholder="Password" id="password" name="password" value="" />

					</div>

				</div>

			</div>

			<div class="form-actions">

				<!-- <label class="checkbox">

				<input type="checkbox" name="remember" value="1"/> Remember me

				</label> -->

				<button type="submit" class="btn green pull-right">
					Login
				</button>

			</div>

			<div class="forget-password"></div>

			<div class="create-account"></div>

		</form>

		<!-- END LOGIN FORM -->


	</div>

	<!-- END LOGIN -->

	<!-- BEGIN COPYRIGHT -->

	<div></div>
	<script src="${ctx }/static/js/jquery/jquery-1.11.3.min.js" type="text/javascript"></script>
	<script src="${ctx }/static/media/js/jquery.validate.min.js"></script>
	<script src="${ctx }/static/media/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="${ctx }/static/media/js/jquery.backstretch.min.js" type="text/javascript"></script>
	<script>
		$(document).ready(function() {
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
			$('#login-form input').keypress(function(e) {
				if (e.which == 13) {
					if ($('#login-form').validate().form()) {
						loginSubmit();
					}
					return false;
				}
			});
			/*
			$.backstretch([
			      "${ctx }/static/media/image/bg/1.jpg",
			      "${ctx }/static/media/image/bg/2.jpg",
			      "${ctx }/static/media/image/bg/3.jpg",
			      "${ctx }/static/media/image/bg/4.jpg",
			      "${ctx }/static/media/image/bg/5.jpg",
			      "${ctx }/static/media/image/bg/6.jpg",
			      "${ctx }/static/media/image/bg/7.jpg",
			      "${ctx }/static/media/image/bg/8.jpg"
			      ], {
			        fade: 1000,
			        duration: 4000
			    });*/

		});

		function loginSubmit() {
			var param = {
				"loginName" : $.trim($('#username').val()),
				"password" : $.trim($('#password').val())
			}
			$.ajax({
				url : '${ctx}/webLogin',
				contentType : 'application/json',
				dataType : "json",
				type : "post",
				data : JSON.stringify(param),
				success : function(data) {
					if (data && data.code == '10000') {
						window.location.href = '${ctx}/index';
					} else {
						$('#errorContent').html(data.message);
						$('#sysAlert').show();
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