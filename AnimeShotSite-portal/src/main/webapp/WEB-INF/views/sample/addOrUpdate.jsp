<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
				<a href="javascript:void();">个人中心</a> 
				<i class="icon-angle-right"></i>
			</li>
			<li>
				<a href="javascript:void();">个人资料</a>
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
					<div class="caption"><i class="icon-reorder"></i>案例</div>
				</div>
				<div class="portlet-body form">
					<!-- BEGIN FORM-->
					<form action="${ ctx }/sample/addOrUpdate" class="form-horizontal" id="sample-form" method="post">
						<input type="hidden" name="id" value="${sample.id }" />
						<input type="hidden" name="version" value="${sample.version }" />
						<div class="control-group">
							<label class="control-label">参数1</label>
							<div class="controls">
								<input type="text" name="param1" class="span7 m-wrap" value="${ sample.param1 }">
							</div>
						</div>
						<div class="control-group">
							<label class="control-label">参数2</label>
							<div class="controls">
								<input class="span7 m-wrap" name="param2" type="text" placeholder="" value="${ sample.param2 }">
							</div>
						</div>
						
						<div class="form-actions">
							<button type="submit" class="btn blue">Submit</button>
							<button type="button" class="btn">Cancel</button>                            
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
	$('#sample-form').validate({
		errorElement : 'span', // default input error message container
		errorClass : 'help-block', // default input error message class
		rules : {			
			param1 : {
				required : true
			},
			param2 : {
				required : true
			}
		},
		messages : {
			param1 : {
				required : "必填"
			},
			param2 : {
				required : "必填"
			}
		}
	});
})
</script>
</body>
</html>