<%@ page language="java" contentType="text/html; UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<div>
	<nav class="navbar navbar-inverse" role="navigation">

		<div class="navbar-header">
			<a class="navbar-brand" href="${ctx}/index">动画截图网</a>
		</div>

		<div>
			<ul class="nav navbar-nav">
				<li class="active">
					<a href="${ctx}/index">首页</a>
				</li>

				<li>
					<a href="${ctx}/anime/list">动画列表</a>
				</li>

				<li>
					<a href="${ctx}/shot/random">随便看</a>
				</li>

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