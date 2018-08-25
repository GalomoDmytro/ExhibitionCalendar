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
                  <fmt:message key="home.title"/>
               </h1>
               <br/>
               <hr>
               <form action="${pageContext.request.contextPath}/controller?command=home" method="post">
                  <input type="text" name="searchField" value="${searchField}" placeholder='<fmt:message key="home.search"/>'>
                  <jsp:useBean id="now" class="java.util.Date"/>
                  <input type="date" name="searchDate" value="${searchDate}" min='<fmt:formatDate value="${now}" pattern="yyyy-MM-dd" />'/>
                  <input class="submitBtn" type="submit" name="search" value='<fmt:message key="home.search"/>' />
               </form>
               <hr>
               <div align="center">
                  <table border="1" cellpadding="7" >
                     <caption>
                     </caption>
                     <tr>
                        <th>
                           <fmt:message key="home.NameExpo"/>
                        </th>
                        <th>
                           <fmt:message key="home.ExpoCenter"/>
                        </th>
                        <th>
                           <fmt:message key="home.Address"/>
                        </th>
                        <th>
                           <fmt:message key="home.toDate"/>
                        </th>
                        <th></th>
                     </tr>
                     <c:forEach var="list" items="${listForCustomer}">
                        <tr>
                           <td>
                              <c:out value="${list.exhibitionTitle}" />
                           </td>
                           <td>
                              <c:out value="${list.exhibitionCenterTitle}" />
                           </td>
                           <td>
                           </td>
                           <td>
                              <c:out value="${list.dateTo}" />
                           </td>
                           <td>
                           </td>
                           <td>
                              &nbsp;
                              <a class="blueButton" href="${pageContext.request.contextPath}/controller?command=expoInfo&idContract=<c:out value='${list.id}'/>">
                                 <fmt:message key="btn.expoInfo"/>
                              </a>
                           </td>
                           <td>
                              &nbsp;
                              <a class="blueButton" href="${pageContext.request.contextPath}/controller?command=purchase&idContract=<c:out value='${list.id}'/>&dateTicket=<c:out value='${searchDate}'/>">
                                 <fmt:message key="btn.buy"/>
                              </a>
                           </td>
                        </tr>
                     </c:forEach>
                  </table>
                  <br/>
                  <br/>
                  <br/>
                  <br/>
                  <div>
                      <ul class="pagination">
                         <c:forEach begin="1" end="${numberOfPages}" var="i">
                            <c:choose>
                               <c:when test="${currentPage eq i}">
                                  <li class="page-item active"><a class="page-link">
                                     ${i} <span class="sr-only">(current)</span></a>
                                  </li>
                               </c:when>
                               <c:otherwise>
                                  <li class="page-item"><a class="page-link"
                                     href="${pageContext.request.contextPath}/controller?command=home&currentPage=${i}&searchField=${searchField}&searchDate=${searchDate}">${i}</a>
                                  </li>
                               </c:otherwise>
                            </c:choose>
                         </c:forEach>
                      </ul>
                  </div>
               </div>
         </div>
         </section>
         <footer>
            <jsp:include page="utility/footer.jsp" />
         </footer>
      </center>
   </body>
</html>