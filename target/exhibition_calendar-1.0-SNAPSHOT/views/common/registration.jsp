<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
   <head>
      <title>LogIn</title>
   </head>

   <body>
      <center>
            <h1>Registration</h1>
            <h2>${role}</h2>

            <!-- Registration form-->
            <form action="${pageContext.request.contextPath}/controller?command=registration" method="post">
                Name profile:<input type="text" name="name"/><br/>
                ${errorNameProfile}<br/>

                Password:<input type="password" name="password"/><br/>
                Repeat password:<input type="password" name="passwordRepeat"/> <br/>
                ${errorPassword}<br/>

                First name:<input type="text" name="firstName"/><br/><br/>
                Last name:<input type="text" name="lastName"/><br/><br/>
                eMail:<input type="text" name="eMail"/> <br/>
                Repeat eMail:<input type="text" name="eMailRepeat"/> <br/>
                ${errorMail}<br/>
                Phone number:<input type="text" name="phone"/><br/><br/>

                <button type="submit" value="Submit">Submit</button>
                <button type="reset" value="Reset">Reset</button>
            </form>
            <!-- /Registration form-->

            <br>
            <a href="${pageContext.request.contextPath}/controller?command=home">go home</a>
      </center>
   </body>
</html>
