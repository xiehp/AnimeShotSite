<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="xie.animeshotsite.db.entity.AnimeInfo"%>
<%@page import="org.springframework.data.domain.Page"%>
<%@page import="com.hankcs.hanlp.HanLP"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<title>动画列表一览 - 动画截图网</title>

<script>
	var startTime = 0;
	var timeMark = 0;
	var $animeList;
	var $searchAnimeName;
	lazyRun(function() {
		$animeList = $(".animeInfoDiv");
		$searchAnimeName = $(".enter-search-event");

		$searchAnimeName.on("input", function() {
			timeMark = new Date().getTime();
			var newTimeMark = timeMark;
			if (startTime == 0) {
				startTime = timeMark;
			}
			setTimeout(function() {
				doSearch(newTimeMark);
			}, 500);
		});
	});

	function doSearch(newTimeMark) {
		if (timeMark != newTimeMark) {
			return;
		}

		var searchAnimeNameS = $searchAnimeName.val();
		var searchAnimeName = searchAnimeNameS.toLocaleLowerCase();

		$animeList.each(function() {
			var $this = $(this);

			// 拼音检查，整个输入是否包含在拼音名字中存在
			var thisAnimeNamePinyin = $this.find(".hiddenPinyin").val();
			if (thisAnimeNamePinyin.indexOf(searchAnimeName) > -1) {
				$this.show();
				return;
			}

			// 完整名字检查，输入的每个字是否存在于名字中
			var thisAnimeName = $this.text().toLocaleLowerCase();
			for (i = 0; i < searchAnimeName.length; i++) {
				var c = searchAnimeName.charAt(i);

				if (c == " " || c == "　") {
					continue;
				}

				if (thisAnimeName.indexOf(c) == -1) {
					$this.hide();
					return;
				}
			}
			$this.show();
		});

		//$("body").trigger("scroll");
		$("body").scroll();

		// 进行统计
		if (isDefinedVariable("_czc") && _czc != null) {
			_czc.push([ "_trackEvent", "动画", "搜索", searchAnimeNameS, timeMark - startTime, "enter-search-event" ]);
		}
		if (isDefinedVariable("_hmt") && _hmt != null) {
			_hmt.push([ '_trackEvent', '动画', '搜索', searchAnimeNameS, timeMark - startTime ]);
		}
		startTime = 0;
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
				<input id="enter-search-event" class="input-sm enter-search-event">
			</div>
		</div>
		<div class="row">
			<%
				Map<Integer, String> pinyinMap = new HashMap<>();

				Page<AnimeInfo> animeInfopage = (Page<AnimeInfo>) request.getAttribute("animeInfoPage");
				List<AnimeInfo> animeInfoList = animeInfopage.getContent();
				for (int i = 0; i < animeInfoList.size(); i++) {
					AnimeInfo info = animeInfoList.get(i);
					String name = info.getFullName() + " " + info.getSecondName();
					String pinyin = HanLP.convertToPinyinString(name, "", false);
					pinyinMap.put(i, pinyin);
				}

				request.setAttribute("pinyinMap", pinyinMap);
			%>
			<c:forEach items="${ animeInfoPage.content }" var="anime" varStatus="status">
				<div class="col-lg-3 col-sm-4 col-xs-6 thumbnail animeInfoDiv">
					<a href="${ctx}/episode/list/${anime.id}" title="<c:out value='${anime.fullName}' /> <c:out value='${anime.secondName}' />">
						<img src="${ctx}/static/img/imageLoading_mini.jpg" data-original="${anime.titleUrl.urlS}" class="img-responsive imagelazy">
						<div class="wordKeepLine" style="margin-top: 5px;">
							<c:out value='${anime.fullName}' />
							<c:out value='${anime.secondName}' />
							<input class="hiddenPinyin" type="hidden" value="<c:out value='${pinyinMap[status.index]}' />">
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

