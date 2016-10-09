function HomeFuction() {
	function publicLike1(id) {
		publicLike2(id, "#publicLike_" + id);
	}

	function publicLike2(id, jqueryCountId) {
		var param = {};
		param.id = id;
		$.homePost("/shot/publicLike", param, function(data) {
			$(jqueryCountId).text(data.newCount);
		});
	}

	this.publicLike = function() {
		if (arguments.length == 1) {
			publicLike1(arguments[0]);
		} else if (arguments.length == 2) {
			publicLike2(arguments[0], arguments[1]);
		} else {
			console.warn("publicLike arguments error.");
		}
	}

	function masterLike1(url, id) {
		masterLike2(url, id, "#publicLike_" + id, "#masterLike_" + id);
	}

	function masterLike2(url, id, jqueryLikeCountId, jqueryCommandCountId, callback) {
		var param = {};
		param.id = id;
		$.homePost(url, param, function(data) {
			if (data) {
				$(jqueryLikeCountId).text(data.newPublicCount);
				$(jqueryCommandCountId).text(data.newMasterCount);
			}
		});
	}

	this.masterLike = function() {
		if (arguments.length == 2) {
			masterLike1(arguments[0], arguments[1]);
		} else if (arguments.length > 2) {
			masterLike2(arguments[0], arguments[1], arguments[2], arguments[3]);
		} else {
			console.warn("masterLike arguments error.");
		}
	}

	this.setAnimeTitleImage = function(url, animeInfoId, animeEpisodeId, animeShotId) {
		var param = {};
		param.animeInfoId = animeInfoId;
		param.animeEpisodeId = animeEpisodeId;
		param.animeShotId = animeShotId;

		$.homePost(url, param, function(data) {
			if (data) {
				$.showMessageModal("更新成功");
			}
		});
	}
}

var home = new HomeFuction();

/**
 * 切换网站语言
 */
function changeLanguage(language) {
	param = {};
	param.new_lang = language;
	$.homePost("/tool/changeLanguage", param, function(data) {
		if (data.hashMap.success) {
			$.showMessageModal(data.hashMap.message);
			window.location.reload();
		} else {
			$.showMessageModal(data.hashMap.message);
		}
	});
}

/**
 * 切换是否显示所有字幕
 */
function changeShowAllSubtitle() {
	var SHOW_ALL_SUBTITLE_FLAG = HomeCookie.getCookie("SHOW_ALL_SUBTITLE_FLAG");
	if (SHOW_ALL_SUBTITLE_FLAG == 1) {
		HomeCookie.setCookie("SHOW_ALL_SUBTITLE_FLAG", 0);
	} else {
		HomeCookie.setCookie("SHOW_ALL_SUBTITLE_FLAG", 1);
	}
	window.location.reload();
}
