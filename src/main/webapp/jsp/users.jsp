<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<%@taglib prefix="myTags" tagdir="/WEB-INF/tags" %>
<%@ page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
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
    <h3 class="text-center"><fmt:message key="users"/></h3>
	<form action="/TimeKeeping/controller?action=users" method="get"  id="fileForm" role="form">
         <input type="hidden" name="action" value="users" />
         <label><fmt:message key="choose.user.status"/>:</label>
		 <select name = "status">
             <option value="all" ${param.status eq 'all' ? 'selected' : ''}><fmt:message key="all.users"/></option>
             <option value="active" ${param.status eq 'active' ? 'selected' : ''}><fmt:message key="active"/></option>
             <option value="inactive" ${param.status eq 'inactive' ? 'selected' : ''}><fmt:message key="inactive"/></option>
             <option value="deactivated" ${param.status eq 'deactivated' ? 'selected' : ''}><fmt:message key="deactivated"/></option>
         </select>
         <input class="btn btn-primary" type="submit" value="<fmt:message key="show"/>">
    </form>

<table id="datatable" class="table table-striped table-bordered" cellspacing="0" width="100%">
    <thead>
		<tr>
			<th><fmt:message key="login"/></th>
			<th><fmt:message key="first.name"/> </th>
			<th><fmt:message key="last.name"/></th>
            <th><fmt:message key="email"/></th>
            <th><fmt:message key="status"/></th>
            <th><fmt:message key="notification"/></th>
            <th><fmt:message key="update"/></th>
		</tr>
	</thead>
	<tfoot>
        <tr>
			<th><fmt:message key="login"/></th>
			<th><fmt:message key="first.name"/> </th>
			<th><fmt:message key="last.name"/></th>
            <th><fmt:message key="email"/></th>
            <th><fmt:message key="status"/></th>
            <th><fmt:message key="notification"/></th>
            <th><fmt:message key="update"/></th>
		</tr>
	</tfoot>
	<tbody>
		<c:forEach var="element" items="${users}">
			<tr>
                <form action="/TimeKeeping/controller" method="post" action="/TimeKeeping/controller" id="tab">
                <input type="hidden" name="action" value="admin_update" />
                <input type="hidden" name="oldlogin" value="${element.getLogin()}" />
                <td>
                    ${element.getLogin()}
				</td>
                <td class="form-group">
                    <input name="newfirstname" type="text" value="${element.getFirstName()}" class="input-xlarge">
				</td>
		        <td class="form-group">
                    <input name="newlastname" type="text" value="${element.getLastName()}" class="input-xlarge">
        		</td>
  		        <td class="form-group">
  		            <input name="newemail" type="text" value="${element.getEmail()}" class="input-xlarge">
          		</td>
       		    <td class="form-group">
       		        <select name = "newstatus" value="${element.getStatus()}">
                        <option value="active" ${element.getStatus() eq 'active' ? 'selected' : ''}><fmt:message key="active"/></option>
                        <option value="inactive" ${element.getStatus() eq 'inactive' ? 'selected' : ''}><fmt:message key="inactive"/></option>
                        <option value="deactivated" ${element.getStatus() eq 'deactivated' ? 'selected' : ''}><fmt:message key="deactivated"/></option>
                    </select>
               	</td>
         		<td class="form-group">
         		    <input name="newnotifications" type="checkbox" ${element.isNotificationChecked()}  class="input-xlarge">
                </td>
                <td class="form-group">
                    <input class="btn btn-success" type="submit" name="admin_update" value="<fmt:message key="update"/>">
                </td>
                </form>
		</c:forEach>
 	</tbody>
</table>
<jsp:include page="/blocks/pagination.jsp"/>
</body>