<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
<title>Error</title>
</head>
<body>
    <center>
        <h1>Oops, something went wrong...</h1>
        <br>
        <p>You can go to<a href="${pageContext.request.contextPath}/controller?command=home"> main</a> page.</p>
        <br>
        <img src="${pageContext.request.contextPath}/resources/img/errorImg.jpg" alt="Page error." style="position: absolute; left: 50%; top: 50%; margin-left: -285px; margin-top: -190px;">

    </center>
</body>
</html>