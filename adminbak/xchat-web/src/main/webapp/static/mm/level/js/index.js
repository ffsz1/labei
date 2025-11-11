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
setTimeout(function(){$(".the-border").eq(0).show();var b=new Swiper(".swiper-container",{speed:500,onSlideChangeStart:function(){var a=b.activeIndex;$(".tabbar>span").removeClass("active");$($(".tabbar>span")[a]).addClass("active");$("#tabbar-bottom").css("animation-name","active"+a);setTimeout(function(){$("#tabbar-bottom").removeClass().addClass("active"+a)},500)}});$(".tabbar>span").click(function(){var a=$(this).index();$(".tabbar>span").removeClass("active");$(this).addClass("active");$("#tabbar-bottom").css("animation-name",
"active"+a);setTimeout(function(){$("#tabbar-bottom").removeClass().addClass("active"+a)},500);b.swipeTo($(this).index())});$.ajax({url:allUrl()+"/level/exeperience/get",data:{uid:info.uid},success:function(a){200===a.code&&($(".banner1").html("\n                    <p>"+a.data.levelName+"</p>\n                    <p>\u79bb\u5347\u7ea7\u8fd8\u9700\u6d88\u8017"+a.data.leftGoldNum+"\u91d1\u5e01</p>\n                "),9<Number(a.data.levelName.slice(2))?($(".lv-content2").show(),$(".lv-content2-off").hide()):
($(".lv-content2").hide(),$(".lv-content2-off").show(),$(".lv-content1").hide(),$(".lv-content1-off").show()),0<Number(a.data.levelName.slice(2))&&(console.log(111),$(".lv-content1").show(),$(".lv-content1-off").hide()))}});$.ajax({url:allUrl()+"/level/charm/get",data:{uid:info.uid},success:function(a){200===a.code&&$(".banner2").html("\n                    <p>"+a.data.levelName+"</p>\n                    <p>\u79bb\u5347\u7ea7\u8fd8\u9700\u589e\u52a0"+a.data.leftGoldNum+"\u9b45\u529b</p>\n                ")}})},
50);
