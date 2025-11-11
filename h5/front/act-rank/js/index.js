var browser = checkVersion();
var info = {};


// info.uid = 10222;
// info.ticket = "eyJhbGciOiJIUzI1NiJ9.eyJ0aWNrZXRfdHlwZSI6bnVsbCwidWlkIjoxMDIyMiwidGlja2V0X2lkIjoiMTFlNmExYTMtZDM3NS00OGYzLWJjM2ItMGQ5NmUxNDQwNGYwIiwiZXhwIjozNjAwLCJjbGllbnRfaWQiOiJlcmJhbi1jbGllbnQifQ.kqDYidn4u8xEfxSUmKbNGO3EV6v_axO4EQaot4i_tco"
// getAjax()

if (browser.ios && window.webkit) {
    window.webkit.messageHandlers.getUid.postMessage(null);
    window.webkit.messageHandlers.getTicket.postMessage(null);
} else if (browser.android) {
    info.uid = parseInt(window.androidJsObj.getUid());
    info.ticket = window.androidJsObj.getTicket();
    window.androidJsObj.showShareButton(true)
    getAjax();

}
var getMessage = function getMessage(key, value) {
    info[key] = value;
    if (info.uid && info.ticket) {
        getAjax();
    }
};
var list = []

function getAjax() {
    $.ajax({
        url: allUrl() + '/charm/activity/getCharmByList',
        data: {
            uid: info.uid,
            ticket: info.ticket
        },
        success: function(res) {
            console.log(res);
            if (res.code != 200) {

            } else {
                console.log(res.data);
                list = res.data;
                showData()
            }
        },
        error: function(err) {
            console.log('错误', err);
        }
    })
}

function showData() {
    if (list.length == 0) {

    }
    if (list.length >= 1) {
        $(".i2").css("background-image", "url(" + list[0].avatar + ")")
        $('.r1 .nick').html(list[0].nick)
        $('.r1 .ml').html(list[0].totalNum)
    }
    if (list.length >= 2) {
        $(".i4").css("background-image", "url(" + list[1].avatar + ")")
        $('.r2 .nick').html(list[1].nick)
        $('.r2 .ml').html(list[1].totalNum)
    }
    if (list.length >= 3) {
        $(".i6").css("background-image", "url(" + list[2].avatar + ")")
        $('.r3 .nick').html(list[2].nick)
        $('.r3 .ml').html(list[2].totalNum)
    }
    if (list.length >= 4) {
        var str = ''
        for (var i = 3; i < list.length; i++) {
            str += '<div class="item"><div class="a">' + (i + 1) + '</div><div class="b" style="background-image:url(' + list[i].avatar + ')"></div><div class="c"><div class="c1">' + list[i].nick + '</div><div class="c2">ID:' + list[i].erbanNo + '</div></div><div class="d"><div class="d1"></div><div class="d2">' + list[i].totalNum + '</div></div></div>';

        }
        $('.rr4').html(str)
    }
}
$(".arrow").on('click', function() {
    $('.r4').animate({ scrollTop: parseInt($('.item').height()) * 10 + $('.r4').scrollTop() }, 500)
})