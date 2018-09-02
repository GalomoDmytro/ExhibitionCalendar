<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page errorPage="/utility/error.jsp" %>
<c:choose>
   <c:when test="${langBundle == null}">
      <fmt:setBundle basename="strings_ru"/>
   </c:when>
   <c:otherwise>
      <fmt:setBundle basename="${langBundle}"/>
   </c:otherwise>
</c:choose>
<!DOCTYPE html>
<html>
   <head>
      <title>Purchase</title>
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
               <h1><fmt:message key="purchase.title"/></h1>
               <br/>
               <hr>
               <div align="center">
                  <table border="1" cellpadding="7">
                     <caption>
                        <h2><fmt:message key="purchase.checkVal"/></h2>
                     </caption>
                     <tr>
                        <th><fmt:message key="purchase.date"/></th>
                        <th><fmt:message key="purchase.nameExpo"/></th>
                        <th><fmt:message key="purchase.nameExpoCenter"/></th>
                        <th><fmt:message key="purchase.address"/></th>
                        <th><fmt:message key="purchase.price"/></th>
                        <th><fmt:message key="purchase.ticketsAv"/></th>
                        <th></th>
                     </tr>
                     <tr>
                        <td>${dateTicketToApply}</td>
                        <td>${exhibition.title}</td>
                        <td>${exhibitionCenter.title}</td>
                        <td>${exhibitionCenter.address}</td>
                        <td>${contract.ticketPrice}</td>
                        <td>${ticketsAv}</td>
                        <td></td>
                     </tr>
                  </table>
                  <form action="${pageContext.request.contextPath}/controller?command=checkOut" method="post">
                     <table>
                        <tr>
                           <td></td>
                           <td> <input type="email" name="eMail" value="${eMailHold}" placeholder="eMail" autocomplete="off"required/></td>
                        </tr>
                        <tr>
                           <td>Quantity</td>
                           <td><input type="number" name="quantity"  value="1" min="1" max="10"/><br><br></td>
                        </tr>
                     </table>
                     <input name="priceTickets" type="hidden" value="${contract.ticketPrice}">
                     <input name="dateTicketToApply" type="hidden" value="${dateTicketToApply}">
                     <input name="idContract" type="hidden" value="${idContract}">
                     <input class="submitBtn"  type="submit" name="buy" value='<fmt:message key="btn.buy"/>' />
                  </form>
                  <br/><br/>
                  <form action="${pageContext.request.contextPath}/controller?command=home" method="post">
                     <input  class="blueButton" type="submit" value='<fmt:message key="btn.goHome"/>'/>
                  </form>
               </div>
            </section>
         </div>
      </center>
      <footer>
         <jsp:include page="../utility/footer.jsp" />
      </footer>
   </body>
</html>