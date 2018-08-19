<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
   <head>
   </head>
   <body>
        <c:choose>
            <c:when test="${role == 'MODERATOR' || role == 'ADMIN'}">
               <a  href="${pageContext.request.contextPath}/controller?command=moderatorHome">moderator page</a>
            </c:when>

         </c:choose>

        <c:choose>
            <c:when test="${role == 'ADMIN'}">
               <a  href="${pageContext.request.contextPath}/controller?command=admin">admin page</a>
            </c:when>

         </c:choose>

      <c:choose>
         <c:when test="${role == 'USER' || role == 'MODERATOR' || role == 'ADMIN'}">
            ${userName}  <a  href="${pageContext.request.contextPath}/controller?command=logout">logout</a>
            &nbsp;&nbsp;
            <a  href="${pageContext.request.contextPath}/controller?command=userHome">user info</a><br>
         </c:when>
         <c:otherwise>
            <a  href="${pageContext.request.contextPath}/controller?command=login">login</a>
            &nbsp;&nbsp;
            <a  href="${pageContext.request.contextPath}/controller?command=registration">registration</a><br>
         </c:otherwise>
      </c:choose>
   </body>
</html>