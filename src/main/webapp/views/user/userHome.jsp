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
      <title>User Info</title>
      <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/style.css">
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
               <h1>
                  <fmt:message key='userInfo.title'/>
               </h1>
               <br/>
               <br/>
               <table>
                  <tr>
                     <td>
                        <fmt:message key='common.profileName'/>
                     </td>
                     <td>${user.name}</td>
                  </tr>
                  <tr>
                     <td>
                        <fmt:message key='common.eMail'/>
                     </td>
                     <td>${user.mail}</td>
                  </tr>
                  <tr>
                     <td>
                        <fmt:message key='common.firstName'/>
                     </td>
                     <td>${user.firstName}</td>
                  </tr>
                  <tr>
                     <td>
                        <fmt:message key='common.lastName'/>
                     </td>
                     <td>${user.lastName}</td>
                  </tr>
                  <tr>
                     <td>
                        <fmt:message key='common.phoneNumber'/>
                     </td>
                     <td>
                        <c:forEach var="phone" items="${phonesList}">
                           <c:out value = "${phone}"/>
                           <br/>
                        </c:forEach>
                     </td>
                  </tr>
                  <tr>
                     <td>
                        <fmt:message key='common.purchasedTickets'/>
                     </td>
                     <td>
                        <c:forEach var="ticket" items="${ticketList}">
                           <c:out value = "${ticket}"/>
                           <br/>
                           <hr/>
                        </c:forEach>
                     </td>
                  </tr>
               </table>
               <br/>
               <form action="${pageContext.request.contextPath}/controller?command=home" method="post">
                  <input  class="blueButton" type="submit" value='<fmt:message key="btn.goHome"/>'/>
               </form>
            </section>
         </div>
      </center>
      <footer>
         <jsp:include page="../utility/footer.jsp" />
      </footer>
   </body>
</html>