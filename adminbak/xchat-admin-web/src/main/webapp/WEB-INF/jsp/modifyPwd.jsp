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
    <link rel="icon" href="/static/favicon.ico">

    <title>修改密码</title>

    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <!-- Custom styles for this template -->
    <link rel ="stylesheet" href="/static/css/supersized.css">
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->

    <style type="text/css">
        .loginTips{
            color: red;
            font-size: 14px;
            margin-left: 10px;
        }
    </style>
</head>
<body>

     <div class="form-horizontal">
         <form id="modifyPwdForm" method="post">
             <div class="form-group">
                <span class="col-sm-12" style="font-size:24px;font-weight:bold;align:center">修改密码</span>
            </div>
             <div class="form-group">
                 <input type="hidden" id="adminId" value="${adminUser.id}">
                 <input type="hidden" id="account" value="${adminUser.username}">
                 <label for="account" class="col-sm-2 control-label">旧密码：</label>
                 <div class="col-sm-10">
                     <input id="oldPwd" name="oldPwd" type="password" class="form-control">
                 </div>
                 
             </div>

             <div class="form-group">
	             <label for="password" class="col-sm-2 control-label">新密码：</label>
	             <div class="col-sm-10">
	                 <input id="password" name="password" type="password" class="form-control">
	             </div>
			</div>
             
             <div class="form-group">
	             <label for="password" class="col-sm-2 control-label">确认密码：</label>
	             <div class="col-sm-10">
	                 <input id="confirmPwd" name="confirmPwd" type="password" class="form-control">
	             </div>
             </div>

             <div class="form-group">
                 <label for="validateCode" class="col-sm-2 control-label">验证码：</label>
                 <div class="col-sm-10">
	                 <input id="validateCode" name="authCode" autocomplete="off" class="form-control x164 in" style="width: 210px;">
	                 <span id="getSmsCode">获取短信验证码</span>
                 </div>
             </div>

             <div class="form-group" style="margin-bottom: 0px;">
                 <label for="validateMsg" class="col-sm-2 control-label"></label>
                 <div class="col-sm-10"><span class="loginTips"></span></div>
             </div>

             <div class="form-group space">
                 <label class="col-sm-2 control-label"></label>　　　
                 <button type="button" id="saveBtn" class="btn btn-primary">保存</button>
                 &nbsp;&nbsp;&nbsp;&nbsp;
                 <input type="button" onclick="cancel()" value="取消" class="btn btn-default">
             </div>
         </form>
     </div>

<!-- Bootstrap core JavaScript -->
<script src="/static/js/jquery/jquery.min.js"></script>
<script src="/static/bootstrap/js/bootstrap.min.js"></script>
<script src="/static/js/jquery/supersized.3.2.7.min.js"></script>
<script src="/static/js/supersized-init.js"></script>
<script src="/static/js/jquery/jquery.md5.js"></script>
<script type="text/javascript">
   function cancel(){
	   window.location.href = "/admin/main.action";
   }
    var iframeHasLoginPage = true;	//为避免登录页面嵌套在iframe中而定义
    var timeForIframe = new Date().getTime();
    $(function(){
        if(top.timeForIframe!=timeForIframe){	//避免首页嵌套首页的情况发生(当前页处于最外层，top指向的是当前窗口本身，
            top.window.location.reload();		//则top.timeForIframe==timeForIframe，如果首页中嵌套首页，
        }										//top.timeForIframe和当前窗口timeForIframe不一致，刷新顶层)

        var username='${adminUser.username}';
        if(username==""||username==null){
        	window.location.href = "/login/logout.action";
        }
        $("#saveBtn").click(function() {
        	var adminId=$("#adminId").val();
            var oldPwd = $("#oldPwd").val();
            var password = $("#password").val();
            var confirmPwd = $("#confirmPwd").val();
            var validateCode = $("#validateCode").val();
            if(oldPwd.trim()==''){
                $(".loginTips").html("旧密码不能为空！");
                return;
            }else if(password.trim()==''){
                $(".loginTips").html("新密码不能为空！");
                return;
            }else if(confirmPwd.trim()==''){
                $(".loginTips").html("确认密码不能为空！");
                return;
            }else if(validateCode.trim()==''){
                $(".loginTips").html("验证码不能为空！");
                return;
            }
            if(oldPwd.trim()==password.trim()){
            	$(".loginTips").html("新密码不能和旧密码相同！");
                return;
            }
            if(password.trim()!=confirmPwd.trim()){
            	$(".loginTips").html("确认密码与新密码不一致！");
                return;
            }
            
            
            var param = {'adminId':adminId,'oldPwd':oldPwd,'password':password,'confirmPwd':confirmPwd,'authCode':validateCode};
            $.ajax({
                type: "post",
                url: "/login/modifyPwd.action",
                data: param,
                dataType: "json",
                success: function (data) {
                    if (data.message == '修改密码成功') {
                    	alert("修改密码成功")
                        window.location.href = "/admin/main.action";
                    } else {
                        $(".loginTips").html(data.message).css("padding", "3px 5px");
                        return false;
                    }
                }
            });
        });
        $("#modifyPwdForm :input").not("#rememberAccount").focus(function(){
            $(".loginTips").html("").css("padding", "0");;
        }).keyup(function(e){
            var keyCode = e.which || e.keyCode;
            if(keyCode==13){
                $("#saveBtn").click();
            }
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
            $.get("/login/getCodeSms?account=" + account+"&type=2", function(result){
                if(result.code == 200){
                    $(".loginTips").html(result.message).css("padding", "3px 5px");
                    Timer($('#getSmsCode'), 60, '发送验证码')
                }else{
                    $(".loginTips").html(result.message).css("padding", "3px 5px");
                }
            });

        }
    })


</script>

</body>
</html>
