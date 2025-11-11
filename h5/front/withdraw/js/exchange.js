var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function(obj) {
    return typeof obj;
} : function(obj) {
    return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj;
};

var browser = checkVersion();
var initData = getQueryStringArgs();
console.log('initData', decodeURI(window.location.href), getQueryStringArgs(), initData);
var info = {
    diamondNum: initData.diamondNum || 0,
    goldNum: initData.goldNum || 0,
    uid: initData.uid,
    phone: initData.phone,
    openId: initData.openId,
    token: initData.token,
    code: initData.codeMatch,
    withdrawNum: 0,
    isBindAlipay: false,
    type: 3,
    withdrawType: 2,
    alipayAccount: initData.alipayAccount,
    alipayAccountName: initData.alipayAccountName,
    bankcard: initData.bankcard,
    bankcardName: initData.bankcardName,
};
var timer;

initPage();

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

let drawlist = [];

// 初始化页面 (type [1.提现; 3.兑换])
function initPage() {
    console.log('initPage', info.type)
    if (info.type == 1) {
        info.diamondNum = info.diamondNum - info.withdrawNum > 0 ? toFiexd2(info.diamondNum - info.withdrawNum) : info.diamondNum - info.withdrawNum; //减去上次提现数额
    } else if (info.type == 3) {
        info.diamondNum = info.diamondNum - info.withdrawNum > 0 ? toFiexd2(info.diamondNum - info.withdrawNum) : info.diamondNum - info.withdrawNum; //减去上次提现数额
        info.goldNum = info.goldNum + parseFloat(info.withdrawNum);
    }
    if (info.type == 3) {
        $('.submit span').text('立即兑换');
    } else if (info.type == 1) {
        $('.submit span').text('提取');
    }
    info.withdrawNum = 0;
    
    console.log(info, info.type, info.diamondNum)
    $('#balance').text(info.diamondNum || 0);
    $('#gold-balance').text(info.goldNum || 0);
    // $(".choose").html("");
    // $('.tab span').removeClass('active');
    // $('.tab span:eq(0)').addClass('active');
    // $(".exchange-box, .exchange-tips").show();
    // $(".withdraw-box, .withdraw-tips, .notice").hide();
    $('.diamond-count>input').val("");
    $('.code>input').val("");
    $('#gold-num').text(0)
    getAccount();

    // 获取提现列表
    // $.ajax({
    //     url: allUrl() + '/wxPublic/findWithdrawal',
    //     data: {},
    //     success: function(res) {
    //         drawlist = res.data || [];
    //         let html = '';
    //         for (let i = 0; i < drawlist.length; i++) {
    //             html += `<div class="choose-item" data-value="${drawlist[i].cashNum}" data-id="${drawlist[i].cashProdId}">
    //                         <div class="value">
    //                             <span>${drawlist[i].cashNum}元</span>
    //                         </div>
    //                         <div class="obtain">
    //                             <span>${drawlist[i].diamondNum}</span>
    //                             <img src="./img/diamond.png" alt="diamond" />
    //                         </div>
    //                     </div>`;
    //         }
    //         $(".choose").append(html);
    //         $(".choose-item:eq(0)").addClass("active")
    //     }
    // })
}

// 标签栏切换
$('.tab span').on('touchend', function() {
    $('.tab span').removeClass('active');
    $(this).addClass('active');
    info.type = $(this).data().index;
    if (info.type == 3) {
        $('.container .submit>span').text('立即兑换');
        $(".exchange-box, .exchange-tips").show();
        $(".withdraw-box, .withdraw-tips").hide();
    } else if (info.type == 1) {
        $('.container .submit>span').text('提取');
        $(".exchange-box, .exchange-tips").hide();
        $(".withdraw-box, .withdraw-tips").show();
    }
    $('#code-btn').text('获取验证码');
    $('.code>input').val('');
    clearInterval(timer);
});

// 发送短信验证码
$('#code-btn').click(function() {
    if (info.phone.length < 11) {
        $toast.show({
            text: '请进入APP内绑定手机号!',
            time: 2000
        });
        return;
    }
    sendMessage();
});

// 发送短信方法
function sendMessage() {
    if ($('#code-btn').text() == '获取验证码') {
        $.ajax({
            type: 'get',
            url: allUrl() + '/wxPublic/getSmsByCode',
            data: {
                phone: info.phone
            },
            success: function success(res) {
                if (res.code === 200) {
                    countDown();
                    $toast.show({
                        text: '发送成功!',
                        time: 2000
                    });
                    $('.notice').text(`验证码已发送至您绑定的手机号${info.phone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2')}`).show();
                } else if (res.code === 4004) {
                    $toast.show({
                        text: '该手机号未绑定任何官方APP账号，请先在APP绑定',
                        time: 3000
                    })
                } else if (res.code === 4001) {
                    $toast.show({
                        text: '短信次数已达最大限制，请明天再试',
                        time: 3000
                    })
                } else {
                    $toast.show({
                        text: res.message.split(':')[1],
                        time: 2500
                    })
                }
            }
        });
    }
}

// 验证码倒数
function countDown() {
    $('#code-btn').text('60s');
    var i = 60;
    timer = setInterval(function () {
        i--;
        if (i < 0) {
            clearInterval(timer);
            $('#code-btn').text('获取验证码');
            $('.notice').hide();
            return;
        }
        $('#code-btn').text(i + 's');
    }, 1000)
}

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
    info.uid ? bindAlipay() : '';
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
                            info.uid ? bindAlipay() : '';
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

function getAccount() {
    $.ajax({
        url: allUrl() + '/wxPublic/getFinancialAccount',
        method: 'get',
        data: {
            uid: info.uid,
        },
        success: function success(res) {
            if (res.code === 200) {
                info.alipayAccount = res.data.alipayAccount;
                info.alipayAccountName = res.data.alipayAccountName;
                info.bankcard = res.data.bankCard;
                info.bankcardName = res.data.bankCardName;
                if (info.withdrawType == 2) {
                    $('#input-number').val(info.alipayAccount);
                    $('#input-name').val(info.alipayAccountName);
                } else if (info.withdrawType == 3) {
                    $('#input-number').val(info.bankcard);
                    $('#input-name').val(info.bankcardName);
                }
                bindAlipay();
            } else {
                $toast.show({
                    text: '获取信息失败！',
                    time: 2000
                });
            }
        }
    });
}

// 获取绑定支付宝信息
function bindAlipay() {
    $.ajax({
        url: allUrl() + '/wxPublic/checkBindAliPay',
        data: {
            uid: info.uid,
        },
        method: 'get',
        success: function success(res) {
            if (res.code === 200) {
                info.isBindAlipay = res.data.isBindAlipay;
                $('#balance').text(res.data.diamondNum || 0);
                $('#gold-balance').text(res.data.goldNum || 0);
                if (info.isBindAlipay) {
                    info.alipayAccount = res.data.alipayAccount;
                    info.alipayAccountName = res.data.alipayAccountName;
                }
            } else {
                $toast.show({
                    text: '获取信息失败！',
                    time: 2000
                });
            }
        }
    });
}

$('.bingZfb').on('touchend', function() {
    location.href = './indexZfb.html?uid=' + info.uid;
});

// 选择绑定的提现账号
$('input[name="type"]').click(function (e) {
    const val = $(e.target).val();
    if (val == 2) {
        $('#input-number').attr('placeholder', '请输入支付宝账号(手机/邮箱)');
        $('#input-name').attr('placeholder', '请输入支付宝账号的真实姓名');
        $('.danger').text('* 请确保所填写支付宝账号正确无误');
        $('#input-number').val(info.alipayAccount);
        $('#input-name').val(info.alipayAccountName);
    } else if (val == 3) {
        $('#input-number').attr('placeholder', '请输入银行卡账号');
        $('#input-name').attr('placeholder', '请输入持卡人姓名');
        $('.danger').text('* 请确保所填写银行卡账号正确无误');
        $('#input-number').val(info.bankcard);
        $('#input-name').val(info.bankcardName);
    }
});

function setDom() {
    $('.user-id').text(info.yyId);
    $('.nick-name').text(info.nickName);
    $('#diamond-num').text(info.diamondNum);
    $('#diamond-num1').text(info.goldNum);
}

$('.mobileAccount').on('touchend', function() {
    location.replace("./index.html");
});

// 监听兑换金币输入
$('#withdraw-num').on('input', function() {
    var val = $(this).val();
    $('#gold-num').text(val);
});

// 监听提现钻石输入
$('#diamond-num').on('input', function() {
    var val = $(this).val();
    $('#money-num').text(val / 10);
});

// 提交兑换/提现
$('.container .submit').on('touchend', function() {
    console.log($('#withdraw-num').val().length)
    var len = $('#withdraw-num').val().length;
    var val = $('#withdraw-num').val();
    if (info.type == 3) {
        if (len <= 0) {
            $toast.show({
                text: '请输入兑换金额！',
                time: 2000
            });
        } else if ((len > 0 && (parseInt(val) % 10 != 0))) {
            $toast.show({
                text: '请输入10的整数倍！',
                time: 2000
            });
    
            $('#gold-num').text(0);
        } else if ((len > 0 && (parseInt(val) % 10 != 0)) || parseInt(val) > 200000) {
            $toast.show({
                text: '单次最多可兑换20万金币！',
                time: 2000
            });
    
            $('#gold-num').text(0);
        } else if (parseInt(val) > parseInt(info.diamondNum)) {
            $toast.show({
                text: '超出可兑换额度！',
                time: 2000
            });
    
            $('#gold-num').text(0);
        } else if ($('.code>input').val() == '') {
            $toast.show({
                text: '请输入短信验证码！',
                time: 2000
            });
        } else {
            goldExchage();
        }
    } else if (info.type == 1) {
        if ($('#diamond-num').val() == "") {
            $toast.show({
                text: '请输入提现钻石数量!',
                time: 2000
            });
        } else if (parseInt(val) > parseInt(info.diamondNum) || parseInt($('#diamond-num').val()) > parseInt(info.diamondNum)) {
            $toast.show({
                text: '超出可兑换额度！',
                time: 2000
            });
    
            $('#gold-num').text(0);
        } else if (parseInt($('#diamond-num').val()) % 1000 != 0) {
            $toast.show({
                text: '请输入1000的整数倍!',
                time: 2000
            });
            
            $('#diamond-num').val("");
            $('#money-num').text(0);
        } else if ($('.code>input').val() == '') {
            $toast.show({
                text: '请输入短信验证码！',
                time: 2000
            });
        } else if ($('#alipay-img').css('display') == 'none' && $('#unionpay-img').css('display') == 'none') {
            $toast.show({
                text: '请选择提现账号！',
                time: 2000
            });
        } else {
            withdrawDiamonds();
        }
    }
});

// 钻石兑换金币
function goldExchage() {
    $.ajax({
        type: 'post',
        url: allUrl() + '/wxPublic/exchangeGoldCoin',
        data: {
            type: 1, //类型 1、钻石 2、拉贝 暂时只支持钻石兑换
            uid: info.uid,
            token: info.token,
            phone: info.phone,
            code: $('.code>input').val(),
            exchangeNum: $('#withdraw-num').val()
        },
        success: function success(res) {
            if (res.code === 200) {
                info.diamondNum = res.data.diamondNum || 0;
                info.goldNum = res.data.goldNum || 0;
                initPage();
                $toast.show({
                    text: '金币兑换成功！',
                    time: 2500
                });
            } else if (res.code === 2501) {
                $toast.show({
                    text: '请先前往APP内进行实名认证',
                    time: 2000
                });
            } else if (res.code === 409) {
                $toast.show({
                    text: res.message.split(':')[1],
                    time: 2000
                });

                setTimeout(function() {
                    location.replace('./index.html');
                }, 500);
            } else if (res.code === 500) {
                $toast.show({
                    text: res.message.split(':')[1],
                    time: 500
                })
                // setTimeout(function() {
                //     location.replace('./index.html');
                // }, 500);
            } else {
                $toast.show({
                    text: res.message.split(':')[1],
                    time: 2500
                });
            }
        }
    })
};

// 绑定提现账号信息
$('#submit-account').on('touchend', function() {
    if ($('#input-number').val() == '' && $('#input-name').val() == '') {
        $toast.show({
            text: '请填写完整的提现信息',
            time: 2500
        });
        return;
    }

    const val = $('input[name="type"]:checked').val();
    if (val == 2) {
        info.alipayAccount = $('#input-number').val();
        info.alipayAccountName = $('#input-name').val();
        $('#alipay-img').css("display", "block");
        $('#unionpay-img').css("display", "none");
    } else if (val == 3) {
        info.bankcard = $('#input-number').val();
        info.bankcardName = $('#input-name').val();
        $('#alipay-img').css("display", "none");
        $('#unionpay-img').css("display", "block");
    }
    info.withdrawType = val;
    $('#input-number').val("");
    $('#input-name').val("");
    $('.financial-popup').hide();
});

$('.choose-platform').on("touchend", function() {
    $('.financial-popup').show();
});

// 关闭弹框
$(".close-pop").on("touchend", function() {
    $('.popup').hide();
});

$('.financial-close').on("touchend", function() {
    $('.financial-popup').hide();
});

let pvalue = '100';
let pid = '1';

// 点击提现选项
$(document).on("touchend", ".choose-item", function() {
    $(this).addClass("active").siblings(".choose-item").removeClass("active");
    pvalue = $(this).attr("data-value");
    pid = $(this).attr("data-id");
    if (pvalue && pvalue * 10 <= info.diamondNum) {
        $('.container .submit').addClass('active');
    } else if (pvalue * 10 >= info.diamondNum) {
        $('.container .submit').removeClass('active');
    }
});

function withdrawDiamonds() {
    let account = '';
    let accountName = '';
    if (info.withdrawType == 2) {
        account = info.alipayAccount;
        accountName = info.alipayAccountName;
    } else if (info.withdrawType == 3) {
        account = info.bankcard;
        accountName = info.bankcardName;
    }
    
    $.ajax({
        type: 'post',
        url: allUrl() + '/wxPublic/bindAndWithdraw',
        data: {
            pid: pid,
            diamondNum: $('#diamond-num').val() / 10,
            type: info.withdrawType,
            account: account,
            accountName: accountName,
            phone: info.phone,
            code: $('.code>input').val(),
            uid: info.uid,
            token: info.token
        },
        success: function success(res) {
            console.log(res)
            if (res.code === 200) {
                initPage();
                $toast.show({
                    text: '已提交提现申请，会在24小时内处理',
                    time: 2500
                });
            } else if (res.code === 2501) {
                $toast.show({
                    text: '请先前往APP内进行实名认证',
                    time: 2000
                });
            } else if (res.code === 409) {
                $toast.show({
                    text: res.message.split(':')[1],
                    time: 2000
                });

                setTimeout(function() {
                    location.replace('./index.html');
                }, 500);
            } else if (res.code === 500) {
                $toast.show({
                    text: res.message.split(':')[1],
                    time: 500
                });

                // setTimeout(function() {
                //     location.replace('./index.html');
                // }, 500);
            } else {
                $toast.show({
                    text: res.message.split(':')[1],
                    time: 2500
                });
            }
        }
    })
}

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
};

//点击 支付宝提现
/**
 * zfbAcc 淘宝账号
 * isBindAlipay  否绑定淘宝账号
 * info.zfbAcc 判断是否纪录淘宝账号，如果有，择不管是否绑定淘宝账号，如果没有，择判断是否有绑定淘宝账号
 */
function zfbtx() {
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

// 解析URL拿到参数对象
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

// 解析整个URL为对象
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