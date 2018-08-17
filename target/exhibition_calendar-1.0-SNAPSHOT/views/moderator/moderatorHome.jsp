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

            <a href="${pageContext.request.contextPath}/controller?command=expoCenterManagement">Expo Center management</a>
            <br>
            <a href="${pageContext.request.contextPath}/controller?command=expoManagement">Expo Management</a>
            <br>
            <a href="${pageContext.request.contextPath}/controller?command=contractManagement">Contract Management</a>
            <br>
            <a href="${pageContext.request.contextPath}/controller?command=addExposition">Create Expo</a>
            <br>
            <a href="${pageContext.request.contextPath}/controller?command=addExpoCenter">Create Expo center</a>
            <br>
            <a href="${pageContext.request.contextPath}/controller?command=home">Go home</a>
            <br>
            <a href="${pageContext.request.contextPath}/controller?command=logout">Logout</a>
      </center>
   </body>
</html>
