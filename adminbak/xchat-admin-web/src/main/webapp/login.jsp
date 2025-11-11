<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/9/13
  Time: 15:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="http://localhost:8081/user/login" method="post">
    <table align="center">
        <tr>
            <td>用户名:<input type="text" name="username"></td>
        </tr>
        <tr>
            <td>密&nbsp;&nbsp;码:<input type="password" name="password"></td>
        </tr>
        <tr>
        <td align="center"><input value="登录" type="submit"></td>
        </tr>
    </table>
</form>
</body>
</html>
