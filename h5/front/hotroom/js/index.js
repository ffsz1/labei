var browser = checkVersion();
var info = {};

if (browser.ios) {
    var getMessage = function getMessage(key, value) {
        info[key] = value;
    };
    window.webkit.messageHandlers.getUid.postMessage(null);
    window.webkit.messageHandlers.getTicket.postMessage(null);
} else if (browser.android) {

    info.uid = parseInt(window.androidJsObj.getUid());
    info.ticket = window.androidJsObj.getTicket();

}

// info.uid = 10180;
// info.ticket = "eyJhbGciOiJIUzI1NiJ9.eyJ0aWNrZXRfdHlwZSI6bnVsbCwidWlkIjoxMDE4MCwidGlja2V0X2lkIjoiNTQ4YTE1ZjUtOWNjYS00OTkyLWE1NWEtZTQ2MzMzYmVhOWUxIiwiZXhwIjozNjAwLCJjbGllbnRfaWQiOiJlcmJhbi1jbGllbnQifQ.rUZyd2fMjQGeY23re75pBKBNfl8_qp2Q7mfWEq0neDo";


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

pay = function pay() {
    console.log(2)
    if (new RegExp("^[0-9]{0,8}$").test($('#theID').val()) === false) {
        $toast.show({
            text: '请输入正确格式',
            time: 1500
        })
        return;
    }
    if ($('#theID').val() === '') {
        $toast.show({
            text: '请输入房间ID',
            time: 1500
        })
        return;
    }
    if ($('#date').val() === '') {
        $toast.show({
            text: '请选择推荐日期',
            time: 1500
        })
        return;
    }
    if ($('#time').val() === '') {
        $toast.show({
            text: '请选择开始推荐时间',
            time: 1500
        })
        return;
    }
    $('.rommID').html($('#theID').val());
    var lastTime = $('#time').val().slice(0, 2) * 1 + 1 + ':00';
    $('.pay-time').html($('#date').val() + ' ' + $('#time').val() + '-' + lastTime);
    console.log('成功');
    $('.tips').show();
};
cancel = function cancel() {
    $('.tips').hide();
};
$('.list').click(function() {
    location.href = allUrl() + '/front/hotroom/list.html';
});
payment = function payment() {
    $.ajax({
        type: 'POST',
        url: allUrl() + '/purseHotRoom/purse',
        data: {
            uid: info.uid,
            ticket: info.ticket,
            erbanNo: $('#theID').val(),
            date: $('#date').val(),
            hour: $('#time').val()
        },
        success: function success(res) {
            if (res.code !== 200) {
                $('.tips').hide();
                $toast.show({
                    text: res.message,
                    time: 3000
                })
            } else {
                $('.tips').hide();
                $toast.show({
                    text: '恭喜你购买成功',
                    time: 3000
                })
            }
        },
        error: function(err) {
            console.log(err)
        }
    });
};
$('.popup-btn').click(function() {
    $('.tips').hide();
    $('.popup').hide();
});