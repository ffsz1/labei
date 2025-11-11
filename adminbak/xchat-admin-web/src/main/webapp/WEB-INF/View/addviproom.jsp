<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/8/31
  Time: 18:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<font size="50">添加Vip房间</font>
<form action="http://localhost:8081/hotroom/viproom" method="post">
    <table align="center">
        <td>开房人拉贝号:<input type="text" name="erbanNo"></td>
        <td><input type="submit"></td>
    </table>
</form>
</body>
</html>
