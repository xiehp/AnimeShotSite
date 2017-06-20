(function() {
	var imageLazyImg = $("img.imagelazy");

	// 设定图片的max-height，防止非16:9图片过高导致排列异常
	{
		function setMaxHeight() {
			// 获得当前图片高度，通常为默认的待加载图片
			var $imageLazyDiv = imageLazyImg.parents(".thumbnail");
			var width = $imageLazyDiv.width();
			// var width = imageLazyImg.width();
			var maxHeight = width * 9 / 16 - 0.5; // 因为宽度获取不到小数点，这里减去0.5
			imageLazyImg.css("max-height", maxHeight);
		}

		// setMaxHeight();
		$(window).resize(function() {
			// setMaxHeight();
		});
	}

	// 图片懒加载
	imageLazyImg.lazyload({
		// placeholder : global.ctx + "/static/img/imageLoading_mini.jpg",
		effect : "fadeIn"
	});
	imageLazyImg.show();

//	var selectorStrDiv = "div.thumbnail";
//	var selectorStrImg = "img.imagelazy";
//
//	resetRowMaxHeightBySelector(selectorStrDiv, selectorStrImg);
//	$(window).resize(function() {
//		resetRowMaxHeightBySelector(selectorStrDiv, selectorStrImg);
//	});
})();

// 表格内容滚动
lazyRun(function() {
	// 设置表格属性
	var jqueryTable = $(".table-body-scroll");
	if (jqueryTable.length > 0) {
		jqueryTable.each(function() {
			resetTableBodyScroll($(this));
		});

		var doResizeTimeout = 100;
		var timeOutValue = doResizeTimeout;
		var startDate = new Date();
		$(window).resize(function() {
			if (timeOutValue == doResizeTimeout) {
				timeOutValue = doResizeTimeout - 1
				startDate = new Date();
				setTimeout(doResize, doResizeTimeout);
			} else {
				// 重设开始时间
				startDate = new Date();
			}
		});

		function doResize() {
			var endDate = new Date();
			if (endDate.getTime() - startDate.getTime() > doResizeTimeout) {
				$(".table-body-scroll").each(function() {
					resetTableBodyScroll($(this));
				});

				timeOutValue = doResizeTimeout
			} else {
				setTimeout(doResize, doResizeTimeout);
			}
		}

		function resetTableBodyScroll(jqueryTableObj) {
			var width = jqueryTableObj.width();

			var tHead = jqueryTableObj.find('thead');
			tHead.css("display", "block");

			var tBody = jqueryTableObj.find('tbody');
			tBody.css("display", "block");
			tBody.css("overflow-y", "auto");
			tBody.css("overflow-x", "hidden");
			// 设置表格最大高度
			var maxHeght = tBody.css("max-height");
			if (maxHeght == null || maxHeght == "" || maxHeght == "none") {
				tBody.css("max-height", "400px");
			}
			// 设置英文单词强制换行
			var wordBreak = tBody.css("word-break");
			if (wordBreak == null || wordBreak == "" || wordBreak == "none") {
				tBody.css("word-break", "break-all");
			}

			var bodyTd1 = tBody.find('td:eq(0)');
			var bodyTd2 = tBody.find('td:eq(1)');
			var bodyTd3 = tBody.find('td:eq(2)');
			bodyTd1.width(width * 0.3);
			bodyTd2.width(width * 0.4);
			bodyTd3.width(width * 0.3);

			tHead.find('td:eq(0)').width(bodyTd1.width());
			tHead.find('td:eq(1)').width(bodyTd2.width());
			tHead.find('td:eq(2)').width(bodyTd3.width());
		}
	}
});

// 百度分享代码
lazyRun(function() {
	var sslFlag = document.location.protocol == "https:" ? true : false; // 当前分享代码不支持https
	var bdsharebuttonbox = $(".bdsharebuttonbox");
	if (!sslFlag && bdsharebuttonbox.length > 0) {
		window._bd_share_config = {
			"common" : {
				"bdSnsKey" : {},
				// "bdText" : "bdText",
				// "bdDesc" : "bdDesc",
				// "bdUrl" : "bdUrl",
				// "bdPic" : "",
				"bdMini" : "3",
				"bdMiniList" : false,
				"bdPic" : "",
				"bdStyle" : "0",
				"bdSize" : "16"
			},
			"share" : {}
		/*,
					"image" : {
						"viewList" : [ "tieba", "sqq", "weixin", "bdxc", "duitang", "qzone", "tsina", "tqq", "renren", "copy", "evernotecn" ],
						"viewText" : "分享到：",
						"viewSize" : "16"
					}
				,
				"selectShare" : {
					"bdContainerClass" : null,
					"bdSelectMiniList" : [ "tieba", "sqq", "weixin", "bdxc", "duitang", "qzone", "tsina", "tqq", "renren", "copy", "evernotecn" ]
				}*/
		};

		var srcStr = '//bdimg.share.baidu.com/static/api/js/share.js?v=89860593.js?cdnversion=' + ~(-new Date() / 36e5);
		with (document)
			0[(getElementsByTagName('head')[0] || body).appendChild(createElement('script')).src = srcStr];

		$(".quickShareDivClass").show();
	} else {
		$(".quickShareDivClass").hide();
	}
}, 300);

/** herf标签实现form提交 */
lazyRun(function() {
	$(".postByFrom").on("click", function(event) {
		var formTag = $(this).closest("form");
		if (formTag != null) {
			event.preventDefault();// 使a自带的方法失效
			formTag.attr("action", this.href);
			formTag.submit();
		}
	});
}, 100);

// 以下代码部分改为直接运行，而不是加载后运行
// 百度收录推送
if (canBaiduIndex) {
	lazyRun(function() {
		(function() {
			var bp = document.createElement('script');
			var curProtocol = window.location.protocol.split(':')[0];
			if (curProtocol == 'https') {
				bp.src = '//zz.bdstatic.com/linksubmit/push.js';
			} else {
				bp.src = '//push.zhanzhang.baidu.com/push.js';
			}
			var s = document.getElementsByTagName("script")[0];
			s.parentNode.insertBefore(bp, s);
		})();
	}, 100);
}

// 360收录推送
if (canBaiduIndex) {
	lazyRun(function() {
		(function() {
			// var src = document.location.protocol + '//js.passport.qihucdn.com/11.0.1.js?a1d5ba23049ed1de4d6a6aa4db2557c6';
			// document.write('<script src="' + src + '" id="sozz"><\/script>');

			/*
			// 改为加载后运行
			var script = document.createElement('script');
			script.src = document.location.protocol + '//js.passport.qihucdn.com/11.0.1.js?a1d5ba23049ed1de4d6a6aa4db2557c6';
			var firstScript = document.getElementsByTagName("script")[0];
			firstScript.parentNode.insertBefore(script, firstScript);
			*/
		})();
	}, 100);
}

if (canBaiduRecord) {
	// 百度统计
	lazyRun(function() {
		var _hmt = _hmt || [];
		(function() {
			var hm = document.createElement("script");
			hm.src = "//hm.baidu.com/hm.js?292dc181c5dbc431b3ded9d841c0920e";
			var s = document.getElementsByTagName("script")[0];
			s.parentNode.insertBefore(hm, s);
		})();
	}, 100);

	// 腾讯分析
	/*
	 * (function() { //var src = '<script type="text/javascript" src="//tajs.qq.com/stats?sId=56001858" charset="UTF-8"></script>'; //document.write(src); })();
	 */

	// 站长统计
	(function() {
		/*
		var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");
		document.write(unescape("%3Cspan id='cnzz_stat_icon_1259030003'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol
				+ "s95.cnzz.com/z_stat.php%3Fid%3D1259030003%26online%3D1%26show%3Dline' type='text/javascript'%3E%3C/script%3E"));
		*/

		lazyRun(function() {
			// 改为加载后运行
			loadScript("//s95.cnzz.com/z_stat.php?id=1259030003&web_id=1259030003");

			// 隐藏站长统计
			if (!IS_MASTER) {
				$("#cnzz_stat_icon_1259030003").attr("style", "display:none")
				setTimeout(function() {
					// $("#cnzz_stat_icon_1259030003").remove();
				}, 5000);
			}

			// 声明_czc对象:
			var _czc = _czc || [];
			// 绑定siteid，请用您的siteid替换下方"XXXXXXXX"部分
			_czc.push([ "_setAccount", "1259030003" ]);
		}, 100);
	})();

	// 360分析
	/*
	lazyRun(function() {
		(function() {
			var script360 = document.createElement("script");
			script360.src = "//s.union.360.cn/67006.js";
			var scriptNode = document.getElementsByTagName("script")[0];
			scriptNode.parentNode.insertBefore(script360, scriptNode);
		})();
	}, 100);
	*/
}

/**
 * 语言菜单初始化操作
 */
lazyRun(function() {
	// 加载amazonmenu的js和css
	loadStyles(global.ctx + "/static/plugins/menu/amazonmenu/amazonmenu.css");
	loadScript(global.ctx + "/static/plugins/menu/amazonmenu/amazonmenu.js", function() {
		// 加载语言菜单插件
		$(".amazonmenu").each(function() {
			amazonmenu.init({
				// menuid : this.id
				menuSelector : $(this)
			})
		});
	});

	// 语言菜单点击事件
	{
		var tranLanColorPicker = null;
		var languageSetupMenuInitFlag = false;
		$("#languageSetupMenu").on("click", function() {
			// 获得列表成功，并且还没有初始化过
			if (!languageSetupMenuInitFlag) {
				// 展示翻译语言
				{
					getTranLanList(function(result) {
						languageSetupMenuInitFlag = true;
						$("#languageSetupDropdownMenuLoadingLabel").remove();

						var timeout = 0;
						var nowTranLan = result.data.nowTranLan;
						result.data.tranLanList.forEach(function(val, index) {
							timeout = timeout + 50;
							setTimeout(function() {
								var paramsTemplate = {};
								paramsTemplate.lanValue = val.value;
								paramsTemplate.lanName = val.name;
								paramsTemplate.lanOriginalName = val.originalName;
								if (val.selectedFlag) {
									if (val.value == "notTranFlag") {
										paramsTemplate.selectedText = "✔";
									} else {
										paramsTemplate.selectedText = "●";
									}
									paramsTemplate.selectedCss = "menuSelected";
								}
								var htmlStr = $("#languageSetupDropdownMenuLiTpl").render(paramsTemplate);
								// $(dropdownMenu.lastChild).after(buttonHtml);
								// $(dropdownMenu.lastChild).append(htmlStr);
								$("#tranLanListPostion").before(htmlStr);
							}, timeout);
						});

						// 将获取到的颜色填入调色板span
						var tranLanColor = result.data.tranLanColor;
						if (tranLanColor != null && tranLanColor != "") {
							$("#tranLanColorSpan").text(tranLanColor);
							$("#tranLanColorSpan").css("color", tranLanColor);
							if (tranLanColorPicker != null) {
								tranLanColorPicker.setColor(tranLanColor);
							}
						}
					});
				}

				// 展示翻译颜色
				{
					// 加载翻译颜色调色板
					loadStyles(global.ctx + "/static/plugins/panel/farbtastic/farbtastic.css");
					loadScript(global.ctx + "/static/plugins/panel/farbtastic/farbtastic.js", function() {
						// 初期化调色板
						tranLanColorPicker = $.farbtastic('#tranLanColorpicker', function(color) {
							console.log(color);
							// changeTranLanColor(color);
							$(".subtitleTranslatedText").css("color", color);

							$("#tranLanColorSpan").text(color);
							$("#tranLanColorSpan").css("color", color);
						});

						// 将颜色设定到调色板上
						if ($("#tranLanColorSpan").text() != null && $("#tranLanColorSpan").text() != "") {
							tranLanColorPicker.setColor($("#tranLanColorSpan").text());
						}
					});
				}

				// 展示翻译字体大小
				$(".tranLanFontsizeLi a").each(function() {
					$(this).text($(this).css("font-size"));
				});
			}
		});
	}

	/**
	 * 获得网站语言
	 */
	function getTranLanList(successCallback) {
		param = {};
		$.homePost("/tool/getTranLanList", param, function(result) {
			if (result.success) {
				if (successCallback) {
					successCallback(result);
				}
			} else {
				$.showMessageModal(data.message);
			}
		});
	}
}, 500)