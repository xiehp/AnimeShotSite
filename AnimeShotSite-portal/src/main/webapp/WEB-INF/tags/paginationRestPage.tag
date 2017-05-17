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
	int end = Math.min(begin + (paginationSize - 1), page.getTotalPages());

	request.setAttribute("current", current);
	request.setAttribute("begin", begin);
	request.setAttribute("end", end);

	//String paramStr = "?page=" + (current - 1);
	//String requestURI = request.getRequestURI();
	//request.setAttribute("paramStr", paramStr);
	//request.setAttribute("paramStr", paramStr);
%>

<div align="center">
	<ul class="pagination">
		<%
			if (page.hasPrevious()) {
		%>
		<li>
			<a href="${requestURI}">&lt;&lt;</a>
		</li>
		<li>
			<c:if test="${current - 1 eq 1}">
				<a href="${requestURI}">&lt;</a>
			</c:if>
			<c:if test="${current - 1 ne 1}">
				<a href="?page=${current - 1}">&lt;</a>
			</c:if>
		</li>
		<%
			} else {
		%>
		<li class="disabled">
			<c:if test="${isMipPage}">
				&lt;&lt;
			</c:if>
			<c:if test="${not isMipPage}">
				<a href="javascript:void(0);">&lt;&lt;</a>
			</c:if>
		</li>
		<li class="disabled">
			<c:if test="${isMipPage}">
				&lt;	
			</c:if>
			<c:if test="${not isMipPage}">
				<a href="javascript:void(0);">&lt;</a>
			</c:if>
		</li>
		<%
			}
		%>

		<c:forEach var="i" begin="${begin}" end="${end}">
			<c:choose>
				<c:when test="${i == current}">
					<li class="active">
						<c:if test="${isMipPage}">
							${i}
						</c:if>
						<c:if test="${not isMipPage}">
							<a class="disabled" href="javascript:void(0);">${i}</a>
						</c:if>
					</li>
				</c:when>
				<c:otherwise>
					<li>
						<c:if test="${i eq 1}">
							<a href="${requestURI}">${i}</a>
						</c:if>
						<c:if test="${i ne 1}">
							<a href="?page=${i}">${i}</a>
						</c:if>
					</li>
				</c:otherwise>
			</c:choose>
		</c:forEach>

		<%
			if (page.hasNext()) {
		%>
		<li>
			<a href="?page=${current+1}">&gt;</a>
		</li>
		<li>
			<a href="?page=${page.totalPages}">&gt;&gt;</a>
		</li>
		<%
			} else {
		%>
		<li class="disabled">
			<c:if test="${isMipPage}">
				&gt;
			</c:if>
			<c:if test="${not isMipPage}">
				<a href="javascript:void(0);">&gt;</a>
			</c:if>
		</li>
		<li class="disabled">
			<c:if test="${isMipPage}">
				&gt;&gt;
			</c:if>
			<c:if test="${not isMipPage}">
				<a href="javascript:void(0);">&gt;&gt;</a>
			</c:if>
		</li>
		<%
			}
		%>
	</ul>

	<div style="margin-top: -20px; margin-bottom: 20px; font-size: 8px;">
		<span> ${current}/${page.totalPages}<spring:message code='页' /> <spring:message code='共' />${page.totalElements}<spring:message code='条' /></span>
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
						if (goPageNumber == 1) {
							document.location.href = "${requestURI}";
						} else {
							document.location.href = "${requestURI}" + "?page=" + goPageNumber;
						}
					}
				</script>
			</c:if>
		</c:if>
	</div>
</div>
