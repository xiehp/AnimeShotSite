/**
 * Doc: http://www.layui.com/doc/modules/layer.html#icon<br>
 * Doc: http://www.jeasyui.net/plugins/182.html 2016/10/27.<br>
 * 
 * 关于options， options.notEscapeHtml 不对内容进行转换（为了安全，默认文本会将html关键字转换掉）
 */
var Message = function() {

	/**
	 * 提示框 <br>
	 * 
	 * @param content 内容
	 * @param func 点击确定按钮后执行的方法(可空)
	 */
	var alert = function(content, title, func, options) {
		if (title == null) {
			title = "提示"
		}

		var escapeContent = escapeContentByOption(content, options);

		var layerOptions = {
			title : title,
			move : false,
			closeBtn : 0
		};
		if (options != null) {
			$.extend(layerOptions, options);
		}

		layer.alert(escapeContent, layerOptions, function(index) {
			layer.close(index);

			if (func != null) {
				func();
			}
		})

		// easyui
		// $.messager.alert(title, content, "info", func);
	}

	/**
	 * 确认框
	 * 
	 * @param content 内容
	 * @param func 点击确定按钮后执行的方法(可空)
	 */
	var confirm = function(content, title, successCallback, failCallback, options) {
		if (title == null) {
			title = "确认"
		}

		var escapeContent = escapeContentByOption(content, options);

		var layerOptions = {
			title : title,
			move : false,
			closeBtn : 0
		};
		if (options != null) {
			$.extend(layerOptions, options);
		}

		// layer
		layer.confirm(escapeContent, layerOptions, function(index) {
			layer.close(index);

			if (func != null) {
				func();
			}
		})

		// easyui
		// $.messager.confirm(title, content, function(okFlg) {
		// if (okFlg) {
		// if (successCallback) {
		// successCallback();
		// }
		// } else {
		// if (failCallback) {
		// failCallback();
		// }
		// }
		// });
	}

	/**
	 * 消息框
	 * 
	 * @param content 内容
	 * @param options 其他參數
	 * @param callback 点击确定按钮后执行的方法(可空)
	 */
	var msg = function(content, options, callback) {
		if (options == null) {
			options = {};
		}

		var escapeContent = escapeContentByOption(content, options);

		var layerOptions = {
			move : false,
			closeBtn : 0
		};
		if (options != null) {
			$.extend(layerOptions, options);
		}

		// layer
		layer.msg(escapeContent, layerOptions, function(index) {
			layer.close(index)
			if (callback != null) {
				callback();
			}
		})

		// easyui
		// $.messager.confirm(title, content, function(okFlg) {
		// if (okFlg) {
		// if (successCallback) {
		// successCallback();
		// }
		// } else {
		// if (failCallback) {
		// failCallback();
		// }
		// }
		// });
	}
	
	function escapeContentByOption(content, options) {
		var escapeContent = content;
		if(options == null || !options.notEscapeHtml) {
			// 对content进行转义
			escapeContent = $.escapeHtml(content);
		}
		return escapeContent;
	}

	/**
	 * 自定义弹出层
	 */
	var custom = function() {
		alert("还没开发!")
	}

	/**
	 * 弹出页面
	 * 
	 * @param data 可以为jQuery对象
	 * @param weight 弹出层的宽(可空 默认500px)
	 * @param height 弹出层的高(可空 默认300px)
	 * @param title 弹出层的标的(可空 默认为信息)
	 */
	var openPage = function(data, weight, height, title) {
		if (weight == null) {
			weight = "500"
		}
		if (height == null) {
			height = "300"
		}
		if (title == null) {
			title = "信息"
		}
		if (data instanceof jQuery) {
			layer.open({
				type : 1,
				move : false,
				title : title,
				area : [ weight + 'px', height + 'px' ],
				content : data
			})
		}
	}

	/**
	 * 关闭所有窗口
	 */
	var closeAll = function() {
		layer.closeAll()
	}

	/**
	 * 显示加载层
	 */
	var ajaxLoadingIndex;
	var ajaxLoading = function() {
		ajaxLoadingIndex = layer.load(1, {
			time : 10 * 1000
		});
	}

	/**
	 * 关闭加载层
	 */
	var ajaxLoadEnd = function() {
		layer.close(ajaxLoadingIndex);
	}

	return {
		alert : alert,
		confirm : confirm,
		msg : msg,
		custom : custom,
		openPage : openPage,
		closeAll : closeAll,
		ajaxLoading : ajaxLoading,
		ajaxLoadEnd : ajaxLoadEnd
	}
}()
