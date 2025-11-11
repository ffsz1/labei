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
<table border="1px" width="40%" align="center" id="billlist">
    <tr>
        <th>拉贝号</th>
        <th>昵称</th>
        <th>扣款钻石</th>
        <th>提现金额</th>
        <th>提现时间</th>
        <th>操&nbsp;&nbsp;作</th>
    </tr>
</table>
<script type="text/javascript">
    $(function () {
        $.ajax({
            url: "http://localhost:8081/withdrawcash/getbilllist",
            type: "GET",
            contentType: "application/json; charset=utf-8",
            data: JSON,
            success: function (data) {
                data = eval("(" + data + ")");
                for (var i = 0; i < data.length; i++) {
                    var str = str + "<tr><td>" + data[i].erbanNo + "</td><td>" + data[i].nick + "</td><td>" + data[i].diamondNum + "</td><td>" + data[i].money + "</td><td>" + data[i].createTime +
                        "</td><td><a href='http://localhost:8081/withdrawcash/finish?billId="+data[i].billId+"' >允许提现</a></td></tr>"
                }
                $("#billlist").append(str);
            },
            error: function () {
                alert("error");
            }
        });
    });
</script>
</body>
</html>
