
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
   <head>
      <title>Exhibition Calendar</title>
   </head>

   <body>
      <center>
        <h1>USER INFO</h1>
        <h2>${role}</h2>
         <br/>
             <a  href="${pageContext.request.contextPath}/controller?command=logout">logout</a>
            <br/>

         <br/>

		<hr>
		nickname<br/>
		eMail<br/>
		First name<br/>
		Last name<br/>
		Phone 1<br/>
		Phone 2<br/>

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
            <c:forEach var="list" items="${listTickets}">
                <tr>
                    <td><c:out value="${list.exhibitionTitle}" /></td>
                    <td><c:out value="${list.exhibitionCenterTitle}" /></td>
                    <td></td>
                    <td></td>

                    <td><a href="${pageContext.request.contextPath}/controller?command=expoInfo&idContract=<c:out value='${list.id}'/>">Expo info</a></td>

                </tr>
            </c:forEach>
        </table>
    </div>


      </center>
   </body>
</html>
