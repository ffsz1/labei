var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function (obj) {
    return typeof obj;
} : function (obj) {
    return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj;
};
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
            try{
                window.androidJsObj.showShareButton(true)
            }
            catch(err){
                
            }
        }
    }
}
function formatDate(timestamp) {
    var date = new Date(timestamp);
    Y = date.getFullYear() + '年';
    M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '月';
    D = date.getDate() + '日';
    h = date.getHours() + ':';
    m = date.getMinutes() + ':';
    s = date.getSeconds();
    if(s <10){
        s = '0' + s;
    }
    var time =  [Y + M + D , h + m + s];
    return time;
}
setTimeout(function(){$.ajax({url:allUrl()+"/statpacket/invitedetail",data:{uid:info.uid},success:function(a){if(200===a.code){$("#todayRegisterCount").html(a.data.inviteDetail.todayRegisterCount);$("#totalRegisterCount").html(a.data.inviteDetail.totalRegisterCount);$("#packetNum").html("\uffe5"+a.data.inviteDetail.packetNum);var b="";a.data.inviteList.forEach(function(a){a.createTime=formatDate(new Date(a.createTime));b+='<li>    <span>        <span class="list-member">            <p>\u4f60\u9080\u8bf7\u7684*</p>            <p class="text-hidden">'+
a.nick+'</p>            <p>*\u6ce8\u518c\u6210\u529f</p>        </span>        <span class="list-time">            <p>'+a.createTime[0]+"</p>            <p>"+a.createTime[1]+"</p>        </span>    </span>    <span>+"+a.packetNum+"\u5143</span></li>"});$(".list").html(b)}}});$("#btn-share").click(function(){browser.app&&(browser.android?window.androidJsObj.openSharePage():browser.ios&&window.webkit.messageHandlers.openSharePage.postMessage(null))})},50);
