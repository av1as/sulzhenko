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

<div class="container">
	<div class="row">
        <div class="col-md-6">
            <form action="/TimeKeeping/controller" method="post" action="/TimeKeeping/controller" id="fileForm" role="form">
                <input type="hidden" name="action" value="register"/>
                <fieldset><legend class="text-center"><fmt:message key="valid.info"/> <span class="req"><br><small> (<fmt:message key="required"/> *)</small></span></legend>

                <div class="form-group">
                    <label for="email"><span class="req">* </span> <fmt:message key="email"/>: </label>
                    <div title="<fmt:message key="email.requirements"/>"><fmt:message key="hover.requirements"/></div>
                    <input type="text" name="email" id="email" class="form-control login"  placeholder=""/>
                </div>

                <div class="form-group">
                    <label for="firstname"><fmt:message key="first.name"/>: </label>
                    <input class="form-control" type="text" name="firstname" id = "txt" onkeyup = "Validate(this);"  />
                    <div id="errFirst"></div>
                </div>

                <div class="form-group">
                    <label for="lastname"> <fmt:message key="last.name"/>: </label>
                    <input class="form-control" type="text" name="lastname" id = "txt" onkeyup = "Validate(this)" placeholder=""  />
                    <div id="errLast"></div>
                </div>

                <div class="form-group">
                    <label for="login"><span class="req">* </span> <fmt:message key="login"/>:   </label>
                    <div title="<fmt:message key="login.requirements"/>"><fmt:message key="hover.requirements"/></div>
                    <input class="form-control" type="text" name="login" id = "txt" onkeyup = "Validate(this)"  />
                    <div id="errLast"></div>
                </div>

                <div class="form-group">
                    <label for="password"><span class="req">* </span> <fmt:message key="password"/>: </label>
                    <div title="<fmt:message key="password.requirements"/>"><fmt:message key="hover.requirements"/></div>
                    <input name="password1" type="password" class="form-control inputpass"   id="pass1" /> </p>
                    <label for="password"><span class="req">* </span> <fmt:message key="password.confirm"/>: </label>
                    <input name="password2" type="password" class="form-control inputpass"  placeholder="<fmt:message key="repeat.validate"/>"  id="pass2" onkeyup="checkPass(); return false;" />
                    <span id="confirmMessage" class="confirmMessage"></span>
                </div>
                     <input type="checkbox"
                                   onclick="Toggle('pass1'); Toggle('pass2')">
                            <strong><fmt:message key="show.password"/></strong>

                            <script>
                                // Change the type of input to password or text
                                function Toggle(id) {
                                    var temp = document.getElementById(id);
                                    if (temp.type === "password") {
                                        temp.type = "text";
                                    }
                                    else {
                                        temp.type = "password";
                                    }
                                }
                            </script>

                <div class="form-group">
                    <input type="checkbox" name="notifications"  id="notifications">   <label for="notifications"><fmt:message key="get.notification"/>.</label>
                </div>

                <div class="form-group">
                    <input class="btn btn-success" type="submit" name="submit_reg" value="<fmt:message key="register"/>">
                </div>
                    <h5><fmt:message key="confirm.registration1"/>. </h5>
                    <h5><fmt:message key="confirm.registration2"/>. </h5>
                </fieldset>
            </form><!-- ends register form -->
        </div><!-- ends col-6 -->
	</div>
</div>

</body></html>