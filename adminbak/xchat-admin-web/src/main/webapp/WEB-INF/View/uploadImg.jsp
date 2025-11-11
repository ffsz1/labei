<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
</head>
<body>
<font size="50">上传banner图片</font>
<form action="http://localhost:8081/img/addbanner"
      method="post" enctype="multipart/form-data">
    <table align="center" border="1" cellpadding="0">
        <tr>
            <td>banner名字:</td>
            <td><input id="title" name="title" type="text"></td>
        </tr>
        <tr>
            <td>bannner图片:</td>
            <td><input id="file" name="file" type="file"></td>
        </tr>
        <tr>
            <td>bannner类型:</td>
            <td><select id="type" name="type">
                <option value="1">1跳app页面</option>
                <option value="2">2跳聊天室</option>
                <option value="3">3跳h5页面</option>
            </select></td>
        </tr>
        <tr>
            <td>bannner序号:</td>
            <td><input id="seqNo" name="seqNo" type="text"></td>
        </tr>
        <tr>
            <td align="center" colspan="2"><input type="submit"></td>
        </tr>
    </table>
</form>
</body>
</html>
