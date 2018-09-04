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
             <form action="${pageContext.request.contextPath}/controller?command=login" method="post">
                <input type="text" min="2" max="255" name="nameOrMail" placeholder="<fmt:message key='common.profileNameOrMail'/>" required/><br/><br/>
                <input type="password" name="password" placeholder="<fmt:message key='common.password'/>" min="6" max="64" required/><br/>
                ${errorLogin}<br>
                <input  class="submitBtn" class="submitBtn" type="submit" value="login" name="loginBtn"/>
             </form>
             <br>
             <form action="${pageContext.request.contextPath}/controller?command=registration" method="post">
                <input  class="blueButton" type="submit" value='<fmt:message key="header.registration"/>'/>
             </form>
             <br/>
             <form action="${pageContext.request.contextPath}/controller?command=home" method="post">
                <input  class="blueButton" type="submit" value='<fmt:message key="btn.goHome"/>'/>
             </form>

          </ section>
      </div>

      </center>

      <footer>
        <jsp:include page="../utility/footer.jsp" />
      </footer>
   </body>
</html>