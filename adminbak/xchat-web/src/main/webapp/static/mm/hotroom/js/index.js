var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function (obj) { return typeof obj; } : function (obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; };
var share = {
    title: '语音交友，首选拉贝！', // 分享标题
    link: window.location.href, // 分享链接
    imgUrl: allUrl() + '/home/images/logo.png',
    desc: '拉贝语音里面个个都是人才，说话又好听，我超喜欢这里的。' // 分享描述
};

function shareInfo() {
    var _url = allUrl() + '/ttyy/download/download.html';
    var info = {
        title: '语音交友，首选拉贝！', // 分享标题
        showUrl: _url,
        imgUrl: allUrl() + '/home/images/logo.png',
        desc: '拉贝语音里面个个都是人才，说话又好听，我超喜欢这里的。' // 分享描述
    };
    return JSON.stringify(info);
}
var browser = checkVersion();
var info = {};
if (browser.app) {
    if (browser.ios) {
        var getMessage = function getMessage(key, value) {
            info[key] = value;
        };
        window.webkit.messageHandlers.getUid.postMessage(null);
    } else if (browser.android) {
        if (androidJsObj && (typeof androidJsObj === 'undefined' ? 'undefined' : _typeof(androidJsObj)) === 'object') {
            info.uid = parseInt(window.androidJsObj.getUid());
        }
    }
}
$($('.rule-text')[0]).show();
$($('.rule-pages')[0]).addClass('on-page');
setTimeout(function () {
    var date = new Date();
    date.setDate(date.getDate() + 2);
    var calendar = new LCalendar();
    calendar.init({
        'trigger': '#date',
        'type': 'date',
        'minDate': new Date().getFullYear() + '-' + (new Date().getMonth() + 1) + '-' + new Date().getDate(),
        'maxDate': date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate()
    });
    var calendartime = new LCalendar();
    calendartime.init({
        'trigger': '#time',
        'type': 'time'
    });
    changePage = function changePage(page) {
        $('.rule-pages').forEach(function (item) {
            $(item).removeClass('on-page');
        });
        $('.rule-text').forEach(function (item) {
            $(item).hide();
        });
        $($('.rule-text')[page]).show();
        $($('.rule-pages')[page]).addClass('on-page');
    };
    pay = function pay() {
        if (new RegExp("^[0-9]{0,8}$").test($('#theID').val()) === false) {
            $('.prompt').show();
            $('.prompt-text').html('请输入正确格式');
            setTimeout(function () {
                $('.prompt').hide();
            }, 1500);
            return;
        }
        if ($('#theID').val() === '') {
            $('.prompt').show();
            $('.prompt-text').html('请输入房间ID');
            setTimeout(function () {
                $('.prompt').hide();
            }, 1500);
            return;
        }
        if ($('#date').val() === '') {
            $('.prompt').show();
            $('.prompt-text').html('请选择推荐日期');
            setTimeout(function () {
                $('.prompt').hide();
            }, 1500);
            return;
        }
        if ($('#time').val() === '') {
            $('.prompt').show();
            $('.prompt-text').html('请选择开始时间');
            setTimeout(function () {
                $('.prompt').hide();
            }, 1500);
            return;
        }
        $('.rommID').html($('#theID').val());
        var lastTime = $('#time').val().slice(0, 2) * 1 + 1 + ':00';
        $('.pay-time').html($('#date').val() + ' ' + $('#time').val() + '-' + lastTime);
        $('.tips').show();
    };
    cancel = function cancel() {
        $('.tips').hide();
    };
    $('.list').click(function () {
        location.href = allUrl() + '/mm/hotroom/list.html';
    });
    payment = function payment() {
        $.ajax({
            type: 'GET',
            url: allUrl() + '/purseHotRoom/purse',
            data: {
                uid: info.uid,
                erbanNo: $('#theID').val(),
                date: $('#date').val(),
                hour: $('#time').val()
            },
            success: function success(res) {
                if (res.code !== 200) {
                    $('.tips').hide();
                    $('.prompt').show();
                    $('.prompt-text').html(res.message);
                    setTimeout(function () {
                        $('.prompt').hide();
                    }, 1700);
                    return;
                }
                // if (res.code === 405) {
                //     $('.popup-date').html($('#date').val());
                //     var html = '';
                //     res.data.forEach(function (item) {
                //         html += '<p>' + item.hour + '</p>';
                //     });
                //     $('.popup-time').html(html);
                //     $('.popup').show();
                // }
                if (res.code === 200) {
                    $('.tips').hide();
                    $('.prompt').show();
                    $('.prompt-text').html('恭喜你购买成功');
                    setTimeout(function () {
                        $('.prompt').hide();
                    }, 1700);
                    return;
                }
            }
        });
    };
    $('.popup-btn').click(function () {
        $('.tips').hide();
        $('.popup').hide();
    });
}, 50);
