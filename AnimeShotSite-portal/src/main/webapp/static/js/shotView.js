var orImgWidth = 16; // 预想中的图片宽度比例，不能直接使用
var orImgHeight = 9; // 预想中的图片高度比例，不能直接使用
var divPaddingLen = 4; // bootstrap的thumbnail类中设定的padding宽度
var divBorderLen = 1; // bootstrap的thumbnail类中设定的border宽度

/**
 * 读取cookie，并且设置图片尺寸，该尺寸为预想中的尺寸，可能和实际尺寸有出入，真实的尺寸在图片读入成功后再次设定<br>
 * isCreateDom:是否正在创建dom的过程中<br>
 */
function readCookieAndSetWidth(isCreateDom) {
	var ShotViewImgWidth = $.cookie("ShotViewImgWidth");
	$.log("读取设置图片尺寸cookie:" + ShotViewImgWidth);

	var $shotImgDiv = $("#shotImgDiv");
	var ImageAspectRatio = $shotImgDiv.attr("data-ImageAspectRatio");
	if (ImageAspectRatio == "") {
		ImageAspectRatio = orImgHeight / orImgWidth;
	}

	if (!isNaN(ShotViewImgWidth) && ShotViewImgWidth > 0) {
		// 有cookie
		var ShotViewImgDivWidth = ShotViewImgWidth * 1 + divPaddingLen * 2 + divBorderLen * 2;
		if (isCreateDom) {
			var setImgHeight = ShotViewImgWidth * ImageAspectRatio;
			var setImgDivHeight = setImgHeight + divPaddingLen * 2 + divBorderLen * 2;
			$shotImgDiv.css("width", ShotViewImgDivWidth);
			$shotImgDiv.css("height", setImgDivHeight);
			$.log("设置图片div尺寸:" + ShotViewImgDivWidth + "," + setImgDivHeight);
		} else {
			$("#ShotViewImgWidth").val(ShotViewImgWidth);
		}
	} else {
		// 无cookie，直接根据当前div宽度设定宽高
		if (isCreateDom) {
			var divWidth = $shotImgDiv.css("width");
			divWidth = parseFloat(divWidth);
			$.log("当前div宽度:" + divWidth);
			if (!isNaN(divWidth) && divWidth > 0) {
				var setHeight = (divWidth - divPaddingLen * 2 - divBorderLen * 2) * ImageAspectRatio + divPaddingLen * 2 + divBorderLen * 2;
				$shotImgDiv.css("height", setHeight);
				$.log("设置图片div尺寸:" + divWidth + "," + setHeight);
			}
		}
	}
}

/** 复制操作初始化，使用execCommand */
function initZeroClipboard(obj, successMessage, failMessage) {
	// 气泡弹出提示參數
	if (successMessage == null || successMessage == "") {
		successMessage = '复制成功-_-';
	}
	if (failMessage == null || failMessage == "") {
		failMessage = '复制失败，\n请手动复制';
	}
	var options = {
		"trigger" : '',
		"placement" : 'auto right',
		"delay" : {
		// "show" : 400,
		// "hide" : 1000
		},
		"title" : successMessage
	};

	var copyResult = false;
	var objCount = 0;
	var targetObj = null;

	function onButtonCopy() {
		copyResult = true;
	}

	var targetObjId = $(obj).data("clipboard-target");
	if (targetObjId != null && targetObjId != "") {
		targetObj = document.getElementById(targetObjId);

		// 拷贝对象oncopy事件
		targetObj.oncopy = onButtonCopy;
	}

	function showToolTip() {
		var closeTimeOut = 1000;
		if (copyResult) {
			options.title = successMessage;
		} else {
			options.title = failMessage;
			closeTimeOut = 2000;
		}

		// 显示结果气泡
		$(obj).tooltip(options);
		$(obj).tooltip('show');
		objCount++;
		var thisObjCount = objCount;
		setTimeout(function() {
			closeToolTip(thisObjCount);
		}, closeTimeOut);
	}

	function closeToolTip(thisObjCount) {
		if (thisObjCount == objCount) {
			$(obj).tooltip('destroy');
		}
	}

	// execCommand
	obj.onclick = function(event) {

		trackEvent("截图", "复制", "", 0);

		copyResult = false;

		if (targetObj) {
			// 复制目标选择
			$(targetObj).focus();
			$(targetObj).select();

			// 离开复制目标
			// $(targetObj).blur();

			// 执行拷贝
			var result = document.execCommand('copy', false, null);
		}

		// 0.2秒后显示气泡
		setTimeout(function() {
			showToolTip();
		}, 200);
	};
}

function initScroll() {
	// 设置初始滚动条值
	document.getElementById("scorllTop").value = HomeCookie.getCookie("ShotViewScorllTop");
	var scorllTop = document.getElementById("scorllTop").value;
	if (scorllTop != null && scorllTop > 0) {
		scrollTo(0, scorllTop);
		$.log("设置初始滚动条高度:" + scorllTop);
	}
}

/**
 * 定时执行获取图片的高宽
 */
var firstScrollFlag = false;
var checkImgSize = function(imgDomObject, maxCheckCount) {
	if (maxCheckCount <= 0) {
		return;
	}
	if (imgDomObject == null) {
		return;
	}

	if (imgDomObject.width > 0) {
		$.log("读取到图片宽度:" + imgDomObject.width);

		// 设置标题栏图片大小
		$("#imgWidth").text(imgDomObject.width);
		$("#imgHeight").text(imgDomObject.height);

		// 重新设置图片div高宽
		$.log("重新设置图片div高宽");
		changeShotViewImgWidth()

		// 设置初始滚动条值
		initScroll();

		// // 动态生成左右图片热点
		// window.onresize = resizeShotImg;
		// resizeShotImg();

		$.log("读取到图片宽度后处理结束。");
		return;
	} else {
		if (!firstScrollFlag) {
			firstScrollFlag = true;

			// 设置初始滚动条值
			initScroll();
		}
	}

	maxCheckCount = maxCheckCount - 1;
	setTimeout(function() {
		checkImgSize(imgDomObject, maxCheckCount);
	}, 200);
};

/**
 * 改变图像框大小动态生成左右图片热点链接
 */
/* 热点代码删除
function resizeShotImg(width, height) {
	var $shotImg = $("#shotImg");
	var shotImg = $shotImg[0];
	var shotImgMapWidth = shotImg.width;
	var shotImgMapHeight = shotImg.height;
	if (width != null && height != null) {
		shotImgMapWidth = width;
		shotImgMapHeight = height;
	}

	var $areaPrev = $("#areaPrev");
	$areaPrev.attr("coords", "0,0," + shotImgMapWidth / 3 + "," + shotImgMapHeight);
	var $areaNext = $("#areaNext");
	$areaNext.attr("coords", shotImgMapWidth / 3 * 2 + ",0," + shotImgMapWidth + "," + shotImgMapHeight);
	$.log("图片左热点设置：" + $areaPrev.attr("coords"));
	$.log("图片右热点设置：" + $areaNext.attr("coords"));
}
*/

function changeShotViewImgWidth(saveToCookieFlag) {
	var $ShotViewImgWidth = $("#ShotViewImgWidth");
	var ShotViewImgWidth = $ShotViewImgWidth.val();
	if (ShotViewImgWidth != null && isNaN(ShotViewImgWidth)) {
		$ShotViewImgWidth.val("");
		$.showMessageModal("请输入数字");
		return;
	}

	var $shotImgDiv = $("#shotImgDiv");
	if (ShotViewImgWidth <= 0) {
		HomeCookie.removeCookie("ShotViewImgWidth");
		// 设置图片尺寸
		var width = '';
		var height = '';
		$shotImgDiv.css("width", width);
		$shotImgDiv.css("height", height);
		$.log("改变图片div尺寸：" + width + ", " + height);
		setShotImgDivWidthCookie($shotImgDiv.css("width"));
		// $("#shotImg").css("width", '');
	} else {
		if (originalImg != null && originalImg.width > 0 && ShotViewImgWidth > originalImg.width) {
			ShotViewImgWidth = originalImg.width;
			if (saveToCookieFlag) {
				$ShotViewImgWidth.val(ShotViewImgWidth);
			}
		}
		if (saveToCookieFlag) {
			HomeCookie.setCookie("ShotViewImgWidth", ShotViewImgWidth);
		}

		// 设置图片尺寸
		// var divWidth = ShotViewImgWidth * 1 + divPaddingLen * 2 + divBorderLen * 2;
		var divWidth = ShotViewImgWidth;
		var divHeight = '';
		$shotImgDiv.css("width", divWidth);
		$shotImgDiv.css("height", divHeight);
		// $("#shotImg").css("width", ShotViewImgWidth);
		$.log("改变图片div尺寸：" + divWidth + ", " + divHeight);
	}

	// resizeShotImg();
}

function setShotImgDivWidthCookie(value) {
	value = parseFloat(value);
	$.log("设置cookie, ShotImgDivWidth：" + value);
	if (value == null || value == "") {
		HomeCookie.removeCookie("ShotImgDivWidth");
	} else {
		HomeCookie.setCookie("ShotImgDivWidth", value);
	}
}

/**
 * 获取截图<br>
 * 
 * refShotInfoId 参照的截图ID<br>
 * offsetTime 偏移多少毫秒<br>
 * preFlg 向前还是向后<br>
 */
var startTime = new Date();
var doCreateShotCheckTime = 1000;
var taskResultStatus = 0;
var taskResultMessage = "";
var taskResultWriteCount = 0;
function doCreateShot(shotInfoId, offsetTime, preFlg, doneCallback) {

	alert("对不起，该功能已不可用。");
	return;

	if (confirm("是否继续？")) {
		var param = {};
		param.refShotInfoId = shotInfoId;
		param.offsetTime = offsetTime;
		param.preFlg = preFlg;
		$.homePost("/shot/doCreateShot", param, function(result) {
			if (result.message != null && result.message != "") {
				$.showMessageModal(result.message);
			}

			if (result.success) {
				startTime = new Date();
				checkCreateShot(result.taskId, result.animeEpisodeId, result.timestamp, doneCallback);
				rewriteResultMessage();
			}
		});
	}
}

function rewriteResultMessage() {
	setTimeout(function() {
		var div = $("#createShotResultDiv");
		var nowTime = new Date();
		var nowSecond = parseInt((nowTime.getTime() - startTime.getTime()) / 1000);
		var span = document.createElement("span")
		if (taskResultStatus == 1) {
			// 成功
			span.setAttribute("style", "color:green;font-size:18px;");
		} else if (taskResultStatus == 2) {
			// 处理
			span.setAttribute("style", "color:blue;font-size:18px;");
		} else if (taskResultStatus == 3) {
			// 失败
			span.setAttribute("style", "color:red;font-size:18px;");
		} else if (taskResultStatus == 11) {
			// 等待30-120秒
			span.setAttribute("style", "color:orange;font-size:18px;");
		} else if (taskResultStatus == 12) {
			// 等待120秒以上
			span.setAttribute("style", "color:red;font-size:18px;");
		} else {
			// 等待
			span.setAttribute("style", "color:black;font-size:18px;");
		}
		div.empty();
		div.append(span);
		taskResultWriteCount++;
		var pointStr = "";
		for (var i = 0; i <= taskResultWriteCount % 5; i++) {
			pointStr = pointStr + ".";
		}
		div.find("span").text(taskResultMessage + "  " + nowSecond + "  " + pointStr);

		if (taskResultStatus != 1) {
			rewriteResultMessage();
		}
	}, 200);
}

/**
 * 检测是否截图成功<br>
 * 
 * refShotInfoId 参照的截图ID<br>
 * offsetTime 偏移多少毫秒<br>
 * preFlg 向前还是向后<br>
 */
function checkCreateShot(taskId, animeEpisodeId, timestamp, doneCallback) {
	var param = {};
	param.taskId = taskId;
	param.animeEpisodeId = animeEpisodeId;
	param.timestamp = timestamp;
	$.homePost("/shot/checkCreateShot", param, function(result) {
		if (result.success) {
			if (result.taskMessage != null && result.taskMessage != "") {
				taskResultMessage = result.taskMessage;
				taskResultStatus = result.taskResutStatus;
			}

			if (result.shotInfoId != null) {
				$.showMessageModal(result.message);
				setTimeout(function() {
					window.location = global.ctx + "/shot/view/" + result.shotInfoId;
				}, 2000);
				return;
			} else {
				setTimeout(function() {
					checkCreateShot(taskId, animeEpisodeId, timestamp, doneCallback);
				}, doCreateShotCheckTime);
			}
		} else {
			if (result.message != null && result.message != "") {
				$.showMessageModal(result.message);
			}
		}
	});
}

/**
 * 删除图片
 */
function deleteShotById(id) {
	if (confirm("确定是否删除？")) {
		var param = {};
		param.id = id;
		$.homePost("/shot/delete/" + id, param, function(result) {
			$.showMessageModal(result.message);
			// if (result.showShotInfoId != null && showShotInfoId != "") {
			// setTimeout(function() {
			// window.location = global.ctx + "/shot/view/" + result.showShotInfoId;
			// }, 2000);
			// }
		});
	}
}

// 追加滚动事件
lazyRun(function() {
	window.onscroll = function() {
		var nowScrollTop = document.body.scrollTop;
		if (nowScrollTop == 0) {
            nowScrollTop = document.documentElement.scrollTop;
		}
		document.getElementById("scorllTop").value = nowScrollTop;
		HomeCookie.setCookie("ShotViewScorllTop", nowScrollTop);
		$.log("滚动事件:" + nowScrollTop);
	};
	$.log("追加滚动事件");
});

// 复制按钮事件
lazyRun(function() {
	$(".ZeroClipboardButton").each(function() {
		initZeroClipboard(this, "复制成功-_-", "复制失败，\n请手动复制");
	});
});

// https://whatanime.ga/?loop&url=${FullImageUrl}
function openWhatanimeSite(thisLink, imgUrl) {
	trackEvent("截图", "短视频", imgUrl, 0);

	//thisLink.href = "https://whatanime.ga/?loop&url=" + imgUrl;
	//thisLink.onclick = null;
	//thisLink.click();
	window.open("https://whatanime.ga/?loop&url=" + imgUrl);
}

/**
 * 创建评论
 */
function createShotCommentRecord() {
	$("#createCommentForm").attr("action", $("#createCommentForm").data("action"));
	$.homeAjaxSubmit("createCommentForm", null, function() {
		location.reload();
	});
}