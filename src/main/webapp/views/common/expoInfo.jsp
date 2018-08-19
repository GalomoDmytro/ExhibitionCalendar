<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
   <head>
      <title>Exhibition info</title>
   </head>

   <body>
      <center>
        <h1>Exhibition info</h1>
        <h2>${role}</h2>
         <br/>
             <a  href="${pageContext.request.contextPath}/controller?command=login">login</a><br>
             <a  href="${pageContext.request.contextPath}/controller?command=registration">registration</a>
             <br/>
             <a  href="${pageContext.request.contextPath}/controller?command=logout">logout</a>
            <br/>

         <br/>
        <a  href="${pageContext.request.contextPath}/controller?command=home">show</a>


		<hr>


		<div align="center">

        <table border="1" cellpadding="7">
            <caption><h2>Expo info</h2></caption>
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
                    <td><c:out value="${list.exhibitionTitle}" /></td>
                    <td><c:out value="${list.exhibitionCenterTitle}" /></td>
                    <td></td>
                    <td></td>

                    <td><a href="${pageContext.request.contextPath}/controller?command=purchase&idContract=<c:out value='${list.id}'/>&dateTicket=">Buy</a></td>

                </tr>
            </c:forEach>
        </table>
         <br><a href="${pageContext.request.contextPath}/controller?command=home">go home</a>
                    <br>
    </div>


      </center>
   </body>
</html>
