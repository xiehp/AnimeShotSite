<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<head>
<meta name="keywords" content="动画截图网,字幕搜索,截图搜索,截图字幕,<c:out value='${name}' />,<c:out value='${keyword}' />" />
<title>动画字幕台词截图搜索 <c:out value='${name}' /> <c:out value='${keyword}' /> ${searchPageNumber} - 动画截图网
</title>
<style>
.blockTitle {
	margin-top: 10px;
	margin-left: 10px;
	margin-bottom: 10px;
	font-size: 21px;
	text-align: left;
}
</style>
</head>

<script>
	function searchKeyword() {
		var animeName = document.getElementById("animeName").value;
		var keyword = document.getElementById("keyword").value;

		if (animeName == "" && keyword == "") {
			document.location.href = "${requestURI}";
		} else {
			var encodeAnimeName = encodeURIComponent(animeName);
			var encodeKeyword = encodeURIComponent(keyword);
			document.location.href = "?name=" + encodeAnimeName + "&keyword=" + encodeKeyword;
		}
	}
	function onkeydownEvent(event) {
		var e = event || window.event || arguments.callee.caller.arguments[0];
		if (e && e.keyCode == 13) { // enter 键
			searchKeyword();
		}
	};

	$(function() {
		$(".enter-search-event").keydown(function(event) {
			var e = event || window.event || arguments.callee.caller.arguments[0];
			if (e && e.keyCode == 13) { // enter 键
				searchKeyword();
			}
		});

		function markTextWithArray(fullText, arrayWord) {
			// 编码替换文本的html字符
			fullText = fullText + "";
			fullText = $.escapeHtml(fullText);

			var markedFullText = fullText;
			var regStr = "";
			for ( var x in arrayWord) {
				//markedFullText = markTextWithOneWord(markedFullText, arrayWord[x]);
				var oneWord = arrayWord[x];

				if (oneWord == null || oneWord.trim() == "") {
					continue;
				}
				// 编码高亮文字的html字符和正则字符
				var encodeWord = oneWord.trim();
				encodeWord = $.escapeHtml(encodeWord + "");
				encodeWord = $.replaceExp(encodeWord);

				regStr = regStr == "" ? encodeWord : regStr + "|" + encodeWord;
			}

			if (regStr == null || regStr.trim() == "") {
				return fullText;
			}

			// 正则匹配
			var regExp = new RegExp("(" + regStr + ")", "gi");
			var replaceText = fullText.replace(regExp, "<mark>$1</mark>");
			return replaceText;
		}
		function markTextWithOneWord(fullText, oneWord) {
			// 编码替换文本的html字符
			fullText = fullText + "";
			fullText = $.escapeHtml(fullText);

			if (oneWord == null || oneWord.trim() == "") {
				return fullText;
			}

			// 编码高亮文字的html字符和正则字符
			var encodeWord = oneWord.trim();
			encodeWord = $.escapeHtml(encodeWord + "");
			encodeWord = $.replaceExp(encodeWord);
			var encodeWordArray = encodeWord.split(" ");

			// 编码替换文本的html字符
			var fullText = fullText + "";
			fullText = $.escapeHtml(fullText);

			// 正则匹配
			var regExp = new RegExp("(" + encodeWord + ")", "gi");
			var replaceText = fullText.replace(regExp, "<mark>$1</mark>");
			return replaceText;
		}

		// 编码html和正则字符
		var keywordHidden = $("#keywordHidden").val();
		var keywordArray = keywordHidden.split(" ");
		var animeNameHidden = $("#animeNameHidden").val();
		var animeNameArray = animeNameHidden.split(" ");

		// 高亮文字
		$(".textLightMark").each(function() {
			var text = $(this).text() + "";
			$(this).html(markTextWithArray(text, keywordArray));
		});

		$(".animeNameLightMark").each(function() {
			var text = $(this).text() + "";
			$(this).html(markTextWithArray(text, animeNameArray));
		});
	});
</script>
<div>
	<!-- 搜索框 -->
	<div class="row">
		<div class="col-sm-12" style="margin-bottom: 20px;">
			<span style="font-size: 20px;">请输入要搜索的动画名</span>
			<input class="input-lg enter-search-event" id="animeName" name="animeName" value="<c:out value='${name}' />">
			<input type="hidden" id="animeNameHidden" value="<c:out value='${name}' />">
		</div>
		<div class="col-sm-12" style="margin-bottom: 20px;">
			<span style="font-size: 20px;">请输入要搜索的字幕</span>
			<input class="input-lg enter-search-event" id="keyword" name="keyword" value="<c:out value='${keyword}' />">
			<input type="hidden" id="keywordHidden" value="<c:out value='${keyword}' />">
			<a herf="javascript:void(0);" class="btn btn-lg btn-primary" onclick="searchKeyword();">搜索</a>
		</div>
	</div>

	<!-- 标题 -->
	<div align="center" class="blockTitle" style="margin-bottom: 20px;">
		<h1 style="font-size: 25px;">
			<c:if test="${!empty subtitleLinePage and subtitleLinePage.totalElements > 0}">
				<c:out value='${name}' />
				<c:out value='${keyword}' />
				搜索结果
				<small>第${subtitleLinePage.number + 1}/${subtitleLinePage.totalPages}页</small>
			</c:if>
		</h1>
	</div>

	<!-- 搜索结果 -->
	<c:if test="${subtitleLinePage.totalPages == 0 and ! empty keyword}">
		<div>
			<span style="font-size: 20px;">没有搜索到任何结果 -_-||</span>
		</div>
	</c:if>

	<c:if test="${subtitleLinePage.totalPages > 0 }">
		<div class="row">
			<c:forEach items="${subtitleLinePageXXXXXXX.content}" var="subtitleLine">
				<!-- 此处代码暂时废弃 -->
				<c:set var="shotFullName" value="${subtitleLine.animeEpisode.fullName} ${shotInfoMap[subtitleLine.id].formatedTimeChina}" />
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 20px">
					<div align="center" class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
						<a target="_blank" href="${ctx}/shot/view/${shotInfoMap[subtitleLine.id].id}">
							<img style="max-height: 200px; display: inline;" class="img-responsive imagelazy" data-original="${shotInfoMap[subtitleLine.id].urlS}" alt="<c:out value="${shotFullName}" />">
						</a>
					</div>
					<div align="left" class="col-lg-8 col-md-8 col-sm-12 col-xs-12">
						<div>
							<a target="_blank" href="${ctx}/shot/view/${shotInfoMap[subtitleLine.id].id}">
								<c:out value="${shotFullName}" />
							</a>
						</div>
						<div>
							${subtitleLine.startTimeMinSecMicro} ${subtitleLine.endTimeMinSecMicro} <span class="textLightMark"><c:out value='${subtitleLine.text}' /></span>
						</div>
					</div>
				</div>
			</c:forEach>
			<c:forEach items="${searchResultMap}" var="searchResult">
				<c:set var="shotInfoData" value="${searchResult.value.shotInfoData}" />
				<c:set var="subtitleListData" value="${searchResult.value.subtitleListData}" />
				<c:set var="shotInfoUrl" value="${ctx}/shot/view/${shotInfoData.id}" />
				<c:set var="episodeUrl" value="${ctx}/shot/list/${subtitleListData[0].animeEpisode.id}" />
				<c:set var="shotInfoFullName" value="${subtitleListData[0].animeEpisode.fullName} ${shotInfoData.formatedTimeChina}" />
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 20px">
					<div align="center" class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
						<c:if test="${!empty shotInfoData}">
							<a target="_blank" href="${shotInfoUrl}">
								<img style="max-height: 200px; display: inline;" class="img-responsive imagelazy" data-original="${shotInfoData.urlS}" alt="<c:out value="${shotInfoFullName}" />">
							</a>
						</c:if>
					</div>
					<div align="left" class="col-lg-8 col-md-8 col-sm-12 col-xs-12">
						<div style="padding-top: 5px;">
							<c:if test="${!empty shotInfoData}">
								<a target="_blank" href="${shotInfoUrl}">
									<span class="animeNameLightMark"><c:out value="${shotInfoFullName}" /></span>
								</a>
							</c:if>
							<c:if test="${empty shotInfoData}">
								<a target="_blank" href="${episodeUrl}">
									<span class="animeNameLightMark"><c:out value="${shotInfoFullName}" /></span>
								</a>
							</c:if>
						</div>
						<c:forEach items="${subtitleListData}" var="subtitle">
							<div>
								${subtitle.startTimeMinSecMicro} ${subtitle.endTimeMinSecMicro} <span class="textLightMark"><c:out value='${subtitle.text}' /></span>
							</div>
						</c:forEach>
					</div>
				</div>
			</c:forEach>
		</div>
	</c:if>

	<c:if test="${subtitleLinePage.totalPages > 0 }">
		<div>
			<tags:paginationKeyword page="${subtitleLinePage}" paginationSize="6" />
		</div>
	</c:if>
</div>


