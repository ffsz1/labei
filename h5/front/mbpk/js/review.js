var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function(obj) {
    return typeof obj;
} : function(obj) {
    return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj;
};
var share = {
    title: '萌币pk', // 分享标题
    link: allUrl() + '/front/download/download.html', // 分享链接
    imgUrl: allUrl() + '/front/mbpk/images/ym.png',
    desc: '萌币PK，多数派赢大量萌币!' // 分享描述
};

function shareInfo() {
    var _url = allUrl() + '/front/download/download.html';
    var info = {
        title: '萌币pk', // 分享标题
        showUrl: _url,
        imgUrl: allUrl() + '/front/mbpk/images/ym.png',
        desc: '萌币PK，多数派赢大量萌币!' // 分享描述
    };
    return JSON.stringify(info);
}
var browser = checkVersion();
var info = {};

// var androidJsObj = null;
// info.uid = 10247;
// info.ticket = "eyJhbGciOiJIUzI1NiJ9.eyJ0aWNrZXRfdHlwZSI6bnVsbCwidWlkIjoxMDI0NywidGlja2V0X2lkIjoiNjY1YTY5YjMtODYxMC00NmFhLWI0YWYtODM0ZTdhZDY4NmMwIiwiZXhwIjozNjAwLCJjbGllbnRfaWQiOiJlcmJhbi1jbGllbnQifQ.Xk9QXUpAeLpjkzd9u2fjLiQlPNLIMIpTFKtiWRZIDj0"
// getAjax()

if (browser.ios && window.webkit) {
    window.webkit.messageHandlers.getUid.postMessage(null);
    window.webkit.messageHandlers.getTicket.postMessage(null);
} else if (browser.android) {
    if (
        androidJsObj &&
        (typeof androidJsObj === "undefined" ?
            "undefined" :
            _typeof(androidJsObj)) === "object"
    ) {
        info.uid = parseInt(window.androidJsObj.getUid());
        info.ticket = window.androidJsObj.getTicket();
        window.androidJsObj.showShareButton(true)
        getAjax();
    }
}
var getMessage = function getMessage(key, value) {
    info[key] = value;
    if (info.uid && info.ticket) {
        getAjax();
    }
};

// $(".container").css("display", 'flex');


// 获取信息
getAjax();

function getAjax() {
    $.ajax({
        url: allUrl() + '/mcoin/pk/pastPeriod',
        data: {
            uid: info.uid,
            ticket: info.ticket
        },
        success: function(res) {
            if (res.code == 200) {
                $(".container").css("display", 'flex');
                var data = res.data.periodList;
                var html = '';
                for (var i = 0; i < data.length; i++)
                    html += ' <div class="review-box flex-row"><div class="review-left">第' + data[i].term + '期</div><div class="review-right flex1"><p class="review-title">' + data[i].title + '</p><p class="review-des">大部分人选了' + data[i].answer + '</p></div></div>';
                $('#reviewBox').html(html);
            } else {
                // $toast.show({
                //     text: '活动尚未开启，敬请期待',
                //     time: 10000
                // })
            }
        },
        error: function(err) {
            console.log('111');
            console.log('错误', err);
        }
    })
}