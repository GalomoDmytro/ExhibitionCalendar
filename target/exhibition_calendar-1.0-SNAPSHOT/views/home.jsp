<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
   <head>
      <meta charset="utf-8">
      <title>Exhibition Calendar</title>
      <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
   </head>
   <body>
      <header>
         <div>
            <jsp:include page="utility/header.jsp" />
         </div>
      </header>
      <center>
         <div class="content">
            <section class="main">
               <h1>EXHIBITION CALENDAR</h1>
               <br/>
               <hr>
               <form action="${pageContext.request.contextPath}/controller?command=home" method="post">
                  <input type="text" name="searchField" placeholder="Search">
                  Date<input type="date" name="searchDate">
                  <input class="submitBtn" type="submit" name="search" value="Search" />
                  <input class="resetBtn" type="reset" name="search" value="reset" />
               </form>
               <hr>
               <div align="center">
                  <table border="1" cellpadding="7">
                     <caption>
                        <h2>Expo info</h2>
                     </caption>
                     <tr>
                        <th>Name Expo</th>
                        <th>Expo Center</th>
                        <th>Address</th>
                        <th>img</th>
                        <th></th>
                        <th></th>
                     </tr>
                     <c:forEach var="list" items="${listForCustomer}">
                        <tr>
                           <td>
                              <c:out value="${list.exhibitionTitle}" />
                           </td>
                           <td>
                              <c:out value="${list.exhibitionCenterTitle}" />
                           </td>
                           <td></td>
                           <td></td>
                           <td>&nbsp;<a class="blueButton" href="${pageContext.request.contextPath}/controller?command=expoInfo&idContract=<c:out value='${list.id}'/>"> Expo info </a></td>
                           <td>&nbsp;<a class="blueButton" href="${pageContext.request.contextPath}/controller?command=purchase&idContract=<c:out value='${list.id}'/>&dateTicket="> Buy </a></td>
                        </tr>
                     </c:forEach>
                  </table>
               </div>
         </div>
         </section>
         <footer>
            <jsp:include page="utility/footer.jsp" />
         </footer>
      </center>
   </body>
</html>