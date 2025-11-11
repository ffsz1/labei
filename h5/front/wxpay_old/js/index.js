/*
 * 变量初始化
 * */
var num = $('#num'),
    golds = $('.gold'),
    mask = $('.mask'),
    layer = $('.layer'),
    cancel = $('#cancel'),
    confirm = $('#confirm'),
    descConfirm = $('#desc_confirm'),
    payBase_url = '/wx/submitPay?',
    list_url = '/chargeprod/list?channelType=1',
    locateObj = getQueryString();

var url = window.location.href;

var fail_url = url.split('pay.html')[0] + 'failed.html';

var open = url.split('?')[1];
if (open) {
    // open = open.split('=')[1];
    open = locateObj.openId;
    if (!localStorage.getItem('openId')) {
        localStorage.setItem('openId', open);
    }
}

if (localStorage.mmHistory) {
    getHistory()
    $('#num').val($($(".num_history_item")[0]).children('p').html())
} else {
    localStorage.setItem('mmHistory', '')
}

$(function() {
    $.ajax({
        type: 'GET',
        url: list_url,
        asyc: true,
        success: function(res) {
            if (res.code == 200) {
                renderList(res.data);

                var sub_btn = $('.charge');
                resPay(sub_btn, res.data);
            }
        },
        error: function(res) {

        }
    });
});

function getHistory() {
    var historyList = '';
    var mmHistory = localStorage.mmHistory.split(',');

    mmHistory.forEach(function(item, index) {
        if (item != '') {
            historyList += "<span class=\"num_history_item\"><p onclick=\"selectNum(" + item + ")\">" + item + "</p><img src=\"./image/close.png\" onclick=\"delHistory(" + index + ")\"></span>";
        }
    })
    $('.num_history > div').html(historyList)
}


function renderList(data) {
    for (var i = 0; i < data.length; i++) {
        var chargeProdId = data[i].chargeProdId;
        var prodName = data[i].prodName;
        var money = data[i].money;
        if (data[i].prodDesc) {
            var prodDesc = data[i].prodDesc;
            golds.append('<li class="border-1px charge" _charge="' + chargeProdId + '">' +
                '<span class="amount">' + prodName + '</span>' +
                '<span class="desc">' + prodDesc + '</span>' +
                '<span id="price_1" class="price">￥' + money + '</span>' +
                '</li>');
        } else {
            golds.append('<li class="border-1px charge" _charge="' + chargeProdId + '">' +
                '<span class="amount">' + prodName + '</span>' +
                '<span id="price_1" class="price">￥' + money + '</span>' +
                '</li>');
        }

    }
}

function resPay(btn, arr) {
    $('.gold').on('click', 'li', function() {
        var charge = $(this).attr('_charge'),
            reg = /^[0-9]*$/,
            _num = num.val();
        var index = $(this).index();
        price = arr[index].money;

        var openId = localStorage.getItem('openId');

        if (_num === '' || !reg.test(_num)) {
            alert('请输入正确的手机号或者拉贝号');
            return;
        } else {
            var _reg = /^1\d{10}$/;
            if (_reg.test(_num)) {
                pay_url = payBase_url + 'phone=' + _num + '&chargeProdId=' + charge + '&openId=' + openId;
            } else {
                pay_url = payBase_url + 'erban_no=' + _num + '&chargeProdId=' + charge + '&openId=' + openId;
            }
        }
        $('.loading-box').show();
        $.ajax({
            type: 'POST',
            url: pay_url,
            asyc: true,
            success: function(res) {
                $('.loading-box').hide();
                if (res.code == 4002) {
                    alert('该手机号未注册过拉贝账号');
                    window.location.reload();
                } else if (res.code == 1404) {
                    alert('拉贝号不存在');
                    window.location.reload();
                } else {
                    var packages = res.data;
                    var timeStamp = packages.timestamp; //时间戳，自1970年以来的秒数
                    var nonceStr = packages.nonce_str; //随机串
                    var package = packages.prepay_id;
                    var paySign = packages.sign; //微信签名
                    var nick = packages.nick;
                    var erbanNo = packages.erban_no;

                    mask.toggle();
                    layer.toggle();
                    // descConfirm.text('向' + nick + '(拉贝号：' + erbanNo + ')充值' + price + '.00元?');
                    descConfirm.html('向<span class="nick">' + nick + '</span>(' + '拉贝号：' + erbanNo + ')充值<span class="desc_price">' + price + '</span>元？');

                    confirm.on('click', function() {
                        num.val('');
                        mask.toggle();
                        layer.toggle();

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
                                                localStorage.mmHistory = _num.toString();
                                            } else {

                                            }
                                            mmHistory.push(_num)
                                            localStorage.mmHistory = mmHistory.toString();

                                        } else {
                                            if (mmHistory.indexOf(_num.toString()) !== -1) {
                                                return;
                                            }
                                            mmHistory.unshift(_num)
                                            localStorage.mmHistory = mmHistory.join(',');
                                        }

                                        if (localStorage.mmHistory.split(',').length > 6) {
                                            var MmHistory = localStorage.mmHistory.split(',');
                                            localStorage.mmHistory = MmHistory.slice(0, 6).join(',');
                                        }

                                        window.location.reload();
                                    } else if (res.err_msg == "get_brand_wcpay_request:fail") {
                                        wind_reload(fail_url);
                                    } else {
                                        wind_reload(fail_url);
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
                    });
                }
            },
            error: function(res) {
                $('.loading-box').hide();
                console.log(res);
            }
        });
    })
}

$('#cancel').on('click', function() {
    mask.toggle();
    layer.toggle();

    // window.location.reload(true);
    console.log('讲道理应该会刷新的二号');
    window.location.href = window.location.href + '&timestamp=' + (new Date()).valueOf();

    // window.location.reload(true);
});

function wind_reload(url) {
    window.location.assign(url);
}

// 获取地址栏参数
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

$('form').on('submit', function(e) {
    e.preventDefault()
    $(".num_history").hide();
});

function showHistory() {
    if (localStorage.mmHistory.length > 1) {
        getHistory()
        $(".num_history").show();
    }
}

function delHistory(index) {
    var mmHistory = localStorage.mmHistory.split(',');
    mmHistory.splice(index, 1)
    localStorage.mmHistory = mmHistory.join(',');
    getHistory()
}

function selectNum(value) {

    $('#num').val(value)
    $(".num_history").hide();
}