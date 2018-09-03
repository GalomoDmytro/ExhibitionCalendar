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
               <h2>Edit contract:</h2>
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
                        <c:out value='${exhibition.id}'/>
                     </th>
                     <th>
                        <c:out value='${exhibition.title}'/>
                     </th>
                     <th>
                        <c:out value='${exhibition.languageTags}'/>
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
                        <c:out value='${exhibitionCenter.id}'/>
                     </th>
                     <th>
                        <c:out value='${exhibitionCenter.title}'/>
                     </th>
                     <th>
                        <c:out value='${exhibitionCenter.address}'/>
                     </th>
                     <th>
                        <c:out value='${exhibitionCenter.eMail}'/>
                     </th>
                     <th>
                        <c:out value='${exhibitionCenter.webPage}'/>
                     </th>
                     <th>
                        <c:forEach var="phones" items="${exhibitionCenterPhones}" >

                            <c:out value='${phones}'/><br/>
                        </c:forEach>

                     </th>
                  </tr>
               </table>
               <!-- Contract -->
               <h3>Contract :</h3>
               <form action="${pageContext.request.contextPath}/controller?command=editContract" method="post">
               <table>
               <tr>
                  <td>Date from:</td><td><input type="date" name="dateFrom" value='${contract.getDateFrom()}' min="2000-01-01" autocomplete="nope" required/></td>
               </tr>
               <tr>
                  <td>End date:</td><td><input type="date" name="dateTo" value='${contract.getDateTo()}' min="2000-01-01" autocomplete="nope" required/></td>
               </tr>
               <tr>
                  <td>Price (coins):</td><td><input type="number" name="price" value='${contract.getTicketPrice()}' min="0" autocomplete="nope" required/></td>
               </tr>
               <tr>
                  <td>Work Time:</td><td><input type="text" name="workTime" value='${contract.getWorkTime()}' max="64"/></td>
               </tr>
               <tr>
                  <td>Max Ticket per day:</td><td><input type="number" name="maxTicketDay" value='${contract.getMaxTicketPerDay()}' min="0" required/></td>
               </tr>

               </table>
                  <input type="hidden"  name="exhibitionId" value='${exhibition.id}'>
                  <input type="hidden"  name="exhibitionCenterId" value='${exhibitionCenter.id}'>
                  <button class="submitBtn" type="submit" name="saveChangesContract" value="Submit">Save</button>
                  <button class="resetBtn" type="reset" value="Reset">Cancel</button>
                  <br>
                  <h4>${insertInfo}</h4>
               </form>
               <!-- /Contract -->
               <br>
               <h2>${error}</h2>
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