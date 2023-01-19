
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<%@taglib prefix="myTags" tagdir="/WEB-INF/tags" %>
<%@page import="java.util.Hashtable"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:setBundle basename="resources"/>

<head>
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
    <script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
    <!------ Include the above in your HEAD tag ---------->
</head>

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

        <li>            <jsp:include page="/blocks/logout.jsp"/></li>
      </ul>
    </div>
  </div><!-- /container -->
</div>


<div class="well">

      <h3><fmt:message key="profile"/></h3>

      <div>(*<fmt:message key="not.empty"/>)</div>
      <!------ <li><a href="#profile" data-toggle="tab">Password</a></li> ---------->

    <div id="myTabContent" class="tab-content">
<!------
      <div class="tab-pane active in" id="home">
---------->
        <form action="/TimeKeeping/controller?action=update_user" method="post" id="fileForm" role="form">
                <input type="hidden" name="action" value="update_user" />
<!------
                 <div class="form-group">
                    <label><span class="req">* </span><fmt:message key="login"/></label><br>
                    <div title="<fmt:message key="login.requirements"/>"><fmt:message key="hover.requirements"/></div>
                     <input name="newlogin" type="text" value="${user.getLogin()}" class="input-xlarge">
                 </div>
---------->
             <div>
                <label><fmt:message key="login"/></label><br>
                <h4>${user.getLogin()}</h4>

             </div>

            <div class="form-group">
               <label><fmt:message key="first.name"/></label><br>
                <input name="newfirstname" type="text" value="${user.getFirstName()}" class="input-xlarge">
            </div>
           <div class="form-group">
               <label><fmt:message key="last.name"/></label><br>
                <input name="newlastname" type="text" value="${user.getLastName()}" class="input-xlarge">
           </div>
           <div class="form-group">
               <label><span class="req">* </span><fmt:message key="email"/></label><br>
               <div title="<fmt:message key="email.requirements"/>"><fmt:message key="hover.requirements"/></div>
                <input name="newemail" type="text" value="${user.getEmail()}" class="input-xlarge">
            </div>

            <div class="form-group">

                <label><span class="req">* </span><fmt:message key="current.password"/>: </label><br>
                <input name="currentpassword" type="password" class="input-xlarge"   id="password" />
            </div>

            <div class="form-group">
            <label for="password"><fmt:message key="new.password"/>: </label><br>
            <div title="<fmt:message key="password.requirements"/>"><fmt:message key="hover.requirements"/></div>

                <input name="newpassword" type="password" class="input-xlarge" id="pass1" />
            </div>

           <div class="form-group">
                <label for="password"><fmt:message key="new.password.confirm"/>: </label><br>
                <input name="newpasswordconfirm" type="password" class="input-xlarge" placeholder="<fmt:message key="repeat.validate"/>"  id="pass2" onkeyup="checkPass(); return false;" />
                <span id="confirmMessage" class="confirmMessage"></span>
            </div>
            <br>
            <div class="form-group">
                <input type="checkbox" name="newnotifications"   id="notifications" ${user.isNotificationChecked()}> <label for="notifications"><fmt:message key="get.notification"/>.</label>
            </div>
<!------
                        </div>

          	<div>
---------->
        	 <div class="form-group">
        	    <input class="btn btn-success" type="submit" name="update_user" value = "<fmt:message key="update"/>">
        	 </div>
<!------          	</div>
---------->
        </form>
      </div>
<!------

      <div class="tab-pane fade" id="profile">
    	<form id="tab2">
        	<label>New Password</label>
        	<input type="password" class="input-xlarge">
        	<div>
        	    <button class="btn btn-success">Update</button>
        	</div>
    	</form>
      </div>
---------->
  </div>