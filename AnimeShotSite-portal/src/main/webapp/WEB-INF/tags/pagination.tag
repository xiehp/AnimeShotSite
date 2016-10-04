<%@ tag pageEncoding="UTF-8"%>
<%@ attribute name="page" type="org.springframework.data.domain.Page" required="true"%>
<%@ attribute name="paginationSize" type="java.lang.Integer"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%
	if (paginationSize == null) {
		paginationSize = 5;
	}
	int current = page.getNumber() + 1;
	int begin = Math.max(1, current - paginationSize / 2);
	int total = page.getTotalPages();
	int end = Math.min(begin + (paginationSize - 1), page.getTotalPages());
	long totalElements = page.getTotalElements();

	request.setAttribute("current", current);
	request.setAttribute("begin", begin);
	request.setAttribute("end", end);
	request.setAttribute("totalPages", total);
	request.setAttribute("totalElements", totalElements);
%>
<div align="center">

	<ul class="pagination" style="font-size: 8px;">
		<%--<li class="disabled"><a href="#">共 ${totalPages} 页 , 第 ${current} 页, 共 ${page.totalElements} 条记录</a></li>--%>
		<%
			if (page.hasPrevious()) {
		%>
		<li>
			<a href="?page=1&sortType=${sortType}&${searchParams}">&lt;&lt;</a>
		</li>
		<li>
			<a href="?page=${current - 1}&sortType=${sortType}&${searchParams}">&lt;</a>
		</li>
		<%
			} else {
		%>
		<li class="disabled">
			<a href="javascript:void(0);">&lt;&lt;</a>
		</li>
		<li class="disabled">
			<a href="javascript:void(0);">&lt;</a>
		</li>
		<%
			}
		%>

		<c:forEach var="i" begin="${begin}" end="${end}">
			<c:choose>
				<c:when test="${i == current}">
					<li class="active">
						<a class="disabled" href="javascript:void(0);">${i}</a>
					</li>
				</c:when>
				<c:otherwise>
					<li>
						<a href="?page=${i}&sortType=${sortType}&${searchParams}">${i}</a>
					</li>
				</c:otherwise>
			</c:choose>
		</c:forEach>

		<%
			if (page.hasNext()) {
		%>
		<li>
			<a href="?page=${current+1}&sortType=${sortType}&${searchParams}">&gt;</a>
		</li>
		<li>
			<a href="?page=${page.totalPages}&sortType=${sortType}&${searchParams}">&gt;&gt;</a>
		</li>
		<%
			} else {
		%>
		<li class="disabled">
			<a href="javascript:void(0);">&gt;</a>
		</li>
		<li class="disabled">
			<a href="javascript:void(0);">&gt;&gt;</a>
		</li>
		<%
			}
		%>

	</ul>
</div>
