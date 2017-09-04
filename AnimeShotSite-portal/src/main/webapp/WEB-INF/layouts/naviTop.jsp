<%@ page language="java" contentType="text/html; UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<style>
<!--

.dropdown-menu .menuTitle a, .dropdown-menu .menuTitle a:FOCUS, .dropdown-menu .menuTitle a:HOVER, .dropdown-menu .menuTitle a:ACTIVE {
	font-weight: bold;
	cursor: default;
	background-color: gray;
	color: white;
}

.menuSelected {
	background-color: highlight;
}

.tranLanColorLi {
	width: 100px;
}

@media ( max-width : 768px) {
	.navbar-nav {
		margin: 5px !important;
	}
}
-->
</style>

<div id="nav">
	<nav class="navbar navbar-inverse" role="navigation">
		<div class="navbar-header">
			<a class="navbar-brand" href="${ctx}/">
				<spring:message code='动画截图网' />
			</a>
		</div>

		<div>
			<ul class="nav navbar-nav">
				<li class="activeXXX">
					<a href="${ctx}/">
						<spring:message code='首页' />
					</a>
				</li>
				<li>
					<a href="${ctx}/anime">
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
					<a href="${ctx}/msgboard">
						<spring:message code='留言板' />
					</a>
				</li>
				<!-- <li>
					<a href="${ctx}/maCalc">
						<spring:message code='Ma伤害计算器' />
					</a>
				</li> -->
				<li id="languageSetupMenu" class="dropdown">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown">
						Language<b class="caret"></b>
					</a>
					<ul class="dropdown-menu">
						<li>
							<nav class="amazonmenu">
								<ul>
									<li>
										<a href="javascript:void(0);" onclick="changeShowAllSubtitle();">
											<spring:message code='显示所有字幕' />
											${showAllSubtitleFlag eq '1' ? ' ✔' : ''}
										</a>
									</li>
									<li>
										<a href="javascript:void(0);" class="menuTitle"> language </a>
										<ul>
											<li>
												<a href="javascript:void(0);" onclick="changeLanguage('zh_CN');">简体中文 ${localeLanguage eq 'zh_cn' ? ' ●' : ''}</a>
											</li>
											<li>
												<a href="javascript:void(0);" onclick="changeLanguage('zh_TW');">繁體中文 ${localeLanguage eq 'zh_tw' ? ' ●' : ''}</a>
											</li>
											<li>
												<a href="javascript:void(0);" onclick="changeLanguage('ja');">日本語 ${localeLanguage eq 'ja' ? ' ●' : ''}</a>
											</li>
											<li>
												<a href="javascript:void(0);" onclick="changeLanguage('en_US');">English ${localeLanguage eq 'en_us' ? ' ●' : ''}</a>
											</li>
											<li>
												<a href="javascript:void(0);" onclick="changeLanguage('ar');">عربي ${localeLanguage eq 'ar' ? ' ●' : ''}</a>
											</li>
											<li>
												<a href="javascript:void(0);" onclick="changeLanguage('');">
													<spring:message code='清除' />
												</a>
											</li>
										</ul>
									</li>
									<li>
										<a href="javascript:void(0);" class="menuTitle">
											<spring:message code='字幕翻译' />
										</a>
										<ul>
											<li id="tranLanListPostion" style="display: none;"></li>
											<li id="languageSetupDropdownMenuLoadingLabel">
												<a href="javascript:void(0);">
													<spring:message code='Loding...' />
												</a>
											</li>
											<script id="languageSetupDropdownMenuLiTpl" type="text/x-jsrender">
											<li>
												<a class="littlePadding {{>selectedCss}}" href="javascript:void(0);" onclick="changeTranLan('{{>lanValue}}');">{{>lanOriginalName}} {{>selectedText}}</a>
											</li>
											</script>
										</ul>
									</li>
									<li>
										<a href="javascript:void(0);" class="menuTitle">
											<spring:message code='翻译颜色' />
										</a>
										<div data-clicknotclose="true" style="text-align: center;">
											<div id="tranLanColorpicker"></div>
											<span id="tranLanColorSpan" style="font-size: 30px;"></span>
											<a class="btn btn-primary btn-xs" onclick="changeTranLanColor($('#tranLanColorSpan').text());">OK</a>
										</div>
									</li>
									<li>
										<a href="javascript:void(0);" class="menuTitle">
											<spring:message code='翻译字体大小' />
										</a>
										<div>
											<ul>
												<li class="tranLanFontsizeLi">
													<a href="javascript:void(0);" style="font-size: 8px;" onclick="changeTranLanFontsize(this);">8</a>
												</li>
												<li class="tranLanFontsizeLi">
													<a href="javascript:void(0);" style="font-size: 10px;" onclick="changeTranLanFontsize(this);">10</a>
												</li>
												<li class="tranLanFontsizeLi">
													<a href="javascript:void(0);" style="font-size: 12px;" onclick="changeTranLanFontsize(this);">12</a>
												</li>
												<li class="tranLanFontsizeLi">
													<a href="javascript:void(0);" style="font-size: 14px;" onclick="changeTranLanFontsize(this);">14</a>
												</li>
												<li class="tranLanFontsizeLi">
													<a href="javascript:void(0);" style="font-size: 16px;" onclick="changeTranLanFontsize(this);">16</a>
												</li>
												<li class="tranLanFontsizeLi">
													<a href="javascript:void(0);" style="font-size: 18px;" onclick="changeTranLanFontsize(this);">18</a>
												</li>
												<li class="tranLanFontsizeLi">
													<a href="javascript:void(0);" style="font-size: 20px;" onclick="changeTranLanFontsize(this);">20</a>
												</li>
												<li class="tranLanFontsizeLi">
													<a href="javascript:void(0);" style="font-size: 24px;" onclick="changeTranLanFontsize(this);">24</a>
												</li>
												<li class="tranLanFontsizeLi">
													<a href="javascript:void(0);" style="font-size: 30px;" onclick="changeTranLanFontsize(this);">30</a>
												</li>
											</ul>
										</div>
									</li>
								</ul>
							</nav>
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
							<li>
								<a href="${ctxManage}/logout">
									<spring:message code='登出' />
								</a>
							</li>
						</ul>
					</li>
				</c:if>

				<c:if test="${! empty aaaaadasdasdasdas  }">
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