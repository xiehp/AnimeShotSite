<%@ page language="java" contentType="text/html; UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="ctxManage" value="${ctx}${MANAGE_URL_STR}" />

<div class="mip-nav-wrapper">
	<mip-nav-slidedown data-id="bs-navbar" class="mip-element-sidebar container" data-showbrand="1" data-brandhref="${siteBaseUrl}/mip" data-brandname="<spring:message code='动画截图网' />">
	<nav id="bs-navbar" class="navbar-collapse collapse navbar navbar-static-top">
		<ul class="nav navbar-nav navbar-right">
			<li>
				<a href="${siteBaseUrl}/mip">
					<spring:message code='首页' />
				</a>
				<hr class="hr-xs">
			</li>
			<li class="doc-body">
				<a href="${siteBaseUrl}/mip/anime">
					<spring:message code='动画列表' />
				</a>
				<hr class="hr-xs">
			</li>
			<li class="timeline-body">
				<a href="${siteBaseUrl}/mip/shot/recommend">
					<spring:message code='截图推荐' />
				</a>
			</li>
			<li class="">
				<a href="${siteBaseUrl}/mip/gif/list">
					<spring:message code='动态图片gif' />
				</a>
				<hr class="hr-xs">
			</li>
			<li>
				<a href="${siteBaseUrl}/mip/search">
					<spring:message code='字幕搜索' />
				</a>
				<hr class="hr-xs">
			</li>
			<li>
				<a href="${siteBaseUrl}/mip/shot/random">
					<spring:message code='随便看' />
				</a>
				<hr class="hr-xs">
			</li>
			<li>
				<a href="${thisPageOriginalUrl}">
					<spring:message code='切换到原始页面' />
				</a>
				<hr class="hr-xs">
			</li>
			<li class="languageChangeLi" >
				<table>
					<tr>
						<td >
							<a href="#">Language</a>
						</td>
						<td>
							<ul class="languageChangeLink">
								<!-- <li>
							<a href="${siteBaseUrl}/tool/changeShowAllSubtitle">
								<spring:message code='显示所有字幕' />✔
								${showAllSubtitleFlag eq '1' ? ' ✔' : ''}
							</a>
							</li> -->
								<li>
									<a href="${siteBaseUrl}/tool/changeLanguage/zh_CN">简体中文 ${localeLanguage eq 'zh_cn' ? ' ●' : ''}</a>
								</li>
								<li>
									<a href="${siteBaseUrl}/tool/changeLanguage/zh_TW">繁體中文 ${localeLanguage eq 'zh_tw' ? ' ●' : ''}</a>
								</li>
								<li>
									<a href="${siteBaseUrl}/tool/changeLanguage/ja">日本語 ${localeLanguage eq 'ja' ? ' ●' : ''}</a>
								</li>
								<li>
									<a href="${siteBaseUrl}/tool/changeLanguage/en_US">English ${localeLanguage eq 'en_us' ? ' ●' : ''}</a>
								</li>
								<li>
									<a href="${siteBaseUrl}/tool/changeLanguage/ar">عربي ${localeLanguage eq 'ar' ? ' ●' : ''}</a>
								</li>
								<li>
									<a href="${siteBaseUrl}/tool/changeLanguage/clear">
										<spring:message code='清除' />
									</a>
								</li>
							</ul>
						</td>
					</tr>
				</table>

				<hr class="hr-xs">
			</li>

			<li></li>


			<li class="navbar-wise-close">
				<span id="navbar-wise-close-btn"></span>
			</li>
		</ul>
	</nav>
	</mip-nav-slidedown>
</div>