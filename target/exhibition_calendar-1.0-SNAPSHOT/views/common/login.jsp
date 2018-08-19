<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
   <head>
      <title>LogIn</title>
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
          <section class="login">
             <h1>Login</h1>

             <h2>${role}</h2>
             <form action="${pageContext.request.contextPath}/controller?command=login" method="post">
                <input type="email" name="eMail" placeholder="Email"/><br/><br/>
                <input type="password" name="password" placeholder="Password"/><br/>
                ${errorLogin}<br>
                <input  class="submitBtn" class="submitBtn" type="submit" value="login"/>
             </form>
             <br>
             <form action="${pageContext.request.contextPath}/controller?command=registration" method="post">
                <input  class="blueButton" type="submit" value="registration"/>
             </form>
             <form action="${pageContext.request.contextPath}/controller?command=home" method="post">
                <input  class="blueButton" type="submit" value="go home"/>
             </form>

          </ section>
      </div>

      </center>

      <footer>
        <jsp:include page="../utility/footer.jsp" />
      </footer>
   </body>
</html>