<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page errorPage="../utility/error.jsp" %>

<html>
   <head>
      <title>Create Contract</title>
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
               <h1>Create Contract</h1>
               <br>
               <h2>You want to create a contract with:</h2>
               <table border="1" cellpadding="3">
                  <caption>
                     <h3>Exhibition</h3>
                  </caption>
                  <tr>
                     <th>ID</th>
                     <th>Title</th>
                     <th>Languages</th>
                  </tr>
                  <tr>
                     <th>
                        <c:out value='${exhibitionId}'/>
                     </th>
                     <th>
                        <c:out value='${exhibitionTitle}'/>
                     </th>
                     <th>
                        <c:out value='${exhibitionLangTags}'/>
                     </th>
                  </tr>
               </table>
               <table border="1" cellpadding="5">
                  <caption>
                     <h3>Exhibition Center</h3>
                  </caption>
                  <tr>
                     <th>ID</th>
                     <th>Title</th>
                     <th>Address</th>
                     <th>eMail</th>
                     <th>Web page</th>
                     <th>Phones</th>
                  </tr>
                  <tr>
                     <th>
                        <c:out value='${exhibitionCenterId}'/>
                     </th>
                     <th>
                        <c:out value='${exhibitionCenterTitle}'/>
                     </th>
                     <th>
                        <c:out value='${exhibitionAddress}'/>
                     </th>
                     <th>
                        <c:out value='${exhibitionMail}'/>
                     </th>
                     <th>
                        <c:out value='${exhibitionWebPage}'/>
                     </th>
                     <th>
                        <c:out value='${exhibitionCenterPhones}'/>
                     </th>
                  </tr>
               </table>
               <!-- Contract -->
               <h3>Contract :</h3>
               <form action="${pageContext.request.contextPath}/controller?command=createContract" method="post">
                  Date from<input type="date" name="dateFrom" required="required"/><br/>
                  Date to<input type="date" name="dateTo" required="required"/><br/>
                  <input type="text" name="price" placeholder="Price" required="required"/><br/>
                  <input type="text" name="workTime" placeholder="Work Time" /><br/>
                  <input type="text" name="maxTicketDay" placeholder="Max Ticket per day" required="required"/><br/>
                  <button class="submitBtn" type="submit" name="addNewContract" value="Submit">Add</button>
                  <button class="resetBtn" type="reset" value="Reset">Reset</button>
                  <br> ${error}
                  <br>
                  <h4>
                  ${insertInfo}
                  </h4>
               </form>
               <!-- /Contract -->
               <br>
               <a class="blueButton" href="${pageContext.request.contextPath}/controller?command=moderatorHome">moderator home</a>
               <br>
               <br>

            </section>
         </div>
      </center>
      <footer>
         <jsp:include page="../utility/footer.jsp" />
      </footer>
   </body>
</html>