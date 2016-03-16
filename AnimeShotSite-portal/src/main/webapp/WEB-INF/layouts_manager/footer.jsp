<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
//提示层
function showMessage(obj, globalAlertHideTime){
	
	if(!globalAlertHideTime){
		globalAlertHideTime = 2000;
	}
	
	if(obj && obj.success=='true'){
		$('#content').html("操作成功!");
	}else{
		$('#content').html(obj.message);
	}
	$('#globalAlert').modal('show');
	setTimeout(function(){
		$('#globalAlert').modal('hide');
	}, globalAlertHideTime);
	if(obj && obj.success=='true'){
		setTimeout(function(){
			window.location.reload();
		}, (globalAlertHideTime + 1000));
	}
}
//提示层
function showMessageWithoutRefresh(obj){
	if(obj && obj.success=='true'){
		$('#content').html("操作成功!");
	}else{
		$('#content').html(obj.message);
	}
	$('#globalAlert').modal('show');
	setTimeout(function(){
		$('#globalAlert').modal('hide');
	}, 3000);
}
//提示层
function showMessageWithoutClose(obj){
	if(obj && obj.success=='true'){
		$('#content').html("操作成功!");
	}else{
		$('#content').html(obj.message);
	}
	$('#globalAlert').modal('show');
}
//清空表单，去除disabled属性
function clearForm(formId){
	$(':input','#' + formId)
	 .not(':button, :submit, :reset')
	 .val('')
	 .removeAttr('checked')
	 .removeAttr('selected');
	$(':input','#' + formId).removeAttr('disabled');
}
//仅仅清空表单
function clearValueInForm(formId){
	$(':input','#' + formId)
	 .not(':button, :submit, :reset')
	 .val('')
	 .removeAttr('checked')
	 .removeAttr('selected');
}
//确定取消层
/*
  参数说明 :
 confirmTitle : 提示信息
 callback : 点击continue后调用的方法
 */
function showComfirmAlert(confirmTitle, callback){
	$('#comfirmTitle').html(confirmTitle);
	$('#globalComfirmAlert').modal('show');
	
	//此处先解绑事件,避免多次调用
	$("body").off('click', '#global-comfirm-ok');
	$("body").on('click', '#global-comfirm-ok', function(){
		callback();
		$('#globalComfirmAlert').modal('hide');
	});
}

jQuery(document).ready(function() {       
   App.init();
});

//用来存放当前正在操作的日期文本框的引用。  
var datepicker_CurrentInput;

//加载日期控件
$.datepicker.setDefaults($.datepicker.regional['zh-CN']);
$('.simple-date').datepicker({
	dateFormat : "yy-mm-dd",
	changeMonth: true,
	changeYear: true,
	numberOfMonths : 2,
	showMonthAfterYear: true,
	showButtonPanel : true,
	closeText : '清除',
	beforeShow : function(input, inst) {
		datepicker_CurrentInput = input;
	}
});

$(".ui-datepicker-close").live("click", function() {
	datepicker_CurrentInput.value = "";
});

var datepicker_old_goToToday = $.datepicker._gotoToday
$.datepicker._gotoToday = function(id) {
	datepicker_old_goToToday.call(this,id)
	this._selectDate(id);
	$(id).blur();
}

function backToLogin(){
	window.location.href=baseUrl + '/index';
}
</script>
<!-- 提示层 -->
<div id="globalAlert" class="modal hide fade" tabindex="-1" data-backdrop="static" data-keyboard="false" data-width="400">
	<div class="modal-body text-center">
		<h5 id="content"></h5>
	</div>
	<div class="modal-footer">
		<button type="button" data-dismiss="modal" class="btn green">我知道了</button>
	</div>
</div>

<!-- 确认取消层   -->
<div id="globalComfirmAlert" class="modal hide fade" tabindex="-1" data-backdrop="static" data-keyboard="false" data-width="400">
	<div class="modal-body text-center">
		<h5 id="comfirmTitle"></h5>
	</div>
	<div class="modal-footer">
		<button type="button" data-dismiss="modal" class="btn">取消</button>
		<button type="button" class="btn green" id="global-comfirm-ok">继续</button>
	</div>
</div>

<!-- 登录超时提示层   -->
<div id="globalSessionInvalidAlert" class="modal hide fade" tabindex="-1" data-backdrop="static" data-keyboard="false" data-width="400">
	<div class="modal-body text-center">
		<h5>登录超时，请重新登录</h5>
	</div>
	<div class="modal-footer">
		<button type="button" class="btn green" id="global-relogin-comfirm-ok">我知道了</button>
	</div>
</div>
