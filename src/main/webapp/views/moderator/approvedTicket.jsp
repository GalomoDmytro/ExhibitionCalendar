<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page errorPage="../utility/error.jsp" %>
<html>
   <head>
      <title>Create Contract</title>
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
               <h1>Approved Ticket</h1>
               <br>
               <h2>Edit contract:</h2>
               <table border="1" cellpadding="8">
                  <tr>
                     <th>ID</th>
                     <th>date to apply</th>
                     <th>contract id</th>
                     <th>date transaction</th>
                     <th>user mail</th>
                     <th>id user</th>
                     <th>approved by</th>
                  </tr>
                  <c:forEach var="list" items="${listTickets}">
                     <tr>
                        <td>
                           <c:out value="${list.id}" />
                        </td>
                        <td>
                           <c:out value="${list.dateToApply}" />
                        </td>
                        <td>
                           <c:out value="${list.contractId}" />
                        </td>
                        <td>
                           <c:out value="${list.dateTransaction}" />
                        </td>
                        <td>
                           <c:out value="${list.userEMail}" />
                        </td>
                        <td>
                           <c:out value="${list.userId}" />
                        </td>
                        <td>
                           <c:out value="${list.approvedById}" />
                        </td>
                     </tr>
                  </c:forEach>
                  </tr>
               </table>
               <br/>
               <a class="blueButton" href="${pageContext.request.contextPath}/controller?command=moderatorHome">moderator home</a>
            </section>
         </div>
      </center>
      <footer>
         <jsp:include page="../utility/footer.jsp" />
      </footer>
   </body>
</html>