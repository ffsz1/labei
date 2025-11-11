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
} else {
    info.uid = 100491
}
setTimeout(function() {
    $(".the-border").eq(0).show();
    var tabsSwiper;
    tabsSwiper = new Swiper('.swiper-container', {
        speed: 500,
        onSlideChangeStart: function onSlideChangeStart() {
            const index = tabsSwiper.activeIndex;
            $('.tabbar>span').removeClass('active');
            $($('.tabbar>span')[index]).addClass('active');
            $('#tabbar-bottom').css('animation-name', 'active' + index);
            setTimeout(function() {
                $('#tabbar-bottom').removeClass().addClass('active' + index);
            }, 500)
        }
    });
    $('.tabbar>span').click(function() {
        const index = $(this).index();
        $('.tabbar>span').removeClass('active');
        $(this).addClass('active');
        $('#tabbar-bottom').css('animation-name', 'active' + index);
        setTimeout(function() {
            $('#tabbar-bottom').removeClass().addClass('active' + index);
        }, 500)
        tabsSwiper.swipeTo($(this).index());
    })
    $.ajax({
        url: allUrl() + '/level/exeperience/get',
        data: {
            uid: info.uid
        },
        success: res => {
            if (res.code === 200) {
                console.log(res);

                $('.banner1').html(`
                    <p>${res.data.levelName}</p>
                    <p>离升级还需消耗${res.data.leftGoldNum}金币</p>
                `)
                if (Number(res.data.levelName.slice(2)) > 0) {
                    $('.lv-content1').show()
                    $('.lv-content1-off').hide()
                }
                if (Number(res.data.levelName.slice(2)) > 9) {
                    $('.lv-content2').show()
                    $('.lv-content2-off').hide()
                }
                if (Number(res.data.levelName.slice(2)) > 19) {
                    $('.lv-content3').show()
                    $('.lv-content3-off').hide()
                }
                if (Number(res.data.levelName.slice(2)) > 34) {
                    $('.lv-content4').show()
                    $('.lv-content4-off').hide()
                }
                if (Number(res.data.levelName.slice(2)) > 39) {
                    $('.lv-content5').show()
                    $('.lv-content5-off').hide()
                }
                if (Number(res.data.levelName.slice(2)) > 49) {
                    $('.lv-content6').show()
                    $('.lv-content6-off').hide()
                }
            }
        }
    })
    $.ajax({
        url: allUrl() + '/level/charm/get',
        data: {
            uid: info.uid
        },
        success: res => {
            if (res.code === 200) {
                $('.banner2').html(`
                    <p>${res.data.levelName}</p>
                    <p>离升级还需增加${res.data.leftGoldNum}魅力</p>
                `)
            }
        }
    })
}, 50);