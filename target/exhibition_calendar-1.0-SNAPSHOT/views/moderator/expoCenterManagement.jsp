<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page errorPage="../utility/error.jsp" %>

<!DOCTYPE html>
   <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
      <title>ExpoCenterManagement</title>
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
               <h1>Exhibitions Centers Management</h1>
               <br>
               <form action="${pageContext.request.contextPath}/controller?command=expoCenterManagement" method="post">
                  Search<input type="text" name="searchField">
                  <input class="submitBtn" type="submit" name="search" value="Search" />
               </form>
               <!-- table -->

               <table border="1" cellpadding="8" style="table-layout: fixed; width: 100%;">
                  <caption>
                     <h2>List of Expo Centers</h2>
                  </caption>
                  <tr>
                     <th>ID</th>
                     <th>Title</th>
                     <th>Address</th>
                     <th>eMail</ht>
                     <th>webPage</th>
                     <th>phone 1</th>
                     <th>phone 2</th>
                     <th>Action</th>
                  </tr>
                  <c:forEach var="expoCenter" items="${listExpoCenter}" >
                     <tr>
                        <td>
                           <c:out value="${expoCenter.id}" />
                        </td>
                        <td>
                           <c:out value="${expoCenter.title}" />
                        </td>
                        <td>
                           <c:out value="${expoCenter.address}" />
                        </td>
                        <td>
                           <c:out value="${expoCenter.eMail}" />
                        </td>
                        <td>
                           <c:out value="${expoCenter.webPage}" />
                        </td>
                        <td>
                           <c:forEach var="expoCenterPhone" items="${expoCenter.phone}" >
                                <c:out value="${expoCenterPhone}" />
                           </c:forEach>
                        </td>
                        <td>
                            <form action="${pageContext.request.contextPath}/controller?command=editExpositionCenter" method="post">
                                <input type="hidden" name="idEdit" value="${expoCenter.id}">
                                <%-- Comment
                                <input type="hidden" name="expoCTitle" value="${expoCenter.title}">
                                <input type="hidden" name="expoCAddress" value="${expoCenter.address}">
                                <input type="hidden" name="expoCMail" value="${expoCenter.eMail}">
                                <input type="hidden" name="expoCWebPage" value="${expoCenter.webPage}">--%>
                                <input class="submitBtn" type="submit" name="Edit" value="Edit" />
                            </form>
                            <a class="submitBtn" href="${pageContext.request.contextPath}/controller?command=editExpositionCenter&idEdit=<c:out value='${expoCenter.id}'/>&expoCTitle=<c:out value='${expoCenter.title}'/>&expoCAddress=<c:out value='${expoCenter.address}'/>&expoCWebPage=<c:out value='${expoCenter.webPage}'/>&expoCMail=<c:out value='${expoCenter.eMail}'/>">Edit</a>
                           &nbsp;
                           <a class="resetBtn" href="${pageContext.request.contextPath}/controller?command=expoCenterManagement&idDelete=<c:out value='${expoCenter.id}'/>">Delete</a>
                           &nbsp;
                           <a class="submitBtn" href="${pageContext.request.contextPath}/controller?command=combineExpoWithCenter&expoCenterId=<c:out value='${expoCenter.id}' />">Form_contract</a>
                        </td>
                     </tr>
                  </c:forEach>
               </table>

               <!-- /table -->
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