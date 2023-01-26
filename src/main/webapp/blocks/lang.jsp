<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:setBundle basename="resources"/>

<form method="POST" class="d-flex" >
    <label style="color: #d3d3d3">
    <input type="radio" name="locale" value="uk_UA" ${sessionScope.locale eq 'uk_UA' ? 'checked' : ''} onchange="submit()">
           <fmt:message key="ua"/>
    <input type="radio" name="locale" value="en" ${sessionScope.locale eq 'en' ? 'checked' : ''} onchange="submit()">
           <fmt:message key="en"/>
    </label>
</form>