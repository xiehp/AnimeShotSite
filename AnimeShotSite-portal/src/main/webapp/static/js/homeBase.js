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

	function processGoPageResult(data, callback) {
		// 显示信息
		if (data.alertMessage != null && data.alertMessage.length > 0) {
			// showMessageWithoutRefresh(obj);
			// alert(data.alertMessage);

			var message = data.alertMessage.join("\n");
			if (data.success) {
				Message.successMessage(message, function() {
					processCallbackAndGoPage(data, callback);
				});
			} else {
				Message.failedMessage(message, function() {
					processCallbackAndGoPage(data, callback);
				});
			}
		} else {
			processCallbackAndGoPage(data, callback);
		}
	}

	function processCallbackAndGoPage(data, callback) {
		// 执行callback
		if (callback) {
			callback(data);
		}

		// 跳转
		if (data.goPage != null && data.goPage.trim() != "") {
			if (data.goPage.indexOf("/") != 0) {
				data.goPage = "/" + data.goPage;
			}
			window.location.href = baseUrl + data.goPage;
		}
	}

	function getDocumentCharset() {
		return document.characterSet ? document.characterSet : document.charset;
	}

	/** ajax提交form表单 */
	$.homeAjaxSubmit = function(formId, callback, otherParam) {
		var ajaxParam = {};
		$.extend(ajaxParam, {
			async : true,
			contentType : "application/x-www-form-urlencoded; charset=" + getDocumentCharset(),
			dataType : "json",
			type : "post",
			data : otherParam,
			success : function(data) {
				if (callback) {
					callback(data);
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				console.log('XMLHttpRequest: ' + XMLHttpRequest + ',textStatus:' + textStatus + ',errorThrown:' + errorThrown);
				alertErrorMsg(XMLHttpRequest, ',textStatus:' + textStatus + ',errorThrown:' + errorThrown);
			}
		});

		$('#' + formId).ajaxSubmit(ajaxParam);
	}

	$.homePost = function(url, param, callback) {
		$.ajax({
			url : global.ctx + url,
			contentType : 'application/x-www-form-urlencoded',
			dataType : "json",
			type : "post",
			data : param,
			success : function(data) {
				if (callback) {
					callback(data);
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				console.log('XMLHttpRequest: ' + XMLHttpRequest + ',textStatus:' + textStatus + ',errorThrown:' + errorThrown);
				alertErrorMsg(XMLHttpRequest, ',textStatus:' + textStatus + ',errorThrown:' + errorThrown);
			}
		});
	}

	$.homeGet = function(url, callback) {
		$.ajax({
			url : global.ctx + url,
			contentType : 'application/json',
			dataType : "json",
			type : "get",
			success : function(data) {
				if (callback) {
					callback(data);
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				console.log('XMLHttpRequest: ' + XMLHttpRequest + ',textStatus:' + textStatus + ',errorThrown:' + errorThrown);
				alertErrorMsg(XMLHttpRequest);
			}
		});
	}

	$.homeJsonPost = function(url, param, callback) {
		$.ajax({
			url : global.ctx + url,
			contentType : 'application/json',
			dataType : "json",
			type : "post",
			data : JSON.stringify(param),
			success : function(data) {
				if (callback) {
					callback(data);
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				console.log('XMLHttpRequest: ' + XMLHttpRequest + ',textStatus:' + textStatus + ',errorThrown:' + errorThrown);
				alertErrorMsg(XMLHttpRequest);
			}
		});
	}

	$.homeJsonGet = function(url, callback) {
		$.ajax({
			url : global.ctx + url,
			contentType : 'application/json',
			dataType : "json",
			type : "get",
			success : function(data) {
				if (callback) {
					callback(data);
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				console.log('XMLHttpRequest: ' + XMLHttpRequest + ',textStatus:' + textStatus + ',errorThrown:' + errorThrown);
				alertErrorMsg(XMLHttpRequest);
			}
		});
	}

	$.homePostNotAsync = function(url, param, callback) {
		$.ajax({
			url : global.ctx + url,
			contentType : 'application/json',
			dataType : "json",
			type : "post",
			data : JSON.stringify(param),
			async : false,
			success : function(data) {
				if (callback) {
					callback(data);
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				console.log('XMLHttpRequest: ' + XMLHttpRequest + ',textStatus:' + textStatus + ',errorThrown:' + errorThrown);
				alertErrorMsg(XMLHttpRequest);
			}
		});
	}

	$.homeGetNotAsync = function(url, callback) {
		$.ajax({
			url : global.ctx + url,
			contentType : 'application/json',
			dataType : "json",
			type : "GET",
			async : false,
			success : function(data) {
				if (callback) {
					callback(data);
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				console.log('XMLHttpRequest: ' + XMLHttpRequest + ',textStatus:' + textStatus + ',errorThrown:' + errorThrown);
				alertErrorMsg(XMLHttpRequest, msg);
			}
		});
	}

	function alertErrorMsg(XMLHttpRequest, msg) {
		if (XMLHttpRequest.responseText.indexOf('login page do not delete') != -1) {
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

/*
 * 设置 autocomplete 配置参数 ：
 * {
 *  isAutoCleanSourceObj:是否自动清除输入值 :true 自动清除，false 不自动清除 (默认true)
	minLength: 最少输入几个字，才去服务器获取选择项的数据，默认为1
	maxItemCount:下拉选择项的最大条数.默认15条(global_autocomplete_max_item_count)	
	isClickShowMenu:是否点击就提供下拉选择框，true/false,默认false
 * }
 */
var defaultAutoCompleteParams = {};
function configAutoComplete(conf) {
	$.extend(defaultAutoCompleteParams, conf);
}

/*
fillObjId: 绑定autocomplete事件的input对象的ID
sourceUrl:选择项的URL (返回元素为item的json集合,每个item,必须有code,label属性)
setparams:JSON类型的参数，包含：
	fillRelation:
           	页面元素ID 与 返回的Item值 的匹配关系, 如 ["name":"name","address":"userAddress"] 
	selectFunc: 选着后的回调函数，参数为  item 对象
	cleanFunc: 不选择的回调函数，无参数
	isAutoCleanSourceObj:是否自动清除输入值 :true 自动清除，false 不自动清除 (默认true)
	minLength: 最少输入几个字，才去服务器获取选择项的数据，默认为1
	maxItemCount:下拉选择项的最大条数.默认15条(global_autocomplete_max_item_count)	
	isClickShowMenu:是否点击就提供下拉选择框，true/false,默认false
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

// 格式化
Date.prototype.Format = function(fmt) {
	// author: meizz
	var o = {
		"M+" : this.getMonth() + 1, // 月份
		"d+" : this.getDate(), // 日
		"h+" : this.getHours(), // 小时
		"m+" : this.getMinutes(), // 分
		"s+" : this.getSeconds(), // 秒
		"q+" : Math.floor((this.getMonth() + 3) / 3), // 季度
		"S" : this.getMilliseconds()
	// 毫秒
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