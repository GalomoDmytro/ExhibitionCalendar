<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page errorPage="../utility/error.jsp" %>
<c:choose>
   <c:when test="${langBundle == null}">
      <fmt:setBundle basename="strings_ru"/>
   </c:when>
   <c:otherwise>
      <fmt:setBundle basename="${langBundle}"/>
   </c:otherwise>
</c:choose>
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
               <h1><fmt:message key="title.waitApproval"/></h1>
               <br>
               <h2>Edit contract:</h2>
               <table border="1" cellpadding="8">

                  <tr>
                     <th>ID</th>
                     <th>date to apply</th>
                     <th>contract id</th>
                     <th>date transaction</th>
                     <th>user mail</th>
                     <th>quantity</th>
                     <th>id user</th>
                     <th>action</th>
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
                           <c:out value="${list.quantity}" />
                        </td>
                        <td>
                           <c:out value="${list.userId}" />
                        </td>
                        <td>
                           <form action="${pageContext.request.contextPath}/controller?command=waitApprovalTicket" method="post">
                                <input type="hidden" name="idTicket" value="${list.id}">
                                <input type="submit" name="action" value="Approve">
                                <br/>
                                <input type="submit" name="action" value="Delete">
                           </form>
                        </td>
                     </tr>
                     </c:forEach>

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