<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<%@taglib prefix="myTags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ctg" uri="/WEB-INF/tld/custom.tld" %>
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
            <jsp:include page="/blocks/profile.jsp"/>
        </li>
        <li>
            <jsp:include page="/blocks/logout.jsp"/></li>
      </ul>
    </div>
  </div><!-- /container -->
</div>

<div class="text-right">
    <h3><fmt:message key="today"/><ctg:now/></h3>
</div>

<div class="container">
    <div class="row">
    <div class="col-md-3">
      <!-- Left column -->
      <h2><i class="glyphicon glyphicon-wrench"></i><fmt:message key="tools"/></h2>
            <ul>
                <li><a href="/TimeKeeping/controller?action=users"><p style="font-size:25px"><i class="glyphicon glyphicon-user"></i><fmt:message key="users"/></p></a></li>
                <li><a href="/TimeKeeping/controller?action=show_categories"><p style="font-size:25px"><i class="glyphicon glyphicon-cog"></i><fmt:message key="categories.activities"/></p></a></li>
                <li><a href="/TimeKeeping/controller?action=show_activities"><p style="font-size:25px"><i class="glyphicon glyphicon-flag"></i><fmt:message key="activities"/></p></a></li>
                <li><a href="/TimeKeeping/controller?action=show_requests"><p style="font-size:25px"><i class="glyphicon glyphicon-comment"></i> <fmt:message key="requests"/></p></a></li>
            </ul>
          <h3><li><a href="/TimeKeeping/controller?action=show_full_report"></i><fmt:message key="report"/></a></li></i></h3>
        </li>
      </ul>