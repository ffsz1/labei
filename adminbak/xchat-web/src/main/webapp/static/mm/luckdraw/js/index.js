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
setTimeout(function(){new Swiper(".swiper-container",{direction:"vertical",loop:!0,autoplay:!0,observer:!0,observeParents:!0});$.ajax({url:allUrl()+"/draw/list",success:function(a){if(200===a.code){var b="";a.data.forEach(function(a){b+='<div class="swiper-slide">    <p>\u606d\u559c</p>    <p>'+a.userVo.nick+"</p>    <p>\u83b7\u5f97</p>    <p>"+a.drawPrizeName+"</p></div>"});$("#list-item").html(b)}}});$.ajax({type:"get",url:allUrl()+"/draw/get",data:{uid:info.uid},success:function(a){200===a.code&&
    $("#leftDrawNum-text").html(a.data.leftDrawNum+"\u6b21")}});arrow=function(){$(".arrow").removeAttr("onclick");$.ajax({type:"get",url:allUrl()+"/draw/do",data:{uid:info.uid},success:function(a){if(200===a.code){var b=0;$("#leftDrawNum-text").html(a.data.leftDrawNum+"\u6b21");$(".tips-text").html("<p>\u606d\u559c\u60a8\u62bd\u4e2d</p><p>"+a.data.drawPrizeName+"</p>");switch(a.data.drawPrizeId){case 0:b=1368;break;case 8:b=1152;break;case 50:b=1188;break;case 100:b=1224;break;case 300:b=1296;break;
    case 1E3:b=1116;break;case 3E3:b=1404;break;case 8888:b=1440;break;case 1E4:b=1260}var c=setInterval(function(){switch($(".turnable1").css("display")){case "block":$(".turnable1").hide();$(".turnable2").show();break;case "none":$(".turnable1").show(),$(".turnable2").hide()}},100);$(".turnble-content").animate({"-webkit-transform":"rotate("+b+"deg)"},3500,"swing",function(){clearInterval(c);0===a.data.drawPrizeId?$("#lose").show():($(".prize-name").html(a.data.drawPrizeName),$("#prize").show());$(".arrow").attr("onclick",
    "arrow()");$(".turnble-content").css("transform","")})}else 9E3===a.code&&($(".no-chance").html(a.message),$(".no-chance").show(),setTimeout(function(){$(".no-chance").hide();$(".arrow").attr("onclick","arrow()")},1500))}})};toCharge=function(){browser.app&&(browser.android?window.androidJsObj.openChargePage():browser.ios&&window.webkit.messageHandlers.openChargePage.postMessage(null))};$(".clo").click(function(){$(".mask").hide();$(".rule-tips").hide()});$(".mask").click(function(){$(".mask").hide()});
    showRule=function(){$(".rule-tips").show()}},50);
