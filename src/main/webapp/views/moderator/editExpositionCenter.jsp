<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
   <head>
      <title>Edit ExC</title>
   </head>

   <body>
      <center>
            <h1>Edit expo Center</h1>
            <h2>${role}</h2>
            <br><br>

            <form action="${pageContext.request.contextPath}/controller?command=editExpositionCenter" method="post">
                Id:<c:out value="${idEdit}" /><br/>

                Expo Center Title:<input type="text" name="expoCTitle" value="<c:out value='${expoCTitle}'/>"/><br/>
                Expo Center Address:<input type="text" name="expoCAddress" value="${expoCAddress}"/><br/>
                Expo Center eMail:<input type="text" name="expoCMail" value="${expoCMail}"/><br/>
                Expo Center webPage:<input type="text" name="expoCWebPage" value="${expoCWebPage}"/><br/>
                Expo Center Phone 1:<input type="text" name="phone1" value="${expoCPhone1}"/><br/>
                Expo Center Phone 2:<input type="text" name="phone2" value="${expoCPhone2}"/><br/>

                <button type="submit" name="editExpoCenter" value="Save">Save</button>
                <button type="submit" name="denieEdit" value="Cancel">Cancel</button>
                <br> ${error}
            </form>


            <br>
            <a href="${pageContext.request.contextPath}/controller?command=moderatorHome">moderator home</a>
            <br>
            <a href="${pageContext.request.contextPath}/controller?command=home">go home</a>
            <br>
            <a href="${pageContext.request.contextPath}/controller?command=logout">logout</a>
      </center>
   </body>
</html>
