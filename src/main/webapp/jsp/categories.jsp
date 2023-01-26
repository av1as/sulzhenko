<%@page import="java.util.Hashtable"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<%@taglib prefix="myTags" tagdir="/WEB-INF/tags" %>
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

<table id="datatable" class="table table-striped table-bordered" cellspacing="0" width="100%">
    <thead>
        <h3 class="text-center"><fmt:message key="show.categories"/></h3>
		<tr>
			<th><fmt:message key="category.name"/></th>
			<th><fmt:message key="update"/></th>
			<th><fmt:message key="delete"/></th>
		</tr>
	</thead>
	<tfoot>
        <tr>
			<th><fmt:message key="category.name"/></th>
			<th><fmt:message key="update"/></th>
			<th><fmt:message key="delete"/></th>
		</tr>
	</tfoot>
	<tbody>
		<c:forEach var="element" items="${categories}">
		    <tr>
                <form action="/TimeKeeping/controller" method="post" action="/TimeKeeping/controller" id="tab">
                    <input type="hidden" name="action" value="update_category" />
                    <input type="hidden" name="category_name" value="${element.getName()}" />
                    <td class="form-group">
                        <input name="newname" type="text" value="${element.getName()}" class="form-control">
					</td>
                    <td class="form-group">
                        <input class="btn btn-success" type="submit" name="update_category" value="<fmt:message key="update"/>">
                    </td>
                </form>
                <form action="/TimeKeeping/controller" method="post" action="/TimeKeeping/controller" id="tab">
                    <td class="form-group">
                        <input class="btn btn-danger" type="submit" name="submit_reg" value="<fmt:message key="delete"/>">
                            <input type="hidden" name="category_name" value="${element.getName()}"/>
                            <input type="hidden" name="action" value="remove_category"/>
                    </td>
                </form>
            </tr>
		</c:forEach>
 	</tbody>
</table>

<table id="newdatatable" class="table table-striped table-bordered" cellspacing="0" width="100%">
    <thead>
    	<h3 class="text-center"><fmt:message key="add.category"/></h3>
			<tr>
				<th><fmt:message key="category.name"/></th>
				<th><fmt:message key="add"/></th>
			</tr>
	</thead>

	<tfoot>
        <tr>
		    <th><fmt:message key="category.name"/></th>
			<th><fmt:message key="add"/></th>
		</tr>
	</tfoot>

	<tbody>
		<form action="/TimeKeeping/controller" method="post" action="/TimeKeeping/controller" id="tab">
            <input type="hidden" name="action" value="add_category" />
            <td class="form-group">
                <input name="addedname" type="text" class="form-control">
			</td>
            <td class="form-group">
                <input class="btn btn-success" type="submit" name="add_category" value="+ <fmt:message key="add"/>">
            </td>
        </form>
 	</tbody>
</table>

 <%--For displaying Previous link except for the 1st page --%>
    <c:if test="${currentPage != 1}">
        <td><a href="/TimeKeeping/controller?action=show_categories&page=${currentPage - 1}"><fmt:message key="previous"/></a></td>
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
                        <td><a href="/TimeKeeping/controller?action=show_categories&page=${i}">${i}</a></td>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </tr>
    </table>

    <%--For displaying Next link --%>
    <c:if test="${currentPage lt noOfPages}">
        <td><a href="/TimeKeeping/controller?action=show_categories&page=${currentPage + 1}"><fmt:message key="next"/></a></td>
    </c:if>
</body>