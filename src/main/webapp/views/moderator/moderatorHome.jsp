<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page errorPage="../utility/error.jsp" %>

<html>
   <head>
      <title>MHP</title>
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
               <h1>Moderator Home Page</h1>
               <br>
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
            </section>
         </div>
      </center>
      <footer>
         <jsp:include page="../utility/footer.jsp" />
      </footer>
   </body>
</html>