<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<font size="50">更新礼物图片</font>
	<form action="http://localhost:8081/img/updategift"
		method="post" enctype="multipart/form-data">
		<table align="center" border="1" cellpadding="0">
			<tr>
				<td>礼物id:</td>
				<td><input id="giftId" name="giftId" type="text"></td>
			</tr>
			<tr>
				<td>礼物图片:</td>
				<td><input id="file" name="file" type="file"></td>
			</tr>
			<tr>
				<td align="center" colspan="2"><input type="submit"></td>
			</tr>
		</table>
	</form>
</body>
</html>
