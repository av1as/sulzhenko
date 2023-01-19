
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

      </ul>
    </div>
  </div><!-- /container -->
</div>

  </div><!-- /container -->
</div>
<div class="container">
	<div class="row">
<form name="loginForm" method="POST" action="controller?action=login">
<input type="hidden" name="action" value="login" />
<h3><fmt:message key="login"/>:</h3>

<input type="text" name="login" value=""/>
<br/>

<h3><fmt:message key="password"/>:</h3>
<input type="password" name="password" value=""/>
<br/>

  <!--
<span class="error">${error}</span>
<div style="color: red "><fmt:message key="${error}"/></div>
-->


<br/>
${wrongAction}
<br/>
${nullPage}
<br/>
<h3><input type="submit" value="<fmt:message key="log.in"/>"/></h3>
</form><hr/>

<a href="jsp/register.jsp"><h3><fmt:message key="register"/></h3></a>
</div>
</body></html>