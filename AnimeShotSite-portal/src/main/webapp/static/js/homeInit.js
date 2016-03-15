$(function() {
	$("img.imageLazy").show().lazyload({
		placeholder : global.ctx + "/static/img/imageLoading.gif",
		effect : "fadeIn"
	});
});
