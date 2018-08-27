<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page errorPage="../utility/error.jsp" %>

<!DOCTYPE html>
   <head>
       <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
      <title>Edit ExC</title>
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
               <h1>Edit expo Center</h1>
               <h2>${role}</h2>
               <br><br>
               <form action="${pageContext.request.contextPath}/controller?command=editExpositionCenter" method="post">
                  Id:
                  <c:out value="${idEdit}" />
                  <br/>
                  Expo Center Title:<input type="text" name="expoCTitle" value="<c:out value='${expoCTitle}'/>" min="1" max="45" required/><br/>
                  Expo Center Address:<input type="text" name="expoCAddress" value="${expoCAddress}" min="1" max="255" required/><br/>
                  Expo Center eMail:<input type="text" name="expoCMail" value="${expoCMail}" max="255"/><br/>
                  Expo Center webPage:<input type="text" name="expoCWebPage" value="${expoCWebPage}" max="255"/><br/>
                  Expo Center Phone 1:<input type="text" name="phone1" value="${expoCPhone1}" max="255"/><br/>
                  Expo Center Phone 2:<input type="text" name="phone2" value="${expoCPhone2}" max="255"/><br/>
                  <button class="submitBtn" type="submit" name="editExpoCenter" value="Save">Save</button>
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