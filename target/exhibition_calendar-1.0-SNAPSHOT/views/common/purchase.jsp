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
               <h1>Purchase</h1>
               <br/>
               <hr>
               <div align="center">
                  <table border="1" cellpadding="7">
                     <caption>
                        <h2>Expo info</h2>
                     </caption>
                     <tr>
                        <th>Date</th>
                        <th>Name Expo</th>
                        <th>Expo Center</th>
                        <th>Address</th>
                        <th>Price</th>
                        <th>ticket?</th>
                        <th></th>
                     </tr>
                     <tr>
                        <td>${dateTicketToApply}</td>
                        <td>${nameExpo}</td>
                        <td>${nameExpoCen}</td>
                        <td>${addressExpoCen}</td>
                        <td>${price}</td>
                        <td>${ticketsAvailable}</td>
                        <td></td>
                     </tr>
                  </table>
                  <form action="${pageContext.request.contextPath}/controller?command=checkOut" method="post">
                     <table>
                        <tr>
                           <td></td>
                           <td> <input type="email" name="eMail" value="${eMailHold}" placeholder="eMail" required/></td>
                        </tr>
                        <tr>
                           <td>Quantity</td>
                           <td><input type="number" name="quantity"  value="1" min="1" max="10"/><br><br></td>
                        </tr>
                     </table>
                     <input name="priceTickets" type="hidden" value="${price}">
                     <input name="dateTicketToApply" type="hidden" value="${dateTicketToApply}">
                     <input name="idContract" type="hidden" value="${idContract}">
                     <input class="submitBtn"  type="submit" name="buy" value="buy" />
                     <input class="resetBtn" type="submit" name="cancel" value="cancel"/>
                  </form>
                  <br/><br/>
                  <form action="${pageContext.request.contextPath}/controller?command=home" method="post">
                     <input  class="blueButton" type="submit" value="go home"/>
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