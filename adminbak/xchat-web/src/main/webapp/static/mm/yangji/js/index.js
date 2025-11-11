var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function (obj) {
    return typeof obj;
} : function (obj) {
    return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj;
};
var browser = checkVersion();
var info = {};
if (browser.app) {
    if (browser.ios) {
        window.webkit.messageHandlers.getUid.postMessage(null);
        function getMessage(key, value) {
            info[key] = value;
        }
    } else if (browser.android) {
        if (androidJsObj && (typeof androidJsObj === 'undefined' ? 'undefined' : _typeof(androidJsObj)) ===
            'object') {
            info.uid = parseInt(window.androidJsObj.getUid());
        }
    }
}
setTimeout(function () {
    var arr = ['images/close.png', 'images/meiguihua.png', 'images/bangbangtang.png', 'images/momoda.png', 'images/xiaoxiong.png', 'images/yangjitoukuang.png', 'images/ainio.png', 'images/deng.png', 'images/xiaomai.png', 'images/mmzuojia.png', 'images/Sjisaiche.png', 'images/yangjizuojia.png', 'images/qianmingzhao.png', 'images/no.png'],
        i = -1,
        num = 0,
        flag = true;
    $('#nums').text("你拥有" + num + "次抽奖机会（每日机会在24点清空）");
    function activity() {
        $.get('/activity/youngg/count?uid=' + info.uid, function (response) {
            if (response.code === 200) {
                $('#nums').text("你拥有" + response.data + "次抽奖机会（每日机会在24点清空）");
                num = response.data;
            } else {
                $('#nums').text("你拥有0次抽奖机会（每日机会在24点清空）");
            }
        });
    };
    function draw() {
        if (!flag) {
            return false;
        }
        flag = false;
        if (!num) {
            console.log(1);
            $('.warm_img').attr('src', arr[13]);
            setTimeout(function () {
                $('.warm_img').show();
                $('.mask').show();
            }, 300);
            setTimeout(function () {
                flag = true;
            }, 1500);
            return false;
        }
        $.post('/activity/youngg/draw', { uid: info.uid }, function (response) {
            var data = JSON.parse(response);
            if (data.code == 200) {
                $('.warm_img').attr('src', arr[data.data]);
                setTimeout(function () {
                    $('.warm_img').show();
                    $('.mask').show();
                    activity();
                }, 300);
            }
            setTimeout(function () {
                flag = true;
            }, 2000);
        });
    };
    $('.open,.box').tap(function (e) {
        draw();
    });
    $('.mask').tap(function (e) {
        $('.warm_img').attr('src', '');
        $('.warm_img').hide();
        $('.mask').hide();
    });
    activity();
}, 50);
