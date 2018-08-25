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
  	<form action="${pageContext.request.contextPath}/controller?command=purchaseProcessing" method="post">
      <div class="checkout-header">
        <h1 class="checkout-title">
          Checkout
          <span class="checkout-price">$10</span>
        </h1>
      </div>
      <p>
        <input type="text" class="checkout-input checkout-name" placeholder="Your name" autocomplete="off" autofocus>
        <input type="text" class="checkout-input checkout-exp" placeholder="MM" maxlength="2" autocomplete="off">
        <input type="text" class="checkout-input checkout-exp" placeholder="YY" maxlength="2" autocomplete="off">
      </p>
      <p>
        <input type="text" class="checkout-input checkout-card" placeholder="1111 1111 1111 1111" autocomplete="off">
        <input type="text" class="checkout-input checkout-cvc" style="font-size: 11px;" placeholder="CVC" maxlength="4" autocomplete="off">
      </p>
      <p>
        <input type="submit" value="Purchase" class="checkout-btn">
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
