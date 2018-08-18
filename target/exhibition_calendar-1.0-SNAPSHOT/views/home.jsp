<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
   <head>
      <title>Exhibition Calendar</title>
   </head>

   <body>
      <center>
        <h1>Exhibition calendar</h1>
        <h2>${role}</h2>
         <br/>
             <a  href="${pageContext.request.contextPath}/controller?command=login">login</a><br>
             <a  href="${pageContext.request.contextPath}/controller?command=registration">registration</a>
             <a  href="${pageContext.request.contextPath}/controller?command=admin">admin page</a>
             <a  href="${pageContext.request.contextPath}/controller?command=moderatorHome">moderator page</a>
             <br/>
             <a  href="${pageContext.request.contextPath}/controller?command=logout">logout</a>
            <br/>

         <br/>
        <a  href="${pageContext.request.contextPath}/controller?command=home">show</a>
		<hr>
		<form action="${pageContext.request.contextPath}/controller?command=home" method="post">
                        Search<input type="text" name="searchField">
                        Date<input type="text" name="searchDate">
                        <input type="submit" name="search" value="go" />
                    </form>

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

                    <td>btn: more info</td>
                    <td><a href="${pageContext.request.contextPath}/controller?command=purchase&idContract=<c:out value='${list.id}'/>&dateTicket=">Buy</a></td>

                </tr>
            </c:forEach>
        </table>
    </div>


      </center>
   </body>
</html>
