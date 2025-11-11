var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function(obj) {
    return typeof obj;
} : function(obj) {
    return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj;
};
var browser = checkVersion();
var info = {};
var setInterCode = null;
var flag = false;
var flag1 = false;

function getMessage(key, value) {
    info[key] = value;
    console.log(key, value, 'getMessage');
    if (info.uid && info.ticket && !flag) {
        flag = true;
        console.log(info);
        idStatu();
    }
    if (info.phone && !flag1) {
        flag1 = true;
        selePhone();
    }
}
setTimeout(function() {
    console.log(browser.ios, browser.android)
    if (browser.ios && window.webkit) {
        window.webkit.messageHandlers.getUserPhoneNumber.postMessage(null);
        window.webkit.messageHandlers.getUid.postMessage(null);
        window.webkit.messageHandlers.getTicket.postMessage(null);
        window.webkit.messageHandlers.setupNavigationBarRightItem.postMessage(JSON.stringify({
            id: 2,
            data: {
                imageUrl: allUrl() + "/front/real_name/images/rule.png"
            }
        }));

    } else if (browser.android) {
        info.uid = parseInt(window.androidJsObj.getUid());
        info.ticket = window.androidJsObj.getTicket();
        info.phone = window.androidJsObj.getUserPhoneNumber();
        window.androidJsObj.setupNavigationBarRightItem(JSON.stringify({
            id: 2,
            data: {
                imageUrl: allUrl() + "/front/real_name/images/rule.png"
            }
        }));
        if (info.uid && info.ticket) {
            console.log(info);
            idStatu();
        }
        if (info.phone) {
            selePhone();
        }
    }
}, 10)

// info.ticket = 'eyJhbGciOiJIUzI1NiJ9.eyJ0aWNrZXRfdHlwZSI6bnVsbCwidWlkIjoxMzk3MiwidGlja2V0X2lkIjoiNjk0MDFhYTYtZDIxOC00M2Q3LThlZmItZGExZjkzZDJjM2UzIiwiZXhwIjozNjAwLCJjbGllbnRfaWQiOiJlcmJhbi1jbGllbnQifQ.j9Ia9GraLtj-yhEGEFLOtpCgbuxbP-UcuLGAXgLwje8';
// info.uid = '13972';
// info.phone = '';
// selePhone();
// idStatu();
// $('body>div').hide();
// $('.container').show();
// diaInt(3)

info.dom = null;

function isChinese(temp) {
    var re = /[^\u4e00-\u9fa5]/;
    if (re.test(temp)) return false;
    return true;
}
$('.close').click(function() {
    $('.introduce').hide();
    $('.introduce .rule').hide();
    $('.introduce .intType').hide();
    $('.introduce .box .close').hide();
    if (info.auditStatus == 0 || info.auditStatus == 1) {
        if (browser.ios) {
            window.webkit.messageHandlers.closeWin.postMessage(null);
        } else if (browser.android) {
            window.androidJsObj.closeWin();
        }
    }
});

$('.intType div').click(function() {
    $('.introduce').hide();
    $('.introduce .rule').hide();
    $('.introduce .intType').hide();
    $('.introduce .box .close').hide();
    if (info.auditStatus == 0 || info.auditStatus == 1) {
        if (browser.ios) {
            window.webkit.messageHandlers.closeWin.postMessage(null);
        } else if (browser.android) {
            window.androidJsObj.closeWin();
        }
    }
});

window.onNavigationBarRightItemDidClicked = function onNavigationBarRightItemDidClicked() {
    $('.introduce .intType').hide();
    $('.introduce .rule').show();
    $('.introduce .rule').show();
    $('.introduce').show();
    $('.introduce .box .close').show();
};

function diaInt(e) {
    switch (e) {
        case 1:
            console.log(1);
            $('.introduce .intType').css('display', 'flex');
            $('.introduce .intType img').attr('src', 'images/shenghe.png');
            $('.introduce .intType span:eq(0)').text('实名信息已提交');
            $('.introduce .intType span:eq(1)').text('审核中（请耐心等待4个工作日）');
            break;
        case 2:
            $('.introduce .intType').css('display', 'flex');
            $('.introduce .intType img').attr('src', 'images/chenggong.png');
            $('.introduce .intType span:eq(0)').text('认证成功！');
            $('.introduce .intType span:eq(1)').text('');
            console.log(2);
            break;
        case 3:
            $('.introduce .intType').css('display', 'flex');
            $('.introduce .intType img').attr('src', 'images/no.png');
            $('.introduce .intType span:eq(0)').text('实名认证不通过');
            $('.introduce .intType span:eq(1)').text('请重新填写真实信息进行验证');
            console.log(3);
            break;
        default:
            return;
    }
    $('.introduce .intType').show();
    $('.introduce').show();
}

function isCardNo(card) {
    // 身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X  
    var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
    if (reg.test(card) === false) {
        console.log("身份证输入不合法");
        return false;
    } else {
        return true;
    }
}

function isPhone(num) {
    var valid_rule = /^(13[0-9]|14[5-9]|15[012356789]|166|17[0-8]|18[0-9]|19[8-9])[0-9]{8}$/; // 手机号码校验规则
    if (!valid_rule.test(num)) {
        return false;
    } else {
        return true;
    }
}

function selePhone() {
    if (info.phone) {
        $('.phone input').val(info.phone).attr('disabled', '');
    }
}
$('.failed button').click(function() {
    $('.container').show();
    $('.failed').hide();
})
$('.file_ .bot-top').click(function() {
    console.log(1111111, $(this).find('.tu'));
    requestImg();
    info.dom = $(this).find('.tu');
    // onImageChooserResult(0, 'images/renzheng_guohui.png');
})

function onImageChooserResult(e, value) {
    console.log(e, value, '图片返回');
    // if(this.dom ){
    //     this.dom.attr('src',e)
    // }
    if (browser.ios) {
        info.dom.attr('src', value)
    } else if (browser.android) {
        info.dom.attr('src', e)
    }

};

function requestImg() {
    if (browser.ios) {
        window.webkit.messageHandlers.requestImageChooser.postMessage(null);
    } else if (browser.android) {
        if (androidJsObj) {
            window.androidJsObj.requestImageChooser();
        }
    }
}

function note_() {
    var type = 0;
    var url = '/user/realname/v1/getSmsCode';
    var header = '';
    var data = {};
    data.phone = $('.phone input').val();
    // console.log('note_', data)
    if (browser.ios) {
        var obj = {};
        obj.requestMethod = type;
        obj.urlController = url;
        obj.headerMapString = header;
        obj.paramMapString = data;
        window.webkit.messageHandlers.httpRequest.postMessage(JSON.stringify(obj));
        // window.webkit.messageHandlers.httpRequest.postMessage(type,url,header,JSON.stringify(data));JSON.stringify(obj)
    } else if (browser.android) {
        if (androidJsObj) {
            window.androidJsObj.httpRequest(type, url, header, JSON.stringify(data));
        }
    }
}
var onHttpResponse = function(e, item) {
    console.log(e, '接口返回', item)
    var body = null;
    if (browser.ios) {
        body = item;
        if (body.urlController == "/user/realname/v1/getSmsCode") {
            var bodyString = body.bodyString;
            console.log(bodyString, 'bodyString');
            if (!body.isRequestError) {
                $('.toast').text('发送成功').show();
                setTimeout(function() {
                    $('.toast').hide();
                }, 1200);
            } else {
                // $('.toast').text(bodyString.message || '发送失败').show();
                // setTimeout(function () {
                //     $('.toast').hide();
                // }, 1200);
                clearInterval(setInterCode);
                $('.code .getCode').text('获取').attr('onclick', 'time_()');
            }
        }
        if (body.urlController == "/user/realname/v1/save") {
            var bodyString = body.bodyString;
            console.log(bodyString, 'bodyString');
            if (!body.isRequestError) {
                // var bodyString = JSON.parse(body.bodyString);
                $('.toast').text('提交成功').show();
                setTimeout(function() {
                    $('.toast').hide();
                }, 1200);
                idStatu();
            } else {
                // $('.toast').text(bodyString.message).show();
                // setTimeout(function () {
                //     $('.toast').hide();
                // }, 1200);
                $('.submit button').attr('onclick', 'submit()').removeClass('active').text('提交');
            }
        }
    } else {
        body = JSON.parse(JSON.stringify(e));
        if (body.urlController == "/user/realname/v1/getSmsCode") {
            var bodyString = JSON.parse(body.bodyString);
            console.log(bodyString, 'bodyString');
            if (!body.isRequestError) {
                if (bodyString.code == 200) {
                    $('.toast').text('发送成功').show();
                    setTimeout(function() {
                        $('.toast').hide();
                    }, 1200);
                } else {
                    console.log(bodyString, bodyString.message, 'bodyString.message');
                    $('.toast').text(bodyString.message).show();
                    setTimeout(function() {
                        $('.toast').hide();
                    }, 1200);
                    clearInterval(setInterCode);
                    $('.code .getCode').text('获取').attr('onclick', 'time_()');
                }
            } else {
                $('.toast').text('发送失败').show();
                setTimeout(function() {
                    $('.toast').hide();
                }, 1200);
                $('.code .getCode').text('获取').attr('onclick', 'time_()');
            }
        }
        if (body.urlController == "/user/realname/v1/save") {
            if (!body.isRequestError) {
                var bodyString = JSON.parse(body.bodyString);
                console.log(bodyString, 'bodyString');
                if (bodyString.code == 200) {
                    $('.toast').text('提交成功').show();
                    setTimeout(function() {
                        $('.toast').hide();
                    }, 1200);
                    idStatu();
                } else {
                    $('.toast').text(bodyString.message).show();
                    setTimeout(function() {
                        $('.toast').hide();
                    }, 1200);
                    $('.submit button').attr('onclick', 'submit()').removeClass('active').text('提交');
                }
            } else {
                $('.toast').text('提交失败').show();
                setTimeout(function() {
                    $('.toast').hide();
                }, 1200);
                $('.submit button').attr('onclick', 'submit()').removeClass('active').text('提交');

            }

        }
    }

    // console.log(body,132132132,body.bodyString);
};

function time_() {
    //校验手机号
    if (!$('.phone input').val()) {
        console.log("没有输入手机号码");
        $Dialog({
            content: '请输入手机号码',
            diaClass: 'diaClass',
            buttons: [{
                className: "confirm",
                text: "确定",
                callback: function() {

                }
            }]
        })
        return
    } else {
        if (!isPhone($('.phone input').val())) {
            console.log('手机号码不符合规则');
            $Dialog({
                content: '手机号码错误',
                diaClass: 'diaClass',
                buttons: [{
                    className: "confirm",
                    text: "确定",
                    callback: function() {

                    }
                }]
            })
            return
        }
    }
    $('.code .getCode').attr('onclick', '');
    var second = 60;
    $('.code .getCode').text(second);
    setInterCode = setInterval(function() {
        if (second == 1) {
            clearInterval(setInterCode);
            $('.code .getCode').text('获取').attr('onclick', 'time_()');;
            return;
        }
        second--
        $('.code .getCode').text(second);
    }, 1000);
    note_();
}

function tester() {
    //校验姓名
    if (!$('.name input').val()) {
        console.log("没有姓名");
        $Dialog({
            content: '请输入姓名',
            diaClass: 'diaClass',
            buttons: [{
                className: "confirm",
                text: "确定",
                callback: function() {

                }
            }]
        })
        return
    } else {
        if (!isChinese($('.name input').val())) {
            console.log('姓名不符合规则');
            $Dialog({
                content: '姓名只能使用中文',
                diaClass: 'diaClass',
                buttons: [{
                    className: "confirm",
                    text: "确定",
                    callback: function() {

                    }
                }]
            })
            return
        }
    }
    //校验身份证
    if (!$('.sfz input').val()) {
        console.log("没有输入身份证号码");
        $Dialog({
            content: '请输入身份证号码',
            diaClass: 'diaClass',
            buttons: [{
                className: "confirm",
                text: "确定",
                callback: function() {

                }
            }]
        })
        return
    } else {
        if (!isCardNo($('.sfz input').val())) {
            console.log('身份证号码不符合规则');
            $Dialog({
                content: '身份证号码错误',
                diaClass: 'diaClass',
                buttons: [{
                    className: "confirm",
                    text: "确定",
                    callback: function() {

                    }
                }]
            })
            return
        }
    }
    //校验手机号
    if (!$('.phone input').val()) {
        console.log("没有输入手机号码");
        $Dialog({
            content: '请输入手机号码',
            diaClass: 'diaClass',
            buttons: [{
                className: "confirm",
                text: "确定",
                callback: function() {

                }
            }]
        })
        return
    } else {
        if (!isPhone($('.phone input').val())) {
            console.log('手机号码不符合规则');
            $Dialog({
                content: '手机号码错误',
                diaClass: 'diaClass',
                buttons: [{
                    className: "confirm",
                    text: "确定",
                    callback: function() {

                    }
                }]
            })
            return
        }
    };
    //验证码
    if (!$('.code input').val()) {
        console.log("没有输入验证码");
        $Dialog({
            content: '请输入验证码',
            diaClass: 'diaClass',
            buttons: [{
                className: "confirm",
                text: "确定",
                callback: function() {

                }
            }]
        })
        return
    }
    //身份证
    function imgRap(e) {
        console.log(e, '图片')
        if (e.indexOf('http') >= 0) {
            return false;
        } else {
            return true;
        }
    }
    console.log(imgRap($('.file_ .bottom:eq(0) .sfzImg').attr('src')), 1)
    console.log(imgRap($('.file_ .bottom:eq(1) .sfzImg').attr('src')), 2)
    console.log(imgRap($('.file_ .bottom:eq(2) .sfzImg').attr('src')), 3)
    if (imgRap($('.file_ .bottom:eq(0) .tu').attr('src')) || imgRap($('.file_ .bottom:eq(1) .tu').attr('src')) || imgRap($('.file_ .bottom:eq(2) .tu').attr('src'))) {
        console.log("没有上传身份证");
        $Dialog({
            content: '请上传身份证',
            diaClass: 'diaClass',
            buttons: [{
                className: "confirm",
                text: "确定",
                callback: function() {

                }
            }]
        })
        return
    }
    return true
}

function submit() {
    var flag = tester();
    console.log(flag);
    if (flag) {
        $('.submit button').attr('onclick', '').addClass('active').text('提交中...');
        var type = 1;
        var url = '/user/realname/v1/save';
        var header = '';
        var data = {};
        data.realName = $('.name input').val(); //名字
        data.idcardNo = $('.sfz input').val(); //身份证号码
        data.phone = $('.phone input').val(); //手机号
        data.smsCode = $('.code input').val(); //手机验证码
        data.idcardFront = $('.file_ .bottom:eq(0) .tu').attr('src'); //正面照
        data.idcardOpposite = $('.file_ .bottom:eq(1) .tu').attr('src'); //反面照
        data.idcardHandheld = $('.file_ .bottom:eq(2) .tu').attr('src'); //手持照
        console.log('submit', data)
        if (browser.ios) {
            var obj = {};
            obj.requestMethod = type;
            obj.urlController = url;
            obj.headerMapString = header;
            obj.paramMapString = data;
            // alert(1111111111)
            window.webkit.messageHandlers.httpRequest.postMessage(JSON.stringify(obj));
            // window.webkit.messageHandlers.httpRequest.postMessage(type,url,header,JSON.stringify(data));JSON.stringify(obj)
        } else if (browser.android) {
            if (androidJsObj) {
                window.androidJsObj.httpRequest(type, url, header, JSON.stringify(data));
            }
        }
    }
}

function idStatu() {
    $.ajax({
        // url: allUrl() + '/user/realname/v1/get?ticket=' + info.ticket + '&uid=' + info.uid,
        url: allUrl() + '/user/realname/v1/get',
        type: 'GET',
        data: {
            ticket: info.ticket,
            uid: info.uid
        },
        success: function success(res) {
            console.log(res);
            if (res.code == 200) {
                $('body>div').hide();
                $('.container').show();
                if (res.data.auditStatus == 0) {
                    info.auditStatus = res.data.auditStatus + '';
                    diaInt(1)
                }
                if (res.data.auditStatus == 1) {
                    diaInt(2)
                    info.auditStatus = res.data.auditStatus + '';
                }
                if (res.data.auditStatus == 2) {
                    diaInt(3)
                    info.auditStatus = res.data.auditStatus + '';
                }

            } else {
                $('body>div').hide();
                $('.container').show();
            }
        }
    });
}