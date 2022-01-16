<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="/WEB-INF/includes/taglib.jsp" %>
    <title>首页</title>
</head>
<body>
<h1>欢迎${user.name}登陆${fns:getConfig("application.name")}首页</h1>
</body>
</html>
