// 读取cookie，并且设置图片尺寸
// isCreateDom:是否正在创建dom的过程中
function readCookieAndSetWidth(isCreateDom) {
	var ShotViewImgWidth = $.cookie("ShotViewImgWidth");
	$.log("读取设置图片尺寸cookie:" + ShotViewImgWidth);
	var orImgWidth = 16;
	var orImgHeight = 9;
	var divPaddingLen = 4;
	var divBorderLen = 1;

	if (!isNaN(ShotViewImgWidth) && ShotViewImgWidth > 0) {
		// 有cookie
		if (isCreateDom) {
			var setHeight = ShotViewImgWidth * orImgHeight / orImgWidth + divPaddingLen + divBorderLen;
			$("#shotImgDiv").css("width", ShotViewImgWidth);
			$("#shotImgDiv").css("height", setHeight);
			$.log("设置图片div尺寸:" + ShotViewImgWidth + "," + setHeight);
		} else {
			$("#ShotViewImgWidth").val(ShotViewImgWidth);
		}
	} else {
		// 无cookie，直接根据当前div宽度设定宽高
		if (isCreateDom) {
			var divWidth = $("#shotImgDiv").css("width");
			divWidth = parseFloat(divWidth);
			$.log("当前div宽度:" + divWidth);
			if (!isNaN(divWidth) && divWidth > 0) {
				var setHeight = divWidth * orImgHeight / orImgWidth + divPaddingLen + divBorderLen;
				$("#shotImgDiv").css("height", setHeight);
				$.log("设置图片div尺寸:" + divWidth + "," + setHeight);
			}
		}
	}
}

function copyText(obj) {
	try {
		var rng = document.body.createTextRange();
		rng.moveToElementText(obj);
		rng.scrollIntoView();
		rng.select();
		rng.execCommand("Copy");
		rng.collapse(false);
		alert("已经复制到粘贴板!你可以使用Ctrl+V 贴到需要的地方去了哦!");
	} catch (e) {
		alert("您的浏览器不支持此复制功能，请选中相应内容并使用Ctrl+C进行复制!");
	}
}

function initZeroClipboard(obj) {
	// 气泡弹出提示
	var options = {
		"placement" : 'auto right',
		"trigger" : 'click',
		"delay" : {
			"show" : 200,
			"hide" : 600
		},
		"title" : '复制成功-_-'
	};
	$(obj).tooltip(options);

	// 复制剪切板插件
	var client = new ZeroClipboard(obj);
	client.on("ready", function(readyEvent) {
		client.on("aftercopy", function(event) {
			$(obj).tooltip('show');
			setTimeout(function() {
				$(obj).tooltip('hide');
			}, 1000);
		});
	});
}

// 定时执行获取宽高
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
		document.getElementById("scorllTop").value = HomeCookie.getCookie("ShotViewScorllTop");
		var scorllTop = document.getElementById("scorllTop").value;
		if (scorllTop != null && scorllTop > 0) {
			scrollTo(0, scorllTop);
			$.log("设置初始滚动条高度:" + scorllTop);
		}

		// 追加滚动事件
		setTimeout(function() {
			window.onscroll = function() {
				var nowScrollTop = document.body.scrollTop;
				document.getElementById("scorllTop").value = nowScrollTop;
				HomeCookie.setCookie("ShotViewScorllTop", nowScrollTop);
				$.log("滚动事件:" + nowScrollTop);
			};
			$.log("追加滚动事件");
		});

		// 动态生成左右图片热点
		window.onresize = reCreateImgHotLink;
		reCreateImgHotLink();

		$.log("读取到图片宽度后处理结束。");
		return;
	}

	maxCheckCount = maxCheckCount - 1;
	setTimeout(function() {
		checkImgSize(imgDomObject, maxCheckCount);
	}, 200);
};

// 动态生成左右图片热点链接
function reCreateImgHotLink(width, height) {
	var jqueryShotImg = $("#shotImg");
	var shotImg = jqueryShotImg[0];
	var shotImgMapWidth = shotImg.width;
	var shotImgMapHeight = shotImg.height;
	if (width != null && height != null) {
		shotImgMapWidth = width;
		shotImgMapHeight = height;
	}
	$("#areaPrev").attr("coords", "0,0," + shotImgMapWidth / 3 + "," + shotImgMapHeight);
	$("#areaNext").attr("coords", shotImgMapWidth / 3 * 2 + ",0," + shotImgMapWidth + "," + shotImgMapHeight);
	$.log("图片左热点设置：" + $("#areaPrev").attr("coords"));
	$.log("图片右热点设置：" + $("#areaNext").attr("coords"));
}

function changeShotViewImgWidth() {
	var ShotViewImgWidth = $("#ShotViewImgWidth").val();
	if (ShotViewImgWidth != null && isNaN(ShotViewImgWidth)) {
		$("#ShotViewImgWidth").val("");
		$.showMessageModal("请输入数字");
		return;
	}

	if (ShotViewImgWidth <= 0) {
		HomeCookie.removeCookie("ShotViewImgWidth");
		// 设置图片尺寸
		var width = '';
		var height = '';
		$("#shotImgDiv").css("width", width);
		$("#shotImgDiv").css("height", height);
		$.log("改变图片div尺寸：" + width + ", " + height);
		setShotImgDivWidthCookie($("#shotImgDiv").css("width"));
		// $("#shotImg").css("width", '');
	} else {
		if (originalImg != null && originalImg.width > 0 && ShotViewImgWidth > originalImg.width) {
			ShotViewImgWidth = originalImg.width;
			$("#ShotViewImgWidth").val(ShotViewImgWidth);
		}
		HomeCookie.setCookie("ShotViewImgWidth", ShotViewImgWidth);
		// 设置图片尺寸
		var width = ShotViewImgWidth;
		var height = '';
		$("#shotImgDiv").css("width", width);
		$("#shotImgDiv").css("height", height);
		// $("#shotImg").css("width", ShotViewImgWidth);
		$.log("改变图片div尺寸：" + width + ", " + height);
	}

	reCreateImgHotLink();
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