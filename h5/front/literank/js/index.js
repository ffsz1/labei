var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function (obj) { return typeof obj; } : function (obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; };
var share = {
    title: '语音交友，首选拉贝！', // 分享标题
    link: window.location.href, // 分享链接
    imgUrl: allUrl() + '/home/images/logo.png',
    desc: '拉贝星球里面个个都是人才，说话又好听，我超喜欢这里的。' // 分享描述
};

function shareInfo() {
    var _url = allUrl() + '/ttyy/download/download.html';
    var info = {
        title: '语音交友，首选拉贝！', // 分享标题
        showUrl: _url,
        imgUrl: allUrl() + '/home/images/logo.png',
        desc: '拉贝星球里面个个都是人才，说话又好听，我超喜欢这里的。' // 分享描述
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
    function getRank1(type, datatype, uid) {
        $.ajax({
            type: 'GET',
            url: allUrl() + '/activity/html/rank',
            // url:'http://localhost:1995/allrank',
            data: {
                type: type
            },
            success: function success(res) {
                // res = JSON.parse(res)
                if (res.code === 200) {
                    var rankHtml = '';
                    if (res.data[0]) {
                        rankHtml = "<div></div><div data-id=\"" + res.data[0].uid + "\" class=\"toMine\" onclick=\"tomine(" + res.data[0].uid + ")\">    <img src=\"" + res.data[0].avatar + "\" class=\"head-one\">    <span class=\"rank-name\">" + res.data[0].nick + "</span>     <span class=\"rank-star2\">" + res.data[0].total + "</span>    <span class=\"rank-num\">\u55b5\u55b5\u53f7\uff1a" + res.data[0].erbanNo + "</span>    <img src=\"images/gold.png\" class=\"crown-one\">    <img src=\"images/no.1.png\" class=\"no1\"></div>\n                       <div></div>";
                    }
                    if (res.data[1]) {
                        rankHtml = "<div data-id=\"" + res.data[1].uid + "\" class=\"toMine\" onclick=\"tomine(" + res.data[1].uid + ")\">    <img src=\"" + res.data[1].avatar + "\" class=\"head-two\">    <span class=\"rank-name\">" + res.data[1].nick + "</span>     <span class=\"rank-star2\">" + res.data[1].total + "</span>    <span class=\"rank-num\">\u55b5\u55b5\u53f7\uff1a" + res.data[1].erbanNo + "</span>    <img src=\"images/silver.png\" class=\"crown\">    <img src=\"images/no.2.png\" class=\"no2\"></div><div data-id=\"" + res.data[0].uid + "\" class=\"toMine\" onclick=\"tomine(" + res.data[0].uid + ")\">    <img src=\"" + res.data[0].avatar + "\" class=\"head-one\">    <span class=\"rank-name\">" + res.data[0].nick + "</span>     <span class=\"rank-star2\">" + res.data[0].total + "</span>    <span class=\"rank-num\">\u55b5\u55b5\u53f7\uff1a" + res.data[0].erbanNo + "</span>    <img src=\"images/gold.png\" class=\"crown-one\">    <img src=\"images/no.1.png\" class=\"no1\"></div><div></div>";
                    }
                    if (res.data[2]) {
                        rankHtml = "<div data-id=\"" + res.data[1].uid + "\" class=\"toMine\" onclick=\"tomine(" + res.data[1].uid + ")\">    <img src=\"" + res.data[1].avatar + "\" class=\"head-two\">    <span class=\"rank-name\">" + res.data[1].nick + "</span>     <span class=\"rank-star2\">" + res.data[1].total + "</span>    <span class=\"rank-num\">\u55b5\u55b5\u53f7\uff1a" + res.data[1].erbanNo + "</span>    <img src=\"images/silver.png\" class=\"crown\">    <img src=\"images/no.2.png\" class=\"no2\"></div><div data-id=\"" + res.data[0].uid + "\" class=\"toMine\" onclick=\"tomine(" + res.data[0].uid + ")\">    <img src=\"" + res.data[0].avatar + "\" class=\"head-one\">    <span class=\"rank-name\">" + res.data[0].nick + "</span>     <span class=\"rank-star2\">" + res.data[0].total + "</span>    <span class=\"rank-num\">\u55b5\u55b5\u53f7\uff1a" + res.data[0].erbanNo + "</span>    <img src=\"images/gold.png\" class=\"crown-one\">    <img src=\"images/no.1.png\" class=\"no1\"></div><div data-id=\"" + res.data[2].uid + "\" class=\"toMine\" onclick=\"tomine(" + res.data[2].uid + ")\">    <img src=\"" + res.data[2].avatar + "\" class=\"head-three\">    <span class=\"rank-name\">" + res.data[2].nick + "</span>     <span class=\"rank-star2\">" + res.data[2].total + "</span>    <span class=\"rank-num\">\u55b5\u55b5\u53f7\uff1a" + res.data[2].erbanNo + "</span>    <img src=\"images/copper.png\" class=\"crown\">    <img src=\"images/no.3.png\" class=\"no2\"></div>";
                    }
                    $('#rank-title1').html(rankHtml);
                    var html = '';
                    res.data.forEach(function (item, index, arr) {
                        if (index > 2) {
                            html += "     <span class=\"toMine\" onclick=\"tomine(" + item.uid + ")\" data-id=\"" + item.uid + "\"><span>" + (index*1+1) + "</span><span>    <span>        <img src=\"" + item.avatar + "\">    </span>    <span class=\"rank-list-content\">        <span>          <p>" + item.nick + "</p>        </span>                <span>\u55b5\u55b5\u53f7\uff1a" + item.erbanNo + "</span>        </span>       <span class=\"rank-list-star2\">    <p>" + (arr[index - 1].total - arr[index].total) + "</p>                                    <p>\u8DDD\u79BB\u524D\u4E00\u540D</p>    </span></span>                        </span>                           ";
                        }
                    });
                    $('#rank-list1').html(html);
                }
            }
        });
    }

    function getRank2(type, datatype, uid) {
        $.ajax({
            type: 'GET',
            url: allUrl() + '/activity/html/rank',
            // url:'http://localhost:1995/allrank',
            data: {
                type: type
            },
            success: function success(res) {
                // res = JSON.parse(res)
                if (res.code === 200) {
                    var rankHtml = '';
                    if (res.data[0]) {
                        rankHtml = "<div></div><div data-id=\"" + res.data[0].uid + "\" class=\"toMine\" onclick=\"tomine(" + res.data[0].uid + ")\">    <img src=\"" + res.data[0].avatar + "\" class=\"head-one\">    <span class=\"rank-name\">" + res.data[0].nick + "</span>     <span class=\"rank-star2\">" + res.data[0].total + "</span>    <span class=\"rank-num\">\u55b5\u55b5\u53f7\uff1a" + res.data[0].erbanNo + "</span>    <img src=\"images/gold.png\" class=\"crown-one\">    <img src=\"images/no.1.png\" class=\"no1\"></div>\n                       <div></div>";
                    }
                    if (res.data[1]) {
                        rankHtml = "<div data-id=\"" + res.data[1].uid + "\" class=\"toMine\" onclick=\"tomine(" + res.data[1].uid + ")\">    <img src=\"" + res.data[1].avatar + "\" class=\"head-two\">    <span class=\"rank-name\">" + res.data[1].nick + "</span>     <span class=\"rank-star2\">" + res.data[1].total + "</span>    <span class=\"rank-num\">\u55b5\u55b5\u53f7\uff1a" + res.data[1].erbanNo + "</span>    <img src=\"images/silver.png\" class=\"crown\">    <img src=\"images/no.2.png\" class=\"no2\"></div><div data-id=\"" + res.data[0].uid + "\" class=\"toMine\" onclick=\"tomine(" + res.data[0].uid + ")\">    <img src=\"" + res.data[0].avatar + "\" class=\"head-one\">    <span class=\"rank-name\">" + res.data[0].nick + "</span>     <span class=\"rank-star2\">" + res.data[0].total + "</span>    <span class=\"rank-num\">\u55b5\u55b5\u53f7\uff1a" + res.data[0].erbanNo + "</span>    <img src=\"images/gold.png\" class=\"crown-one\">    <img src=\"images/no.1.png\" class=\"no1\"></div><div></div>";
                    }
                    if (res.data[2]) {
                        rankHtml = "<div data-id=\"" + res.data[1].uid + "\" class=\"toMine\" onclick=\"tomine(" + res.data[1].uid + ")\">    <img src=\"" + res.data[1].avatar + "\" class=\"head-two\">    <span class=\"rank-name\">" + res.data[1].nick + "</span>     <span class=\"rank-star2\">" + res.data[1].total + "</span>    <span class=\"rank-num\">\u55b5\u55b5\u53f7\uff1a" + res.data[1].erbanNo + "</span>    <img src=\"images/silver.png\" class=\"crown\">    <img src=\"images/no.2.png\" class=\"no2\"></div><div data-id=\"" + res.data[0].uid + "\" class=\"toMine\" onclick=\"tomine(" + res.data[0].uid + ")\">    <img src=\"" + res.data[0].avatar + "\" class=\"head-one\">    <span class=\"rank-name\">" + res.data[0].nick + "</span>     <span class=\"rank-star2\">" + res.data[0].total + "</span>    <span class=\"rank-num\">\u55b5\u55b5\u53f7\uff1a" + res.data[0].erbanNo + "</span>    <img src=\"images/gold.png\" class=\"crown-one\">    <img src=\"images/no.1.png\" class=\"no1\"></div><div data-id=\"" + res.data[2].uid + "\" class=\"toMine\" onclick=\"tomine(" + res.data[2].uid + ")\">    <img src=\"" + res.data[2].avatar + "\" class=\"head-three\">    <span class=\"rank-name\">" + res.data[2].nick + "</span>     <span class=\"rank-star2\">" + res.data[2].total + "</span>    <span class=\"rank-num\">\u55b5\u55b5\u53f7\uff1a" + res.data[2].erbanNo + "</span>    <img src=\"images/copper.png\" class=\"crown\">    <img src=\"images/no.3.png\" class=\"no2\"></div>";
                    }
                    $('#rank-title2').html(rankHtml);
                    var html = '';
                    res.data.forEach(function (item, index, arr) {
                        if (index > 2) {
                            html += "     <span class=\"toMine\" onclick=\"tomine(" + item.uid + ")\" data-id=\"" + item.uid + "\"><span>" + (index*1+1) + "</span><span>    <span>        <img src=\"" + item.avatar + "\">    </span>    <span class=\"rank-list-content\">        <span>          <p>" + item.nick + "</p>        </span>                <span>\u55b5\u55b5\u53f7\uff1a" + item.erbanNo + "</span>        </span>       <span class=\"rank-list-star2\">    <p>" + (arr[index - 1].total - arr[index].total) + "</p>                                    <p>\u8DDD\u79BB\u524D\u4E00\u540D</p>    </span></span>                        </span>                           ";
                        }
                    });
                    $('#rank-list2').html(html);
                }
            }
        });
    }
    getRank1(98, 1, info.uid);
    // getRank2(99, 1, info.uid);
    $(".the-border").eq(0).show();
    var tabsSwiper;
    tabsSwiper = new Swiper('.swiper-container', {
        speed: 500,
        onSlideChangeStart: function onSlideChangeStart() {
            $(".tabs .active").removeClass('active');
            $(".the-border").hide();
            $(".tabs span").eq(tabsSwiper.activeIndex).addClass('active');
            $(".tabs span").eq(tabsSwiper.activeIndex).children('.the-border').show();
            switch($(".tabs .active").data().type){
                case 1:
                    getRank1(98, 1, info.uid);
                    break;
                case 2:
                    getRank2(99, 1, info.uid);
                    break;
                default:
                    break;    
            }
        }
    });

    $(".tabs span").on('touchstart mousedown', function (e) {
        e.preventDefault();
        $(".tabs .active").removeClass('active');
        $(".the-border").hide();
        $(this).addClass('active');
        $(this).children('.the-border').show();
        tabsSwiper.swipeTo($(this).index());
        switch($(".tabs .active").data().type){
            case 1:
                getRank1(98, 1, info.uid);
                break;
            case 2:
                getRank2(99, 1, info.uid);
                break;
            default:
                break;    
        }
    });

    $(".tabs span").click(function (e) {
        e.preventDefault();
    });
    tomine = function tomine(params) {
        if (browser.ios) {
            window.webkit.messageHandlers.openPersonPage.postMessage(params);
        } else if (browser.android) {
            if (androidJsObj && (typeof androidJsObj === 'undefined' ? 'undefined' : _typeof(androidJsObj)) === 'object') {
                window.androidJsObj.openPersonPage(params);
            }
        }
    };
    rankList = function rankList(e) {
        for (var i = 0; i < 3; i++) {
            $($('.rank-tab>span')[i]).removeClass('acolor');
        }
        $($('.rank-tab>span')[e]).addClass('acolor');
        switch (e) {
            case 0:
                $('.rank-tab-bg').animate({
                    left: '-0.01rem'
                }, 100);
                getRank1(98, 1, info.uid);
                break;
            case 1:
                $('.rank-tab-bg').animate({
                    left: '2.1rem'
                }, 100);
                getRank1(98, 2, info.uid);
                break;
            case 2:
                $('.rank-tab-bg').animate({
                    left: '4.26rem'
                }, 100);
                getRank1(98, 3, info.uid);
                break;
            default:
                break;

        }
    };
    rankList2 = function rankList2(e) {
        for (var i = 0; i < 3; i++) {
            $($('.rank-tab2>span')[i]).removeClass('acolor2');
        }
        $($('.rank-tab2>span')[e]).addClass('acolor2');
        switch (e) {
            case 0:
                $('.rank-tab-bg2').animate({
                    left: '-0.01rem'
                }, 100);
                getRank2(99, 1, info.uid);
                break;
            case 1:
                $('.rank-tab-bg2').animate({
                    left: '2.1rem'
                }, 100);
                getRank2(99, 2, info.uid);
                break;
            case 2:
                $('.rank-tab-bg2').animate({
                    left: '4.26rem'
                }, 100);
                getRank2(99, 3, info.uid);
                break;
            default:
                break;

        }
    };
    $('#the-share').on('click', function () {
        if (browser.app) {
            if (browser.android) {
                window.androidJsObj.openSharePage();
            } else if (browser.ios) {
                window.webkit.messageHandlers.openSharePage.postMessage(null);
            }
        }
    });
}, 50);
