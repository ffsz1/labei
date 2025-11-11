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


$(".js_mbzx").on("click", function() {
        if (browser.ios) {
            window.webkit.messageHandlers.closeWin.postMessage(null);
        } else if (browser.android) {
            window.androidJsObj.closeWin();
        }
    })
    // 获取信息
    // getAjax();
    //||15700
    //||'eyJhbGciOiJIUzI1NiJ9.eyJ0aWNrZXRfdHlwZSI6bnVsbCwidWlkIjoxNjAzNiwidGlja2V0X2lkIjoiODlmNWQ2NTktMmNlNy00NTRiLTk0ZGQtODA3YjFjMjJmOWQxIiwiZXhwIjozNjAwLCJjbGllbnRfaWQiOiJlcmJhbi1jbGllbnQifQ.Z2U36JSHDgw8F3EO58-cGAlbg5i18L6gt8snYv-VDbU'
function getAjax() {
    $.ajax({
        url: allUrl() + '/mcoin/pk/rankingList',
        data: {
            uid: info.uid,
            ticket: info.ticket
        },
        success: function(res) {
            var data = res.data;
            if (res.code == 200) {
                console.log(res);
                $(".container").css("display", 'flex');
                $('#myScore').html(data.historyGain);
                $('#myRate').html(data.historyWinRate + '%');
                $('#myTimes').html(data.historyTimes);
                $('#myWinTimes').html(data.historyWinTimes);
                $('#pic').attr('src', data.avatar)
                var list = res.data.rankingList;

                var html = '';
                for (var i = 0; i < list.length; i++) {
                    var num = '';
                    if (!(i == 0 || i == 1 || i == 2)) {
                        num = Number(i + 1)
                    }
                    html += '<div class="rank_list_box"><div class="rank_list_left">' + num + '</div><div class="rank_list_right"><span class="rank_list_avatar" onclick="openPersonPage(' + list[i].uid + ')"><img src="' + list[i].avatar + '" class="rank_list_img"></span><span class="rank_list_nick"><span><p class="rank_list_name">' + list[i].nick + '</p></span><span><p class="rank_list_name">场次' + list[i].termCount + '场</p></span></span><div class="rank_list_link">胜率' + list[i].winRate + '%</div></div></div>';
                }
                $('#listRank').html(html);
            } else {
                console.log('进去else');
                $toast.show({
                    text: '活动尚未开启，敬请期待',
                    time: 10000
                })
            }
        },
        error: function(err) {
            console.log('111');
            console.log('错误', err);
        }
    })
}

$(".btn_share").on("click", function() {
    if (browser.android) {
        window.androidJsObj.openSharePage();
    } else if (browser.ios) {
        window.webkit.messageHandlers.openSharePage.postMessage(null);
    }
})

function openPersonPage(uid) {
    if (browser.android) {
        window.androidJsObj.openPersonPage(uid);
    } else if (browser.ios) {
        window.webkit.messageHandlers.openPersonPage.postMessage(uid);
    }
}