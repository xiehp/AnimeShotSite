<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>出库列表</title>
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
				<a href="javascript:void();">案例管理</a> 
				<i class="icon-angle-right"></i>
			</li>
			<li>
				<a href="javascript:void();">案例列表</a>
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
							<div class="controls">
								<form action="${ ctx }/sample/list">
									<input id="search_LIKE_param1" name="search_LIKE_param1" value="${ param.search_LIKE_param1 }" class="span2 m-wrap" type="text" placeholder="参数1">
									<input id="search_EQ_param2" name="search_EQ_param2" value="${ param.search_EQ_param2 }" class="span2 m-wrap" type="text" placeholder="参数2">
									<input readonly type="text" class="simple-date"/>
									<i>&nbsp;</i>
									<button type="submit" class="btn green"><i class="icon-search"></i>  搜索案例</button>
								</form>
								<shiro:hasPermission name="${ ctx }/sampleList:add">
									<a class="btn blue" href="${ ctx }/sample/toAdd" ><i class="icon-plus"></i>  新增案例</a>
								</shiro:hasPermission>
								<a class="btn blue" href="${ ctx }/sample/toUpload" ><i class="icon-plus"></i>  上传案例</a>
							</div>
						</div>
					</div>
					<table class="table table-striped table-bordered table-hover dataTable" aria-describedby="sample_1_info">
					<thead>
						<tr role="row">
							<th style="width: 130px;">
								#
							</th>
							<th style="width: 130px;">
								参数1
							</th>
							<th style="width: 148px;" class="sorting_disabled" >
								参数2
							</th>
							<th style="width: 127px;">
								日期
							</th>
							<th class="sorting_disabled" style="width: 164px;">
								操作
							</th>
						</tr>
					</thead>
				<tbody role="alert" aria-live="polite" aria-relevant="all">
					<c:choose>
						<c:when test="${ !empty sampleList.content }">
							<c:forEach items="${ sampleList.content }" var="sample" varStatus="status">
								<tr class="gradeX odd">
									<td>
										${ status.index + 1 }
									</td>
									<td>${ sample.param1 }</td>
									<td>${ sample.param2 }</td>
									<td>${ sample.createDateFormat }</td>
									<td>
										<a href="${ ctx }/sample/view/${ sample.id }" class="btn green mini"  title="查看"> View</a>
										<a class="btn red mini sample-del-btn" data-id="${ sample.id }" title="删除"><i class="icon-trash"></i> Del</a>
									</td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr>
								<td colspan="12">没有查到相关数据</td>
							</tr>
						</c:otherwise>
					</c:choose>
					
						</tbody></table>
						<div class="span6 pull-right">
							<tags:pagination page="${sampleList}" paginationSize="10"/>
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
$(function(){
	$('.sample-del-btn').on('click', function(){
		var id = $(this).data('id');
		showComfirmAlert('确定要删除该数据吗?', function() {
			
			$.homeGet('/sample/delete/'+ id, function(data) {
				showMessage(data);
			});
		})
	});
})
$.homeAutoComplete('search_LIKE_param1', '/sample/autoComplete');
$.homeAutoComplete('search_EQ_param2', '/sample/autoComplete');
</script>
</body>
</html>