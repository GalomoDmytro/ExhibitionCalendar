<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
               <h2>${role}</h2>
               <!-- Registration form-->
               <form action="${pageContext.request.contextPath}/controller?command=registration" method="post">
                  <input type="text" name="name" placeholder="*Profile Name"/><br/>
                  ${errorNameProfile}<br/>
                  <input type="password" name="password" placeholder="*Password"/><br/>
                  <input type="password" name="passwordRepeat" placeholder="*Repeat password"/><br/>
                  ${errorPassword}<br/>
                  <input type="text" name="firstName" placeholder="First name"/><br/>
                  <input type="text" name="lastName" placeholder="Last name"/><br/><br/>
                  <input type="email" name="eMail" placeholder="*eMail"/><br/>
                  <input type="email" name="eMailRepeat" placeholder="*Repeat eMail"/><br/>
                  ${errorMail}<br/>
                  <input type="text" name="phone1" placeholder="Phone number 1"/><br/>
                  <input type="text" name="phone2" placeholder="Phone number 2"/><br/><br/>
                  <button class="submitBtn" type="submit" value="Submit">Submit</button>
                  <button class="resetBtn" type="reset" value="Reset">Reset</button>
               </form>
               <!-- /Registration form-->
               <br>
               <a class="blueButton" href="${pageContext.request.contextPath}/controller?command=home">go home</a>
         </div>
         </section>
      </center>
      <footer>
         <jsp:include page="../utility/footer.jsp" />
      </footer>
   </body>
</html>