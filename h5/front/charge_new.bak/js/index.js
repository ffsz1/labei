// wxCode
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

if (localStorage.mmHistory) {
    getHistory()
    $('#userNo').val($($('.userNo_history_item')[0]).children('p').html())
} else {
    localStorage.setItem('mmHistory', '')
}

function getHistory() {
    var historyList = '';
    var mmHistory = localStorage.mmHistory.split(',');

    mmHistory.forEach(function(item, index) {
        if (item != '') {
            historyList += "<span class=\"userNo_history_item\"><p onclick=\"selectNum(" + item + ")\">" + item + "</p><img src=\"images/empty.png\" onclick=\"delHistory(" + index + ")\"></span> ";;
        }
    })
    $('.userNo_history').html(historyList)
}


function showAlert(msg, cb) {
    var $alert = $('<div class="alert-tips">');
    var $wrap = $('<div class="alert-wrap">');
    $wrap.append('<div class="alert-msg">' + msg + '</div>');
    $wrap.append('<a href="#" class="btn-confirm">确定</a>');
    $alert.append($wrap);
    $alert.on('click', '.btn-confirm', function(e) {
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
    $confirm.on('click', '.btn-r', function(e) {
        e.preventDefault();
        if (cb) {
            cb();
        }
        $confirm.remove();
    });
    $confirm.on('click', '.btn-l', function(e) {
        e.preventDefault();
        $confirm.remove();
    });
    $confirm.appendTo('body');
}
if (sessionStorage.getItem('wx_pub')) {
    var _chargeRecordId = sessionStorage.getItem('wx_pub');
    sessionStorage.removeItem('wx_pub');
    showConfirm("请确定微信支付是否完成", function() {
        $.ajax({
            url: allUrl() + '/charge/checkApply',
            type: "get",
            data: {
                chargeRecordId: _chargeRecordId
            },
            success: function(ret) {
                if (ret.code == 200 && ret.data == true) {
                    showSuccess();
                } else {
                    showAlert("支付失败，请重试");
                }
            },
            error: function() {
                // showTips('服务器出差了');
            }
        });
    });
}
var openId = '';
if (wxCode) {
    $.ajax({
        url: allUrl() + '/wx/snsapi/baseinfo/get',
        data: {
            code: wxCode,
            state: '123#wechat_redirect'
        },
        method: 'get',
        success: function success(res) {
            console.log(res)
            if (res.code === 200) {
                openId = res.data
            }
        }
    });
} else {
    $('.tips').hide()
}

function showSuccess() {
    $('.pop-success').show();
    setTimeout(function() {
        if (sessionStorage.getItem('_successUrl')) {
            window.location.href = sessionStorage.getItem('_successUrl')
        }
        $('.pop-success').hide();
    }, 1500)
}
var goldList = []
var chargeProdId = "19";
var urlInfo = getQueryString(); //用于拿去用户ID
var _money = 48;
var fadeList = [{ "chargeProdId": "11", "prodName": "80金币", "prodDesc": "首充送20金币", "money": 8, "giftGoldNum": 0, "channel": 1, "seqNo": 10 }, { "chargeProdId": "19", "prodName": "480金币", "prodDesc": "", "money": 48, "giftGoldNum": 0, "channel": 1, "seqNo": 20 }, { "chargeProdId": "20", "prodName": "980金币", "prodDesc": "", "money": 98, "giftGoldNum": 0, "channel": 1, "seqNo": 30 }, { "chargeProdId": "21", "prodName": "1980金币", "prodDesc": "", "money": 198, "giftGoldNum": 0, "channel": 1, "seqNo": 40 }, { "chargeProdId": "22", "prodName": "4980金币", "prodDesc": "", "money": 498, "giftGoldNum": 0, "channel": 1, "seqNo": 50 }, { "chargeProdId": "23", "prodName": "9980金币", "prodDesc": "", "money": 998, "giftGoldNum": 0, "channel": 1, "seqNo": 60 }, { "chargeProdId": "24", "prodName": "49980金币", "prodDesc": "", "money": 4998, "giftGoldNum": 0, "channel": 1, "seqNo": 70 }, { "chargeProdId": "25", "prodName": "99990金币", "prodDesc": "", "money": 9999, "giftGoldNum": 0, "channel": 1, "seqNo": 80 }, { "chargeProdId": "26", "prodName": "300000金币", "prodDesc": null, "money": 30000, "giftGoldNum": 0, "channel": 1, "seqNo": 121 }, { "chargeProdId": "27", "prodName": "600000金币", "prodDesc": null, "money": 60000, "giftGoldNum": 0, "channel": 1, "seqNo": 122 }];
for (var i = 0; i < fadeList.length; i++) {
    if (i == 1) {
        $('.goldList').append('<div class="goldItem"><div><img src="images/gold.png" alt=""><span>' + fadeList[i].prodName + '</span></div><div class="goldItem_money active">￥' + fadeList[i].money + '</div></div>')
    } else {
        $('.goldList').append('<div class="goldItem"><div><img src="images/gold.png" alt=""><span>' + fadeList[i].prodName + '</span></div><div class="goldItem_money">￥' + fadeList[i].money + '</div></div>')
    }
}
for (var i = 0; i < $('.goldItem').length; i++) {
    (function(i) {
        $('.goldItem')[i].onclick = function() {
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

function getList() {
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
                if ($('#userNo')[0].value) {
                    $('.zfb').attr('src', 'images/zfb_act.png');
                    $('.wx').attr('src', 'images/wx_act.png');
                }
                for (var i = 0; i < res.data.length; i++) {
                    if (i == 1) {
                        $('.goldList').append('<div class="goldItem"><div><img src="images/gold.png" alt=""><span>' + res.data[i].prodName + '</span></div><div class="goldItem_money active">￥' + res.data[i].money + '</div></div>')
                    } else {
                        $('.goldList').append('<div class="goldItem"><div><img src="images/gold.png" alt=""><span>' + res.data[i].prodName + '</span></div><div class="goldItem_money">￥' + res.data[i].money + '</div></div>')
                    }
                }
                for (var i = 0; i < $('.goldItem').length; i++) {
                    (function(i) {
                        $('.goldItem')[i].onclick = function() {
                            chargeProdId = goldList[i].chargeProdId
                            for (var j = 0; j < $('.goldItem_money').length; j++) {
                                $($('.goldItem_money')[j]).removeClass('active');
                            }
                            $($('.goldItem_money')[i]).addClass('active');

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
var userData = false;
var chargeChannel = 1;
getList();

$('#userNo')[0].onfocus = function(e) {
    $('.infoMsg').hide();
    $('.empty').show();
    $(".userNo_history").show();
}

$('#userNo')[0].onblur = function(e) {
    setTimeout(function() {
        $(".userNo_history").hide();
    });
}

$('.empty')[0].onclick = function() {
    $('#userNo')[0].value = '';
    $('#userNo')[0].focus();
    $('.zfb').attr('src', 'images/zfb.png');
    $('.wx').attr('src', 'images/wx.png');
}

//支付宝
$('.ali_btn')[0].onclick = function() {
    if ($('#userNo')[0].value.trim() == '') {
        showAlert("请输入拉贝号");
        return
    }
    inputVal = $('#userNo')[0].value;
    $.ajax({
        url: allUrl() + '/charge/checkUser',
        data: {
            userNo: inputVal
        },
        success: function success(res) {
            if (res.code == 200 && res.data) {
                userData = res.data
            } else {
                userData = false
            }
            if (!userData) {
                showAlert("用户不存在");
                return
            }
            showCheckBox()
            var info = {
                userNo: inputVal,
                name: name,
                money: _money
            }
            sessionStorage.setItem('info', JSON.stringify(info));
            chargeChannel = 2;
        }
    });
}

$('.wx_btn')[0].onclick = function() {
    if ($('#userNo')[0].value.trim() == '') {
        showAlert("请输入拉贝号");
        return
    }
    inputVal = $('#userNo')[0].value;
    $.ajax({
        url: allUrl() + '/charge/checkUser',
        data: {
            userNo: inputVal
        },
        success: function success(res) {
            if (res.code == 200 && res.data) {
                userData = res.data
            } else {
                userData = false
            }
            if (!userData) {
                showAlert("用户不存在");
                return
            }
            showCheckBox()
            var info = {
                userNo: inputVal,
                name: name,
                money: _money
            }
            sessionStorage.setItem('info', JSON.stringify(info));
            chargeChannel = 1;
        }
    });
}

// $('.protocol')[0].onclick = function () {
//     window.location.href = allUrl() + '/mm/charge_new/protocol.html';
// }

$('.pop-tips').click(function() {
    $('.pop-tips').hide();
});

function showHistory() {
    if (localStorage.mmHistory.length > 1) {
        getHistory()
        $(".userNo_history").show();
    }
}

function delHistory(index) {
    var mmHistory = localStorage.mmHistory.split(',');
    mmHistory.splice(index, 1)
    localStorage.mmHistory = mmHistory.join(',');
    getHistory()
}

function selectNum(value) {
    $('#userNo').val(value)
    $(".userNo_history").hide();
}

function hideCheckBox() {
    $('.check_box').hide()
}

function showCheckBox() {
    $('.check_box_text').html('向 ' + userData + ' 充值' + _money + '.00元?')
    $('.check_box').show()
}

function charge() {
    if (chargeChannel === 1) {
        if (openId) {
            $.ajax({
                url: allUrl() + '/wx/submitPay',
                data: {
                    chargeProdId: chargeProdId,
                    erban_no: inputVal,
                    openId: openId,
                    payChannel: 'wx_pub'
                },
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
                                function(res) {
                                    if (res.err_msg == "get_brand_wcpay_request:ok") { // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回ok，但并不保证它绝对可靠。

                                        var mmHistory = localStorage.mmHistory.split(',');
                                        if (mmHistory.length <= 1) {

                                            if (mmHistory[0] == '') {
                                                localStorage.mmHistory = inputVal.toString();
                                            } else {

                                            }
                                            mmHistory.push(inputVal)
                                            localStorage.mmHistory = mmHistory.toString();

                                        } else {
                                            if (mmHistory.indexOf(inputVal.toString()) !== -1) {
                                                return;
                                            }
                                            mmHistory.unshift(inputVal)
                                            localStorage.mmHistory = mmHistory.join(',');
                                        }

                                        if (localStorage.mmHistory.split(',').length > 6) {
                                            var MmHistory = localStorage.mmHistory.split(',');
                                            localStorage.mmHistory = MmHistory.slice(0, 6).join(',');

                                        }
                                    } else if (res.err_msg == "get_brand_wcpay_request:fail") {} else {}
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
                payChannel: 'wx_pub',
                userNo: inputVal
            }
            if (urlInfo && urlInfo.redirect_url) {
                sessionStorage.setItem('_successUrl', urlInfo.redirect_url)
            }
            $.ajax({
                url: allUrl() + '/charge/webApply',
                type: 'post',
                data: _data,
                success: function(res) {
                    if (res.code == 200) {
                        sessionStorage.setItem('wx_pub', res.data.orderNo);
                        pingpp.createPayment(res.data, function(result, err) {
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
                        setTimeout(function() {
                            $('.toast').hide();
                        }, 1500)
                    }
                }
            })
        }
    } else if (chargeChannel === 2) {
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
            $.ajax({
                url: allUrl() + '/charge/webApply',
                type: 'post',
                data: {
                    chargeProdId: chargeProdId,
                    payChannel: 'alipay_wap',
                    userNo: inputVal,
                    successUrl: allUrl() + '/mm/charge_new/success.html'
                },
                success: function(res) {
                    if (res.code == 200) {
                        pingpp.createPayment(res.data, function(result, err) {
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
                        setTimeout(function() {
                            $('.toast').hide();
                        }, 1500)
                    }
                }
            })
        }
    }
}
