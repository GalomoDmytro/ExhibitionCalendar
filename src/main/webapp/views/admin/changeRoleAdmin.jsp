<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
   <head>
      <title>Admin</title>
   </head>

   <body>
      <center>
            <h1>Admin page</h1>
            <h2>${role}</h2>

            <!-- Registration form-->
            <p>Set role to user</p>
            <form action="${pageContext.request.contextPath}/controller?command=admin" method="post">
                Enter  id of user<input type="text" name="id"/> <br/>
                or eMail of user<input type="text" name="eMail"/> <br/>
                <select name="role" size="1">
                    <option value="user" selected="selected">USER</option>
                    <option  value="author">AUTHOR</option>
                    <option value="moderator">MODERATOR</option>
                    <option value="admin">ADMIN</option>
                </select>

                <button type="submit" name="action" value="changeRole">Change Role</button>
                <button type="reset" value="Reset">Reset</button>
                <button type="submit" name="action" value="getRole">Get User Role</button>
                <br> ${showRole} ${mess}
            </form>
            <!-- /Registration form-->

            <br>
            <a href="${pageContext.request.contextPath}/controller?command=home">go home</a>
      </center>
   </body>
</html>
