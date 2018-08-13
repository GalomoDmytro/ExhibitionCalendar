<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
   <head>
      <title>MHP</title>
   </head>

   <body>
      <center>
            <h1>Moderator Home Page</h1>
            <h2>${role}</h2>
            <br><br>

            <a href="${pageContext.request.contextPath}/controller?command=expoCenterManagement">expo center management</a>
            <br>
            <a href="${pageContext.request.contextPath}/controller?command=addExpoCenter">go createexpo center</a>
            <br>
            <a href="${pageContext.request.contextPath}/controller?command=home">go home</a>
            <br>
            <a href="${pageContext.request.contextPath}/controller?command=logout">logout</a>
      </center>
   </body>
</html>
