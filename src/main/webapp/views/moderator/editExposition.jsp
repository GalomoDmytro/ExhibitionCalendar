<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
   <head>
      <title>Edit Ex</title>
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
               <h1>Edit Exposition</h1>
               <h2>${role}</h2>
               <br><br>
               <form action="${pageContext.request.contextPath}/controller?command=editExposition" method="post">
                  Id:
                  <c:out value="${idEdit}" />
                  <br/>
                  Expo Title:<input type="text" name="title" value="<c:out value='${title}'/>"/><br/>
                  Expo imgSrc:<input type="text" name="imageSrc" value="${imgSrc}"/><br/>
                  <table border="0" cellpadding="5">
                     <tr>
                        <th>Lang</th>
                        <th>Text</th>
                     </tr>
                     <c:forEach var="langDescript" items="${mapLang}" >
                        <tr>
                           <td>Tag:<input type="text" name="<c:out value='${langDescript.key}'/>" value="<c:out value='${langDescript.key}'/>"/></td>
                           <td>Text:<input type="text" name="<c:out value='${langDescript.value}'/>" value="${langDescript.value}"/></td>
                        </tr>
                     </c:forEach>
                  </table>
                  New Lang Tag:<input type="text" name="newTag" />New Text:<input type="text" name="newTextDescription" /><br/>
                  <button class="submitBtn" type="submit" name="editExpo" value="Save">Save</button>
                  <button class="resetBtn" type="submit" name="denieEdit" value="Cancel">Cancel</button>
                  <br> ${error}
               </form>
               <br>
               <a class="blueButton" href="${pageContext.request.contextPath}/controller?command=moderatorHome">moderator home</a>
            </section>
         </div>
      </center>
      <footer>
         <jsp:include page="../utility/footer.jsp" />
      </footer>
   </body>
</html>