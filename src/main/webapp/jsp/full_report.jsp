<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<%@taglib prefix="myTags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ctg" uri="/WEB-INF/tld/custom.tld" %>
<%@ page import="java.util.List"%>
<%@page import="java.util.Hashtable"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:setBundle basename="resources"/>

<head>
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
    <script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
</head>
<body>
<div id="top-nav" class="navbar navbar-inverse navbar-static-top">
  <div class="container">
    <div class="navbar-header">
      <a class="navbar-brand" href="${pageContext.request.contextPath}${menu}"><p style="font-size:35px"><fmt:message key="timekeeping"/></p></a>
    </div>
    <div class="navbar-collapse collapse">
      <ul class="nav navbar-nav navbar-right">
        <li>
        <br>
            <myTags:lang/>
        </li>
        <li>
        <br>
            <jsp:include page="/blocks/logout.jsp"/>
        </li>
      </ul>
    </div>
  </div><!-- /container -->
</div>

<button class="glyphicon glyphicon-download-alt" type="submit" onclick="window.open('report.pdf')">full report PDF</button>
<button class="glyphicon glyphicon-download-alt" type="submit" onclick="window.open('page.pdf')">this page PDF</button>

<div class="text-left">
    <h3><fmt:message key="today"/><ctg:now/></h3>
</div>
<h3 class="text-center"><fmt:message key="report"/></h3>

<table id="datatable" class="table table-striped " cellspacing="0" width="100%">
    <col style="width: 30%">
    <col style="width: 40%">
    <col style="width: 10%">
    <col style="width: 10%">
    <col style="width: 10%">
        <thead>
			<tr>
				<th><fmt:message key="login"/></th>
				<th><fmt:message key="activity.name"/> </th>
                <th><fmt:message key="time.amount"/></th>
                <th><fmt:message key="number.activities"/></th>
                <th><fmt:message key="total.time"/></th>
			</tr>
		</thead>
		<tfoot>
            <tr>
			    <th><fmt:message key="login"/></th>
				<th><fmt:message key="activity.name"/> </th>
                <th><fmt:message key="time.amount"/></th>
                <th><fmt:message key="number.activities"/></th>
                <th><fmt:message key="total.time"/></th>
			</tr>
		</tfoot>

		<tbody>
	        <c:forEach var="element" items="${report}">
			    <tr>
                    <td class="form-group">
                        ${element.getLogin()}
					</td>
					<td class="form-group" colspan="2" border="0" cellpadding="0" cellspacing="0">
                        <table id="datatable" class="table table-striped " >
                            <col style="width: 80%">
                            <col style="width: 20%">
                            <tbody>
                                <c:forEach var="row" items="${element.getActivitiesWithTime()}">
                                    <tr>
                                        <td class="form-group">
                                            ${row.getActivityName()}
                                    	</td>
                                    	<td class="form-group">
                                            ${row.getActivityTime()}
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </td>
	                <td class="form-group">
                        ${element.getNumberOfActivities()}
    			    </td>
                    <td class="form-group">
                        ${element.getTotalTime()}
 					</td>
 				</tr>
			</c:forEach>
 		</tbody>
</table>

 <%--For displaying Previous link except for the 1st page --%>
    <c:if test="${currentPage != 1}">
        <td><a href="/TimeKeeping/controller?action=show_full_report&page=${currentPage - 1}"><fmt:message key="previous"/></a></td>
    </c:if>

    <%--For displaying Page numbers.
    The when condition does not display a link for the current page--%>
    <table border="1" cellpadding="5" cellspacing="5">
        <tr>
            <c:forEach begin="1" end="${noOfPages}" var="i">
                <c:choose>
                    <c:when test="${currentPage eq i}">
                        <td>${i}</td>
                    </c:when>
                    <c:otherwise>
                        <td><a href="/TimeKeeping/controller?action=show_full_report&page=${i}">${i}</a></td>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </tr>
    </table>

    <%--For displaying Next link --%>
    <c:if test="${currentPage lt noOfPages}">
        <td><a href="/TimeKeeping/controller?action=show_full_report&page=${currentPage + 1}"><fmt:message key="next"/></a></td>
    </c:if>
</body>