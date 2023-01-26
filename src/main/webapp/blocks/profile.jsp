<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:setBundle basename="resources"/>

<form action="/TimeKeeping/controller?action=show_profile" method="get" class="d-flex" >
    <input type="hidden" name="action" value="show_profile" />
        <button class="btn btn-link btn-lg">
          <span class="glyphicon glyphicon-user"></span> <fmt:message key="profile"/>
        </button>
</form>