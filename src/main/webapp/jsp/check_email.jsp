
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

<html>
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


    <form action="/TimeKeeping/controller?action=logout" method="post" class="d-flex" >
        <input type="hidden" name="action" value="logout" />


            <button class="btn btn-link btn-lg">
              <p style="font-size:35px"><fmt:message key="timekeeping"/></p
            </button>
    </form>



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
<br/>
<h2 class="text-center"><fmt:message key="hello"/>, ${user.getLogin()}</h2>
<h4 class="text-center"><fmt:message key="check.email"/></h4>

</body>
</html>