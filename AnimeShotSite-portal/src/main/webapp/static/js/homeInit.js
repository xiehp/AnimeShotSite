(function() {
	// 图片懒加载
	var imageLazyImg = $("img.imagelazy");
	imageLazyImg.show();
	imageLazyImg.lazyload({
		placeholder : global.ctx + "/static/img/imageLoading_mini.jpg",
		effect : "fadeIn"
	});

	function subWidth(width, subWidth) {
		if (subWidth == null) {
			return width;
		}

		return width - parseFloat(subWidth);
	}

	function resizeImg(imageLazyImg) {
		if (imageLazyImg != null && imageLazyImg.length > 0) {
			divParent = imageLazyImg.parents("div.thumbnail");
			if (divParent != null & divParent.length > 0) {
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

						// if (divWidth >= 300) {
						// // 300为缩略图宽度
						// divWidth = 300;
						// maxHeight = 168;
						// }

						// imageLazyImg.css("max-height", maxHeight);
						imageLazyImg.css("height", maxHeight);
					}
				}
			}
		}
	}

	resizeImg(imageLazyImg);
	$(window).resize(function() {
		resizeImg(imageLazyImg);
	});
})();

$(function() {
	// 隐藏站长统计
	if (!IS_MASTER) {
		$("#cnzz_stat_icon_1259030003").attr("style", "display:none")
	}
});

$(function() {
	/** herf标签实现form提交 */
	$(".postByFrom").on("click", function(event) {
		var formTag = $(this).closest("form");
		if (formTag != null) {
			event.preventDefault();// 使a自带的方法失效
			formTag.attr("action", this.href);
			formTag.submit();
		}
	});
});

if (canBaiduRecord) {
	// 百度收录推送
	if (canBaiduIndex) {
		(function() {
			var bp = document.createElement('script');
			var curProtocol = window.location.protocol.split(':')[0];
			if (curProtocol === 'https') {
				bp.src = 'https://zz.bdstatic.com/linksubmit/push.js';
			} else {
				bp.src = 'http://push.zhanzhang.baidu.com/push.js';
			}
			var s = document.getElementsByTagName("script")[0];
			s.parentNode.insertBefore(bp, s);
		})();
	}

	// 百度统计
	var _hmt = _hmt || [];
	(function() {
		var hm = document.createElement("script");
		hm.src = "//hm.baidu.com/hm.js?292dc181c5dbc431b3ded9d841c0920e";
		var s = document.getElementsByTagName("script")[0];
		s.parentNode.insertBefore(hm, s);
	})();

	// 360收录推送
	if (canBaiduIndex) {
		(function() {
			var src = document.location.protocol + '//js.passport.qihucdn.com/11.0.1.js?a1d5ba23049ed1de4d6a6aa4db2557c6';
			document.write('<script src="' + src + '" id="sozz"><\/script>');
		})();
	}

	// 腾讯分析
	/*
	 * (function() { //var src = '<script type="text/javascript"
	 * src="http://tajs.qq.com/stats?sId=56001858" charset="UTF-8"></script>';
	 * //document.write(src); })();
	 */

	// 站长统计
	(function() {
		var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");
		document.write(unescape("%3Cspan id='cnzz_stat_icon_1259030003'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol
				+ "s95.cnzz.com/z_stat.php%3Fid%3D1259030003%26online%3D1%26show%3Dline' type='text/javascript'%3E%3C/script%3E"));

	})();
}