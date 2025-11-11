

var info = {};


initPage()
function initPage() {
    var browser = checkVersion();
    if (browser.ios) {
        var ua = navigator.userAgent.toLowerCase();
        // $('#guided').show();

        if (ua.match(/MicroMessenger/i) == "micromessenger") {
            $('#guided').show();
            return;
        }
        if (ua.match(/QQ/i) == "qq") {
            $('#guided').show()
            return;
        }
       
    }
    getList()
}

function getList() {
    $.ajax({
        url: allUrl() + '/chargeprod/list',
        data: {
            channelType: 1
        },
        success: function success(res) {
            $('.goldList .goldItem').remove();
            console.log(res)
            if (res.code === 200) {

                goldList = res.data;
                chargeProdId = res.data[1].chargeProdId;

                for (var i = 0; i < res.data.length; i++) {

                    var $item = $('<div class="goldItem "><p class="gold-num"><span class="img"></span><span> ' + res.data[i].prodName + '</span></p><p class="money">' + res.data[i].money + '元</p></div>')
                    $item.data('chargeProdId', res.data[i].chargeProdId);
                    $('.goldList').append($item);

                }

                $('.goldItem').on('click', function () {
                    console.log('chargeProdId:' + $(this).data('chargeProdId'))
                    info.chargeProdId = $(this).data('chargeProdId');
                    $(this).addClass('active').siblings().removeClass('active');
                    info.money = $(this).find('.money').html();
                    check();
                })

            }
        }
    });
}

function submit() {
    // /wx/getsubmitPay

    $.ajax({
        url: allUrl() + '/charge/ecpss/alipay/webApply',
        data: {
            chargeProdId: info.chargeProdId,
            userNo: inputVal,
            payChannel: 'alipay_wap',
            successUrl: allUrl() + 'front/charge_huiju/success.html',
        },
        method: 'post',
        success: function success(res) {
            console.log(res)

            if (res.code == 200) {
                location.href = res.data.payUrl
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

function check() {

    if ($('#userNo')[0].value.trim() == '') {
        // showNoUser()
        $toast.show({ text: '请输入需要充值的ID！', time: 2000 })
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
            console.log(res)
            if (res.code === 200) {
                info.name = res.data.erbanNo;
                info.id = res.data.nick;

                showConfirm(info.id, info.name, info.money)
            } else {
                showNoUser();
            }


        }
    })
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
                submit()//发起支付

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
                $('.goldItem').removeClass('active')

            }
        }]
    })
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