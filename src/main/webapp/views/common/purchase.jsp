<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
   <head>
      <title>Purchase</title>
   </head>

   <body>
      <center>
        <h1>Purchase</h1>
        <h2>${role}</h2>
        <br/>

   		<hr>

		<div align="center">

        <table border="1" cellpadding="7">
            <caption><h2>Expo info</h2></caption>
            <tr>

                <th>Date</th>
                <th>Name Expo</th>
                <th>Expo Center</th>
                <th>Address</th>
                <th>Price</th>
                <th></th>
                <th></th>
            </tr>

            <c:forEach var="list" items="${listForCustomer}">
                <tr>

                </tr>
            </c:forEach>
            </table>




            <form action="${pageContext.request.contextPath}/controller?command=purchase" method="post">
                eMail<input type="text" name="eMail" value="${eMailHold}"><br>
                Quantity<input type="number" name="quantity" min="0"><br>
                <input type="submit" name="search" value="buy" />
                <input type="submit" name="cancel" value="cancel"/>
            </form>


   </div>


      </center>
   </body>
</html>
