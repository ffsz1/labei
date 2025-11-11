<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
</head>
<body>
<font size="50">官方送金币</font>
<form action="http://localhost:8081/intergold/charge" method="post">
    <table align="center" >
        <td>被送金币者erban号:<input type="text" name="erbanNo"></td>
        <td>赠送金币数量：<input type="text" name="goldNum"></td>
        <td><input type="submit"></td>
    </table>
</form>
</body>
</html>
