var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function (obj) { return typeof obj; } : function (obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; };
var share = {
    title: '拉贝，语音交友神器！', // 分享标题
    link: window.location.href, // 分享链接
    imgUrl: allUrl() + '/home/images/logo.png',
    desc: '拉贝语音交友速配处CP，你想要的我们都有。' // 分享描述
};

function shareInfo() {
    var _url = allUrl() + '/mm/download/download.html';
    var info = {
        title: '拉贝，语音交友神器！', // 分享标题
        showUrl: _url,
        imgUrl: allUrl() + '/home/images/logo.png',
        desc: '拉贝语音交友速配处CP，你想要的我们都有。' // 分享描述
    };
    return JSON.stringify(info);
}
function isRealNum(val){
    if(val === "" || val ==null){
        return false;
    }
    if(!isNaN(val)){
        return true;
    }else{
        return false;
    }
}
var urlData = new Array;
location.search.substring(1).split('&').forEach(function(item) {
    urlData.push(item.split('=')[0])
    urlData.push(item.split('=')[1])
});
linkedme.init("51b55040e052e44c1cde9fd9519693be", {type: "live"}, null);
var data = {};
data.type = "live";  //表示现在使用线上模式,如果填写"test", 表示测试模式.【可选】
// data.feature = "功能名称"; // 自定义深度链接功能，多个用逗号分隔，【可选】
// data.stage = "阶段名称"; // 自定义深度链接阶段，多个用逗号分隔，【可选】
// data.channel = "渠道名称"; // 自定义深度链接渠道，多个用逗号分隔，【可选】
// data.tags = "标签名称"; // 自定义深度链接标签，多个用逗号分隔，【可选】
// data.ios_custom_url = allUrl() + "/mm/download/iOS-guide.html"; // 自定义iOS平台下App的下载地址，如果是AppStore的下载地址可以不用填写，需填写http或https【可选】
// data.ios_direct_open = true; //未安装情况下，设置为true为直接打开ios_custom_url，默认为false【可选】
// data.android_custom_url = "";// 自定义安卓平台下App的下载地址，需填写http或https【可选】
// data.android_direct_open = ""; //设置为true，所有情况下跳转android_custom_url，默认为false【可选】
// 下面是自定义深度链接参数，用户点击深度链接打开app之后，params的参数会通过LinkedME服务器透传给app，由app根据参数进行相关跳转
// 例如：详情页面的参数，写入到params中，这样在唤起app并获取参数后app根据参数跳转到详情页面
if(!isRealNum(Number(urlData[3]))){
    urlData[3] = '';
}
data.params = '{"roomuid":"' + urlData[3] + '","uid":"' + urlData[1] + '","type":"' + 2 + '"}'   
linkedme.link(data, function(err, response){
if(err){
} else {
    $('#linked').html('<a id="into" class="linkedme" href="' + response.url + '">\u7ACB\u5373\u4E0B\u8F7D</a>');
}
},false);
