<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/8/30
  Time: 17:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<html>
<script type="text/javascript" src="../../jquery/jquery-3.2.1.js"></script>
<head>
    <title>Title</title>
</head>
<body>
<table border="1px" width="40%" align="center" id="sharelist">
    <tr>
        <th>拉贝号</th>
        <th>昵称</th>
        <th>分享人数</th>
        <th>首冲人数</th>
        <th>统计时间</th>
    </tr>
</table>
<script type="text/javascript">
    $(function () {
        $.ajax({
            url: "http://localhost:8081/statshare/list",
            type: "GET",
            contentType: "application/json; charset=utf-8",
            data: JSON,
            success: function (data) {
                data = eval("(" + data + ")");
                for (var i = 0; i < data.length; i++) {
                    var str = str + "<tr><td>" + data[i].erbanNo + "</td>/br<td>" + data[i].nick + "</td>/br<td>" + data[i].registerCount + "</td>/br<td>" + data[i].chargeCount + "</td>/br<td>" + data[i].statDate + "</td>/br</tr>"
                }
                $("#sharelist").append(str);
            },
            error: function () {
                alert("error");
            }
        });
    });
</script>
</body>
</html>
