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

            <br/>

         <br/>
        <a  href="${pageContext.request.contextPath}/controller?command=home">show</a>
		<hr>
		<p>Search</p>

		<hr>
		<div align="center">
        <table border="1" cellpadding="7">
            <caption><h2>Expo info</h2></caption>
            <tr>

                <th>Name Expo</th>
                <th>Author</th>
                <th>Date From</th>
                <th>Date To</th>
                <th>Expo Center</th>
                <th>Addr</th>
            </tr>
            <c:forEach var="list2" items="${list}">
                <tr>
                    <td><c:out value="${list2.id}" /></td>
                    <td><c:out value="${list2.name}" /></td>


                </tr>
            </c:forEach>
        </table>
    </div>


      </center>
   </body>
</html>
