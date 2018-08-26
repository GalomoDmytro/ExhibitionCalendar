<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page errorPage="/utility/error.jsp" %>

<c:choose>
   <c:when test="${langBundle == null}">
      <fmt:setBundle basename="strings_ru"/>
   </c:when>
   <c:otherwise>
      <fmt:setBundle basename="${langBundle}"/>
   </c:otherwise>
</c:choose>
<!DOCTYPE html>
<html>
   <head>
   <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
      <title>Exhibition Calendar</title>
      <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/style.css">
      <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/bootstrap.css">
   </head>
   <body>
      <header>
         <div>
            <jsp:include page="utility/header.jsp" />
         </div>
      </header>
      <center>
         <div class="content">
            <section class="main">
               <h1>
                  Approved ticket archive
               </h1>


               <hr>
               <div align="center">
                  <table border="1" cellpadding="7" >
                     <caption>
                     </caption>
                     <tr>
                        <th>

                        </th>
                     </tr>
                     <c:forEach var="list" items="${approvedTicket}">
                        <tr>
                           <td>
                           </td>

                           <td>
                              &nbsp;
                              <form action="${pageContext.request.contextPath}/controller?command=" method="post">
                              </form>
                           </td>
                           <td>
                              &nbsp;
                              <form action="${pageContext.request.contextPath}/controller?command=" method="post">
                              </form>
                           </td>
                        </tr>
                     </c:forEach>
                  </table>


         </div>
         <form action="${pageContext.request.contextPath}/controller?command=moderatorHome" method="post">
                         <input type="submit" value="Moderator home page"/>
                     </form>
         </section>
         <footer>
            <jsp:include page="utility/footer.jsp" />
         </footer>
      </center>
   </body>
</html>