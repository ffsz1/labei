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
}
$('.swiper-container').height($('.container').height())
setTimeout(function() {
    function f(a) {
        $.ajax({
            type: "GET",
            url: allUrl() + "/allrank/geth5",
            data: {
                type: 1,
                datetype: a,
                uid: info.uid
            },
            success: function(a) {
                if (200 === a.code) {
                    var c = "",
                        d = "";
                    a.data.rankVoList.forEach(function(a, b, e) {
                        3 > b ? c += '<li class="list-top-' + b + '" onclick="memberDetail(' + a.uid + ')">    <img src="images/no' + b + '.png">    <img src="' + a.avatar + '">    <img src="images/no-' + b + '.png">    <p class="text-hiden">' + a.nick + '</p>    <span class="list-top-id">    <img src="images/cl' + a.charmLevel + '.png" class="img-cl" style="display:' +
                            (0 === a.charmLevel ? "none" : "block") + '">    <span>ID:' + a.erbanNo + '</span>    </span>    <span class="list-top-total">    <span>' + a.totalNum + '</span></span></li>' : d += '<li onclick="memberDetail(' + a.uid + ')">    <span>' + (b + 1) + '</span>    <span class="list-content">    <span>        <img src="' + a.avatar + '">        <span class="list-user">        <span>            <img src="images/cl' + a.charmLevel + '.png" class="img-cl" style="display:' + (0 === a.charmLevel ?
                                "none" : "block") + '">            <p class="text-hiden">' + a.nick + "</p>        </span>        <p>ID:" + a.erbanNo + "</p>        </span>    </span>    <span>        <p>" + (e[b - 1].totalNum - e[b].totalNum) + "</p>        <p>\u8ddd\u79bb\u524d\u4e00\u540d</p>    </span>    </span></li>"
                    });
                    $("#list1 .list-top").html(c);
                    $("#list1 .list-item").html(d)
                }
            }
        })
    }

    function g(a) {
        $.ajax({
            type: "GET",
            url: allUrl() + "/allrank/geth5",
            data: {
                type: 2,
                datetype: a,
                uid: info.uid
            },
            success: function(a) {
                if (200 === a.code) {
                    var c = "",
                        d = "";
                    a.data.rankVoList.forEach(function(a,
                        b, e) {
                        3 > b ? c += '<li class="list-top-' + b + '" onclick="memberDetail(' + a.uid + ')">    <img src="images/no' + b + '.png">    <img src="' + a.avatar + '">    <img src="images/no-' + b + '.png">    <p class="text-hiden">' + a.nick + '</p>    <span class="list-top-id">    <img src="images/lv' + a.experLevel + '.png" class="img-cl" style="display:' + (0 === a.experLevel ? "none" : "block") + '">    <span>ID:' + a.erbanNo + '</span>    </span>    <span class="list-top-total">    <span>' + a.totalNum + '</span></span></li>' :
                            d += '<li onclick="memberDetail(' + a.uid + ')">    <span>' + (b + 1) + '</span>    <span class="list-content">    <span>        <img src="' + a.avatar + '">        <span class="list-user">        <span>            <img src="images/lv' + a.experLevel + '.png" class="img-cl" style="display:' + (0 === a.experLevel ? "none" : "block") + '">            <p class="text-hiden">' + a.nick + "</p>        </span>        <p>ID:" + a.erbanNo + "</p>        </span>    </span>    <span>        <p>" + (e[b - 1].totalNum - e[b].totalNum) + "</p>        <p>\u8ddd\u79bb\u524d\u4e00\u540d</p>    </span>    </span></li>"
                    });
                    $("#list2 .list-top").html(c);
                    $("#list2 .list-item").html(d)
                }
            }
        })
    }

    function h(a) {
        $.ajax({
            type: "GET",
            url: allUrl() + "/allrank/geth5",
            data: {
                type: 3,
                datetype: a,
                uid: info.uid
            },
            success: function(a) {
                if (200 === a.code) {
                    var c = "",
                        d = "";
                    a.data.rankVoList.forEach(function(a, b, e) {
                        3 > b ? c += '<li class="list-top-' + b + '" onclick="memberDetail(' + a.uid + ')">    <img src="images/no' + b + '.png">    <img src="' + a.avatar + '">    <img src="images/no-' + b + '.png">    <p class="text-hiden">' + a.nick + '</p>    <span class="list-top-id">    <span>ID:' +
                            a.erbanNo + '</span>    </span>    <span class="list-top-total">    <span>' + a.totalNum + '</span></span></li>' : d += '<li onclick="memberDetail(' + a.uid + ')">    <span>' + (b + 1) + '</span>    <span class="list-content">    <span>        <img src="' + a.avatar + '">        <span class="list-user">        <span>             <p class="text-hiden">' + a.nick + "</p>        </span>        <p>ID:" + a.erbanNo + "</p>        </span>    </span>    <span>        <p>" + (e[b -
                                1].totalNum - e[b].totalNum) + "</p>        <p>\u8ddd\u79bb\u524d\u4e00\u540d</p>    </span>    </span></li>"
                    });
                    $("#list3 .list-top").html(c);
                    $("#list3 .list-item").html(d)
                }
            }
        })
    }
    var k = 1,
        l = 1,
        m = 1;
    var n = new Swiper(".swiper-container", {
        speed: 300,
        onSlideChangeStart: function() {
            var a = n.activeIndex;
            $("#bottom-line").css("animation-name ", "tabbar" + a);
            switch (a) {
                case 0:
                    f(k);
                    break;
                case 1:
                    g(l);
                    break;
                case 2:
                    h(m)
            }
            setTimeout(function() {
                $("#bottom-line").removeClass().addClass("bottom-line" + a)
            }, 300)
        }
    });
    f(1);
    $(".tabbar>span").on("click",
        function() {
            var a = $(this).index();
            $("#bottom-line").css("animation-name", "tabbar" + a);
            n.swipeTo($(this).index());
            switch (a) {
                case 0:
                    f(k);
                    break;
                case 1:
                    g(l);
                    break;
                case 2:
                    h(m)
            }
            setTimeout(function() {
                $("#bottom-line").removeClass().addClass("bottom-line" + a)
            }, 300)
        });
    $("#switch-bar0>.switch-bar-option").click(function() {
        var a = $(this).index();
        $("#switch-bar0>.switch-bar-option").removeClass("B94BFF");
        $($("#switch-bar0>.switch-bar-option")[a]).addClass("B94BFF");
        $("#switch-bar-item").css("animation-name", "switch-bar-move" +
            a);
        f(a + 1);
        k = a + 1;
        setTimeout(function() {
            $("#switch-bar-item").removeClass().addClass("switch-bar-item" + a)
        }, 300)
    });
    $("#switch-bar1>.switch-bar-option").click(function() {
        var a = $(this).index();
        $("#switch-bar1>.switch-bar-option").removeClass("FFA026");
        $($("#switch-bar1>.switch-bar-option")[a]).addClass("FFA026");
        $("#switch-bar-item1").css("animation-name", "switch-bar-move" + a);
        g(a + 1);
        l = a + 1;
        setTimeout(function() {
            $("#switch-bar-item1").removeClass().addClass("switch-bar-item" + a)
        }, 300)
    });
    $("#switch-bar2>.switch-bar-option").click(function() {
        var a =
            $(this).index();
        $("#switch-bar2>.switch-bar-option").removeClass("a229EFF");
        $($("#switch-bar2>.switch-bar-option")[a]).addClass("a229EFF");
        $("#switch-bar-item2").css("animation-name", "switch-bar-move" + a);
        h(a + 1);
        m = a + 1;
        setTimeout(function() {
            $("#switch-bar-item2").removeClass().addClass("switch-bar-item" + a)
        }, 300)
    });
    memberDetail = function(a) {
        browser.ios ? window.webkit.messageHandlers.openPersonPage.postMessage(a) : browser.android && androidJsObj && "object" === ("undefined" === typeof androidJsObj ? "undefined" : _typeof(androidJsObj)) &&
            window.androidJsObj.openPersonPage(a)
    }
}, 50);