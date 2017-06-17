<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<head>
<title>动画字幕台词截图搜索 <c:out value='${name}' /> <c:out value='${keyword}' /> ${searchPageNumber} - 动画截图网
</title>
<meta name="keywords" content="动画截图网,字幕搜索,截图搜索,截图字幕,<c:out value='${name}' />,<c:out value='${keyword}' />" />
</head>

<div>
	<!-- 搜索框 -->
	<mip-form url="${ctx}/search" method="get" clear>
		<!-- 不要提交参数，即使是空
		<div class="col-sm-12" style="margin-bottom: 20px;">
			<span style="font-size: 20px;"><spring:message code='请输入要搜索的动画名' /></span>
			<input class="input-lg enter-search-event" id="animeName" name="animeName" value="<c:out value='${name}' />">
			<input type="hidden" id="animeNameHidden" value="<c:out value='${nameHidden}' />">
		</div> 
		-->
		<span><spring:message code='输入要搜索的字幕台词' /></span>
		<input type="text" class="input-lg enter-search-event" id="keyword" name="keyword" value="<c:out value='${keyword}' />">
		<input type="hidden" id="keywordHidden" value="<c:out value='${keywordHidden}' />">
		<!-- 不要提交参数，即使是空
		<div class="col-sm-12" style="margin-bottom: 20px;">
			<div style="display: none;">
				<div style="width: 110px; display: inline-block;">
					<input data-on-text="<spring:message code='精确搜索' />" data-off-text="<spring:message code='模糊搜索' />" data-label-width="0" data-handle-width="60" style="display: none;" type="checkbox" id="searchMode" name="searchMode" class="bootstrap-switch-small" ${searchMode ? 'checked' : ''}>
				</div>
			</div>
		</div>
		-->
		<input type="submit" class="btn btn-lg btn-primary" value="<spring:message code='搜索' />" />
	</mip-form>

	<!-- 标题 -->
	<div class="blockTitle">
		<h1text-align:center;">
			<c:if test="${!empty subtitleLinePage and subtitleLinePage.totalElements > 0}">
				<c:out value='${name}' />
				<c:out value='${keyword}' />
				<spring:message code='搜索结果' />
				<small><spring:message code='第' />${subtitleLinePage.number + 1}/${subtitleLinePage.totalPages}<spring:message code='页' /></small>
			</c:if>
			</h1>
	</div>

	<!-- 搜索结果 -->
	<c:if test="${isSearchFlag && subtitleLinePage.totalPages == 0}">
		<div>
			<span><spring:message code='没有搜索到任何结果' /></span>
		</div>
	</c:if>

	<c:if test="${subtitleLinePage.totalPages > 0 }">
		<div>
			<c:forEach items="${subtitleLinePageXXXXXXX.content}" var="subtitleLine">
				<!-- 此处代码暂时废弃 -->
				<c:set var="shotFullName" value="${subtitleLine.animeEpisode.fullName} ${shotInfoMap[subtitleLine.id].formatedTimeChina}" />
				<div align="center" style="margin-bottom: 20px">
					<div align="center" class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
						<a target="_blank" href="${siteBaseUrl}/mip/shot/view/${shotInfoMap[subtitleLine.id].id}">
							<mip-img src="${shotInfoMap[subtitleLine.id].urlS}" style="max-height: 200px; display: inline;" class="img-responsive imagelazy" data-original="${shotInfoMap[subtitleLine.id].urlS}" alt="<c:out value="${shotFullName}" />"></mip-img>
						</a>
					</div>
					<div align="left" class="col-lg-8 col-md-8 col-sm-12 col-xs-12">
						<div>
							<a target="_blank" href="${siteBaseUrl}/mip/shot/view/${shotInfoMap[subtitleLine.id].id}">
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
				<c:set var="shotInfoUrl" value="${siteBaseUrl}/mip/shot/view/${shotInfoData.id}" />
				<c:set var="episodeUrl" value="${siteBaseUrl}/mip/shot/list/${subtitleListData[0].animeEpisode.id}" />
				<c:set var="shotInfoFullName" value="${subtitleListData[0].animeEpisode.fullName} ${shotInfoData.formatedTimeChina}" />
				<div align="center" class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 20px">
					<div align="center" class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
						<c:if test="${!empty shotInfoData}">
							<a target="_blank" href="${shotInfoUrl}">
								<mip-img src="${shotInfoData.urlS}" style="max-height: 200px; display: inline;" class="img-responsive imagelazy" data-original="${shotInfoData.urlS}" alt="<c:out value="${shotInfoFullName}" />"></mip-img>
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
			<tags:paginationKeyword page="${subtitleLinePage}" paginationSize="6" searchKey1="searchMode" />
		</div>
	</c:if>
</div>


