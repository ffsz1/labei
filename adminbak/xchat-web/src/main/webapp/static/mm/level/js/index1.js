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
setTimeout(function () {
    $(".the-border").eq(0).show();
    var tabsSwiper;
    tabsSwiper = new Swiper('.swiper-container', {
        speed: 500,
        onSlideChangeStart: function onSlideChangeStart() {
            const index = tabsSwiper.activeIndex;
            $('.tabbar>span').removeClass('active');
            $($('.tabbar>span')[index]).addClass('active');
            $('#tabbar-bottom').css('animation-name', 'active' + index);
            setTimeout(()=>{
                $('#tabbar-bottom').removeClass().addClass('active' + index);
            },500)
        }
    });
    $('.tabbar>span').click(function(){
        const index = $(this).index();
        $('.tabbar>span').removeClass('active');
        $(this).addClass('active');
        $('#tabbar-bottom').css('animation-name', 'active' + index);
        setTimeout(()=>{
            $('#tabbar-bottom').removeClass().addClass('active' + index);
        },500)
        tabsSwiper.swipeTo($(this).index());
    })
    $.ajax({
        url: allUrl() + '/level/exeperience/get',
        data: {
            uid: info.uid
        },
        success:res=>{
            if(res.code === 200){
                $('.banner1').html(`
                    <p>${res.data.levelName}</p>
                    <p>离升级还需消耗${res.data.leftGoldNum}金币</p>
                `)
                if(Number(res.data.levelName.slice(2)) > 9){
                    $('.lv-content2').show()
                    $('.lv-content2-off').hide()
                }else{
                    $('.lv-content2').hide()
                    $('.lv-content2-off').show()
                    $('.lv-content1').hide()
                    $('.lv-content1-off').show()
                }
                if(Number(res.data.levelName.slice(2)) > 0){
                    $('.lv-content1').show()
                    $('.lv-content1-off').hide()
                }
            }
        }
    })
    $.ajax({
        url: allUrl() + '/level/charm/get',
        data: {
            uid: info.uid
        },
        success:res=>{
            if(res.code === 200){
                $('.banner2').html(`
                    <p>${res.data.levelName}</p>
                    <p>离升级还需增加${res.data.leftGoldNum}魅力</p>
                `)
            }
        }
    })
}, 50);
