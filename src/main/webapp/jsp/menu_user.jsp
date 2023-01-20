
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
    <!------ Include the above in your HEAD tag ---------->
    <!------
    ---------->
</head>
<body>
<div id="top-nav" class="navbar navbar-inverse navbar-static-top">
  <div class="container">
    <div class="navbar-header">

      <a class="navbar-brand" href="${menu}"><p style="font-size:35px"><fmt:message key="timekeeping"/></p></a>

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
        <li>            <jsp:include page="/blocks/logout.jsp"/></li>
      </ul>
    </div>
  </div><!-- /container -->
</div>
<!-- /Header -->




<div class="text-right">
<h3><fmt:message key="today"/><ctg:now/></h3>
</div>



<!-- Main -->
<div class="container">
<div class="row">
    <div class="col-md-3">
      <!-- Left column -->
      <h2><i class="glyphicon glyphicon-wrench"></i><fmt:message key="tools"/></h2>
<!--      <a href="#"><strong><i class="glyphicon glyphicon-wrench"></i> Tools</strong></a>

      <hr>

      <ul class="list-unstyled">
        <li class="nav-header"> <a href="#" data-toggle="collapse" data-target="#userMenu">
          <h3>Settings <i class="glyphicon glyphicon-chevron-down"></i></h3>
-->
          </a>
            <ul class="list-unstyled collapse in" id="userMenu">


                <li><a href="/TimeKeeping/controller?action=user_activities"><p style="font-size:25px"><i class="glyphicon glyphicon-flag"></i><fmt:message key="my.activities"/></p></a></li>


            </ul>
        </li>
      </ul>