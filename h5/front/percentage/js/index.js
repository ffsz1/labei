var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function(obj) {
    return typeof obj;
} : function(obj) {
    return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj;
};
var share = {
    title: '会上瘾的拉贝星球！', // 分享标题
    link: window.location.href, // 分享链接
    imgUrl: allUrl() + '/home/images/logo.png',
    desc: '拉贝星球交友速配处CP，你想要的我们都有。' // 分享描述
};

function shareInfo() {
    var _url = allUrl() + '/mm/download/download.html';
    var info = {
        title: '会上瘾的拉贝星球！', // 分享标题
        showUrl: _url,
        imgUrl: allUrl() + '/home/images/logo.png',
        desc: '拉贝星球交友速配处CP，你想要的我们都有。' // 分享描述
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
            try {
                window.androidJsObj.showShareButton(true)
            } catch (err) {

            }
        }
    }
}

function formatDate(now) {
    var year = now.getFullYear();
    var month = now.getMonth() + 1;
    var date = now.getDate();
    var hour = now.getHours();
    var minute = now.getMinutes();
    var second = now.getSeconds();
    return [year + "年" + month + "月" + date + "日", hour + ":" + minute + ":" + second];
}
setTimeout(function() {
    $.ajax({
        url: allUrl() + "/statbouns/detail",
        data: { uid: info.uid },
        success: function(b) {
            if (200 === b.code) {
                $("#todayBouns").html("\uffe5" + b.data.todayBouns);
                $("#totalBouns").html("\uffe5" + b.data.totalBouns);
                var c = "";
                b.data.bounsList.forEach(function(a) {
                    a.createTime = formatDate(new Date(a.createTime));
                    c += '<li>    <span>        <span class="list-member">            <p>\u4f60\u9080\u8bf7\u7684*</p>            <p class="text-hidden">' + a.nick + '</p>            <p>*\u6ce8\u518c\u6210\u529f</p>        </span>        <span class="list-time">            <p>' +
                        a.createTime[0] + "</p>            <p>" + a.createTime[1] + "</p>        </span>    </span>    <span>+" + a.packetNum + "\u5143</span></li>"
                });
                $(".list").html(c)
            }
        }
    });
    $("#btn-share").click(function() { browser.app && (browser.android ? window.androidJsObj.openSharePage() : browser.ios && window.webkit.messageHandlers.openSharePage.postMessage(null)) })
}, 50);