<%@ page language="java" contentType="text/html; UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="ctxManage" value="${ctx}${MANAGE_URL_STR}" />

<div id="nav">
	<nav class="navbar navbar-inverse" role="navigation">
		<div class="navbar-header">
			<a class="navbar-brand" href="${ctx}/">
				<spring:message code='动画截图网' />
			</a>
		</div>

		<div>
			<ul class="nav navbar-nav">
				<li class="active">
					<a href="${ctx}/">
						<spring:message code='首页' />
					</a>
				</li>
				<li>
					<a href="${ctx}/anime/list">
						<spring:message code='动画列表' />
					</a>
				</li>
				<li>
					<a href="${ctx}/shot/recommend">
						<spring:message code='截图推荐' />
					</a>
				</li>
				<li>
					<a href="${ctx}/gif/list">
						<spring:message code='动态图片gif' />
					</a>
				</li>
				<li>
					<a href="${ctx}/search">
						<spring:message code='字幕搜索' />
					</a>
				</li>
				<li>
					<a href="${ctx}/shot/random">
						<spring:message code='随便看' />
					</a>
				</li>
				<li>
					<a href="${ctx}/maCalc">
						<spring:message code='Ma伤害计算器' />
					</a>
				</li>
				<li class="dropdown">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown">
						Language<b class="caret"></b>
					</a>
					<ul class="dropdown-menu">
						<li>
							<a href="javascript:void(0);" onclick="changeLanguage('zh_CN');">简体中文 ${localeLanguage eq 'zh_cn' ? '●' : ''}</a>
						</li>
						<li>
							<a href="javascript:void(0);" onclick="changeLanguage('zh_TW');">繁體中文 ${localeLanguage eq 'zh_tw' ? '●' : ''}</a>
						</li>
						<li>
							<a href="javascript:void(0);" onclick="changeLanguage('ja');">日本語 ${localeLanguage eq 'ja' ? '●' : ''}</a>
						</li>
						<li>
							<a href="javascript:void(0);" onclick="changeLanguage('en_US');">English ${localeLanguage eq 'en_us' ? '●' : ''}</a>
						</li>
						<li>
							<a href="javascript:void(0);" onclick="changeLanguage('ar');">عربي ${localeLanguage eq 'ar' ? '●' : ''}</a>
						</li>
						<li>
							<a href="javascript:void(0);" onclick="changeLanguage('');">
								<spring:message code='清除' />
							</a>
						</li>
						<li>
							<hr style="margin: 0px; border-top-width: 3px;">
						</li>
						<li>
							<a href="javascript:void(0);" onclick="changeShowAllSubtitle();">
								<spring:message code='显示所有字幕' />
								${showAllSubtitleFlag eq '1' ? '●' : ''}
							</a>
						</li>
					</ul>
				</li>
				<c:if test="${IS_MASTER}">
				<li class="dropdown">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown">
						后台管理<b class="caret"></b>
					</a>
					<ul class="dropdown-menu">
						<li>
							<a href="javascript:void(0);" onclick="clearCache();">清除缓存</a>
							<script>
								function clearCache() {
									if (confirm("是否清除缓存")) {
										$.homePost("/tool/cleanCache", null, function(data) {
											if (data && data.size) {
												alert("清除缓存个数:" + data.size);
											}
										});
									}
								}
							</script>
						</li>
						<li>
							<a href="${ctxManage}/anime/list">
								<spring:message code='动画列表' />
							</a>
						</li>
					</ul>
				</li>
				</c:if>

				<c:if test="${! empty aaa  }">
					<li class="dropdown">
						<a href="#" class="dropdown-toggle" data-toggle="dropdown">
							热门标签 <b class="caret"></b>
						</a>
						<ul class="dropdown-menu">
							<li>
								<a href="#">人物</a>
							</li>
							<li>
								<a href="#">风景</a>
							</li>
							<li>
								<a href="#">道具</a>
							</li>

							<li class="divider"></li>
							<li>
								<a href="#">关系</a>
							</li>

							<li class="divider"></li>
							<li>
								<a href="#">其他</a>
							</li>
						</ul>
					</li>
					<li>
						<a href="#">截图对比</a>
					</li>
					<li>
						<a href="#">gif</a>
					</li>
					<li>
						<a href="#">我的收藏</a>
					</li>
					<li>
						<a href="#">转化进程</a>
					</li>
				</c:if>
			</ul>
		</div>
	</nav>
</div>