<%@ page language="java" contentType="text/html; UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!-- 出错用模态框（Modal） 
aria-hidden="true"
aria-labelledby="myModalLabel" 
-->
<div class="modal fade" id="messageModal" tabindex="-1" role="dialog">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h4 class="modal-title" id="myModalLabel"><spring:message code='信息提示' /></h4>
			</div>
			<div class="modal-body">按下 ESC 按钮退出。</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code='关闭' /></button>
				<!-- 
				<button type="button" class="btn btn-primary">提交更改</button>
				 -->
			</div>
		</div>
	</div>
</div>