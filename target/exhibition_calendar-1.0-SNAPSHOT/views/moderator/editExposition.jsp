<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page errorPage="../utility/error.jsp" %>

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
               <br><br>
               <form action="${pageContext.request.contextPath}/controller?command=editExposition" method="post">
                  Id:
                  <c:out value='${exhibition.id}'/>
                  <br/>
                  Expo Title:<input type="text" name="title" value="<c:out value='${exhibition.title}'/>" min="1" max="255" required/><br/>
                  <c:if test="${exhibition.imgSrc != null}">
                    <img src="<c:out value='${exhibition.imgSrc}'/>" alt="Exhibition poster" style="max-height: 150px; max-width: 150px;">
                  </c:if>
                  <br/>
                  Expo imgSrc:<input type="text" name="imgSrc" value="<c:out value='${exhibition.imgSrc}'/>"/><br/>

                  <br/>
                     <table>
                     <c:forEach var="langDescript" items="${mapLang}" >
                        <tr>
                           <td>Tag:<input type="text" max="15" name="<c:out value='${langDescript.key}'/>" value="<c:out value='${langDescript.key}'/>" autocomplete="nope"/></td>
                           <td><textarea name="val:<c:out value='${langDescript.key}'/>" rows="4" cols="50" autocomplete="nope">${langDescript.value}</textarea>   </td>
                        </tr>
                     </c:forEach>
                     </table>
                  <br/>

                  <table>
                        <tr>
                           <td>New Lang Tag:</td>
                           <td>New Text Description:</td>
                        </tr>
                        <tr>
                           <td>Tag:<input type="text" max="15" name="newTextLabel" value="<c:out value='${langDescript.key}'/>" autocomplete="nope"/></td>
                           <td><textarea name="newTextDescription" rows="4" cols="50" autocomplete="nope"></textarea></td>
                        </tr>
                  </table>
                  <button class="submitBtn" type="submit" name="editExpo" value="Save">Save</button>
                  <br> ${error}
               </form>
               <br>

               <form action="${pageContext.request.contextPath}/controller?command=expoManagement" method="post">
                    <input class="resetBtn" type="submit"  value="Cancel">
               </form>

                <form action="${pageContext.request.contextPath}/controller?command=moderatorHome" method="post">
                    <input class="blueButton" type="submit" value="moderator home">
                </form>
            </section>
         </div>
      </center>
      <footer>
         <jsp:include page="../utility/footer.jsp" />
      </footer>
   </body>
</html>