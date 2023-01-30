<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<%@taglib prefix="myTags" tagdir="/WEB-INF/tags" %>
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
    <h3 class="text-center"><fmt:message key="requests"/></h3>
	<form action="/TimeKeeping/controller?action=requests" method="get"  id="fileForm" role="form">
        <input type="hidden" name="action" value="show_requests" />
        <label><fmt:message key="choose.requests"/>:</label>
		<select name = "action_to_do">
            <option value="all" ${param.action_to_do eq 'all' ? 'selected' : ''}><fmt:message key="all.requests"/></option>
            <option value="add" ${param.action_to_do eq 'add' ? 'selected' : ''}><fmt:message key="to.add"/></option>
            <option value="remove" ${param.action_to_do eq 'remove' ? 'selected' : ''}><fmt:message key="to.remove"/></option>
        </select>
        <input class="btn btn-primary" type="submit" value="<fmt:message key="show"/>">
    </form>



<table id="datatable" class="table table-striped table-bordered" cellspacing="0" width="100%">
  <col style="width: 10%">
  <col style="width: 40%">
  <col style="width: 14%">
  <col style="width: 20%">
  <col style="width: 8%">
  <col style="width: 8%">
    <thead>
		<tr>
			<th><fmt:message key="login"/></th>
			<th><fmt:message key="activity.name"/> </th>
			<th><fmt:message key="action.to.do"/></th>
			<th><fmt:message key="description"/></th>
            <th><fmt:message key="approve"/></th>
            <th><fmt:message key="decline"/></th>
		</tr>
	</thead>

	<tfoot>
        <tr>
			<th><fmt:message key="login"/></th>
			<th><fmt:message key="activity.name"/> </th>
			<th><fmt:message key="action.to.do"/></th>
			<th><fmt:message key="description"/></th>
            <th><fmt:message key="approve"/></th>
            <th><fmt:message key="decline"/></th>
		</tr>
	</tfoot>

	<tbody>
		<c:forEach var="element" items="${requests}">
			<tr>
            <form action="/TimeKeeping/controller" method="post" action="/TimeKeeping/controller" id="tab">
                <input type="hidden" name="action" value="approve_request" />
                <input type="hidden" name="id" value="${element.getId()}" />
                <td>
                    ${element.getLogin()}
				</td>
	            <td>
                    ${element.getActivityName()}
    			</td>
  	            <td>
                    <fmt:message key="${element.getActionToDo()}"/>
      			</td>
  	            <td>
                    ${element.getDescription()}
      			</td>
                <td class="form-group">
                    <input class="btn btn-success" type="submit" value="<fmt:message key="approve"/>">
                </td>
            </form>

            <form action="/TimeKeeping/controller" method="post" action="/TimeKeeping/controller" id="tab">
                <input type="hidden" name="action" value="decline_request" />
                <input type="hidden" name="id" value="${element.getId()}" />
                <td class="form-group">
                    <input class="btn btn-danger" type="submit" name="decline_request" value="<fmt:message key="decline"/>">
                </td>
		</c:forEach>
 	</tbody>
</table>

<jsp:include page="/blocks/pagination.jsp"/>
</body>