<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page errorPage="../utility/error.jsp" %>
<c:choose>
   <c:when test="${langBundle == null}">
      <fmt:setBundle basename="strings_ru"/>
   </c:when>
   <c:otherwise>
      <fmt:setBundle basename="${langBundle}"/>
   </c:otherwise>
</c:choose>
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
               <h1><fmt:message key="title.addExhibitionCenter"/></h1>
               <br>
               ${confirmAdd}
               <br>
               <form action="${pageContext.request.contextPath}/controller?command=addExpoCenter" method="post">
                  <input type="text" name="title"  placeholder="Expo Center Title" min="1" max="45" required/><br/>
                  <input type="text" name="address" placeholder="Expo Center Address"  min="1" max="255" required/><br/>
                  <input type="email" name="eMail" placeholder="eMail" max="255"/><br/>
                  <input type="email" name="eMail_repeat" placeholder="Repeat eMail" max="255"/><br/>
                  <input type="text" name="webPage" placeholder="webPage" max="255"/><br/>
                  <input type="text" name="phone1" placeholder="Phone 1" max="45"/><br/>
                  <input type="text" name="phone2" placeholder="Phone 2" max="45"/><br/>
                  <button class="submitBtn" type="submit" name="addNewExpoCenter" value="Submit">Add</button>
                  <button class="resetBtn" type="reset" value="Reset">Reset</button>
                  <br> ${error}
               </form>
               <br>
               <a class="blueButton" href="${pageContext.request.contextPath}/controller?command=moderatorHome">moderator home</a>
               <br>

         </div>
         </section>
         <footer>
            <jsp:include page="../utility/footer.jsp" />
         </footer>
      </center>
   </body>
</html>