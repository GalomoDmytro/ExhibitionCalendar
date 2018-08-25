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
               <h1>Add Exhibition Center</h1>
               <br><br>
               <form action="${pageContext.request.contextPath}/controller?command=addExpoCenter" method="post">
                  <input type="text" name="title"  placeholder="Expo Center Title" required/><br/>
                  <input type="text" name="address" placeholder="Expo Center Address" required/><br/>
                  <input type="email" name="eMail" placeholder="eMail" /><br/>
                  <input type="email" name="eMail_repeat" placeholder="Repeat eMail"/><br/>
                  <input type="text" name="webPage" placeholder="webPage"/><br/>
                  <input type="text" name="phone1" placeholder="Phone 1"/><br/>
                  <input type="text" name="phone2" placeholder="Phone 2"/><br/>
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