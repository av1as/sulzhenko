<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:setBundle basename="resources"/>

<head>
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
    <script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <!------ Include the above in your HEAD tag ---------->
    <!------
    ---------->
</head>
<!doctype html>
<html>
<c:set var="title" value="Страница входа" scope="page"/>
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
            <jsp:include page="/blocks/lang.jsp"/>

        </li>

        <li>
        <br>
            <jsp:include page="/blocks/logout.jsp"/>
        </li>


      </ul>
    </div>
  </div><!-- /container -->
</div>
<div class="page-container-responsive">
    <div class="row space-top-8 space-8">
        <div class="col-md-5 col-middle">
            <h1 class="text-center">path= ${path}</h1>
            <h2 class="text-center">error= ${error}</h2>
            <h2 class="text-center">filter= ${filter}</h2>
            <h2 class="text-center">categories= ${categories}</h2>
            <h2 class="text-center">activities= ${activities}</h2>


        </div>
        <div class="col-md-5 col-middle text-center">

        </div>
    </div>
</div>

</body>
</html>