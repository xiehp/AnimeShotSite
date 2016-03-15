(function($) {
	$.homePost = function(url, param, callback) {
		$.ajax({
			url : baseUrl + url,
			contentType : 'application/x-www-form-urlencoded',
			dataType : "json",
			type : "post",
			data : param,
			success : function(data) {
				callback(data);
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				console.log('XMLHttpRequest: ' + XMLHttpRequest + ',textStatus:' + textStatus + ',errorThrown:' + errorThrown);
				alertErrorMsg(XMLHttpRequest);
			}
		});
	}

	$.homeGet = function(url, callback) {
		$.ajax({
			url : baseUrl + url,
			contentType : 'application/json',
			dataType : "json",
			type : "get",
			success : function(data) {
				callback(data);
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				console.log('XMLHttpRequest: ' + XMLHttpRequest + ',textStatus:' + textStatus + ',errorThrown:' + errorThrown);
				alertErrorMsg(XMLHttpRequest);
			}
		});
	}
	$.homeJsonPost = function(url, param, callback) {
		$.ajax({
			url : baseUrl + url,
			contentType : 'application/json',
			dataType : "json",
			type : "post",
			data : JSON.stringify(param),
			success : function(data) {
				callback(data);
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				console.log('XMLHttpRequest: ' + XMLHttpRequest + ',textStatus:' + textStatus + ',errorThrown:' + errorThrown);
				alertErrorMsg(XMLHttpRequest);
			}
		});
	}

	$.homeJsonGet = function(url, callback) {
		$.ajax({
			url : baseUrl + url,
			contentType : 'application/json',
			dataType : "json",
			type : "get",
			success : function(data) {
				callback(data);
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				console.log('XMLHttpRequest: ' + XMLHttpRequest + ',textStatus:' + textStatus + ',errorThrown:' + errorThrown);
				alertErrorMsg(XMLHttpRequest);
			}
		});
	}

	$.homePostNotAsync = function(url, param, callback) {
		$.ajax({
			url : baseUrl + url,
			contentType : 'application/json',
			dataType : "json",
			type : "post",
			data : JSON.stringify(param),
			async : false,
			success : function(data) {
				callback(data);
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				console.log('XMLHttpRequest: ' + XMLHttpRequest + ',textStatus:' + textStatus + ',errorThrown:' + errorThrown);
				alertErrorMsg(XMLHttpRequest);
			}
		});
	}

	$.homeGetNotAsync = function(url, callback) {
		$.ajax({
			url : baseUrl + url,
			contentType : 'application/json',
			dataType : "json",
			type : "GET",
			async : false,
			success : function(data) {
				callback(data);
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				console.log('XMLHttpRequest: ' + XMLHttpRequest + ',textStatus:' + textStatus + ',errorThrown:' + errorThrown);
				alertErrorMsg(XMLHttpRequest);
			}
		});
	}

	function alertErrorMsg(XMLHttpRequest) {
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
			obj.message = "发生错误";
			showMessage(obj);
		}
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
				} else if (tagName == 'SELECT' ) {
					obj.val(value);
				} else if (tagName == 'LABEL') {
					$(this).text(value);
				} else if (tagName == 'SPAN') {
					$(this).text(value);
				} else if(tagName == 'TEXTAREA'){
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
	
	$.homeAutoComplete = function(element, url){
		$('#' + element).autocomplete({
			source : function(request, response){
				$.homePost(url, {param : request.term}, function(data){	
					response(data);			
				});
			},
			minLength : 2
		});
	}
	
	// 日期增加年数
	$.homeAddYear = function(date,years){ 
	       var d=new Date(date); 
	       d.setFullYear(d.getFullYear()+years); 
	       var m=d.getMonth()+1; 
	       return d.getFullYear()+'-'+m+'-'+d.getDate(); 
	    }
	// 日期增加天数
	$.homeAddDate = function(date,days){ 
       var d=new Date(date); 
       d.setDate(d.getDate()+days); 
       var m=d.getMonth()+1; 
       if(m<10){
    	   m='0'+m
       }
       var tag=""
       if(d.getDate()<10){
    	   tag="0"
       }
       return d.getFullYear()+'-'+m+'-'+tag+d.getDate(); 
    }
	$.homeDateCompare = function(startDateID,endDateID){
		$('#'+startDateID).on('change',function() {
			endDate = new Date($('#'+endDateID).val())
			startDate = new Date($(this).val())
			if(endDate!= null && endDate!=""){
				if(startDate>=endDate){
					$(this).val($.homeAddDate(endDate,-1))
				}
			}
		})
		$('#'+endDateID).on('change',function() {
			startDate = new Date($('#'+startDateID).val())
			endDate = new Date($(this).val())
			if(startDate!= null && startDate!=""){
				if(startDate>=endDate){
					$(this).val($.homeAddDate(startDate,1))
				}
			}
		})
	}
	
	$.escapeHtml = function(s){
		return (s)? $("<p>").text(s).html(): "";
	}
	
})(jQuery);