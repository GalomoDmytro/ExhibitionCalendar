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
      <title>Exhibition info</title>
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
               <h1>
                  <fmt:message key="expositionInfo.titlePage"/>
               </h1>
               <br><br>
               <img src="<c:out value='${exhibition.imgSrc}'/>" alt="Exhibition poster" style="max-height: 250px; max-width: 250px;">
               <br/>
               <fmt:message key="expositionInfo.address"/>
               <p>
                  <c:out value='${ExhibitionCenter.address}'/>
               </p>
               <br/>
               <p>
                  <fmt:message key="home.fromDate"/>
                  :
                  <c:out value='${contract.dateFrom}'/>
               </p>
               <p>
                  <fmt:message key="home.toDate"/>
                  :
                  <c:out value='${contract.dateTo}'/>
               </p>
               <br/>
               <fmt:message key="expositionInfo.titleExpo"/>
               :
               <c:out value='${exhibition.title}'/>
               <br/>
               <br/>
               <c:forEach var="desc" items="${description}">
                  <p>
                     <c:out value = "${desc}"/>
                  </p>
                  <br/>
               </c:forEach>
               <br>
               <p>
               <form action="${pageContext.request.contextPath}/controller?command=purchase" method="post">
                  <input type="date" name="searchDateLine" value="${searchDate}" min='<fmt:formatDate value="${contract.dateFrom}" pattern="yyyy-MM-dd" />' max='<fmt:formatDate value="${contract.dateTo}" pattern="yyyy-MM-dd" />'/>
                  <input type="hidden" name="idContract" value="<c:out value='${contract.id}'/>">
                  <input type="hidden" name="dateFromExhibitionStart" value='${contract.dateFrom}'>
                  <input class="blueButton" type="submit" value='<fmt:message key="btn.buy"/>'>
               </form>
               </p>
               <br/>
               <a class="blueButton" href="${pageContext.request.contextPath}/controller?command=home">
                  <fmt:message key="btn.goHome"/>
               </a>
            </section>
         </div>
      </center>
      <footer>
         <jsp:include page="../utility/footer.jsp" />
      </footer>
   </body>
</html>