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
setTimeout(function () {
    $.ajax({
        type: 'GET',
        url: allUrl() + '/purseHotRoom/list',
        data: {
            uid: info.uid
        },
        success: function success(res) {
            if (res.code === 200) {
                var html = '<span><p>\u623f\u95f4\u0049\u0044</p><p>\u8D2D\u4E70\u65E5\u671F</p><p>\u8D2D\u4E70\u65F6\u95F4</p><p>\u91D1\u5E01</p></span>';
                res.data.forEach(function (item) {
                    html += '<span><p>' + item.roomNo + '</p><p>' + item.date + '</p><p>' + item.hour + '</p><p>' + item.goldNum + '</p></span>';
                });
                $('.content').html(html);
            }
        }
    });
}, 50);
