<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<title>动画列表一览 - 动画截图网</title>

<script>
	var timeMark = 0;
	var $animeList;
	var $searchAnimeName;
	lazyRun(function() {
		$animeList = $(".animeInfoDiv");
		$searchAnimeName = $(".enter-search-event");

		$searchAnimeName.on("input", function() {
			timeMark = new Date().getTime();
			var newTimeMark = timeMark;
			setTimeout(function() {
				doSearch(newTimeMark);
			}, 500);
		});
	});

	function doSearch(newTimeMark) {
		if (timeMark != newTimeMark) {
			return;
		}

		var searchAnimeName = $searchAnimeName.val().toLocaleLowerCase();

		$animeList.each(function() {
			var $this = $(this);
			var thisAnimeName = $this.text().toLocaleLowerCase();
			for (i = 0; i < searchAnimeName.length; i++) {
				if (thisAnimeName.indexOf(searchAnimeName.charAt(i)) == -1) {
					$this.hide();
					return;
				}
			}
			$this.show();
		});

		//$("body").trigger("scroll");
		$("body").scroll();
	}
</script>
<div class="container-fluid">
	<div class="row-fluid">
		<div class="blockTitle row">
			<div class="col-sm-3">
				<span><spring:message code='动画列表一览' /></span>
			</div>
			<div class="col-sm-6">
				<span style="font-size: smaller;"><spring:message code='请输入要搜索的动画名' /></span>
				<input class="input-sm enter-search-event">
			</div>
		</div>
		<div class="row">
			<c:forEach items="${ animeInfoPage.content }" var="anime">
				<div class="col-lg-3 col-sm-4 col-xs-6 thumbnail animeInfoDiv">
					<a href="${ctx}/episode/list/${anime.id}" title="<c:out value='${anime.fullName}' /> <c:out value='${anime.secondName}' />">
						<img src="${ctx}/static/img/imageLoading_mini.jpg" data-original="${anime.titleUrl.urlS}" class="img-responsive imagelazy">
						<div class="wordKeepLine" style="margin-top: 5px;">
							<c:out value='${anime.fullName}' />
							<c:out value='${anime.secondName}' />
						</div>
					</a>
				</div>
			</c:forEach>
		</div>
	</div>
</div>

<div>
	<tags:paginationRestPage page="${animeInfoPage}" paginationSize="5" />
</div>

