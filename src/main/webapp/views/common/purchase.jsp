<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
                        <td>${dateBuy}</td>
                        <td>${nameExpo}</td>
                        <td>${nameExpoCen}</td>
                        <td>${addressExpoCen}</td>
                        <td>${price}</td>
                        <td>${ticketsAvailable}</td>
                        <td></td>
                     </tr>
                  </table>
                  <form action="${pageContext.request.contextPath}/controller?command=purchase" method="post">
                     <input type="email" name="eMail" value="${eMailHold}" placeholder="eMail"/><br>
                     Quantity<input type="number" name="quantity" value="1" min="1" max="10"/><br><br>
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