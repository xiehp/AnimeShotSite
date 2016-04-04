$(function() {
	$("img.imageLazy").show().lazyload({
		placeholder : global.ctx + "/static/img/imageLoading_mini.jpg",
		effect : "fadeIn"
	});
});
