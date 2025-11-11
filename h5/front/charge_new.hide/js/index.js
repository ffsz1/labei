// wxCode
    if(!isWx&&zfbAct){
        $('.pop-tips').show();
    }

function getQueryString() {
    var _url = location.search;
    var theRequest = new Object();
    if (_url.indexOf('?') != -1) {
        var str = _url.substr(1);
        strs = str.split('&');
        for (var i in strs) {
            theRequest[strs[i].split('=')[0]] = decodeURI(strs[i].split('=')[1]);
        }
    }
    return theRequest;
}

function showAlert(msg, cb) {
    var $alert = $('<div class="alert-tips">');
    var $wrap = $('<div class="alert-wrap">');
    $wrap.append('<div class="alert-msg">' + msg + '</div>');
    $wrap.append('<a href="#" class="btn-confirm">确定</a>');
    $alert.append($wrap);
    $alert.on('click', '.btn-confirm', function (e) {
        e.preventDefault();
        if (cb) {
            cb();
        }
        $alert.remove();
    });
    $alert.appendTo('body');
}
function showConfirm(msg, cb) {
    var $confirm = $('<div class="confirm-tips">');
    var $wrap = $('<div class="confirm-wrap">');
    $wrap.append('<div class="confirm-msg">' + msg + '</div>');
    var $btn = $('<div class="confirm-btn">');
    $btn.append('<a href="#" class="btn-l">取消</a>');
    $btn.append('<a href="#" class="btn-r">已完成支付</a>');
    $wrap.append($btn);
    $confirm.append($wrap);
    $confirm.on('click', '.btn-r', function (e) {
        e.preventDefault();
        if (cb) {
            cb();
        }
        $confirm.remove();
    });
    $confirm.on('click', '.btn-l', function (e) {
        e.preventDefault();
        $confirm.remove();
    });
    $confirm.appendTo('body');
}
//微信充值回调
if (sessionStorage.getItem('wx_wap')) {
    var _chargeRecordId = sessionStorage.getItem('wx_wap');
    sessionStorage.removeItem('wx_wap');
    showConfirm("请确定支付是否完成", function () {
        $.ajax({
            url: allUrl() + '/charge/checkApply',
            type: "get",
            data: {
                chargeRecordId: _chargeRecordId
            },
            success: function (ret) {
                if (ret.code == 200 && ret.data == true) {
                    showSuccess();
                } else {
                    showAlert("支付失败，请重试");
                }
            },
            error: function () {
                showTips('服务器出差了');
            }
        });
    });
}
console.log(wxCode);
var openId = '';
if (wxCode) {
    $.ajax({
        url: allUrl() + '/wx/snsapi/baseinfo/scan/get',
        //url: allUrl() + '/wx/snsapi/baseinfo/get',
        data: {
            code: wxCode,
            state: '123#wechat_redirect'
        },
        method: 'get',
        success: function success(res) {
            console.log(res)
            if (res.code === 200) {
                openId = res.data.openid
                console.log(openId)
            }
        }
    });
}
function showSuccess() {
    $('.pop-success').show();
    setTimeout(function () {
        if (sessionStorage.getItem('_successUrl')) {
            window.location.href = sessionStorage.getItem('_successUrl')
        }
        $('.pop-success').hide();
    }, 1500)
}
var goldList = []
var chargeProdId = 0;
var isDisable = true;

var payChannel = 1;
var numType = 'erbanNo';
var urlInfo = getQueryString(); //用于拿取用户ID
if (urlInfo && urlInfo.userNo) {
    var numType = 'userNo';
    $('.inputArea').hide();
    $('#userNo')[0].value = urlInfo.userNo;
    checkId();
} else if (urlInfo && urlInfo.erbanNo) {
    var numType = 'erbanNo';
    $('.inputArea').hide();
    $('#userNo')[0].value = urlInfo.erbanNo;
    checkId();
} else {
    var numType = 'erbanNo';
    $('#userNo')[0].value = '';
}
var _money = 6 ;
var fadeList = [{ "chargeProdId": "11", "prodName": "80金币", "prodDesc": "首充送20金币", "money": 6, "giftGoldNum": 0, "channel": 1, "seqNo": 10 }, { "chargeProdId": "19", "prodName": "480金币", "prodDesc": "", "money": 48, "giftGoldNum": 0, "channel": 1, "seqNo": 20 }, { "chargeProdId": "20", "prodName": "980金币", "prodDesc": "", "money": 98, "giftGoldNum": 0, "channel": 1, "seqNo": 30 }, { "chargeProdId": "21", "prodName": "1980金币", "prodDesc": "", "money": 198, "giftGoldNum": 0, "channel": 1, "seqNo": 40 }, { "chargeProdId": "22", "prodName": "4980金币", "prodDesc": "", "money": 498, "giftGoldNum": 0, "channel": 1, "seqNo": 50 }, { "chargeProdId": "23", "prodName": "9980金币", "prodDesc": "", "money": 998, "giftGoldNum": 0, "channel": 1, "seqNo": 60 }, { "chargeProdId": "24", "prodName": "49980金币", "prodDesc": "", "money": 4998, "giftGoldNum": 0, "channel": 1, "seqNo": 70 }, { "chargeProdId": "25", "prodName": "99990金币", "prodDesc": "", "money": 9999, "giftGoldNum": 0, "channel": 1, "seqNo": 80 }, { "chargeProdId": "26", "prodName": "300000金币", "prodDesc": null, "money": 30000, "giftGoldNum": 0, "channel": 1, "seqNo": 121 }, { "chargeProdId": "27", "prodName": "600000金币", "prodDesc": null, "money": 60000, "giftGoldNum": 0, "channel": 1, "seqNo": 122 }];
$('.goldList').html('');
for (var i = 0; i < fadeList.length; i++) {
    if (i == 0) {
        $('.goldList').append('<div><div class="goldItem active"><img src="images/gold.png" alt=""><span>' + fadeList[i].prodName + '</span> </div></div>')
    } else {
        $('.goldList').append('<div><div class="goldItem"><img src="images/gold.png" alt=""><span>' + fadeList[i].prodName + '</span> </div></div>')
    }
}
// return 
for (var i = 0; i < $('.goldItem').length; i++) {
    (function (i) {
        $('.goldItem')[i].onclick = function () {
            checkId()
            chargeProdId = fadeList[i].chargeProdId
            for (var j = 0; j < $('.goldItem').length; j++) {
                $($('.goldItem')[j]).removeClass('active');
            }
            $($('.goldItem')[i]).addClass('active');
            $('.price span')[1].innerHTML = '￥' + fadeList[i].money;
            _money = fadeList[i].money;
        };
    })(i);
}

function getClientCofig() {
    $.ajax({
        url: allUrl() + '/client/configure',
        data: {},
        success: function success(res) {
            console.log(res)
            if (res.code == 200) {
                payChannel = res.data.payChannel
            }
        }
    });
}
getClientCofig();

function getList() {
    $.ajax({
        url: allUrl() + '/chargeprod/list',
        data: {
            channelType: 1
        },
        success: function success(res) {
            console.log(res)
            $('.goldList .goldItem').remove();
            if (res.code === 200) {
                goldList = res.data;
                chargeProdId = res.data[0].chargeProdId;
                // if($('#userNo')[0].value && !isDisable) {
                //     $('.zfb').attr('src', 'images/zfb_act.png');
                //     $('.wx').attr('src', 'images/wx_act.png');
                // }
                $('.goldList').html('');
                for (var i = 0; i < res.data.length; i++) {//+'<br>'+res.data[i].money   +'<br>'+res.data[i].money 
                    if (i == 0) {
                        $('.goldList').append('<div><div class="goldItem active"><span><img src="images/gold.png" alt="">' + res.data[i].prodName + '</span><span>￥&nbsp;' + res.data[i].money + '</span> </div></div>')
                    } else {
                        $('.goldList').append('<div><div class="goldItem"><span><img src="images/gold.png" alt="">' + res.data[i].prodName + '</span> <span>￥&nbsp;' + res.data[i].money + '</span></div></div>')
                    }
                }
                for (var i = 0; i < $('.goldItem').length; i++) {
                    (function (i) {
                        $('.goldItem')[i].onclick = function () {
                            checkId()
                            chargeProdId = goldList[i].chargeProdId
                            for (var j = 0; j < $('.goldItem').length; j++) {
                                $($('.goldItem')[j]).removeClass('active');
                            }
                            $($('.goldItem')[i]).addClass('active');
                            // $('.price span')[1].innerHTML = '￥' + goldList[i].money;
                            _money = goldList[i].money;
                        };
                    })(i);
                }
            }
        }
    });
}
var inputVal = '';
var name = '';
var erbanNo = '';
getList();
if (wxCode) {
    // $('.pop-tips').show();
}
function checkId() {
    inputVal = $('#userNo')[0].value
    if (inputVal.trim() == '') {
        return
    }
    console.log($('#userNo')[0].value)
    console.log(numType)
    if (numType && numType == 'userNo') {
        var data = {
            uid: inputVal
        }
        numType = 'erbanNo'
    } else {
        var data = {
            userNo: inputVal
        }
    }
    $.ajax({
        url: allUrl() + '/charge/checkUser',
        data: data,
        success: function success(res) {
            console.log(res)
            if (res.code == 200 && res.data) {
                name = res.data.nick;
                erbanNo = res.data.erbanNo;
                if (erbanNo) {
                    $('#userNo')[0].value = erbanNo;
                }
                $('.infoMsg')[0].innerHTML = '(昵称：' + res.data.nick + ')';
                $('.infoMsg').show();
                isDisable = false
                //    可以点击了
                // $('.zfb').attr('src', 'images/zfb_act.png');
                // $('.wx').attr('src', 'images/wx_act.png');
            } else {
                $('.infoMsg')[0].innerHTML = '不存在此ID的用户';
                $('.infoMsg').show();
                isDisable = true
                // $('.zfb').attr('src', 'images/zfb.png');
                // $('.wx').attr('src', 'images/wx.png');
            }
        }
    });
}

$('#userNo')[0].onfocus = function (e) {
    $('.infoMsg').hide();
    $('.empty').show()
}

$('#userNo')[0].onblur = function (e) {
    if ($('#userNo')[0].value.trim() == '') {
        isDisable = true
        // $('.zfb').attr('src', 'images/zfb.png');
        // $('.wx').attr('src', 'images/wx.png');
    } else {
        checkId();
    }
}

$('.empty')[0].onclick = function () {
    $('#userNo')[0].value = '';
    $('#userNo')[0].focus();
    // $('.zfb').attr('src', 'images/zfb.png');
    // $('.wx').attr('src', 'images/wx.png');
    isDisable = true;
}

// $('.checkBox')[0].oninput = function () {
//     if(!$('.checkBox').prop('checked')) {
//         $('.zfb').attr('src', 'images/zfb.png');
//         $('.wx').attr('src', 'images/wx.png');
//         isDisable = true;
//     }else if($('#userNo')[0].value){
//         isDisable = false
//         $('.zfb').attr('src', 'images/zfb_act.png');
//         $('.wx').attr('src', 'images/wx_act.png');
//     }
// }
//支付宝
$('.zfbBtn')[0].onclick = function () {
    if(!isWx&&zfbAct){
        $('.pop-tips').show();
    }
    if (isDisable || $('#userNo')[0].value.trim() == '') {
        return
    }
    // if (!$('.checkBox').prop('checked')) {
    //     return
    // }
    if (wxCode) {
        $('.pop-tips').show();
    } else {
        inputVal = $('#userNo')[0].value;
        var info = {
            userNo: inputVal,
            name: name,
            money: _money
        }
        sessionStorage.setItem('info', JSON.stringify(info));

        // if(payChannel == 2) {
        //     $.ajax({
        //         url: allUrl() +  '/charge/joinpay/webApply',
        //         type: 'post',
        //         data: {
        //             chargeProdId: chargeProdId,
        //             payChannel: 'ALIPAY_H5',
        //             userNo: inputVal,
        //             successUrl: allUrl() +  '/front/charge_new/success.html'
        //         },
        //         success: function (res) {
        //             console.log(res);
        //             if (res.code == 200) {
        //                 var toPayUrl = res.data.rc_Result.match(/location.href='(\S*)';/)[1];
        //                 if(toPayUrl != -1) {
        //                     console.log(toPayUrl)
        //                     location.href = toPayUrl
        //                     sessionStorage.setItem('wx_wap',res.data.r2_OrderNo);
        //                 }
        //             } else {
        //                 $('#loading').hide();
        //                 $('.toast').html(res.message);
        //                 $('.toast').show();
        //                 setTimeout(function () {
        //                     $('.toast').hide();
        //                 }, 1500)
        //             }
        //         }
        //     })
        //     return
        // }
        
        // Ajax提交
        $.ajax({
			url: "域名地址" + '/charge/sand/alipay/webApply',
            // url: allUrl() + '/charge/webApply',
            type: 'post',
            data: {
                chargeProdId: chargeProdId,
                payChannel: 'sand_alipay_h5',
                userNo: inputVal,
                successUrl: allUrl() + '/front/charge_new/success.html'
            },
            success: function (res) {
                if (res.code == 200) {
                    // Form提交
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

$('.wxBtn')[0].onclick = function () {
    if (isDisable || $('#userNo')[0].value.trim() == '') {
        return
    }
    // if(!$('.checkBox').prop('checked')) {
    //     return
    // }
    inputVal = $('#userNo')[0].value;
    var info = {
        userNo: inputVal,
        name: name,
        money: _money
    }

    sessionStorage.setItem('info', JSON.stringify(info));
    // console.log(info);
    // return;
    if (openId) {
        var obj={};
        obj.chargeProdId=chargeProdId;
        obj.erban_no=inputVal;
        obj.openId=openId;
        obj.payChannel='wx_pub';
        $.ajax({
            url: allUrl() + '/wx/submitPay',
            data: obj,
            method: 'post',
            success: function success(res) {
                console.log(res)
                if (res.code === 200) {
                    var packages = res.data;
                    var timeStamp = packages.timestamp; //时间戳，自1970年以来的秒数
                    var nonceStr = packages.nonce_str; //随机串
                    var package = packages.prepay_id;
                    var paySign = packages.sign; //微信签名
                    var nick = packages.nick;
                    var erbanNo = packages.erban_no;
                    function onBridgeReady() {
                        WeixinJSBridge.invoke(
                            'getBrandWCPayRequest', {
                                "appId": "wxbe281714c57530fd",
                                "timeStamp": timeStamp,
                                "nonceStr": nonceStr,
                                "package": package,
                                "signType": "MD5",
                                "paySign": paySign
                            },
                            function (res) {
                                if (res.err_msg == "get_brand_wcpay_request:ok") { // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回ok，但并不保证它绝对可靠。
                                    window.location.href = allUrl() + '/front/charge_new/success.html';
                                } else if (res.err_msg == "get_brand_wcpay_request:fail") {
                                } else { }
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
                }
            }
        });
    } else {
        var _data = {
            chargeProdId: chargeProdId,
            payChannel: 'wx_wap',
            userNo: inputVal,
            successUrl: allUrl() + '/front/charge_new/success.html'
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
        })
    }
}

// $('.protocol')[0].onclick = function () {
//     window.location.href = allUrl() + '/front/charge_new/protocol.html';
// }

$('.pop-tips').click(function () {
    $('.pop-tips').hide();
    zfbAct =false;
});
