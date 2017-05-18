<%@tag import="freemarker.template.utility.HtmlEscape"%>
<%@tag import="org.springframework.boot.autoconfigure.condition.SearchStrategy"%>
<%@ tag pageEncoding="UTF-8"%>
<%@ attribute name="page" type="org.springframework.data.domain.Page" required="true"%>
<%@ attribute name="paginationSize" type="java.lang.Integer"%>
<%@ attribute name="searchKey1" type="java.lang.String"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%
	if (paginationSize == null) {
		paginationSize = 5;
	}
	int current = page.getNumber() + 1;
	int begin = Math.max(1, current - paginationSize / 2);
	int end = Math.min(begin + (paginationSize - 1), page.getTotalPages());

	request.setAttribute("current", current);
	request.setAttribute("begin", begin);
	request.setAttribute("end", end);

	//String paramStr = "?page=" + (current - 1);
	//String requestURI = request.getRequestURI();
	//request.setAttribute("paramStr", paramStr);
	//request.setAttribute("paramStr", paramStr);

	String searchName = request.getParameter("name");
	String searchKeyword = request.getParameter("keyword");
	String searchKey1Value = request.getParameter(searchKey1);

	StringBuilder searchStr = new StringBuilder(200);
	searchStr.append("?");
	searchStr.append("name=" + searchName);
	searchStr.append("&keyword=" + searchKeyword);
	if (searchKey1Value != null) {
		searchStr.append("&" + searchKey1 + "=" + searchKey1Value);
	}
	request.setAttribute("searchStr", searchStr.toString());
%>

<div align="center">
	<ul class="pagination">
		<%
			if (page.hasPrevious()) {
		%>
		<li>
			<a href="<c:out value='${searchStr}' />">&lt;&lt;</a>
		</li>
		<li>
			<c:if test="${current - 1 eq 1}">
				<a href="<c:out value='${searchStr}' />">&lt;</a>
			</c:if>
			<c:if test="${current - 1 ne 1}">
				<a href="<c:out value='${searchStr}' />&page=${current - 1}">&lt;</a>
			</c:if>
		</li>
		<%
			} else {
		%>
		<li class="disabled">
			<span>&lt;&lt;</span>
		</li>
		<li class="disabled">
			<span>&lt;</span>
		</li>
		<%
			}
		%>

		<c:forEach var="i" begin="${begin}" end="${end}">
			<c:choose>
				<c:when test="${i == current}">
					<li class="active">
						<span class="disabled">${i}</span>
					</li>
				</c:when>
				<c:otherwise>
					<li>
						<c:if test="${i eq 1}">
							<a href="<c:out value='${searchStr}' />">${i}</a>
						</c:if>
						<c:if test="${i ne 1}">
							<a href="<c:out value='${searchStr}' />&page=${i}">${i}</a>
						</c:if>
					</li>
				</c:otherwise>
			</c:choose>
		</c:forEach>

		<%
			if (page.hasNext()) {
		%>
		<li>
			<a href="<c:out value='${searchStr}' />&page=${current+1}">&gt;</a>
		</li>
		<li>
			<a href="<c:out value='${searchStr}' />&page=${page.totalPages}">&gt;&gt;</a>
		</li>
		<%
			} else {
		%>
		<li class="disabled">
			<span>&gt;</span>
		</li>
		<li class="disabled">
			<span>&gt;&gt;</span>
		</li>
		<%
			}
		%>
	</ul>

	<div class="pageInfoLine">
		<input id="paginationPage_goPageSearchStr" type="hidden" value="<c:out value='${searchStr}' />">
		<span> ${page.number + 1}/${page.totalPages}<spring:message code='页' /> <spring:message code='共' />${page.totalElements}<spring:message code='条' /></span>
		<c:if test="${not isMipPage}">
			<c:if test="${page.totalPages > 1}">
				<spring:message code='跳转到' />
				<input id="goPageNumber" type="text" style="width: 40px;">
				<spring:message code='页' />
				<input type="button" value="<spring:message code='跳转' />" onclick="goPage()">
				<script type="text/javascript">
					function goPage() {
						var goPageNumber = document.getElementById("goPageNumber").value;
						if (goPageNumber == null) {
							$.showMessageModal("<spring:message code='页数为空' />");
							return;
						}
						if (isNaN(goPageNumber)) {
							$.showMessageModal("<spring:message code='页数必须数字' />");
							return;
						}
						if (goPageNumber > parseInt("${page.totalPages}")) {
							$.showMessageModal("<spring:message code='没有该页' />");
							return;
						}
						if (goPageNumber < 1) {
							$.showMessageModal("<spring:message code='页数过小' />");
							return;
						}

						// 跳转
						var searchStr = document.getElementById("paginationPage_goPageSearchStr").value;
						if (goPageNumber == 1) {
							document.location.href = searchStr;
						} else {
							var symbol = searchStr.indexOf("?") > -1 ? "&" : "?";
							var url = searchStr + symbol + "page=" + goPageNumber;
							document.location.href = url;
						}
					}
				</script>
			</c:if>
		</c:if>
	</div>
</div>
