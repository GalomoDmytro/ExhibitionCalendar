<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page errorPage="../utility/error.jsp" %>

<html>
   <head>
      <title>Purchase Processing</title>
      <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
   </head>
   <body>
      <header>
         <div>
            <jsp:include page="../utility/header.jsp" />
         </div>
      </header>

      <center>
         <div class="content">
            <section class="main">
               <h1>Thank you for being with us.</h1>
               <h2>Your request has been sent for processing.</h2>
               <br/>
               <hr>
               <div align="center">
                    <form action="${pageContext.request.contextPath}/controller?command=home" method="post">
                        <input  class="blueButton" type="submit" value="go home"/>
                    </form>
               </div>
      </center>
      <footer>
         <jsp:include page="../utility/footer.jsp" />
      </footer>
   </body>
</html>