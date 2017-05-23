/**
 * Doc: http://www.layui.com/doc/modules/layer.html#icon Created by zhao on 2016/10/27.
 */

var Message = function() {

	// 默认带确认框
	var baseStyle = {
		shade : 0.4, // 遮罩透明度
		moveType : 1, // 拖拽风格，0是默认，1是传统拖动
		shift : 4, // 0-6的动画形式，-1不开启
	}

	function showMessageBase(title, html, buttenNameList, shift, argumentsList) {
		var localStyle = baseStyle

		localStyle.title = title
		localStyle.content = html

		if (shift != null) {
			localStyle.shift = shift
		}

		if (buttenNameList != null) {
			// 增加按钮
			if (buttenNameList.length != 0 && buttenNameList instanceof Array) {
				localStyle.btn = buttenNameList

				if (argumentsList != null) {
					for (var i = 0; i < argumentsList.length; i++) {
						var keyName = "yes"
						if (i != 0) { // 如果是第四个参数 设置为yes
							keyName = 'btn' + (i + 1)
						}
						localStyle[keyName] = (function(i) {
							return function(index) {
								argumentsList[i]()
								layer.close(index)
							}
						})(i)
					}
				}
			} else {
				console.log("buttenNameList 参数错误")
			}
		}
		// 生成弹出层
		layer.open(localStyle);
	}

	// 对html增加样式
	function modifyHtmlStyle(html) {
		if (html.indexOf('style') < 0) {
			html = '<div style="text-align: center;margin-top: 50px;">' + html + '</div>'
		}
		return html
	}
	closeLoad

	function info(html, buttenNameList) {
		var argumentsList = null
		if (arguments.length > 2) {
			argumentsList = Array.prototype.slice.call(arguments).splice(2)
		}
		showMessageBase("信息", html, buttenNameList, 4, argumentsList)
	}

	function waring(html, buttenNameList) {
		var argumentsList = null
		if (arguments.length > 2) {
			argumentsList = Array.prototype.slice.call(arguments).splice(2)
		}
		showMessageBase("警告", html, buttenNameList, 6, argumentsList)
	}

	function doConfirm(content, icNum, title, func) {
		layer.confirm(content, {
			icon : icNum,
			title : title
		}, function(index) {
			if (func) {
				func()
			}
			layer.close(index);
		});
	}

	function message(content, func) {
		layer.confirm(content, {
			title : "提示"
		}, function(index) {
			if (func) {
				func()
			}
			layer.close(index);
		});
	}

	function successMessage(content, func) {
		doConfirm(content, 1, "提示", func)
	}

	function failedMessage(content, func) {
		doConfirm(content, 2, "提示", func)
	}

	function questionMessage(content, func) {
		doConfirm(content, 3, "提示", func)
	}

	var loadStatus = null
	function load() {
		loadStatus = layer.load(1); // 风格1的加载
	}

	function closeLoad() {
		layer.close(loadStatus)
	}

	return {
		waring : waring,
		info : info,
		load : load,
		message : message,
		successMessage : successMessage,
		failedMessage : failedMessage,
		questionMessage : questionMessage,
		closeLoad : closeLoad
	}

}()
