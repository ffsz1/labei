var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function(obj) {
    return typeof obj;
} : function(obj) {
    return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj;
};

function checkVersion() {
    var u = navigator.userAgent,
        app = navigator.appVersion;
    return {
        trident: u.indexOf('Trident') > -1, //IE内核
        presto: u.indexOf('Presto') > -1, //opera内核
        webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
        gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
        mobile: !!u.match(/AppleWebKit.*Mobile.*/), //是否为移动终端
        ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
        android: u.indexOf('Android') > -1 || u.indexOf('Adr') > -1, //android终端
        iPhone: u.indexOf('iPhone') > -1, //是否为iPhone或者QQHD浏览器
        iPad: u.indexOf('iPad') > -1, //是否iPad
        webApp: u.indexOf('Safari') == -1, //是否web应该程序，没有头部与底部
        weixin: u.indexOf('MicroMessenger') > -1, //是否微信
        qq: u.match(/\sQQ/i) == " qq", //是否QQ
        app: u.indexOf('yingtaoApp') > -1 //是否在app内
    };
}

function EnvCheck() {
    if (window.location.href) {
        var _url = window.location.href;
        var res = _url.match(/beta/);
        if (res) {
            return 'test';
        } else {
            return 'live';
        }
    }
}

function allUrl() {
    // return '域名地址';
    return '域名地址';
    // return window.location.origin.indexOf('com') > -1 ? window.location.origin : 'https://beta.pinjin12.com';
}

// wxCode
function getQueryString() {
    var _url = location.search;
    var theRequest = new Object();
    if (_url.indexOf('?') != -1) {
        var str = _url.substr(1);
        strs = str.split('&');
        for (var i in strs) {
            theRequest[strs[i].split('=')[0]] = decodeURI(strs[i].split('=')[1]);
        }
    }
    return theRequest;
}
