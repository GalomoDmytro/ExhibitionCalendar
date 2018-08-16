<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
   <head>
      <title>Combine Exhibition Center with Exhibition</title>
   </head>

   <body>
      <center>
            <h1>Combine Exhibition Center with Exhibition</h1>
            <br>
            <h2>You want to create a contract with:</h2>

            <table border="1" cellpadding="5">
                <caption><h2>List of Expo Centers</h2></caption>
                <tr>
                    <th>ID</th>
                    <th>Title</th>
                    <th>Action</th>
                </tr>

                <c:forEach var="combine" items="${list}" >
                    <tr>
                        <td><c:out value="${combine.id}" /></td>
                        <td><c:out value="${combine.title}" /></td>
                        <td><a href="${pageContext.request.contextPath}/controller?command=createContract&combineFrom=<c:out value='${combFrom}'/>&combineFromId=<c:out value='${combFromId}'/>&combineToId=<c:out value='${combine.id}'/>">Create_contract</a></td>
                    </tr>
                </c:forEach>

            </table>

            <br>
            <a href="${pageContext.request.contextPath}/controller?command=moderatorHome">moderator home</a>
            <br>
            <a href="${pageContext.request.contextPath}/controller?command=home">go home</a>
            <br>
            <a href="${pageContext.request.contextPath}/controller?command=logout">logout</a>


      </center>
   </body>
</html>
