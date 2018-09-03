<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page errorPage="../utility/error.jsp" %>
<html>
   <head>
      <title>ExpoCenter</title>
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
               <h1>Moderator: Expo Center Management</h1>
               <br>
               <form action="${pageContext.request.contextPath}/controller?command=expoManagement" method="post">
                  Search<input type="text" name="searchField">
                  <input type="submit" name="search" value="go" />
               </form>
               <!-- table -->
               <table border="1" cellpadding="5">
                  <caption>
                     <h2>List of Expo</h2>
                  </caption>
                  <tr>
                     <th>ID</th>
                     <th>Title</th>
                     <th>Img Src</th>
                     <th>Description languages</ht>
                     <th>Action</th>
                  </tr>
                  <c:forEach var="expo" items="${listExpo}" >
                     <tr>
                        <td>
                           <c:out value="${expo.id}" />
                        </td>
                        <td>
                           <c:out value="${expo.title}" />
                        </td>
                        <td>
                           <c:out value="${expo.imgSrc}" />
                        </td>
                        <td>
                           <c:out value="${expo.languageTags}" />
                        </td>
                        <td>
                        <form action="${pageContext.request.contextPath}/controller?command=editExposition" method="post">
                            <input type="hidden" name="expoId" value="<c:out value='${expo.id}'/>">
                            <input type="submit" value="Edit"/>
                        </form>
                           &nbsp;&nbsp;&nbsp;&nbsp;
                           <a href="${pageContext.request.contextPath}/controller?command=expoManagement&idDelete=<c:out value='${expo.id}'/>">Delete</a>
                           &nbsp;&nbsp;&nbsp;&nbsp;

                           <a href="${pageContext.request.contextPath}/controller?command=combineExpoWithCenter&idExhibition=<c:out value='${expo.id}'/>">Form_contract</a>
                        </td>
                     </tr>
                  </c:forEach>
               </table>
               <!-- /table -->
               <br>
               ${errorDeleting}
               <br>
               <br>
               <a class="blueButton" href="${pageContext.request.contextPath}/controller?command=moderatorHome">moderator home</a>
               <br>
            </section>
         </div>
      </center>
      <footer>
         <jsp:include page="../utility/footer.jsp" />
      </footer>
   </body>
</html>