
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
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



<h3 class="text-center"><fmt:message key="show.activities"/></h3>
	<form action="/TimeKeeping/controller?action=show_activities" method="get"  id="fileForm" role="form">


               <input type="hidden" name="action" value="show_activities" />
               <label><fmt:message key="filter.category"/></label>
		       <select name = "filter">
                        <option value="all categories" ${param.filter eq 'all categories' ? 'selected' : ''}><fmt:message key="all.categories"/></option>
                        <c:forEach var="item" items="${categories}">
                        <option value="${item.getName()}" ${item.getName() == param.filter ? 'selected' : ''}>${item.getName()}</option>
                        </c:forEach>

               </select>
               <br>

               <label><fmt:message key="sort.by"/></label>
		       <select name = "parameter">
                        <option value="name of activity" ${param.parameter eq 'name of activity' ? 'selected' : ''}><fmt:message key="activity.name"/></option>
                        <option value="category of activity" ${param.parameter eq 'category of activity' ? 'selected' : ''}><fmt:message key="activity.category"/></option>
                        <option value="number of users" ${param.parameter eq 'number of users' ? 'selected' : ''}><fmt:message key="users.number"/></option>
               </select>
               <label><fmt:message key="order"/></label>
		       <select name = "order">
                        <option value="ascending" ${param.order eq 'ascending' ? 'selected' : ''}><fmt:message key="ascending"/></option>
                        <option value="descending" ${param.order eq 'descending' ? 'selected' : ''}><fmt:message key="descending"/></option>
               </select>


              </a>

          <input class="btn btn-primary" type="submit" name="submit_add" value="<fmt:message key="sort"/>">
     </form>



<table id="datatable" class="table table-striped table-bordered" cellspacing="0" width="100%">
  <col style="width: 50%">
  <col style="width: 20%">
  <col style="width: 14%">
  <col style="width: 8%">
  <col style="width: 8%">
    				<thead>

						<tr>

							<th><fmt:message key="activity.name"/></th>
							<th><fmt:message key="activity.category"/> </th>
							<th><fmt:message key="users.number"/></th>
                            <th><fmt:message key="update"/></th>
                            <th><fmt:message key="delete"/></th>
						</tr>
					</thead>

					<tfoot>
                        <tr>

							<th><fmt:message key="activity.name"/></th>
							<th><fmt:message key="activity.category"/> </th>
							<th><fmt:message key="users.number"/></th>
                            <th><fmt:message key="update"/></th>
                            <th><fmt:message key="delete"/></th>
						</tr>
					</tfoot>

					<tbody>
					    <c:forEach var="element" items="${activities}">
					      <tr>
                          <form action="/TimeKeeping/controller" method="post" action="/TimeKeeping/controller" id="tab">
                            	<input type="hidden" name="action" value="update_activity" />
                            	<input type="hidden" name="name" value="${element.getName()}" />
                            <td class="form-group">
                                <input name="newname" type="text" value="${element.getName()}" class="form-control">

					        </td>
                            <td class="form-group">
                            	<select name = "newcategory" class="form-control">
                                   <option hidden="">${element.getCategory()}</option>
                                   <c:forEach var="item" items="${categories}">
                                       <option>${item.getName()}</option>
                                   </c:forEach>
                                </select>
					        </td>

  		                    <td  >
                                ${element.getNumberOfUsers()}
          					</td>

                            <td class="form-group">
                                     <input class="btn btn-success" type="submit" name="update_activity" value="<fmt:message key="update"/>">

                            </td>
                          </form>

                          <form action="/TimeKeeping/controller" method="post" action="/TimeKeeping/controller" id="tab">
                                <input type="hidden" name="action" value="delete_activity" />
                                <input type="hidden" name="id" value="${element.getName()}" />
                                <td class="form-group">
                                  <input class="btn btn-danger" type="submit" name="submit_del" value="<fmt:message key="delete"/>">
                                  <input type="hidden" name="activity_name" value="${element.getName()}" />
                                  <input type="hidden" name="action" value="delete_activity" />

                                </td>
                            </form>
					    </c:forEach>
 					</tbody>
				</table>





 <%--For displaying Previous link except for the 1st page --%>
    <c:if test="${currentPage != 1}">
        <td><a href="/TimeKeeping/controller?action=show_activities&page=${currentPage - 1}&filter=${param.filter}&order=${param.order}&parameter=${param.parameter}"><fmt:message key="previous"/></a></td>
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
                        <td><a href="/TimeKeeping/controller?action=show_activities&page=${i}&filter=${param.filter}&order=${param.order}&parameter=${param.parameter}">${i}</a></td>

                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </tr>
    </table>

    <%--For displaying Next link --%>
    <c:if test="${currentPage lt noOfPages}">
        <td><a href="/TimeKeeping/controller?action=show_activities&page=${currentPage + 1}&filter=${filter}&order=${order}&parameter=${parameter}"><fmt:message key="next"/></a></td>
    </c:if>

<table id="newdatatable" class="table table-striped table-bordered" cellspacing="0" width="100%">
  <col style="width: 64%">
  <col style="width: 20%">
  <col style="width: 16%">
                    <thead>
                    	<h3><fmt:message key="add.activity"/></h3>
                			<tr>
                				<th><fmt:message key="activity.name"/></th>
                				<th><fmt:message key="activity.category"/></th>
                				<th><fmt:message key="add"/></th>
                			</tr>
                	</thead>

                	<tfoot>
                             <tr>
                				<th><fmt:message key="activity.name"/></th>
                				<th><fmt:message key="activity.category"/></th>
                				<th><fmt:message key="add"/></th>
                			</tr>
                	</tfoot>

                	<tbody>
                		     <form action="/TimeKeeping/controller" method="post" action="/TimeKeeping/controller" id="tab">
                                   <input type="hidden" name="action" value="new_activity" />
                                 <td class="form-group">
                                    <input name="addedname" type="text" class="form-control">
                				 </td>

                                 <td class="form-group">
                            	    <select name = "addedcategory" class="form-control">
                                        <c:forEach var="item" items="${categories}">
                                            <option>${item.getName()}</option>
                                        </c:forEach>
                                    </select>
					             </td>

                                 <td class="form-group">
                                    <input class="btn btn-success" type="submit" name="new_activity" value="+ <fmt:message key="add"/>">

                                 </td>
                             </form>
                 	</tbody>

                </table>
</body>


