<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
</head>
<body>
<table align="center">
    <input type="button" value="官方送金币" onclick="location.href = 'http://localhost:8081/controller/skip?address=intersendgold'">
    <input type="button" value="上传banner图片" onclick="location.href = 'http://localhost:8081/controller/skip?address=uploadImg'">
    <input type="button" value="修改礼物图片" onclick="location.href = 'http://localhost:8081/controller/skip?address=uploadImgGift'">
    <input type="button" value="房间添加机器人" onclick="location.href = 'http://localhost:8081/controller/skip?address=roomaddperson'">
    <input type="button" value="查询提现人员列表" onclick="location.href='http://localhost:8081/controller/skip?address=withdrawcash'">
    <input type="button" value="查询每日分享列表" onclick="location.href='http://localhost:8081/controller/skip?address=statsharelist'">
    <input type="button" value="添加或更改热门房间" onclick="location.href='http://localhost:8081/controller/skip?address=addhotroom'">
    <input type="button" value="添加Vip房间" onclick="location.href='http://localhost:8081/controller/skip?address=addviproom'">
</table>
</body>
</html>
