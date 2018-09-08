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
<!DOCTYPE html>
   <head>
       <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
      <title>Edit ExC</title>
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
               <h1><fmt:message key="title.editExpositionCenter"/></h1>
               <h2>${role}</h2>
               <br><br>
               <form action="${pageContext.request.contextPath}/controller?command=editExpositionCenter" method="post">
                  Id:
                  <c:out value="${idEdit}" />
                  <br/>
                  <table>
                    <tr>
                        <td>Expo Center Title:</td>
                        <td><input type="text" name="expoCTitle" value="${exhibitionCenter.title}" min="1" max="45" required/></td>
                    </tr>

                    <tr>
                        <td>Expo Center Address:</td>
                        <td><input type="text" name="expoCAddress" value="${exhibitionCenter.address}" min="1" max="255" required/></td>
                    </tr>

                    <tr>
                        <td>Expo Center eMail:</td>
                        <td><input type="email" name="expoCMail" value="${exhibitionCenter.eMail}" max="255" autocomplete="nope"/></td>
                    </tr>

                    <tr>
                        <td>Expo Center webPage:</td>
                        <td><input type="text" name="expoCWebPage" value="${exhibitionCenter.webPage}" max="255" /></td>
                    </tr>


                    <c:forEach var="expoCenterPhone" items="${phones}" varStatus="loop" >
                    <tr>
                        <td>phone ${loop.index + 1}</td>
                        <td><input type="text" name='phone${loop.index}' value="${expoCenterPhone}" max="255" autocomplete="nope"/></td>
                    </tr>
                    </c:forEach>
                  </table>




                  <button class="submitBtn" type="submit" name="editExpoCenter" value="Save">Save</button>
                  <button class="resetBtn" type="submit" name="denieEdit" value="Cancel">Cancel</button>
                  <br> ${error}
               </form>
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