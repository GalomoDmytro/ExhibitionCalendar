<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
   <head>
      <title>Edit Ex</title>
   </head>

   <body>
      <center>
            <h1>Edit Exposition</h1>
            <h2>${role}</h2>
            <br><br>

            <form action="${pageContext.request.contextPath}/controller?command=editExposition" method="post">
                Id:<c:out value="${idEdit}" /><br/>

                Expo Center Title:<input type="text" name="title" value="<c:out value='${expoTitle}'/>"/><br/>
                Expo Center eMail:<input type="text" name="imageSrc" value="${imgSrc}"/><br/>

                <table border="0" cellpadding="5">

                                <tr>
                                    <th>Lang</th>
                                    <th>Text</th>
                                </tr>

                                <c:forEach var="langDescript" items="${mapLang}" >
                                    <tr>
                                        <td>Tag:<input type="text" name="tag" value="<c:out value='${langDescript.key}'/>"/></td>
                                        <td>Text:<input type="text" name="textDescription" value="${langDescript.value}"/></td>
                                    </tr>
                                </c:forEach>

                 </table>

                New Lang Tag:<input type="text" name="tag" value="<c:out value='${langTag}'/>"/>New Text:<input type="text" name="textDescription" value="${descText}"/><br/>New Lang Text:<input type="text" name="textDescription" value="${descText}"/><br/>

                <button type="submit" name="editExpo" value="Save">Save</button>
                <button type="submit" name="denieEdit" value="Cancel">Cancel</button>
                <br> ${error}
            </form>


            <br>
            <a href="${pageContext.request.contextPath}/controller?command=moderatorHome">moderator home</a>
            <br>
            <a href="${pageContext.request.contextPath}/controller?command=home">go home</a>
            <br>
            <a href="${pageContext.request.contextPath}/controller?command=logout">logout</a>
      </center>
   </body>
</html>
