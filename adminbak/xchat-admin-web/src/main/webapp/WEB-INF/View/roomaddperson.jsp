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
<font size="50">房间添加机器人</font>
<form action="http://localhost:8081/room/addperson" method="post">
    <table align="center">
        <td>开房人拉贝号:<input type="text" name="erbanNo"></td>
        <td>添加机器人数：<input type="text" name="personNum"></td>
        <td>机器人性别：<select name="gender" id="gender">
            <option value="1">男性</option>
            <option value="2">女性</option>
        </select>
        <td><input type="submit"></td>
    </table>
</form>
</body>
</html>
