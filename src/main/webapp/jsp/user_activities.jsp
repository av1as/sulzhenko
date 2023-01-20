
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
    <!------
    ---------->
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
        <li>            <jsp:include page="/blocks/logout.jsp"/></li>
      </ul>
    </div>
  </div><!-- /container -->
</div>
<div class="container">
	<div class="row">

		<h2 class="text-center"><fmt:message key="my.activities"/></h2>

	</div>
</div>

<table id="datatable" class="table table-striped table-bordered" cellspacing="0" width="100%">
    				<thead>
						<tr>
							<th><fmt:message key="activity.name"/></th>
							<th><fmt:message key="activity.category"/></th>
							<th><fmt:message key="status"/></th>
							<th><fmt:message key="time.amount"/></th>
                             <th><fmt:message key="set.time"/></th>
                             <th><fmt:message key="remove"/></th>
						</tr>
					</thead>

					<tfoot>
						<tr>

							<th><fmt:message key="activity.name"/></th>
							<th><fmt:message key="activity.category"/></th>
							<th><fmt:message key="status"/></th>
							<th><fmt:message key="time.amount"/></th>
                             <th><fmt:message key="set.time"/></th>
                             <th><fmt:message key="remove"/></th>
						</tr>
					</tfoot>

					<tbody>
					    <c:forEach var="entry" items="${activities}">
						   <form action="/TimeKeeping/controller" method="post" action="/TimeKeeping/controller" id="tab">
    					        <input type="hidden" name="action" value="set_amount" />
    					        <input type="hidden" name="activity_name" value="${entry.getActivityName()}" />

    					   	<tr>
                            <td>
                                ${entry.getActivityName()}
					        </td>
                            <td>
                                ${entry.getCategory()}
					        </td>
		                    <td>
                                        <fmt:message key="${entry.getStatus()}"/>
        					</td>
        					<td>
                                <input name="amount" type="number" value="${entry.getActivityTime()}" class="input-xlarge">
    <!------    ---------->


					          </td>

                              <td class="form-group">
                                  <input class="btn btn-success" type="submit" name="set_amount" value="<fmt:message key="edit"/>" ${entry.getStatus() eq 'pending.removing'? 'disabled': ''}>
                                  <input type="hidden" name="activity_name" value="${entry.getActivityName()}" />


                              </td>
                             </form>



					        <form action="/TimeKeeping/controller" method="post" action="/TimeKeeping/controller" id="tab">

                                <td class="form-group">
                                                    <input class="btn btn-danger" type="submit" name="submit_reg" value="<fmt:message key="remove"/>" ${entry.getStatus() eq 'pending.removing'? 'disabled': ''}>
                                                    <input type="hidden" name="activity_name" value="${entry.getActivityName()}" />
                                                    <input type="hidden" name="action" value="remove_activity" />

                                </td>
                            </form>
					</c:forEach>





 					</tbody>


				</table>

 <%--For displaying Previous link except for the 1st page --%>
    <c:if test="${currentPage != 1}">
        <td><a href="/TimeKeeping/controller?action=user_activities&page=${currentPage - 1}"><fmt:message key="previous"/></a></td>
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
                        <td><a href="/TimeKeeping/controller?action=user_activities&page=${i}">${i}</a></td>

                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </tr>
    </table>

    <%--For displaying Next link --%>
    <c:if test="${currentPage lt noOfPages}">
        <td><a href="/TimeKeeping/controller?action=user_activities&page=${currentPage + 1}"><fmt:message key="next"/></a></td>
    </c:if>




		<form action="/TimeKeeping/controller" method="post" action="/TimeKeeping/controller" id="fileForm" role="form">
                    <input type="hidden" name="action" value="add_activity" />
                    <label><fmt:message key="add.activity"/></label>
		            <select name = "new_activity">
                        <c:forEach var="item" items="${to_add}">
                        <option>${item.getName()}</option>
                        </c:forEach>
                    </select>
                    <input class="btn btn-success" type="submit" name="submit_add" value="+ <fmt:message key="add"/>">


	</div>
	</div>
</div>

<div class="modal fade" id="edit" tabindex="-1" role="dialog" aria-labelledby="edit" aria-hidden="true">
      <div class="modal-dialog">
    <div class="modal-content">
          <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h4 class="modal-title custom_align" id="Heading">Edit Your Detail</h4>
      </div>
          <div class="modal-body">
          <div class="form-group">
        <input class="form-control " type="text" placeholder="Tiger Nixon">
        </div>
        <div class="form-group">

        <input class="form-control " type="text" placeholder="System Architect">
        </div>
        <div class="form-group">


      <input class="form-control " type="text" placeholder="Edinburgh">

        </div>
      </div>
          <div class="modal-footer ">
        <button type="button" class="btn btn-warning btn-lg" style="width: 100%;"><span class="glyphicon glyphicon-ok-sign"></span> Update</button>
      </div>
        </div>
    <!-- /.modal-content -->
  </div>
      <!-- /.modal-dialog -->
    </div>



    <div class="modal fade" id="delete" tabindex="-1" role="dialog" aria-labelledby="edit" aria-hidden="true">
      <div class="modal-dialog">
    <div class="modal-content">
          <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h4 class="modal-title custom_align" id="Heading">Delete this entry</h4>
      </div>
          <div class="modal-body">

       <div class="alert alert-danger"><span class="glyphicon glyphicon-warning-sign"></span> Are you sure you want to delete this Record?</div>

      </div>
        <div class="modal-footer ">
        <button type="button" class="btn btn-success" ><span class="glyphicon glyphicon-ok-sign"></span> Yes</button>
        <button type="button" class="btn btn-default" data-dismiss="modal"><span class="glyphicon glyphicon-remove"></span> No</button>
      </div>
        </div>
    <!-- /.modal-content -->
  </div>
      <!-- /.modal-dialog -->
    </div>
    </body>