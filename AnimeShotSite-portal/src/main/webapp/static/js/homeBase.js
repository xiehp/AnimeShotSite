//去左空格;
function ltrim(s) {
	return s.replace(/^\s*/, "");
}
// 去右空格;
function rtrim(s) {
	return s.replace(/\s*$/, "");
}
function trim(s) {
	return rtrim(ltrim(s));
}

(function($) {

	function processGoPageResult(data, callback, failCallback, options) {
		// 显示信息
		if (data.alertMessage != null && data.alertMessage.length > 0) {
			if (options && (options.notAlertFlag || data.notAlertFlag)) {
				processCallbackAndGoPage(data, callback, failCallback, options);
			} else {
				// showMessageWithoutRefresh(obj);
				// alert(data.alertMessage);
				var message = data.alertMessage.join("\n");
				/* message = '<span style="text-align:center;">'+message+'</span>'; */
				if (data.success || data.code == "10000") {
					Message.alert(message, null, function() {
						processCallbackAndGoPage(data, callback, failCallback, options);
					});
				} else {
					Message.alert(message, null, function() {
						processCallbackAndGoPage(data, callback, failCallback, options);
					});
				}
			}
		} else if (data.code != null && data.message != null) {
			// 原来的ajax 返回数据代码
			processCallbackAndGoPage(data, callback, failCallback, options);
		} else {
			processCallbackAndGoPage(data, callback, failCallback, options);
		}
	}
	$.processGoPageResult = processGoPageResult;

	function processCallbackAndGoPage(data, callback, failCallback, options) {
		// 未成功不调回调函数
		if (data.success || data.code == "10000") {
			// 执行成功callback
			if (callback) {
				callback(data);
			}
		} else {
			// 执行失败callback
			if (failCallback) {
				failCallback(data);
			}
		}

		// 跳转
		if (data.goPage != null && data.goPage.trim() != "") {
			if (data.goPage.indexOf("/") != 0) {
				data.goPage = "/" + data.goPage;
			}
			window.location.href = ctx + data.goPage;
		}
	}

	function validateForm(options) {
		var isValid = true;
		/**
		 * easyui功能 if (options != null && options.validFormId != null) { isValid = $("#" + options.validFormId).form("validate"); // if (!isValid) { // Message.alert("提示", "输入内容有误，请修改后重新提交"); // } }
		 */
		return isValid;
	}

	function getDocumentCharset() {
		return document.characterSet ? document.characterSet : document.charset;
	}

	// 定义提交options参数
	/**
	 * res validFormId: 需要验证的form的ID，如果不为空，则验证<br>
	 * notAlertFlag: 是否对返回的数据进行alert操作，如果指定为true，则不进行alert操作<br>
	 * notLoading: 如果指定为true，则不显示loading进度条
	 */

	/** 提交前根据options做的事情 */
	function doSomethingBeforeSubmit(options) {
		if (options && !options.notLoading) {
			Message.ajaxLoading();
		}
	}
	/** 提交后成功返回信息后，根据options做的事情,(PS:成功还是失败要根据data中数据判断) */
	function doSomethingOnSuccess(options, data) {
		if (options && !options.notLoading) {
			Message.ajaxLoadEnd();
		}
	}
	/** 未能成功提交到服务器时，根据options做的事情 */
	function doSomethingOnError(options, XMLHttpRequest, textStatus, errorThrown, callback) {

		// 关闭加载层
		if (options && !options.notLoading) {
			Message.ajaxLoadEnd();
		}

		// 处理返回的信息
		if (XMLHttpRequest) {
			if (XMLHttpRequest.responseJSON) {
				// 判断是否有回调的json信息，说明是服务器返回了信息
				processGoPageResult(XMLHttpRequest.responseJSON, callback, options.failCallback, options);
			} else {
				// 控制台打印日志
				console.log('XMLHttpRequest: ' + XMLHttpRequest + ',textStatus:' + textStatus + ',errorThrown:' + errorThrown);

				// 弹出错误信息
				if (!options || !options.notAlertFlag) {
					alertErrorMsg(XMLHttpRequest, ',textStatus:' + textStatus + ',errorThrown:' + errorThrown);
				}
			}
		}
	}

	/** ajax提交form表单 */
	$.homeAjaxSubmit = function(formId, otherParam, callback, options) {
		if (options == null) {
			options = {};
		}

		var easyUIForm = false;
		var ajaxParam = {};
		$.extend(ajaxParam, {
			async : true,
			contentType : "application/x-www-form-urlencoded; charset=utf-8",
			dataType : "json",
			type : "post",
			data : otherParam,
			accept : "application/json",
			onSubmit : function(param) {
				if (easyUIForm) {
					var isValid = $(this).form("validate");
					if (!isValid) {
						// $.messager.progress('close'); // hide progress bar while the form is invalid
						return isValid; // return false will stop the form submission
					}
				}

				// 没有错误
				if (otherParam != null) {
					// easyui
					$.extend(param, otherParam);
				}
				param.extend_param_josn_response = true;

				doSomethingBeforeSubmit(options);
			},
			success : function(data) {
				if (easyUIForm) {
					data = jQuery.parseJSON(data);
				}
				doSomethingOnSuccess(options, data);
				processGoPageResult(data, callback, options.failCallback, options);
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				doSomethingOnError(options, XMLHttpRequest, textStatus, errorThrown, callback);
			},
			complete : function(data) {
				// complete code
			}
		});

		if (easyUIForm) {
			ajaxParam.ajax = true;
			ajaxParam.iframe = false;
			ajaxParam.url = $("#" + formId).attr("action");
			ajaxParam.accept = "application/json";
			$('#' + formId).form("submit", ajaxParam);
		} else {
			// alert("need jquery.form.3.51.0.js ajaxSubmit")
			// jquery.form.3.51.0.js
			$('#' + formId).ajaxSubmit(ajaxParam);
		}

	}

	$.homePost = function(url, param, callback, options) {
		if (options == null) {
			options = {};
		}

		if (!validateForm(options)) {
			return;
		}

		doSomethingBeforeSubmit(options);

		$.ajax({
			url : global.baseUrl + url,
			contentType : 'application/x-www-form-urlencoded',
			dataType : "json",
			type : "post",
			data : param,
			success : function(data) {
				doSomethingOnSuccess(options, data);
				processGoPageResult(data, callback, options.failCallback, options);
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				doSomethingOnError(options, XMLHttpRequest, textStatus, errorThrown, callback);
			}
		});
	}

	$.homeGet = function(url, callback, options, runCallBackFlag) {
		if (options == null) {
			options = {};
		}

		if (!validateForm(options)) {
			return;
		}

		doSomethingBeforeSubmit(options);

		$.ajax({
			url : global.baseUrl + url,
			contentType : 'application/x-www-form-urlencoded',
			dataType : "json",
			type : "get",
			success : function(data) {
				doSomethingOnSuccess(options, data);
				if (runCallBackFlag == 1) {
					callback(data)
				} else {
					processGoPageResult(data, callback, options.failCallback, options);
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				doSomethingOnError(options, XMLHttpRequest, textStatus, errorThrown, callback);
			}
		});
	}

	$.homePostNotAsync = function(url, param, callback, options) {
		if (options == null) {
			options = {};
		}

		if (!validateForm(options)) {
			return;
		}

		doSomethingBeforeSubmit(options);

		$.ajax({
			url : global.baseUrl + url,
			contentType : 'application/json',
			dataType : "json",
			type : "post",
			data : JSON.stringify(param),
			async : false,
			success : function(data) {
				doSomethingOnSuccess(options, data);
				processGoPageResult(data, callback, options.failCallback, options);
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				doSomethingOnError(options, XMLHttpRequest, textStatus, errorThrown, callback);
			}
		});
	}

	function alertErrorMsg(XMLHttpRequest, msg) {
		if (XMLHttpRequest && XMLHttpRequest.responseText && XMLHttpRequest.responseText.indexOf('login page do not delete') != -1) {
			// 跳转到登录页
			$('#globalSessionInvalidAlert').modal({
				show : true,
				keyboard : false
			});
			$('#global-relogin-comfirm-ok').off('click');
			$('#global-relogin-comfirm-ok').on('click', function() {
				backToLogin();
			});
		} else {
			var obj = {};
			obj.success = "false";
			obj.message = "提交发生错误," + msg;
			$.showMessageModal(obj.message);
		}
	}

	$.showMessageModal = function(message) {
		$('#messageModal').modal('show');
		$('#messageModal .modal-body').text(message);
	}

	$.showConfirmModal = function(message) {
		$('#messageModal').modal('show');
		$('#messageModal .modal-body').text(message);
	}

	/** 将值从json填充到对应的form */
	$.homeLoadJsonToForm = function(containerId, jsonObj) {
		var key, value, tagName, type, arr;
		for (x in jsonObj) {
			key = x;
			value = jsonObj[x];
			var searchStr = "#" + containerId + " [name=" + key + "]";
			$(searchStr).each(function() {
				tagName = $(this)[0].tagName;
				type = $(this).attr('type');
				if (tagName == 'INPUT') {
					if (type == 'radio') {
						$(this).attr('checked', $(this).val() == value);
					} else if (type == 'checkbox') {
						arr = value.split(',');
						for (var i = 0; i < arr.length; i++) {
							if ($(this).val() == arr[i]) {
								$(this).attr('checked', true);
								break;
							}
						}
					} else {
						$(this).val(value);
					}
				} else if (tagName == 'SELECT') {
					$(this).val(value);
				} else if (tagName == 'LABEL') {
					$(this).text(value);
				} else if (tagName == 'SPAN') {
					$(this).text(value);
				} else if (tagName == 'TEXTAREA') {
					$(this).text(value);
				}
			});
		}
	}

	/** 将值填充到form的对应name中 */
	$.homeSetValueToForm = function(containerId, objName, value) {
		var searchStr = "#" + containerId + " [name='" + objName + "']";
		var obj = $(searchStr);
		var tagName = obj[0].tagName;
		var type = obj.attr('type');

		if (tagName == 'INPUT') {
			if (type == 'radio') {
				obj.attr('checked', obj.val() == value);
			} else if (type == 'checkbox') {
				arr = value.split(',');
				for (var i = 0; i < arr.length; i++) {
					if (obj.val() == arr[i]) {
						obj.attr('checked', true);
						break;
					}
				}
			} else {
				obj.val(value);
			}
		} else if (tagName == 'SELECT' || tagName == 'TEXTAREA') {
			obj.val(value);
		} else if (tagName == 'LABEL') {
			obj.text(value);
		} else if (tagName == 'SPAN') {
			obj.text(value);
		}
	}

	$.log = function(message) {
		if (IS_JS_DEBUG) {
			console.log(message);
			// alert(message);
		}
	}

	$.warn = function(message) {
		if (IS_JS_DEBUG) {
			console.warn(message);
		}
	}

	$.error = function(message) {
		if (IS_JS_DEBUG) {
			console.error(message);
		}
	}

	$.homeAutoComplete = function(element, url) {
		$('#' + element).autocomplete({
			source : function(request, response) {
				$.homePost(url, {
					param : request.term
				}, function(data) {
					response(data);
				});
			},
			minLength : 2
		});
	}

	// 日期增加年数
	$.homeAddYear = function(date, years) {
		var d = new Date(date);
		d.setFullYear(d.getFullYear() + years);
		var m = d.getMonth() + 1;
		return d.getFullYear() + '-' + m + '-' + d.getDate();
	}

	// 日期增加天数
	$.homeAddDate = function(date, days) {
		var d = new Date(date);
		d.setDate(d.getDate() + days);
		var m = d.getMonth() + 1;
		if (m < 10) {
			m = '0' + m
		}
		var tag = ""
		if (d.getDate() < 10) {
			tag = "0"
		}
		return d.getFullYear() + '-' + m + '-' + tag + d.getDate();
	}

	$.homeDateCompare = function(startDateID, endDateID) {
		$('#' + startDateID).on('change', function() {
			endDate = new Date($('#' + endDateID).val())
			startDate = new Date($(this).val())
			if (endDate != null && endDate != "") {
				if (startDate >= endDate) {
					$(this).val($.homeAddDate(endDate, -1))
				}
			}
		})
		$('#' + endDateID).on('change', function() {
			startDate = new Date($('#' + startDateID).val())
			endDate = new Date($(this).val())
			if (startDate != null && startDate != "") {
				if (startDate >= endDate) {
					$(this).val($.homeAddDate(startDate, 1))
				}
			}
		})
	}

	/** 编码html特殊字符 */
	$.escapeHtml = function(s) {
		return (s) ? $("<p>").text(s).html() : "";
	}

	/** 替换正则特殊字符，（加斜杠） */
	$.replaceExp = function(keywordHidden) {
		keywordHidden = keywordHidden.replace(/\\/g, "\\\\");
		// keywordHidden = keywordHidden.replace(/\$/g, "\\$");
		keywordHidden = keywordHidden.replace(/\*/g, "\\*");
		keywordHidden = keywordHidden.replace(/\+/g, "\\+");
		keywordHidden = keywordHidden.replace(/\./g, "\\.");
		keywordHidden = keywordHidden.replace(/\?/g, "\\?");
		keywordHidden = keywordHidden.replace(/\^/g, "\\^");
		keywordHidden = keywordHidden.replace(/\|/g, "\\|");
		keywordHidden = keywordHidden.replace(/\[/g, "\\[");
		keywordHidden = keywordHidden.replace(/\]/g, "\\]");
		keywordHidden = keywordHidden.replace(/\{/g, "\\{");
		keywordHidden = keywordHidden.replace(/\}/g, "\\}");
		keywordHidden = keywordHidden.replace(/\(/g, "\\(");
		keywordHidden = keywordHidden.replace(/\)/g, "\\)");
		return keywordHidden;
	}

	/** 根据父级tag名获得对象的父级对象 */
	$.getParentTagByTagName = function(object, parentTagName) {
		var parent = object.parentNode;
		while (true) {
			if (parent == null) {
				break;
			}
			if (parent.tagName == parentTagName) {
				break;
			}
		}
		return parent;
	};

	$.getWidthAndHeight = function() {

	};

})(jQuery);

// cookie操作
var HomeCookie = {};
HomeCookie.setCookie = function(name, value) {
	return $.cookie(name, value);
}

HomeCookie.getCookie = function(name) {
	return $.cookie(name);
}

HomeCookie.removeCookie = function(name) {
	return $.cookie(name, null);
}

/**
 * url:/SimpleDataxWeb/renderSndData param:{"userName":form.userName.value,"password":form.password.value}
 */
function getDataByAjax(url, jsonParam, isByGet, isOpenMask, options) {
	var result = null;

	var byGet;
	if (isNull(isByGet)) {
		byGet = is_ajax_getmethod_by_get;
	} else {
		byGet = isByGet;
	}

	var ajaxParam = {};
	if (!isNull(isOpenMask)) {
		$.extend(ajaxParam, {
			openMask : isOpenMask
		});
	}

	$.extend(ajaxParam, {
		type : byGet ? "GET" : "POST",
		async : false,
		url : url,
		dataType : "json",
		data : jsonParam,
		contentType : "application/x-www-form-urlencoded; charset=" + getDocumentCharset(),
		success : function(d, textStatus, jqXHR) {
			result = d;
		},
		dataFilter : function(response, dataType) {
			if (response == "") {
				return "null";
			} else {
				return response;
			}
		}
	});

	if (!isNull(options)) {
		$.extend(ajaxParam, options);
	}

	$.ajax(ajaxParam);

	return result;
}

function asynGetDataByAjax(url, jsonParam, isByGet, successFunc, isOpenMask, options) {
	var byGet;
	if (isNull(isByGet)) {
		byGet = is_ajax_getmethod_by_get;
	} else {
		byGet = isByGet;
	}

	var ajaxParam = {};
	if (!isNull(isOpenMask)) {
		$.extend(ajaxParam, {
			openMask : isOpenMask
		});
	}

	var needJsonp = isCrossVisit(url);
	var gourl;
	var dataType;
	if (needJsonp) {
		gourl = urlAddParam(url, "Accept-Error-DataType=jsonp&Accept-DataType=jsonp&Client-Type=jquery");
		dataType = "jsonp";
		var jsonpCallback = "jsonpCallback" + (new Date().getTime()) + parseInt(1000 * Math.random());
		$.extend(ajaxParam, {
			jsonpCallback : jsonpCallback,
			dataFilter : function(response, dataType) {
				if (response == "") {
					return jsonpCallback + "(null);";
				} else {
					return response;
				}
			}
		});
	} else {
		gourl = url;
		dataType = "json";
		$.extend(ajaxParam, {
			dataFilter : function(response, dataType) {
				if (response == "") {
					return "null";
				} else {
					return response;
				}
			}
		});
	}

	if (!isNull(options)) {
		var optionsSuccess = options.success;
		delete options.success;
	}

	$.extend(ajaxParam, {
		type : byGet ? "GET" : "POST",
		async : true,
		url : gourl,
		dataType : dataType,
		data : jsonParam,
		contentType : "application/x-www-form-urlencoded; charset=" + getDocumentCharset(),
		success : function(d, textStatus, jqXHR) {
			successFunc(d);

			if (!isNull(optionsSuccess)) {
				optionsSuccess(d, textStatus, jqXHR);
			}
		}
	});

	if (!isNull(options)) {
		$.extend(ajaxParam, options);
	}

	$.ajax(ajaxParam);

}

function getHtmlByAjax(url, jsonParam, isByGet, isOpenMask, options) {
	var result = null;

	var byGet;
	if (isNull(isByGet)) {
		byGet = is_ajax_getmethod_by_get;
	} else {
		byGet = isByGet;
	}

	var ajaxParam = {};
	if (!isNull(isOpenMask)) {
		$.extend(ajaxParam, {
			openMask : isOpenMask
		});
	}

	$.extend(ajaxParam, {
		type : byGet ? "GET" : "POST",
		async : false,
		url : url,
		dataType : "html",
		data : jsonParam,
		contentType : "application/x-www-form-urlencoded; charset=" + getDocumentCharset(),
		success : function(d, textStatus, jqXHR) {
			result = d;
		}
	});

	if (!isNull(options)) {
		$.extend(ajaxParam, options);
	}

	$.ajax(ajaxParam);

	return result;
}

function asynGetHtmlByAjax(url, jsonParam, isByGet, successFunc, isOpenMask, options) {
	var byGet;
	if (isNull(isByGet)) {
		byGet = is_ajax_getmethod_by_get;
	} else {
		byGet = isByGet;
	}

	var ajaxParam = {};
	if (!isNull(isOpenMask)) {
		$.extend(ajaxParam, {
			openMask : isOpenMask
		});
	}

	if (!isNull(options)) {
		var optionsSuccess = options.success;
		delete options.success;
	}

	$.extend(ajaxParam, {
		type : byGet ? "GET" : "POST",
		async : true,
		url : url,
		dataType : "html",
		data : jsonParam,
		contentType : "application/x-www-form-urlencoded; charset=" + getDocumentCharset(),
		success : function(d, textStatus, jqXHR) {
			successFunc(d);

			if (!isNull(optionsSuccess)) {
				optionsSuccess(d, textStatus, jqXHR);
			}
		}
	});

	if (!isNull(options)) {
		$.extend(ajaxParam, options);
	}

	$.ajax(ajaxParam);
}

function isNull(obj) {
	if ((obj == null) || (typeof (obj) == "undefined")) {
		return true;
	} else {
		return false;
	}

}

// trim str.
function trim(str) {
	if (isNull(str) || (str == "")) {
		return "";
	} else {
		// trim str.
		return str.replace(/(^\s*)|(\s*$)/g, "");
	}
}

function getURLTimeStamp() {
	var dt = new Date();
	return "ts=" + dt.getTime();
}

function urlAddRandom(url) {
	if (url.indexOf('?') >= 0) {
		return url + "&" + getURLTimeStamp();
	} else {
		return url + "?" + getURLTimeStamp();
	}
}

function urlAddParam(url, paramStr) {
	if (url.indexOf('?') >= 0) {
		return url + "&" + paramStr;
	} else {
		return url + "?" + paramStr;
	}
}

/**
 * 设置 autocomplete 配置参数 ： { isAutoCleanSourceObj:是否自动清除输入值 :true 自动清除，false 不自动清除 (默认true) minLength: 最少输入几个字，才去服务器获取选择项的数据，默认为1 maxItemCount:下拉选择项的最大条数.默认15条(global_autocomplete_max_item_count)
 * isClickShowMenu:是否点击就提供下拉选择框，true/false,默认false }
 */
var defaultAutoCompleteParams = {};
function configAutoComplete(conf) {
	$.extend(defaultAutoCompleteParams, conf);
}

/**
 * fillObjId: 绑定autocomplete事件的input对象的ID sourceUrl:选择项的URL (返回元素为item的json集合,每个item,必须有code,label属性) setparams:JSON类型的参数，包含： fillRelation: 页面元素ID 与 返回的Item值 的匹配关系, 如
 * ["name":"name","address":"userAddress"] selectFunc: 选着后的回调函数，参数为 item 对象 cleanFunc: 不选择的回调函数，无参数 isAutoCleanSourceObj:是否自动清除输入值 :true 自动清除，false 不自动清除 (默认true) minLength: 最少输入几个字，才去服务器获取选择项的数据，默认为1
 * maxItemCount:下拉选择项的最大条数.默认15条(global_autocomplete_max_item_count) isClickShowMenu:是否点击就提供下拉选择框，true/false,默认false
 */
function jqueryAutoComplete(sourceObjId, sourceUrl, setparams) {
	var params = {};
	params = $.extend(params, defaultAutoCompleteParams);
	params = $.extend(params, setparams);

	var fillRelation = params.fillRelation;
	var selectFunc = params.selectFunc;
	var cleanFunc = params.cleanFunc;
	var isAutoCleanSourceObj = params.isAutoCleanSourceObj;
	var minLength = params.minLength;
	var fillRelation = params.fillRelation;
	var maxItemCount = params.maxItemCount;
	var isClickShowMenu = params.isClickShowMenu;

	var isClean = true;

	if (isNull(fillRelation)) {
		fillRelation = {};
	}
	if (isNull(fillRelation[sourceObjId])) {
		fillRelation[sourceObjId] = "label";
	}

	if (isNull(isClickShowMenu)) {
		isClickShowMenu = false;
	}

	if (isNull(isAutoCleanSourceObj)) {
		isAutoCleanSourceObj = true;
	}

	if (isNull(minLength)) {
		minLength = 1;
	}

	if (isNull(maxItemCount)) {
		maxItemCount = 20;
	}

	$("#" + sourceObjId).keydown(function(e) {
		isClean = true;
	});

	$("#" + sourceObjId).autocomplete({
		minLength : minLength
	});

	$("#" + sourceObjId).autocomplete({
		/*source : function(request, response) {
			response($.map(getDataByAjax(sourceUrl,request), function(item) {
				return item;
			})); 
		},*/
		source : urlAddRandom(sourceUrl) + "&count=" + maxItemCount,
		change : function(e, ui) {
			if (isClean) {
				for ( var objid in fillRelation) {
					if (!(objid == sourceObjId && !isAutoCleanSourceObj)) {
						$("#" + objid).val("");
						if (objid == sourceObjId) {
							if ($("#" + objid).data("uiAutocomplete") != null) {
								$("#" + objid).data("uiAutocomplete").term = "";
							}
						}
					}
				}
				if (!isNull(cleanFunc)) {
					cleanFunc();
				}
			}

			$("#" + sourceObjId).trigger("focus");
			return false;

			// return true;
		},
		select : function(e, ui) {
			isClean = false;

			for ( var objid in fillRelation) {
				var objvalue = "";

				var itemid = fillRelation[objid];
				if (!isNull(itemid)) {
					objvalue = eval("ui.item." + itemid);
					if (isNull(objvalue)) {
						objvalue = ""
					}
				}

				$("#" + objid).val(objvalue);
			}

			if (!isNull(selectFunc)) {
				selectFunc(ui.item);
			}

			return false;
		}
	});

	if (isClickShowMenu) {
		$("#" + sourceObjId).unbind("click");
		$("#" + sourceObjId).bind("click", function() {
			if ($("#" + sourceObjId).val().length >= minLength) {
				$("#" + sourceObjId).autocomplete("search", $("#" + sourceObjId).val());
			}
		});
	}
}

/**
 * 格式化
 */
Date.prototype.Format = function(fmt) {
	var jidu = Math.floor((this.getMonth() + 3) / 3);
	var o = {
		"M+" : this.getMonth() + 1, // 月份
		"d+" : this.getDate(), // 日
		"h+" : this.getHours(), // 小时
		"m+" : this.getMinutes(), // 分
		// 秒
		"s+" : this.getSeconds(),
		// 季度
		"q+" : jidu,
		// 毫秒
		"S" : this.getMilliseconds()
	};

	if (/(y+)/.test(fmt)) {
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	}
	for ( var k in o) {
		if (new RegExp("(" + k + ")").test(fmt)) {
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
		}
	}
	return fmt;
}

/**
 * 动态加载img文件
 */
function loadImg(url) {
	var link = document.createElement("img");
	link.src = url;
	link.style = "display:none;";
	document.body.appendChild(link);
}

/**
 * 动态加载css文件
 */
function loadStyles(url) {
	var link = document.createElement("link");
	link.type = "text/css";
	link.rel = "stylesheet";
	link.href = url;
	document.getElementsByTagName("head")[0].appendChild(link);
}

/**
 * 动态加载js脚本文件
 */
function loadScript(url, successCallback) {
	if (successCallback == null) {
		// 没有回调，则直接嵌入标签
		var script = document.createElement("script");
		script.type = "text/javascript";
		script.src = url;
		document.body.appendChild(script);
	} else {
		// 需要回调，则走ajax
		$.ajax({
			url : url,
			success : function(code) {
				var script = document.createElement("script");
				script.type = "text/javascript";
				try {
					// firefox、safari、chrome和Opera
					script.appendChild(document.createTextNode(code));
				} catch (ex) {
					// IE早期的浏览器 ,需要使用script的text属性来指定javascript代码。
					script.text = code;
				}
				document.body.appendChild(script);
				if (successCallback) {
					successCallback();
				}
			}
		});
	}
}

/**
 * 使用$(function() {})延迟加载，在文档加载后才计时<br>
 * PS：当延迟时间>=0时，由于使用timeout做定时器，不能保证100%先后关系。
 * 
 * @Parm timeout 该值<0时，和$(function() {})处于同一队列
 */
function lazyRun(fun, timeout) {
	if (timeout == null) {
		timeout = -1;
	}

	$(function() {
		if (timeout >= 0) {
			setTimeout(function() {
				fun();
			}, timeout);
		} else {
			try {
				fun();
			} catch (err) {
				console.error(err);
			}
		}
	});
}

/**
 * 判断变量是否被定义过，参数必须是字符串
 * 
 * @param variableName 必须是字符串
 */
function isDefinedVariable(variableName) {
	try {
		if (eval("typeof (" + variableName + ")") == "undefined") {
			return false;
		} else {
			return true;
		}
	} catch (e) {
	}
	return false;
}

/* ★★框架用函数 START★★★★★★★★★★★★★★★★★★★★★★★★★★★★ */

function subWidth(width, subWidth) {
	if (subWidth == null) {
		return width;
	}

	return width - parseFloat(subWidth);
}

/**
 * 设定图片的max-height，防止非16:9图片过高导致排列异常
 */
function resetRowMaxHeightBySelector(selectorStrDiv, selectorStrImg, radio) {
	// 获得当前容器的宽度
	var $selectorStrDiv = $(selectorStrDiv);
	var divWidth = $selectorStrDiv.width();
	var maxHeight = divWidth * 9 / 16;
	if (radio != null) {
		maxHeight = divWidth * parseFloat(radio);
	}
	maxHeight = maxHeight - 1 // 因为宽度获取不到小数点，这里减去一些

	if (maxHeight > 168) {
		// 168为贴图库小图尺寸
		maxHeight = 168;
	}

	$selectorStrDiv.find(selectorStrImg).css("max-height", maxHeight);
}

/**
 * 设定图片的max-height，防止非16:9图片过高导致排列异常
 */
function resetRowMaxHeightBySelectorOneByOne(selectorStrDiv, selectorStrImg, radio) {
	$(selectorStrDiv).each(function() {
		var divParent = $(this);
		var divWidth = divParent.css("width");
		if (divWidth != null) {
			divWidth = parseFloat(divWidth);
			if (divWidth > 0) {
				// var marginleft = divParent.css("margin-left");
				// var marginright = divParent.css("margin-right");
				var borderwidth = divParent.css("border-width");
				var paddingleft = divParent.css("padding-left");
				var paddingright = divParent.css("padding-right");

				// divWidth = subWidth(divWidth, marginleft);
				// divWidth = subWidth(divWidth, marginright);
				divWidth = subWidth(divWidth, borderwidth);
				divWidth = subWidth(divWidth, paddingleft);
				divWidth = subWidth(divWidth, paddingright);

				var maxHeight = divWidth * 9 / 16;
				if (radio != null) {
					maxHeight = divWidth * radio;
				}

				// if (divWidth >= 300) {
				// // 300为缩略图宽度
				// divWidth = 300;
				// maxHeight = 168;
				// }

				// imageLazyImg.css("max-height", maxHeight);
				divParent.find(selectorStrImg).css("max-height", maxHeight);
			}
		}
	});
}
/* ★★框架用函数 END★★★★★★★★★★★★★★★★★★★★★★★★★★★★ */

