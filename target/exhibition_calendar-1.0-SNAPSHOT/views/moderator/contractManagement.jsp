<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
   <head>
      <title>Contract management</title>
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
               <h1>Moderator: Contract management</h1>
               <br>
               <form action="${pageContext.request.contextPath}/controller?command=contractManagement" method="post">
                  Search<input type="text" name="searchField">
                  <input class="submitBtn" type="submit" name="search" value="Search" />
               </form>
               <!-- table -->
               <table border="1" cellpadding="5">
                  <caption>
                     <h2>List of Expo Centers</h2>
                  </caption>
                  <tr>
                     <th>ID</th>
                     <th>ExC. id</th>
                     <th>ExC. Title</th>
                     <th>Exh. id</ht>
                     <th>Exh. Title</th>
                     <th>Date Fr.</th>
                     <th>Date To</th>
                     <th>T. Price</th>
                     <th>Work Tm.</th>
                     <th>T. MaxForDay</th>
                     <th>Action</th>
                  </tr>
                  <c:forEach var="contract" items="${listContract}" >
                     <tr>
                        <td>
                           <c:out value="${contract.id}" />
                        </td>
                        <td>
                           <c:out value="${contract.exhibitionId}" />
                        </td>
                        <td>
                           <c:out value="${contract.exhibitionTitle}" />
                        </td>
                        <td>
                           <c:out value="${contract.exCenterId}" />
                        </td>
                        <td>
                           <c:out value="${contract.exhibitionCenterTitle}" />
                        </td>
                        <td>
                           <c:out value="${contract.dateFrom}" />
                        </td>
                        <td>
                           <c:out value="${contract.dateTo}" />
                        </td>
                        <td>
                           <c:out value="${contract.ticketPrice}" />
                        </td>
                        <td>
                           <c:out value="${contract.workTime}" />
                        </td>
                        <td>
                           <c:out value="${contract.maxTicketPerDay}" />
                        </td>
                        <td><a class="submitBtn" href="${pageContext.request.contextPath}/controller?command=editContract&idEdit=<c:out value='${contract.id}'/>">Edit</a>
                           &nbsp;&nbsp;&nbsp;&nbsp;
                           <a class="resetBtn" href="${pageContext.request.contextPath}/controller?command=contractManagement&idDelete=<c:out value='${contract.id}'/>">Delete</a>
                        </td>
                     </tr>
                  </c:forEach>
               </table>
               <!-- /table -->
               <br>
               <a class="blueButton" href="${pageContext.request.contextPath}/controller?command=moderatorHome">moderator home</a>
               <br>
            </section>
         </div>
      </center>
      <footer>
         <jsp:include page="../utility/footer.jsp" />
      </footer>
   </body>
</html>