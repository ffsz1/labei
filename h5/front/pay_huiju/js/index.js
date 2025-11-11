var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function (obj) {
    return typeof obj;
} : function (obj) {
    return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj;
};

var share = {
    title: '情深深，音缘萌，语音交友来音缘', // 分享标题
    link: window.location.href, // 分享链接
    imgUrl: allUrl() + '/home/images/logo.png',
    desc: '交友处cp，来音缘。' // 分享描述
};

function shareInfo() {
    var _url = allUrl() + '/front/download/download.html';
    var info = {
        title: '情深深，音缘萌，语音交友来音缘', // 分享标题
        showUrl: _url,
        imgUrl: allUrl() + '/home/images/logo.png',
        desc: '交友处cp，来音缘。' // 分享描述
    };
    return JSON.stringify(info);
}
var browser = checkVersion();
var info = {};

var urlInfo = getQueryString(); //用于拿取用户ID
info.openId = null;
info.openId = urlInfo.openId;

// appFun('getTicket', function (data) {
//     info.ticket = data;
//     appFun('getUid', function (data) {
//         info.uid = data;
//         initPage();
//     })
// })

// info.uid = 17;
// info.ticket = 'eyJhbGciOiJIUzI1NiJ9.eyJ0aWNrZXRfdHlwZSI6bnVsbCwidWlkIjoxNywidGlja2V0X2lkIjoiZjNhOGYzMTItNTUyYS00ODQxLWFiN2ItYTcyOTBlNjgyNWEyIiwiZXhwIjozNjAwLCJjbGllbnRfaWQiOiJlcmJhbi1jbGllbnQifQ.aAiXdm1nJnGyAmQHy7KaAyzAxmX7Da4_61ikdCe660E'
initPage();

function initPage() {
    getChargeGoldList();
}

// 判断当ID为空时取消选中
$('#userNo').blur(function (e) {
    e.preventDefault();
    const val = $(e.target).val();
    if (val == '') {
        $('.goldItem').removeClass('active');
    }
});

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
    }, 500);
});

$('.pop-up-dialog button').on('touchend', function() {
    $('.pop-up').css('display', 'none');
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
                
                // for (var i = 0; i < res.data.length; i++) {
                for (var i = 0; i < 9; i++) {
                    var $item = $('<div class="goldItem "><p class="gold-num"><span class="img"></span><span> ' + res.data[i].prodName + '</span></p><p class="money">' + res.data[i].money + '元</p></div>')
                    $item.data('chargeProdId', res.data[i].chargeProdId);
                    $('.goldList').append($item);
                }

                $('.goldItem').on('click', function () {
                    console.log('chargeProdId:' + $(this).data('chargeProdId'))
                    info.chargeProdId = $(this).data('chargeProdId');
                    $(this).addClass('active').siblings().removeClass('active');
                    info.money = $(this).find('.money').html();
                    checkSubmit();
                });
            }
        }
    });
}

function submit() {
    console.log(info.chargeProdId, inputVal, info.openId);
    if (info.openId) {
        // $.ajax({
        //     url: allUrl() + '/wx/submitPay',
        //     data: {
        //         chargeProdId: info.chargeProdId,
        //         erban_no: inputVal,
        //         openId: info.openId,
        //         payChannel: 'wx_pub'

        //     },

        //     method: 'post',
        //     success: function success(res) {
        //         console.log(res)
        //         if (res.code === 200) {
        //             var packages = res.data;
        //             var timeStamp = packages.timestamp; //时间戳，自1970年以来的秒数
        //             var nonceStr = packages.nonce_str; //随机串
        //             var package = packages.prepay_id;
        //             var paySign = packages.sign; //微信签名
        //             var nick = packages.nick;
        //             var erbanNo = packages.erban_no;

        //             function onBridgeReady() {
        //                 WeixinJSBridge.invoke(
        //                     'getBrandWCPayRequest', {
        //                         "appId": "wxbe281714c57530fd",
        //                         "timeStamp": timeStamp,
        //                         "nonceStr": nonceStr,
        //                         "package": package,
        //                         "signType": "MD5",
        //                         "paySign": paySign
        //                     },
        //                     function (res) {
        //                         if (res.err_msg == "get_brand_wcpay_request:ok") { // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回ok，但并不保证它绝对可靠。
        //                             showSuccess()

        //                         } else if (res.err_msg == "get_brand_wcpay_request:fail") {
        //                             $toast.show({
        //                                 text: '支付失败！',
        //                                 time: 2500
        //                             })
        //                         } else {
        //                             $toast.show({
        //                                 text: '支付失败！',
        //                                 time: 2500
        //                             })
        //                         }
        //                     }
        //                 );
        //             }
        //             if (typeof WeixinJSBridge == "undefined") {
        //                 if (document.addEventListener) {
        //                     document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
        //                 } else if (document.attachEvent) {
        //                     document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
        //                     document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
        //                 }
        //             } else {
        //                 onBridgeReady();
        //             }
        //         }
        //     }
        // });
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
    } else {

        console.log('no openid')
        var _data = {
            chargeProdId: info.chargeProdId,
            payChannel: 'wx_wap',
            userNo: inputVal,
            successUrl: allUrl() + '/front/wxpay/success.html'
        }
        if (urlInfo && urlInfo.redirect_url) {
            sessionStorage.setItem('_successUrl', urlInfo.redirect_url)
        }
        $.ajax({
            url: allUrl() + '/charge/webApply',
            type: 'post',
            data: _data,
            success: function (res) {
                if (res.code == 200) {
                    sessionStorage.setItem('wx_wap', res.data.orderNo);
                    pingpp.createPayment(res.data, function (result, err) {
                        if (result == "success") {
                            // 只有微信公众号 (wx_pub)、QQ 公众号 (qpay_pub)支付成功的结果会在这里返回，其他的支付结果都会跳转到 extra 中对应的 URL
                        } else if (result == "fail") {

                        } else if (result == "cancel") {
                            // 微信公众号支付取消支付
                        }
                    });
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
    }
}

// 检查提交信息
function checkSubmit() {
    console.log($('#agree').is(':checked'))
    if ($('#userNo')[0].value.trim() == '') {
        $toast.show({
            text: '请输入需要充值的ID',
            time: 2000
        })
        $('.goldItem').removeClass('active');
        return;
    } else if (!$('#agree').is(':checked')) {
        $toast.show({
            text: '请同意勾选《充值服务协议》',
            time: 2000
        })
        $('.goldItem').removeClass('active');
        return;
    }
    inputVal = $('#userNo')[0].value;
    $.ajax({
        url: allUrl() + '/charge/checkUser',
        data: {
            userNo: inputVal
        },
        success: function success(res) {
            console.log(res)
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

function verifyTel(num) { //验证手机号码
    var valid_rule = /^(13[0-9]|14[5-9]|15[012356789]|166|17[0-8]|18[0-9]|19[8-9])[0-9]{8}$/; // 手机号码校验规则
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
                $('.goldItem').removeClass('active')

            }
        }, {
            className: "confirm-btn",
            text: "确认",
            callback: function () {
                $('.goldItem').removeClass('active')
                // console.log(111)
                submit() //发起支付

            }
        }]
    })
}
// showConfirm('123', 13432803741, '6元')
function showNoUser() {
    $Dialog({
        title: '   ',
        content: '该用户不存在',
        diaClass: 'confirm',
        buttons: [{
            className: "confirm-btn",
            text: "确认",
            callback: function () {
                $('.goldItem').removeClass('active');
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
// showDiff()


$('body').append('<div class="toast">');
var $toast = {
    show: function (obj) {
        $('.toast').text(obj.text).show();
        setTimeout(function () {
            $('.toast').hide();
        }, obj.time)
    }
};