<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="icon" href="/static/logo.png" type="image/png" />
    <link rel="shortcut icon" href="/static/logo.png" type="image/png"/>
    <title>拉贝星球管理后台</title>
    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <!-- Custom styles for this template -->
    <link rel ="stylesheet" href="/static/css/supersized.css">
    <link rel ="stylesheet" href="/static/css/login.css">
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->

    <style type="text/css">
        *{
            font-family: "Helvetica Neue",Helvetica,STheiti,微软雅黑,宋体,Arial,Tahoma,sans-serif,serif;
        }
        .btn-lg{
            padding: 8px 25px;
        }
        #captcha_img{
            margin-left: 5px;
        }
        .loginTips{
            color: red;
            font-size: 14px;
            margin-left: 10px;
        }
    </style>
</head>
<body>
<div class="page-container">
    <div class="main_box">
        <div class="login_box">
            <div class="login_logo">
                <span style="font-size:24px;color:#222;font-weight:bold;">&nbsp;&nbsp;拉贝星球&nbsp;管&nbsp;理&nbsp;系&nbsp;统</span>
            </div>
            <br>
            <div class="login_form">
                <form id="loginForm" method="post">
                    <div class="form-group">
                        <label for="account" class="t">账　号：</label>
                        <input id="account" name="account" type="text" class="form-control x319 in" autocomplete="off">
                    </div>

                    <div class="form-group">
                    <label for="password" class="t">密　码：</label>
                    <input id="password" name="password" type="password" class="password form-control x319 in">
                    </div>

                    <div class="form-group">
                        <label for="validateCode" class="t">验证码：</label>
                        <input id="validateCode" name="authCode"  autocomplete="off" class="form-control x164 in" style="width: 210px;">
                        <%--<img id="validateImg" alt="点击更换" title="点击更换" src="/admin/login/kaptcha" class="m">--%>
                        <span id="getSmsCode">获取短信验证码</span>
                    </div>

                    <div class="form-group" style="margin-bottom: 0px;">
                        <label class="t"></label>
                        <%--<label for="rememberAccount" class="m" style="font-weight: normal;">--%>
                        <%--<input id="rememberAccount" type="checkbox" value="1" checked>&nbsp;记住我的账号--%>
                        <%--</label>--%>

                        <span class="loginTips"></span>
                    </div>

                    <div class="form-group space">
                        <label class="t"></label>　　　
                        <button type="button" id="loginBtn" class="btn btn-primary btn-lg">登&nbsp;&nbsp;录</button>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <input type="reset" value="重&nbsp;&nbsp;置" class="btn btn-default btn-lg">
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap core JavaScript -->
<script src="/static/js/jquery/jquery.min.js"></script>
<script src="/static/bootstrap/js/bootstrap.min.js"></script>
<script src="/static/js/jquery/supersized.3.2.7.min.js"></script>
<script src="/static/js/supersized-init.js"></script>
<script src="/static/js/jquery/jquery.md5.js"></script>
<script type="text/javascript">
    var iframeHasLoginPage = true;	//为避免登录页面嵌套在iframe中而定义
    var timeForIframe = new Date().getTime();
    $(function(){
        if(top.timeForIframe!=timeForIframe){	//避免首页嵌套首页的情况发生(当前页处于最外层，top指向的是当前窗口本身，
            top.window.location.reload();		//则top.timeForIframe==timeForIframe，如果首页中嵌套首页，
        }										//top.timeForIframe和当前窗口timeForIframe不一致，刷新顶层)

        var rememberMe = getCookie("wolfbe.remember");
        if(rememberMe==1){
            var account = getCookie("wolfbe.account");
            if(account) $("#account").val(account);
        }

        $("#loginBtn").click(function() {
            var account = $("#account").val();
            var password = $("#password").val();
            var validateCode = $("#validateCode").val();
            if(account.trim()==''){
                $(".loginTips").html("账号不能为空！");
                return;
            }else if(password.trim()==''){
                $(".loginTips").html("密码不能为空！");
                return;
            }else if(validateCode.trim()==''){
                $(".loginTips").html("验证码不能为空！");
                return;
            }
            
            var param = {'account':account,'password':password,'authCode':validateCode};
            $.ajax({
                type: "post",
                url: "/login/V3/login.action",
                data: param,
                dataType: "json",
                success: function (data) {
                    if (data.success == 'true') {
                        window.location.href = "/admin/main.action";
                    } else {
                        $(".loginTips").html(data.msg).css("padding", "3px 5px");
                        return false;
                    }
                }
            });
        });
        $("#loginForm :input").not("#rememberAccount").focus(function(){
            $(".loginTips").html("").css("padding", "0");;
        }).keyup(function(e){
            var keyCode = e.which || e.keyCode;
            if(keyCode==13){
                $("#loginBtn").click();
            }
        });

        $("#validateImg").click(function(){
            $(this).attr("src", "/static/admin/kaptcha?t="+new Date().getTime());
        });



    });

    var loginTimer;

    function Timer(obj, time, text) {
        console.log(obj, time)
        if (!Number(obj)) {
            obj.text(time)
        }
        loginTimer = setInterval(function() {
            if (obj.text() <= 0) {
                clearInterval(loginTimer);
                loginTimer = null;
                obj.text(text)
                return false
            }
            obj.text(+obj.text() - 1)
            console.log(loginTimer)
        }, 1000)
    }
    $('#getSmsCode').on('click', function() {
        if (!loginTimer) {
            var account = $("#account").val();
            $.get("/login/getCodeSms?account=" + account+"&type=1", function(result){
                if(result.code == 200){
                    $(".loginTips").html(result.message).css("padding", "3px 5px");
                    Timer($('#getSmsCode'), 60, '发送验证码')
                }else{
                    $(".loginTips").html(result.message).css("padding", "3px 5px");
                }
            });

        }
    })


    //设置Cookie
    function setCookie(c_name, value, expiredays){
        var exdate = new Date();
        exdate.setDate(exdate.getDate() + expiredays);
        document.cookie = c_name + "=" + escape(value) + ((expiredays==null) ? "" : ";expires="+exdate.toGMTString());
    }

    //获取cookie的信息
    function getCookie(name){
        var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
        if(arr=document.cookie.match(reg)){
            return (arr[2]);
        } else{
            return null;
        }
    }

    function delCookie(name){
        var exp = new Date();
        exp.setTime(exp.getTime() - 1);
        var cval=getCookie(name);
        if(cval!=null)
            document.cookie= name + "="+cval+";expires="+exp.toGMTString();
    }
</script>

</body>
</html>
