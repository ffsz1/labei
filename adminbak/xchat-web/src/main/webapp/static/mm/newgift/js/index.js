var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function (obj) {
    return typeof obj;
} : function (obj) {
    return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj;
};
var browser = checkVersion();
var info = {};
var share = {
    title: '会上瘾的拉贝语音！', // 分享标题
    link: window.location.href, // 分享链接
    imgUrl: allUrl() + '/home/images/logo.png',
    desc: '拉贝语音交友速配处CP，你想要的我们都有。' // 分享描述
};

function shareInfo() {
    var _url = allUrl() + '/mm/download/download.html';
    var info = {
        title: '会上瘾的拉贝语音！', // 分享标题
        showUrl: _url,
        imgUrl: allUrl() + '/home/images/logo.png',
        desc: '拉贝语音交友速配处CP，你想要的我们都有。' // 分享描述
    };
    return JSON.stringify(info);
}
if (browser.app) {
    if (browser.ios) {
        window.webkit.messageHandlers.getUid.postMessage(null);
        window.webkit.messageHandlers.getTicket.postMessage(null);
        if (window.webkit.messageHandlers.getCommonParams) {
            window.webkit.messageHandlers.getCommonParams.postMessage(null)
        }
        var getMessage = function getMessage(key, value) {
            info[key] = value;
        };

    }else if (browser.android) {
        if (androidJsObj && (typeof androidJsObj === 'undefined' ? 'undefined' : _typeof(androidJsObj)) === 'object') {
            try{
                window.androidJsObj.showShareButton(true)
            }
            catch(err){
                
            }
        }
    }
}
setTimeout(function () {
    toCharge = function () {
        if (browser.app) {
            if (browser.android) {
                window.androidJsObj.openChargePage();
            } else if (browser.ios) {
                window.webkit.messageHandlers.openChargePage.postMessage(null)
            }
        }
    }
    show = function () {
        $('.rule-box').show()
    }
    closeRule = function () {
        $('.rule-box').hide()
    }
}, 50)
