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
      <title>Registration</title>
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
               <!-- Registration form-->
               <form action="${pageContext.request.contextPath}/controller?command=registration" method="post">
                  <input type="text" name="name" placeholder="*Profile Name" max="24" pattern="^[a-zA-Z][a-zA-Z0-9-_\.]{1,20}$" title="Should start with letter, and contain 2-20 characters. Allowed latin and numbers." required/><br/>
                  ${errorNameProfile}<br/>
                  <input type="password" name="password" placeholder="*Password" min="6" max="64" pattern="^(?=.*[0-9])(?=.*[a-zа-я])(?=.*[A-ZА-Я])(?=\S+$).{6,}$" title="start-of-string;a digit must occur at least once; a lower case letter must occur at least once; an upper case letter must occur at least once ; no whitespace allowed in the entire string;anything, at least 6 places though" required/><br/>

                  <input type="password" name="passwordRepeat" placeholder="*Repeat password" min="6" max="64" required/><br/>
                  ${errorPassword}<br/>
                  <input type="text" name="firstName" placeholder="First name" max="25" max="100"/><br/>
                  <input type="text" name="lastName" placeholder="Last name" max="100"/><br/><br/>
                  <input type="email" name="eMail" placeholder="*eMail" max="255" required/><br/>
                  <input type="email" name="eMailRepeat" placeholder="*Repeat eMail" max="255" required><br/>
                  ${errorMail}<br/>
                  <input type="text" name="phone1" placeholder="Phone number 1" max="45"/><br/>
                  <input type="text" name="phone2" placeholder="Phone number 2" max="45"/><br/>
                  ${errorPhone}<br/>
                  <button class="submitBtn" type="submit" value="Submit"><fmt:message key="btn.submit"/></button>
                  <button class="resetBtn" type="reset" value="Reset"><fmt:message key="btn.reset"/></button>
               </form>
               <!-- /Registration form-->
               <br>
               <a class="blueButton" href="${pageContext.request.contextPath}/controller?command=home"><fmt:message key="btn.goHome"/></a>
         </div>
         </section>
      </center>
      <footer>
         <jsp:include page="../utility/footer.jsp" />
      </footer>
   </body>
</html>