<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
   <head>
      <title>ExpoCenter</title>
   </head>

   <body>
      <center>
            <h1>Moderator: Expo Center Management</h1>
            <br>
            <form action="${pageContext.request.contextPath}/controller?command=expoManagement" method="post">
                Search<input type="text" name="searchField">
                <input type="submit" name="search" value="go" />
            </form>

            <!-- table -->
            <table border="1" cellpadding="5">
                <caption><h2>List of Expo</h2></caption>
                <tr>
                    <th>ID</th>
                    <th>Title</th>
                    <th>Img Src</th>
                    <th>Description languages</ht>

                    <th>Action</th>
                </tr>

                <c:forEach var="expo" items="${listExpo}" >
                    <tr>
                        <td><c:out value="${expo.id}" /></td>
                        <td><c:out value="${expo.title}" /></td>
                        <td><c:out value="${expo.imgSrc}" /></td>
                        <td><c:out value="${expo.languageTags}" /></td>

                        <td><a href="${pageContext.request.contextPath}/controller?command=editExposition&expoId=<c:out value='${expo.id}'/>&expoTitle=<c:out value='${expo.title}'/>">Edit</a>
                            &nbsp;&nbsp;&nbsp;&nbsp;
                            <a href="${pageContext.request.contextPath}/controller?command=expoManagement&idDelete=<c:out value='${expo.id}'/>">Delete</a>
                        </td>
                    </tr>
                </c:forEach>

            </table>

            <!-- /table -->
            <br>
            <a href="${pageContext.request.contextPath}/controller?command=moderatorHome">moderator home</a>
            <br>
            <a href="${pageContext.request.contextPath}/controller?command=home">go home</a>
            <br>
            <a href="${pageContext.request.contextPath}/controller?command=logout">logout</a>
      </center>
   </body>
</html>
