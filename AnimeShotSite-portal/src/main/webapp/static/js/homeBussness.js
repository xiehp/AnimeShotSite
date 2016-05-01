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
}

var home = new HomeFuction();
