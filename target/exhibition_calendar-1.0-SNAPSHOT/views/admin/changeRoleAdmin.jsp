<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
   <head>
      <title>Admin</title>
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
               <h1>Admin page</h1>
               <h2>${role}</h2>
               <!-- Registration form-->
               <p>Set role to user</p>
               <form action="${pageContext.request.contextPath}/controller?command=admin" method="post">
                  <input type="text" name="id" placeholder="id of user"/> <br/>
                  or <input type="text" name="eMail" placeholder="eMail of user"/> <br/>
                  <select name="role" size="1">
                     <option value="user" selected="selected">USER</option>
                     <option value="moderator">MODERATOR</option>
                     <option value="admin">ADMIN</option>
                  </select>
                  </br>
                  <button class="submitBtn" type="submit" name="action" value="changeRole">Change Role</button>
                  <button class="resetBtn" type="reset" value="Reset">Reset</button>
                  <button class="submitBtn" type="submit" name="action" value="getRole">Get User Role</button>
                  <br> ${showRole} ${mess}
               </form>
               <!-- /Registration form-->
               <br>
            </section>
         </div>
      </center>
      <footer>
         <jsp:include page="../utility/footer.jsp" />
      </footer>
   </body>
</html>