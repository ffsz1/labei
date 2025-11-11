var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function(obj) {
    return typeof obj;
} : function(obj) {
    return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj;
};

var browser = checkVersion();
var info = {};

function showSuccess() {
    $('.pop-success').show();
    setTimeout(function() {
        if (sessionStorage.getItem('_successUrl')) {
            window.location.href = sessionStorage.getItem('_successUrl')
        }
        $('.pop-success').hide();
    }, 1500)
}


$('.verify-wx input').on('input', function() { //输入时控制×的出现 +功能
    var len = $(this).val().length;
    if (len > 0) {
        var _this = $(this);
        $(this).parent().find('.clear').show().click(function() {
            _this.val('');
            _this.focus();
            _this.parent().find('.clear').hide()
            $('#submit-wx').removeClass('active');
        });
    } else {
        $(this).parent().find('.clear').hide()
    }
})
$('#input1').focus(function() { //隐藏提示
    $('.verify-wx .info').hide();
})
$('.verify-wx input').on('input', function() { //输入时 控制提交按钮的活动

    if (verifyTel($('#input1').val())) { //验证正确 
        console.log(1)
        if ($('#input2').val().length > 0) { //有验证码
            $('#submit-wx').addClass('active');
        } else {
            $('#submit-wx').removeClass('active');
        }
    } else {
        $('#submit-wx').removeClass('active');
    }
})

$('#submit-wx').on('click', function() { //点击 提交 验证身份 信息 //发送ajax
    if ($(this).hasClass('active')) {
        //发送ajax
        $.ajax({
            type: 'post',
            url: allUrl() + '/wxPublic/getSmsCodeByWithdrawInfo',
            data: {
                phone: $('#input1').val(),
                code: $('#input2').val()
            },
            success: function success(res) {
                console.log(res)
                if (res.code === 200) {
                    var data = res.data;
                    data.codeMatch = codeMatch && codeMatch[1];
                    var url = createURL('./exchange.html', data) //通过URL传数据
                    console.log(url)
                    location.replace(url);
                } else if (res.code === 4005) {
                    //提示 ：
                    //如果未绑定微信
                    $('.popup').show()

                } else {
                    var text = res.message.split(':')[1];
                    $toast.show({
                        text: text,
                        time: 2000
                    });
                }

            }
        })

        //已绑定则跳转
        // location.href = './exchange.html'
    }

})


function verifyTel(num) { //验证手机号码
    var valid_rule = /^(13[0-9]|14[5-9]|15[012356789]|166|17[0-8]|18[0-9]|19[8-9])[0-9]{8}$/; // 手机号码校验规则
    if (!valid_rule.test(num)) {
        return false;
    } else {
        return true;
    }
}

function verifyMail(mail) { //验证邮箱
    var pattern = /^([\w\-\.]+)@([\w\-]+)\.([a-zA-Z]{2,4})$/;
    return pattern.test(mail)
}

function countdown() { //点击 获取验证码 后的倒计时
    $('.get-btn').addClass('active').text('60s');
    var i = 60;
    var timer = setInterval(function() {
        i--;
        if (i < 0) {
            clearInterval(timer);
            $('.get-btn').removeClass('active').text('获取验证码');
            return;
        }
        $('.get-btn').text(i + 's');
    }, 1000)
}

function getCode() { //点击 获取验证码 按钮执行  //发送ajax
    if ($('.get-btn').hasClass('active')) { //60秒之内不能重复请求

    } else {
        var val = $('#input1').val();
        if (verifyTel(val)) { //验证正确 发送请求
            //发送ajax
            $.ajax({
                type: 'get',
                url: allUrl() + '/wxPublic/getSmsByCode',
                data: {
                    phone: val
                },
                success: function success(res) {
                    console.log(res, allUrl() + '/withDraw/getAccountSms')
                    if (res.code === 200) {
                        countdown() //开启倒计时
                        $toast.show({
                            text: '发送请求',
                            time: 2000
                        });
                    } else if (res.code === 4004) {
                        //提示 ：该手机号未绑定任何拉贝APP账号，请先在APP绑
                        $('.verify-wx .info').show();
                    } else if (res.code === 4001) {
                        //提示 ：该手机号未绑定任何拉贝APP账号，请先在APP绑
                        $toast.show({ text: '短信次数已达最大限制，请明天再试', time: 3000 })
                    } else {
                        $toast.show({ text: res.message.split(':')[1], time: 2500 })
                    }

                }
            })


        } else {
            $toast.show({
                text: '手机输入不正确',
                time: 2000
            });
        }
    }
}

function hidePop() {
    $('.popup').hide();
}

// countdown()

//将一个对象拼接在url的后面
function createURL(url, param) {
    var urlLink = '';
    $.each(param, function(item, key) {
        var link = '&' + item + "=" + key;
        urlLink += link;
    })
    urlLink = url + "?" + urlLink.substr(1);
    return urlLink.replace(' ', '');
}



$('body').append('<div class="toast">');
window.$toast = {
    show: function(obj) {
        $('.toast').text(obj.text).show();
        var that = this;
        setTimeout(function() {
            $('.toast').hide();
            obj.callback && that.callback(obj.callback);
        }, obj.time)
    },
    callback: function(fn) {
        typeof fn == "function" && fn();
    }
};