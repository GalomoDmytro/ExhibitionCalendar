<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:choose>
   <c:when test="${langBundle == null}">
      <fmt:setBundle basename="strings_ru"/>
   </c:when>
   <c:otherwise>
      <fmt:setBundle basename="${langBundle}"/>
   </c:otherwise>
</c:choose>
<html>
   <head>
      <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
   </head>
   <body>

<c:choose>
   <c:when test="${langBundle == 'strings_eng'}">
              <a  href="${pageContext.request.contextPath}/controller?command=changeLang">ru</a>
   </c:when>
   <c:otherwise>
                    <a  href="${pageContext.request.contextPath}/controller?command=changeLang">eng</a>
   </c:otherwise>
</c:choose>

        <c:choose>
            <c:when test="${role == 'MODERATOR' || role == 'ADMIN'}">
               <a  href="${pageContext.request.contextPath}/controller?command=moderatorHome"><fmt:message key="header.moderator"/></a>
            </c:when>

         </c:choose>

        <c:choose>
            <c:when test="${role == 'ADMIN'}">
               <a  href="${pageContext.request.contextPath}/controller?command=admin"><fmt:message key="header.admin"/></a>
            </c:when>

         </c:choose>

      <c:choose>
         <c:when test="${role == 'USER' || role == 'MODERATOR' || role == 'ADMIN'}">
            ${userName}  <a  href="${pageContext.request.contextPath}/controller?command=logout"><fmt:message key="header.logout"/></a>
            &nbsp;&nbsp;
            <a  href="${pageContext.request.contextPath}/controller?command=userHome"><fmt:message key="header.userInfo"/></a><br>
         </c:when>
         <c:otherwise>
            <a  href="${pageContext.request.contextPath}/controller?command=login"><fmt:message key="header.login"/></a>
            &nbsp;&nbsp;
            <a  href="${pageContext.request.contextPath}/controller?command=registration"><fmt:message key="header.registration"/></a><br>
         </c:otherwise>
      </c:choose>
   </body>
</html>