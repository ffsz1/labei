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
var _money = 6;
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
                $('.goldList').html('');
                for (var i = 0; i < res.data.length; i++) {
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
            } else {
                $('.infoMsg')[0].innerHTML = '不存在此ID的用户';
                $('.infoMsg').show();
                isDisable = true
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
    } else {
        checkId();
    }
}

$('.empty')[0].onclick = function () {
    $('#userNo')[0].value = '';
    $('#userNo')[0].focus();
    isDisable = true;
}

//支付宝
$('.zfbBtn')[0].onclick = function () {
    alert_();
    return false;
    if (isWx ) {
        $('.pop-tips').show();
        return;
    }
    if (isDisable || $('#userNo')[0].value.trim() == '') {
        return
    }
    inputVal = $('#userNo')[0].value;
    var info = {
        userNo: inputVal,
        name: name,
        money: _money
    }
    sessionStorage.setItem('info', JSON.stringify(info));
    $.ajax({
        url: "域名地址" + '/sand/alipay/webApply',
        type: 'post',
        data: {
            chargeProdId: chargeProdId,
            userNo: inputVal,
            successUrl: allUrl() + '/front/charge_new/success.html'
        },
        success: function (res) {
            if (res.code == 200) {
				console.log(res);
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

$('.pop-tips').click(function () {
    $('.pop-tips').hide();
    zfbAct = false;
});