<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page errorPage="../utility/error.jsp" %>
<!DOCTYPE html>
<head>
   <meta charset="utf-8">
   <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
   <title>Checkout Form</title>
</head>
<body>
   <header>
      <div>
         <jsp:include page="../utility/header.jsp" />
      </div>
   </header>
   <div class="checkout">
      <form action="${pageContext.request.contextPath}/controller?command=checkOut" method="post">
         <div class="checkout-header">
            <h1 class="checkout-title">
               Checkout
               <span class="checkout-price">
                  <c:out value='${price}'/>
               </span>
            </h1>
         </div>
         <p>
            <input type="hidden" name="quantityTickets" value="<c:out value='${quantity}'/>">
            <input type="hidden" name="priceToPay" value="<c:out value='${price}'/>">
            <input type="hidden" name="eMail" value="${eMail}">
            <input type="hidden" name="dateTicketToApply" value="${dateTicketToApply}">
            <input type="text" class="checkout-input checkout-name" placeholder="Your name" autocomplete="off" autofocus required>
            <input type="text" class="checkout-input checkout-exp" placeholder="MM" maxlength="2" autocomplete="off" required>
            <input type="text" class="checkout-input checkout-exp" placeholder="YY" maxlength="2" autocomplete="off" required>
         </p>
         <p>
            <input type="text" class="checkout-input checkout-card" placeholder="1111 1111 1111 1111" autocomplete="off" required>
            <input type="text" class="checkout-input checkout-cvc" style="font-size: 11px;" placeholder="CVC" maxlength="4" autocomplete="off" required>
         </p>
         <p>
            <input type="submit" class="checkout-btn" name="checkoutPurchase" value="Checkout Purchase">
         </p>
      </form>
      <br/>

      <form action="${pageContext.request.contextPath}/controller?command=home" method="post">
         <input type="submit" value="DENIE" class="denie-btn">
      </form>
   </div>
   <footer>
      <jsp:include page="../utility/footer.jsp" />
   </footer>
</body>
</html>