<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
   <head>
      <title>LogIn</title>
   </head>

   <body>
      <center>
            <h1>Login</h1>
            <h2>${role}</h2>
            <form action="${pageContext.request.contextPath}/controller?command=login" method="post">
                Email:<input type="text" name="name"/><br/><br/>
                Password:<input type="password" name="password"/><br/>
                <input type="submit" value="login"/>
            </form>
            <form action="${pageContext.request.contextPath}/controller?command=registration" method="post">
                <input type="submit" value="registration"/>
            </form>
            <br>
            <a href="${pageContext.request.contextPath}/controller?command=home">go home</a>
      </center>
   </body>
</html>
