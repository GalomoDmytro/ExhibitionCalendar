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
               <form action="${pageContext.request.contextPath}/controller?command=expoCenterManagement" method="post">
                  <input type="submit" value="Expo Center management">
               </form>
               <form action="${pageContext.request.contextPath}/controller?command=expoManagement" method="post">
                  <input type="submit" value="Expo Management">
               </form>
               <form action="${pageContext.request.contextPath}/controller?command=contractManagement" method="post">
                  <input type="submit" value="Contract Management">
               </form>
               <form action="${pageContext.request.contextPath}/controller?command=addExpoCenter" method="post">
                  <input type="submit" value="Create Expo center">
               </form>
               <form action="${pageContext.request.contextPath}/controller?command=addExposition" method="post">
                  <input type="submit" value="Create Expo">
               </form>
               <form action="${pageContext.request.contextPath}/controller?command=waitApprovalTicket" method="post">
                  <input type="submit" value="Wait Approval Ticket">
               </form>
               <form action="${pageContext.request.contextPath}/controller?command=approvedTicket" method="post">
                  <input type="submit" value="Approved Ticket">
               </form>
               <br/>
               <form action="${pageContext.request.contextPath}/controller?command=home" method="post">
                  <input type="submit" value="Go home">
               </form>
            </section>
         </div>
      </center>
      <footer>
         <jsp:include page="../utility/footer.jsp" />
      </footer>
   </body>
</html>