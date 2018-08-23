<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
   <head>
      <title>Exhibition info</title>
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
               <h1>Exposition info</h1>

               <br><br>

                  <br/>
                  Expo Title:<c:out value='${title}'/><br/>
                  Expo imgSrc:<c:out value='${imgSrc}'/><br/>
                  Description<c:out value='${description}'/><br/>
                  </table>

               <br>
               <a class="blueButton" href="${pageContext.request.contextPath}/controller?command=home">home</a>
            </section>
         </div>
      </center>
      <footer>
         <jsp:include page="../utility/footer.jsp" />
      </footer>
   </body>
</html>