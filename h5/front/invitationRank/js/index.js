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
// if (browser.app) {
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
// }
setTimeout(function() {
    $.ajax({
        url: allUrl() + "/statpacket/rank",
        data: { uid: info.uid },
        success: function(a) {
            if (200 === a.code) {
                $(".list-header-avatar").attr("src", a.data.me.avatar);
                1E3 <= a.data.me.seqNo && (a.data.me.seqNo = "1000+");
                $(".list-header").html('<span>    <p class="list-header-title">\u6392\u540d</p>    <p>' + a.data.me.seqNo + '</p></span><span>    <p></p>    <p class="text-hidden list-header-name">' +
                    a.data.me.nick + '</p></span><span>    <p class="list-header-title">\u5956\u52b1\u91d1</p>    <p class="FF3B3B">' + a.data.me.packetNum + '\u5143</p></span><i class="list-header-bottom list-header-bottom1"></i><i class="list-header-bottom list-header-bottom2"></i>\n                ');
                var b = "";
                a.data.rankList.forEach(function(a, c) {
                    b = 3 > c ? b + ('    <li>        <span class="list-rank-member">            <img src="images/no' +
                        (c + 1) + '.png" class="rank-img">            <img src="' + a.avatar + '" class="rank-member-avatar">            <p class="text-hidden">' + a.nick + "</p>        </span>        <span>            <p>" + a.packetNum + "\u5143</p>        </span>    </li>    ") : b + ('    <li>        <span class="list-rank-member">            <text class="rank-member-no">' +
                        (c + 1) + '</text>            <img src="' + a.avatar + '" class="rank-member-avatar">            <p class="text-hidden">' + a.nick + "</p>        </span>        <span>            <p>" + a.packetNum + "\u5143</p>        </span>    </li>   ")
                });
                $(".list-content").html(b)
            }
        }
    });
    $("#btn-share").click(function() {
        browser.app && (browser.android ? window.androidJsObj.openSharePage() :
            browser.ios && window.webkit.messageHandlers.openSharePage.postMessage(null))
    })
}, 50);