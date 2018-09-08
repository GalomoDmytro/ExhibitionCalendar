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
               <h1><fmt:message key="title.createContract"/></h1>
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
               <jsp:useBean id="now" class="java.util.Date"/>
                  Date from<input type="date" name="dateFrom" required min='<fmt:formatDate value="${now}" pattern="yyyy-MM-dd" />'/><br/>
                  Date to<input type="date" name="dateTo" required min='<fmt:formatDate value="${now}" pattern="yyyy-MM-dd" />'/><br/>
                  <input type="number" name="price" placeholder="Price" min="0" required/><br/>
                  <input type="text" name="workTime" placeholder="Work Time" max="64"/><br/>
                  <input type="number" name="maxTicketDay" placeholder="Max Ticket per day" min="0" required/><br/>
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