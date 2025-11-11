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
    if (browser.android) {
        if (androidJsObj && (typeof androidJsObj === 'undefined' ? 'undefined' : _typeof(androidJsObj)) === 'object') {
            info.uid = parseInt(window.androidJsObj.getUid());
            try {
                window.androidJsObj.showShareButton(true)
            } catch (err) {}
        }
    }
}

function getData(typeNo, type) {
    $.ajax({
        url: '/activity/html/rank',
        data: {
            type: typeNo
        },
        success: function success(res) {
            if (res.code === 200) {
                var rankHtml = '';
                if (res.data[0]) {
                    rankHtml = '<div></div><div data-id="' + res.data[0].uid + '" class="toMine" onclick="tomine(' + res.data[0].uid + ')"><img src="' + res.data[0].avatar + '" class="head-one"><span class="rank-star1">' + (res.data[0].total + type) + '</span><span class="rank-name rank-name1">' + res.data[0].nick + '</span><span class="rank-num"><img src="../rank/images/lv' + res.data[0].experLevel + '.png" class="level" style="display:' + (res.data[0].experLevel != 0 ? 'block' : 'none') + '"> <p>ID:' + res.data[0].erbanNo + '</p></span><img src="images/no1.png" class="rank-no1"></div><div></div>';
                }
                if (res.data[1]) {
                    rankHtml = '<div data-id="' + res.data[1].uid + '" class="toMine" onclick="tomine(' + res.data[1].uid + ')"><img src="' + res.data[1].avatar + '" class="head-two"><span class="rank-star1">' + (res.data[1].total + type) + '</span><span class="rank-name">' + res.data[1].nick + '</span><span class="rank-num"><img src="../rank/images/lv' + res.data[1].experLevel + '.png" class="level" style="display:' + (res.data[1].experLevel != 0 ? 'block' : 'none') + '"> <p>ID:' + res.data[1].erbanNo + '</p></span><img src="images/no2.png" class="rank-no"></div><div data-id="' + res.data[0].uid + '" class="toMine" onclick="tomine(' + res.data[0].uid + ')"><img src="' + res.data[0].avatar + '" class="head-one"><span class="rank-star1">' + (res.data[0].total + type) + '</span><span class="rank-name rank-name1">' + res.data[0].nick + '</span><span class="rank-num"><img src="../rank/images/lv' + res.data[0].experLevel + '.png" class="level" style="display:' + (res.data[0].experLevel != 0 ? 'block' : 'none') + '"> <p>ID:' + res.data[0].erbanNo + '</p></span><img src="images/no1.png" class="rank-no1"></div><div></div>';
                }
                if (res.data[2]) {
                    rankHtml = '<div data-id="' + res.data[1].uid + '" class="toMine" onclick="tomine(' + res.data[1].uid + ')"><img src="' + res.data[1].avatar + '" class="head-two"><span class="rank-star1">' + (res.data[1].total + type) + '</span><span class="rank-name">' + res.data[1].nick + '</span><span class="rank-num"><img src="../rank/images/lv' + res.data[1].experLevel + '.png" class="level" style="display:' + (res.data[1].experLevel != 0 ? 'block' : 'none') + '"> <p>ID:' + res.data[1].erbanNo + '</p></span><img src="images/no2.png" class="rank-no"></div><div data-id="' + res.data[0].uid + '" class="toMine" onclick="tomine(' + res.data[0].uid + ')"><img src="' + res.data[0].avatar + '" class="head-one"><span class="rank-star1">' + (res.data[0].total + type) + '</span><span class="rank-name rank-name1">' + res.data[0].nick + '</span>  <span class="rank-num"><img src="../rank/images/lv' + res.data[0].experLevel + '.png" class="level" style="display:' + (res.data[0].experLevel != 0 ? 'block' : 'none') + '"> <p>ID:' + res.data[0].erbanNo + '</p></span><img src="images/no1.png" class="rank-no1"></div><div data-id="' + res.data[2].uid + '" class="toMine" onclick="tomine(' + res.data[2].uid + ')"><img src="' + res.data[2].avatar + '" class="head-two"><span class="rank-star1">' + (res.data[2].total + type) + '</span><span class="rank-name">' + res.data[2].nick + '</span><span class="rank-num"><img src="../rank/images/lv' + res.data[2].experLevel + '.png" class="level" style="display:' + (res.data[2].experLevel != 0 ? 'block' : 'none') + '"> <p>ID:' + res.data[2].erbanNo + '</p></span><img src="images/no3.png" class="rank-no"></div>';
                }
                $('#rank-title1').html(rankHtml);
                var html = '';
                res.data.forEach(function (item, index, arr) {
                    if (index > 2) {
                        html += '<span class="toMine" onclick="tomine(' + item.uid + ')" data-id="' + item.uid + '"><span>' + (index + 1) + '</span><span><span><img src="' + item.avatar + '"></span><span class="rank-list-content"><span><p>' + item.nick + '</p></span><span><img src="../rank/images/lv' + item.experLevel + '.png" class="level" style="display:' + (item.experLevel != 0 ? 'block' : 'none') + '"><p>ID:' + item.erbanNo + '</p></span></span><span class="rank-list-star1"><p>' + (item.total + type) + '</p></span></span></span>';
                    }
                });
                $('#rank-list1').html(html);
            }
        }
    });
}
getData(26, '');
$('.type-img').click(function () {
    for (var i = 0; i < $('.type-img').length; i++) {
        $($('.type-img')[i]).attr('src', $($('.type-img')[i]).data('urlon'));
    }
    $(this).attr('src', $(this).data('url'));
    switch ($(this).data('type')) {
        case 1:
            getData(26, '');
            break;
        case 2:
            getData(25, '');
            break;
        case 3:
            getData(24, '');
            break;
        default:
            break;
    }
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
$('#rule-btn1').click(function () {
    $('.rule-box').show();
    $('body').css('overflow', 'hidden')
    $('html').css('overflow', 'hidden')
});
$('#rule-btn2').click(function () {
    $('.rule-box').show();
    $('body').css('overflow', 'hidden')
    $('html').css('overflow', 'hidden')
});
$('.close').click(function () {
    $('.rule-box').hide();
    $('body').css('overflow', '')
    $('html').css('overflow', '')
});
$('.rule-box').click(function () {
    $('.rule-box').hide();
    $('body').css('overflow', '')
    $('html').css('overflow', '')
});
