<%@ page language="java" contentType="text/html; UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!-- 出错用模态框（Modal）-->
<div class="modal fade" id="messageModal" tabindex="-1" role="dialog">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<div class="modal-title" id="myModalLabel">
					<spring:message code='信息提示' />
				</div>
			</div>
			<div class="modal-body" style="font-size: 20px;"></div>
			<div class="modal-footer">
				<button id="ModalCloseButton" type="button" class="btn btn-default" data-dismiss="modal">
					<spring:message code='关闭' />
				</button>
			</div>
		</div>
	</div>
</div>