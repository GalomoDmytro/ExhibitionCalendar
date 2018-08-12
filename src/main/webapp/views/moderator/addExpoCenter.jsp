<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
   <head>
      <title>ExpoCenter</title>
   </head>

   <body>
      <center>
            <h1>Moderator: ExpoCenter</h1>
            <h2>${role}</h2>
            <br><br>

            <form action="${pageContext.request.contextPath}/controller?command=moderatorExpoCenter" method="post">
                Expo Center Title:<input type="text" name="title"/><br/>
                Expo Center Address:<input type="text" name="address"/><br/>
                Expo Center eMail:<input type="text" name="eMail"/><br/>
                Repeat eMail:<input type="text" name="eMail_repeat"/><br/>
                Expo Center webPage:<input type="text" name="webPage"/><br/>
                Expo Center Phone 1:<input type="text" name="phone1"/><br/>
                Expo Center Phone 2:<input type="text" name="phone2"/><br/>

                <button type="submit" name="addNewExpoCenter" value="Submit">Add</button>
                <button type="reset" value="Reset">Reset</button>
                <br> ${error}
            </form>


            <br>
            <a href="${pageContext.request.contextPath}/controller?command=moderatorHome">moderator home</a>
            <br>
            <a href="${pageContext.request.contextPath}/controller?command=home">go home</a>
            <br>
            <a href="${pageContext.request.contextPath}/controller?command=logout">logout</a>
      </center>
   </body>
</html>
