var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function(obj) {
    return typeof obj;
} : function(obj) {
    return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj;
};
var browser = checkVersion();
var info = {};
var initData = getQueryStringArgs()
console.log('initData', decodeURI(window.location.href), getQueryStringArgs(), initData)
info.diamondNum = initData.diamondNum || 0;
info.goldNum = initData.goldNum || 0;
info.nickName = initData.nick || '';
info.yyId = initData.erbanNo || '';
info.uid = initData.uid;
info.openId = null;
info.openId = initData.openId;
info.token = initData.token;
info.code = initData.codeMatch;
info.redbeanNum = initData.redbeanNum || 0;
info.withdrawNum = 0;
// info.type = 1;
info.userInfo = {};
info.isBindAlipay = false;
info.zfbAcc = initData.zfbAcc;
info.zfbName = initData.zfbName;
info.zfbSele = initData.zfbSele || 0;
initPage()

// $('body').append('<div class="toast">');
// var $toast = {
//     show: function (obj) {
//         $('.toast').text(obj.text).show();
//         var that =this;
//         setTimeout(function () {
//             $('.toast').hide();
//             that.callback(obj.callback);
//         }, obj.time)
//     },
//     callback:function(fn){
//         fn();
//     }
// };
function toFiexd2(num) {
    return Number(num.toFixed(2))
}
var drawlist = [];

function initPage() {
    console.log('initPage', info.type)
    if (info.type == 1) {

        info.diamondNum = info.diamondNum - info.withdrawNum > 0 ? toFiexd2(info.diamondNum - info.withdrawNum) : info.diamondNum - info.withdrawNum; //减去上次提现数额
    } else if (info.type == 3) {

        info.diamondNum = info.diamondNum - info.withdrawNum > 0 ? toFiexd2(info.diamondNum - info.withdrawNum) : info.diamondNum - info.withdrawNum; //减去上次提现数额

        info.goldNum = info.goldNum + parseFloat(info.withdrawNum);
    }
    if (info.type == 3) {
        $('.box .submit span').text('兑换');
    } else {
        $('.box .submit span').text('提取');
    }
    info.withdrawNum = 0;
    //初始化页面
    $('#withdraw-num').val('');
    $('.container .submit').removeClass('active')
    $('.container .tips').removeClass('active').text('Tips：当前提现比例为10钻=1元');
    // info.type = 1;
    $(".input-area2,.inputmoney").hide()
    $('.user-id').text(info.yyId || 0);
    $('.nick-name').text(info.nickName || 0);
    console.log(info, info.type, info.diamondNum)
    $('#diamond-num').text(info.diamondNum || 0);
    $('#diamond-num1').text(info.goldNum || 0);
    $('.tab span').removeClass('active');
    $('.tab span:eq(0)').addClass('active');
    $(".choose").show();

    // 获取提现列表
    $.ajax({
        url: allUrl() + '/wxPublic/findWithdrawal',
        data: {},
        success: function(res) {
            console.log(res);
            drawlist = res.data || [];
            var str = '';
            for (var i = 0; i < drawlist.length; i++) {
                str += '<div class="choose-item" data-v="' + drawlist[i].cashNum + '" data-i="' + drawlist[i].cashProdId + '"><div><div class="c1">￥ <span class="c10">' + drawlist[i].cashNum + '</span></div><div class="c2">(消耗<span class="c20">' + drawlist[i].diamondNum + '</span>钻石)</div></div></div>';
            }
            $(".choose").html(str);
        }
    })
}
$('.tab span').on('touchend', function() {
    $('.tab span').removeClass('active');
    $(this).addClass('active');
    info.type = $(this).data().index;
    if (info.type == 3) {
        $('.tips').text('Tips：当前钻石兑换金币比例为10钻石= 10金币').removeClass("active");
        $('.box .submit span').text('兑换');
        $('.box .submit').removeClass("active");
        $('.container .submit').css({ "margin-top": "1rem" })
        $(".choose,.input-area").hide()
        $(".input-area2,.inputmoney").show();
    } else if (info.type == 1) {
        $(".choose,.input-area").show();
        $(".input-area2,.inputmoney").hide()
        $('.tips').text('Tips：当前提现比例为10钻石= 1元').removeClass("active");
        $('.box .submit span').text('提取');
        $("#withdraw-num").val('');
        $('.container .submit').css({ "margin-top": "0.3rem" })
        $('.box .submit,.choose-item').removeClass("active");
    }
})
if (uidMatch) {
    if (info.code) {
        $.ajax({
            url: allUrl() + '/wxPublic/snsapi/baseinfo/scan/get',
            data: {
                code: info.code,
                state: '123#wechat_redirect'
            },
            method: 'get',
            success: function success(res) {
                console.log(res)
                if (res.code === 200) {
                    info.openId = res.data.openid;
                    info.weixinName = res.data.nickname;
                } else {
                    $toast.show({
                        text: '授权过期！',
                        time: 2000,
                        callback: function() {
                            location.replace('./index.html');
                        }
                    })
                }
            }
        });
    }
    //填写支付宝信息后，
    info.uid ? zfbFun() : '';
}
if (wxCode) {
    $.ajax({
        url: allUrl() + '/wxPublic/snsapi/baseinfo/scan/get',
        data: {
            code: wxCode,
            state: '123#wechat_redirect'
        },
        method: 'get',
        success: function success(res) {
            console.log(res)
            if (res.code === 200) {
                info.openId = res.data.openId;
                info.unionid = res.data.unionid;
                info.weixinName = res.data.nickname;
                console.log(info.openId);
                $.ajax({
                    url: allUrl() + '/wxPublic/checkBindWx',
                    data: {
                        unionId: info.unionid,
                        // unionid: 'o3i0V5sZor7fiRtAmTI4gZR4WM5o',
                        // accessToken: info.accessToken
                    },
                    method: 'get',
                    success: function success(res) {
                        console.log(res, '用户信息')
                        if (res.code === 200) {
                            info.userInfo = res.data;
                            info.diamondNum = res.data.diamondNum || 0;
                            info.goldNum = res.data.goldNum || 0;
                            info.uid = res.data.uid || 0;
                            info.yyId = res.data.erbanNo || 0;
                            info.nickName = res.data.nick || 0;
                            info.token = res.data.token || 0;
                            setDom();
                            info.uid ? zfbFun() : '';
                        } else {
                            // location.href = './index.html';
                            location.replace('./index.html');
                        }
                    }
                });
            } else {
                $toast.show({
                    text: '授权过期！',
                    time: 2000,
                    callback: function() {
                        location.replace('./exchange.html');
                    }
                })
            }
        }
    });

} else {
    console.log('no wxcode')
}
//获取绑定支付宝信息
/**
 * info.zfbSele   1 为支付宝账号信息修改了
 */
function zfbFun() {
    $.ajax({
        url: allUrl() + '/wxPublic/checkBindAliPay',
        data: {
            uid: info.uid,
        },
        method: 'get',
        success: function success(res) {
            console.log(res, '支付宝信息')
            if (res.code === 200) {
                info.isBindAlipay = res.data.isBindAlipay;
                info.diamondNum = res.data.diamondNum || 0;
                info.goldNum = res.data.goldNum || 0;
                if (info.isBindAlipay) {
                    $('.bingZfb').text("更改支付宝信息");
                    if (info.zfbSele) {
                        //更改了原有的账号
                    } else {
                        info.zfbAcc = res.data.alipayAccount;
                        info.zfbName = res.data.alipayAccountName;
                    }
                } else {
                    info.zfbAcc ? $('.bingZfb').text("更改支付宝信息") : $('.bingZfb').text("填写支付宝信息");
                }
            } else {
                // location.href ='./indexZfb.html';
            }
        }
    });
}
$('.bingZfb').on('touchend', function() {
    location.href = './indexZfb.html?uid=' + info.uid;
})

function setDom() {
    $('.user-id').text(info.yyId);
    $('.nick-name').text(info.nickName);
    $('#diamond-num').text(info.diamondNum);
    $('#diamond-num1').text(info.goldNum);
}

$('.mobileAccount').on('touchend', function() {
    location.replace("./index.html");
})
var valzs = false;
var pid = false;
$(document).on("touchend", ".choose-item", function() {
    $(this).addClass("active").siblings(".choose-item").removeClass("active");
    valzs = $(this).attr("data-v");
    pid = $(this).attr("data-i");
    if (valzs && valzs * 10 <= info.diamondNum) {
        $('.container .submit').addClass('active')
        $('.tips').text('Tips：当前提现比例为10钻=1元').removeClass('active');
    } else if (valzs * 10 >= info.diamondNum) {
        $('.container .submit').removeClass('active');
        $('.tips').text('超出可兑换额度').addClass('active');
    }
})
$('#withdraw-num').on('input', function() { //输入提现数量的  判断逻辑
    var len = $(this).val().length;
    var val = $(this).val();
    if (len === 0) {
        $('.tips').text('Tips：当前提现比例为10钻=10金币').removeClass('active');
    } else if ((len > 0 && (val % 10 != 0)) || val > 200000) {
        $('.tips').text('Tips：请输入10到200000的10的倍数').addClass('active');
    } else if (val > info.diamondNum) {
        $('.tips').text('超出可兑换额度').addClass('active');
    } else {
        $('.tips').text('Tips：当前提现比例为10钻=10金币').removeClass('active');
    }

    if (val >= 10 && val <= 200000 && val % 10 === 0 && val <= info.diamondNum) {
        $('.container .submit').addClass('active')
    } else {
        $('.container .submit').removeClass('active')
    }

})
$('.container .submit').on('touchend', function() { //点击提交 提现数量 显示相关信息
    if ($(this).hasClass('active')) {
        console.log('提交')
        $('.popup .user .user-id').text(info.userInfo.yyId);
        $('.popup .name .nick-name').text(info.userInfo.nickName);
        if (info.type == 3) {
            $('#limit').text($('#withdraw-num').val() / 10);
            info.withdrawNum = $('#withdraw-num').val();
        } else {
            $('#limit').text(valzs);
            info.withdrawNum = valzs * 10;
        }
        $('.popup .consume span').eq(1).text(info.withdrawNum);
        //显示

        if (info.type == 3) {
            goldExchage();
        } else {
            $('.popup').show().find('.con').show().end().find('.confirm-zfb').hide();
        }
    }
})

$('.verify-zfb input').on('input', function() { //输入支付宝信息时 控制 提交支付宝信息 的按钮
    if ($('#input-number').val().length > 0 && $('#input-name').val().length > 0) {
        $('.verify-zfb .submit').addClass('active')
    } else {
        $('.verify-zfb .submit').removeClass('active')
    }
})

$('#submit-zfb').on('touchend', function() { //点击 提交支付宝信息 的按钮
    if ($(this).hasClass('active')) {
        info.zfbNumber = $('#input-number').val();
        info.realName = $('#input-name').val();
        $('#zfb-num').text(info.zfbNumber);
        $('#real-name').text(info.realName);
        $('.popup').show().find('.con').hide().end().find('.confirm-zfb').show()
    } else {
        $toast.show({
            text: '000',
            time: 2000
        })
    }
})
$(".close-pop").on("touchend", function() {
    $('.popup').hide();
})

function goldExchage() {
    $.ajax({
        type: 'post',
        url: allUrl() + '/wxPublic/exchangeGold',
        data: {
            type: 1, //类型 1、钻石 2、拉贝 暂时只支持钻石兑换
            uid: info.uid,
            token: info.token,
            exchangeNum: info.withdrawNum
        },
        success: function success(res) {

            if (res.code === 200) {
                console.log(res)
                initPage();
                $toast.show({
                    text: '金币兑换成功！',
                    time: 2500
                })
                $('.popup').hide();
            } else if (res.code === 500) {
                $toast.show({
                    text: res.message.split(':')[1],
                    time: 500
                })
                setTimeout(function() {
                    location.replace('./index.html');
                }, 500);

            } else {
                $toast.show({
                    text: res.message.split(':')[1],
                    time: 2500
                })

            }
        }
    })
};
//点击 微信提现  //发送ajax
function wxtx() {
    console.log('点击 微信提现')
        //发送ajax
    var obj = {};
    // obj.erbanno = info.yyId;
    obj.pid = pid;
    // obj.withDrawNum = info.withdrawNum;
    obj.type = 1;
    obj.uid = info.uid;
    obj.token = info.token;
    obj.withdrawType = info.type;
    obj.openId = info.openId;
    obj.weixinName = info.weixinName;

    $.ajax({
            type: 'post',
            url: allUrl() + '/wxPublic/noPublicWithDrawCash',
            data: obj,
            success: function success(res) {

                if (res.code === 200) {
                    console.log(res)
                    initPage();
                    $toast.show({
                        text: '微信提现成功，会在24小时内处理',
                        time: 2500
                    })
                    $('.popup').hide();
                } else if (res.code === 500) {
                    $toast.show({
                        text: res.message.split(':')[1],
                        time: 500
                    })
                    setTimeout(function() {
                        location.replace('./index.html');
                    }, 500);

                } else {
                    $toast.show({
                        text: res.message.split(':')[1],
                        time: 2500
                    })

                }
            }
        })
        // $('.popup').hide();

}
//点击 支付宝提现
/**
 * zfbAcc 淘宝账号
 * isBindAlipay  否绑定淘宝账号
 * info.zfbAcc 判断是否纪录淘宝账号，如果有，择不管是否绑定淘宝账号，如果没有，择判断是否有绑定淘宝账号
 */
function zfbtx() {
    console.log('点击 支付宝提现');
    if (info.zfbAcc || info.isBindAlipay) {
        $Dialog({
            title: '',
            content: '<span>支付宝账号：' + info.zfbAcc + '</span>' + '<span class="colRed">是否将受益提现到以上支付宝</span>',
            diaClass: 'confirm',
            buttons: [{
                className: "",
                text: "取消",
                callback: function() {

                }
            }, {
                className: "color-bold",
                text: "确认",
                callback: function() {
                    confirmZfb();
                }
            }]
        })
    } else {
        $Dialog({
            title: '',
            content: '您尚未绑定支付宝<br>请绑定',
            diaClass: 'confirm',
            buttons: [{
                className: "color-bold",
                text: "去绑定",
                callback: function() {
                    location.href = './indexZfb.html?uid=' + info.uid; //跳去绑定
                }
            }, {
                className: "",
                text: "取消",
                callback: function() {

                }
            }]
        })
    }
}
//点击 确认支付宝信息  //发送ajax
function confirmZfb() {
    console.log('点击 确认支付宝信息')
        //发送ajax
    var obj = {};
    obj.pid = pid;
    obj.token = info.token;
    obj.withdrawType = info.type;
    obj.type = 2;
    obj.uid = info.uid;
    obj.erbanno = info.erbanno;
    // if(!info.isBindAlipay){
    obj.alipayAccount = info.zfbAcc;
    obj.realName = info.zfbName;
    // }
    $.ajax({
            type: 'post',
            url: allUrl() + '/wxPublic/noPublicWithDrawCash',
            data: obj,
            success: function success(res) {

                if (res.code === 200) {
                    initPage();

                    $toast.show({
                        text: '支付宝提现成功，会在24小时内处理',
                        time: 2500
                    })
                } else if (res.code === 500) {
                    $toast.show({
                        text: res.message.split(':')[1],
                        time: 500
                    })
                    setTimeout(function() {
                        location.replace('./index.html');
                    }, 500);

                } else {
                    $toast.show({
                        text: res.message.split(':')[1],
                        time: 2500
                    })

                }
            }
        })
        //返回提现页面
    $('.popup').hide();
    $('.container').show();
    $('.verify-zfb').hide();

    //返回提现页面后 更新数据！
}


function hidePop() {
    $('.popup').hide();
}


//解析url 拿到参数对象
function parseQueryString(url) {
    var result = {};
    var url = decodeURI(url);
    if (url.indexOf('?') > -1) {
        var str = url.split('?')[1];
        var temp = str.split('&');
        for (var i = 0; i < temp.length; i++) {
            var temp2 = temp[i].split('=');
            result[temp2[0]] = temp2[1];
        }
    }
    return result;
}

function getQueryStringArgs() {
    //取得查询字符串并去掉开头的问号
    var qs = (location.search.length > 0 ? location.search.substring(1) : ""),

        //保存数据的对象
        args = {},

        //取得每一项
        items = qs.length ? qs.split("&") : [],
        item = null,
        name = null,
        value = null,
        //在 for 循环中使用
        i = 0,
        len = items.length;
    //逐个将每一项添加到 args 对象中
    for (i = 0; i < len; i++) {
        item = items[i].split("=");
        name = decodeURIComponent(item[0]);
        value = decodeURIComponent(item[1]);
        if (name.length) {
            args[name] = value;
        }
    }

    return args;
}