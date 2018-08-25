<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page errorPage="../utility/error.jsp" %>

<html>
   <head>
      <title>Add Exhibition</title>
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
               <h1>Add Exhibition</h1>
               <h2>${role}</h2>
               <br><br>
               <form action="${pageContext.request.contextPath}/controller?command=addExposition" method="post">
                  Expo Title:<input type="text" name="title"/><br/>
                  Expo imgSrc:<input type="text" name="imgSrc"/><br/>
                  Expo Description:<input type="text" name="description"/><br/>
                  Expo Description Lang:
                  <select name="lang" size="1">
                     <option value="RU" selected="selected">Russian</option>
                     <option  value="ENG">English</option>
                  </select>
                  <br/>
                  <button class="submitBtn" type="submit" name="addNewExpo" value="Submit">Add</button>
                  <button class="resetBtn" type="reset" value="Reset">Reset</button>
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