var info = {},
    alipayFlag = false;

// 用于拿取用户ID
var urlInfo = getQueryString();

info.openId = urlInfo.openId;

var browser = checkVersion();

function isWeiXin() {
    var ua = window.navigator.userAgent.toLowerCase();
    if (ua.match(/MicroMessenger/i) == 'micromessenger') {
        return true; // 是微信端
    } else {
        return false;
    }
}

initPage()

function initPage() {
    getChargeGoldList()
}

$('#open-agreement').click(function () {
    $('.agreement').stop(true, true).animate({
        height: "100%"
    }, 500);
    $('.agreement').css('display', 'block');
});

$('#close-agreement').click(function () {
    $('.agreement').stop(true, true).animate({
        height: "0"
    }, 500);
    setTimeout(function () {
        $('.agreement').css('display', 'none');
    }, 500)
});

$('.pop-up-dialog button').on('touchend', function() {
    $('.pop-up').css('display', 'none');
});

// 微信支付
$('.wechat-pay').on('touchend', function () {
    check();
});

// 支付宝支付
$('.alipay').on('touchend', function () {
    var boolean = isWeiXin();
    if (boolean) {
        $(".shade").slideDown();
    } else {
        alipayFlag = true;
        check();
    }
});

// 获取充值金币列表
function getChargeGoldList() {
    $.ajax({
        url: allUrl() + '/chargeprod/list',
        data: {
            channelType: 1
        },
        success: function success(res) {
            $('.goldList .goldItem').remove();
            if (res.code === 200) {
                goldList = res.data;
                chargeProdId = res.data[1].chargeProdId;
                for (var i = 0; i < goldList.length; i++) {
                    var $item = $('<div class="goldItem "><p class="gold-num"><span class="img"></span><span> ' + res.data[i].prodName + '</span></p><p class="money">' + res.data[i].money + '元</p></div>')
                    $item.data('chargeProdId', res.data[i].chargeProdId);
                    $('.goldList').append($item);
                }

                $('.goldItem').on('click', function () {
                    console.log('chargeProdId:' + $(this).data('chargeProdId'))
                    info.chargeProdId = $(this).data('chargeProdId');
                    $(this).addClass('active').siblings().removeClass('active');
                    info.money = $(this).find('.money').html();
                });
            }
        }
    });
}

// 提交
function submit() {
    if (info.openId && !alipayFlag) {
        $.ajax({
            url: allUrl() + '/charge/joinpay/webApply',
            data: {
                chargeProdId: info.chargeProdId,
                userNo: inputVal,
                openId: info.openId,
                payChannel: 'WEIXIN_GZH',
                successUrl: allUrl() + 'front/charge_huiju/success.html',
            },
            method: 'post',
            success: function success(res) {
                if (res.code == 200) {
                    // var toPayUrl = res.data.rc_Result.match(/location.href='(\S*)';/)[1];
                    var result = JSON.parse(res.data.rc_Result)
                    console.log(result);
                    var packages = result.data;
                    var timeStamp = result.timeStamp; //时间戳，自1970年以来的秒数
                    var nonceStr = result.nonceStr; //随机串
                    var package = result.package;
                    var paySign = result.paySign; //微信签名
                    // var nick = result.nick;
                    var appId = result.appId;
                    var signType = result.signType;
                    // var erbanNo = result.erban_no;
                    function onBridgeReady() {
                        WeixinJSBridge.invoke(
                            'getBrandWCPayRequest', {
                                "appId": appId,
                                "timeStamp": timeStamp,
                                "nonceStr": nonceStr,
                                "package": package,
                                "signType": signType,
                                "paySign": paySign
                            },
                            function (res) {
                                if (res.err_msg == "get_brand_wcpay_request:ok") { // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回ok，但并不保证它绝对可靠。
                                    showSuccess();
                                } else if (res.err_msg == "get_brand_wcpay_request:fail") {
                                    $toast.show({
                                        text: '支付失败！',
                                        time: 2500
                                    });
                                } else {
                                    $toast.show({
                                        text: '支付失败！',
                                        time: 2500
                                    });
                                }
                            }
                        );
                    }

                    if (typeof WeixinJSBridge == "undefined") {
                        if (document.addEventListener) {
                            document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
                        } else if (document.attachEvent) {
                            document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
                            document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
                        }
                    } else {
                        onBridgeReady();
                    }
                } else {
                    $('#loading').hide();
                    $('.toast').html(res.message);
                    $('.toast').show();
                    setTimeout(function () {
                        $('.toast').hide();
                    }, 1500)
                }
            }
        });
    } else if (alipayFlag) {
        $.ajax({
            url: allUrl() + '/charge/sand/alipay/webApply',
            // url: allUrl() + '/charge/webApply',
            type: 'post',
            data: {
                chargeProdId: info.chargeProdId,
                payChannel: 'sand_alipay_h5',
                userNo: inputVal,
                successUrl: allUrl() + '/front/charge_new/success.html'
            },
            success: function (res) {
                if (res.code == 200) {
                    params = {
                        data: JSON.parse(res.data).data,
                        sign: JSON.parse(res.data).sign,
                    };
                    post('https://cashier.sandpay.com.cn/gw/web/order/create', params);

                    // pingpp.createPayment(res.data, function (result, err) {
                    //     if (result == "success") {
                    //         // 只有微信公众号 (wx_pub)、QQ 公众号 (qpay_pub)支付成功的结果会在这里返回，其他的支付结果都会跳转到 extra 中对应的 URL
                    //     } else if (result == "fail") {

                    //     } else if (result == "cancel") {
                    //         // 微信公众号支付取消支付
                    //     }
                    // });
                } else {
                    $('#loading').hide();
                    $('.toast').html(res.message);
                    $('.toast').show();
                    setTimeout(function () {
                        $('.toast').hide();
                    }, 1500)
                }
            }
        })
    }
}

// 模拟Form表单提交跳转
function post(URL, PARAMTERS) {
    //创建form表单
    var temp_form = document.createElement("form");
    temp_form.action = URL;
    //如需打开新窗口，form的target属性要设置为'_blank'
    temp_form.target = "_self";
    temp_form.method = "post";
    temp_form.style.display = "none";
    //添加参数
    for (var item in PARAMTERS) {
        var opt = document.createElement("textarea");
        opt.name = item;
        opt.value = PARAMTERS[item];
        temp_form.appendChild(opt);
    }
    document.body.appendChild(temp_form);
    //提交数据
    temp_form.submit();
}

function check() {
    if ($('#userNo')[0].value.trim() == '') {
        // showNoUser()
        $toast.show({
            text: '请输入需要充值的ID！',
            time: 2000
        })
        $('.goldItem').removeClass('active')
        return
    }

    inputVal = $('#userNo')[0].value;
    $.ajax({
        url: allUrl() + '/charge/checkUser',
        data: {
            userNo: inputVal
        },
        success: function success(res) {
            if (res.code === 200) {
                info.name = res.data.erbanNo;
                info.id = res.data.nick;
                showConfirm(info.id, info.name, info.money)
            } else {
                showNoUser();
            }
        }
    });
}

// 验证手机号码
function verifyPhone(num) {
    // 手机号码校验规则
    const valid_rule = /^(13[0-9]|14[5-9]|15[012356789]|166|17[0-8]|18[0-9]|19[8-9])[0-9]{8}$/;
    if (!valid_rule.test(num)) {
        return false;
    } else {
        return true;
    }
}

function showSuccess() {
    $Dialog({
        title: '   ',
        content: '充值成功',
        diaClass: 'success',
        // buttons: [{
        //     className: "",
        //     text: "确定",
        //     callback: function() {

        //     }
        // }, {
        //     className: "color-bold",
        //     text: "取消",
        //     callback: function() {

        //     }
        // }]
    })
    setTimeout(function () {
        $('.dialog-btn-cenfirm').click();
        $('.goldItem').removeClass('active')
    }, 2000)

}

// 确认弹框
function showConfirm(name, id, money) {
    var text = "向" + name + "(ID:" + id + ")<br>充值" + money + "？";
    $Dialog({
        title: '   ',
        content: text,
        diaClass: 'confirm',
        buttons: [{
            className: "cancel",
            text: "取消",
            callback: function () {
                $('.goldItem').removeClass('active');
            }
        }, {
            className: "confirm-btn",
            text: "确认",
            callback: function () {
                $('.goldItem').removeClass('active')
                submit();
            }
        }]
    })
}

function showNoUser() {
    $Dialog({
        title: '   ',
        content: '该用户不存在',
        diaClass: 'confirm',
        buttons: [{
            className: "confirm-btn",
            text: "确认",
            callback: function () {
                $('.goldItem').removeClass('active')

            }
        }]
    });
}

function showDiff() {
    $Dialog({
        title: '   ',
        content: '下单账户与支付账户不一致，<br>请核实后再支付',
        diaClass: 'confirm',
        buttons: [{
            className: "confirm-btn",
            text: "确认",
            callback: function () {
                $('.goldItem').removeClass('active')
                submit();
            }
        }]
    })
}