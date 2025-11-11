var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function (a) {
    return typeof a
} : function (a) {
    return a && typeof Symbol === "function" && a.constructor === Symbol && a !== Symbol.prototype ? "symbol" : typeof a
};

var info = {};
var getMessage = function getMessage(a, b) {
    info[a] = b;
    if (info.uid && info.ticket) {
        init()
    }
};
var browser = checkVersion();
if (browser.ios && window.webkit) {
    window.webkit.messageHandlers.getUid.postMessage(null);
    window.webkit.messageHandlers.getTicket.postMessage(null);
} else {
    if (browser.android) {
        if (androidJsObj && (typeof androidJsObj === "undefined" ? "undefined" : _typeof(androidJsObj)) === "object") {
            info.uid = parseInt(window.androidJsObj.getUid());
            info.ticket = window.androidJsObj.getTicket();
            if (info.uid && info.ticket) {
                init()
            }
        }
    }
}
var refreshWeb = function refreshWeb() {
    location.reload();
    return ""
};
// loading(3000);
// info.uid = 1;
// info.ticket = 'eyJhbGciOiJIUzI1NiJ9.eyJ0aWNrZXRfdHlwZSI6bnVsbCwidWlkIjoxLCJ0aWNrZXRfaWQiOiI4NTU1OWMwNC0yNDFmLTQ4NzktYWQxYi05NWU1MDU1YmU3MmIiLCJleHAiOjM2MDAsImNsaWVudF9pZCI6ImVyYmFuLWNsaWVudCJ9.QgdoTKM6m_KyI28hC6jNnadHrZ--kQSha2xjQ0NNFsg';
// init();

function init() {
    var c = new Swiper(".swiper-container", {
        direction: "vertical",
        loop: true,
        autoplay: true,
        observer: true,
        observeParents: true
    });
    $.ajax({
        url: allUrl() + "/draw/list",
        data: {
            uid: info.uid,
            ticket: info.ticket
        },
        success: function f(h) {
            if (h.code === 200) {
                var g = "";
                h.data.forEach(function (i) {
                    g += '<div class="swiper-slide"><p>恭喜</p><p>' + i.userVo.nick + "</p><p>获得</p><p>" + i.drawPrizeName + "</p></div>"
                });
                $("#list-item").html(g)
            }
            clearLoading()
        },
        error: function () {
            clearLoading()
        }
    });

    function a() {
        $.ajax({
            type: "get",
            url: allUrl() + "/draw/get",
            data: {
                uid: info.uid,
                ticket: info.ticket
            },
            success: function g(h) {
                if (h.code === 200) {
                    $("#leftDrawNum-text").html(h.data.leftDrawNum+ "次")
                }
            }
        })
    }
    a();

    $(".clo").click(function () {
        $(".mask").hide();
        $(".bg_bg").hide();
        $(".rule-tips").hide()
    });
    $(".mask").click(function () {
        $(".bg_bg").hide();
        $(".mask").hide()
    });
    b = function b() {
        $(".rule-tips").show()
    }
};
var arrow = function arrow() {
    $(".arrow").removeAttr("onclick");
    $.ajax({
        type: "get",
        url: allUrl() + "/draw/do",
        data: {
            uid: info.uid,
            ticket: info.ticket
        },
        success: function h(i) {
            if (i.code === 200) {
                var j = 0;
                $("#leftDrawNum-text").html(i.data.leftDrawNum + "次");
                $(".prize-name").html("<p>恭喜你获得</p><p>" + i.data.drawPrizeName + "</p>")
                $(".prize .bg_pr").show();
                switch (i.data.drawPrizeId) {
                    case 0:
                        j = 1450;
                        $(".prize .bg_pr").hide();
                        $(".bg_0").show();
                        break;
                    case 8:
                        j = 1240;
                        $(".bg_8").show();
                        break;
                    case 50:
                        j = 1270;
                        $(".bg_50").show();
                        break;
                    case 100:
                        j = 36 * 6 + 1080 + 15;
                        $(".bg_100").show();
                        break;
                    case 300:
                        j = 36 * 8 + 15 + 1080;
                        $(".bg_300").show();
                        break;
                    case 1000:
                        j = 36 * 4 - 15 + 1080;
                        $(".bg_1000").show();
                        break;
                    case 3000:
                        j = 36 * 2 - 15 + 1440;
                        $(".bg_3000").show();
                        break;
                    case 8888:
                        j = 36 * 3 - 15 + 1440;
                        $(".bg_8888").show();
                        break;
                    case 10000:
                        j = 36 * 8 - 15 + 1080;
                        $(".bg_10000").show();
                        break;
                    case 10001:
                        j = 360 - 15 + 1080;
                        $(".bg_10001").show();
                        break;
                    default:
                        break
                }
                var k = setInterval(function () {
                    switch ($(".turnable1").css("display")) {
                        case "block":
                            $(".turnable1").hide();
                            $(".turnable2").show();
                            break;
                        case "none":
                            $(".turnable1").show();
                            $(".turnable2").hide();
                            break;
                        default:
                            break
                    }
                }, 100);
                $(".turnble-content").animate({
                    "-webkit-transform": "rotate(" + j + "deg)"
                }, 3500, "swing", function () {
                    clearInterval(k);
                    if (i.data.drawPrizeId === 0) {
                        $(".prize-name").html("你离成功只差一步感谢您的参与")
                        $("#prize").show()
                    } else {
                        $(".prize-name").html("恭喜你获得" + i.data.drawPrizeName)
                        $("#prize").show()
                    }
                    $(".arrow").attr("onclick", "arrow()");
                    $(".turnble-content").css("transform", "")
                })
            } else {
                $(".no-chance").html(i.message.split(":")[1]);
                $(".no-chance").show();
                setTimeout(function () {
                    $(".no-chance").hide();
                    $(".arrow").attr("onclick", "arrow()")
                }, 1500)
            }
        },
        error: function g(i) {
            setTimeout(function () {
                $(".no-chance").hide();
                $(".arrow").attr("onclick", "arrow()")
            }, 1500)
        }
    })
};
var toCharge = function e() {
    if (browser.android) {
        window.androidJsObj.openChargePage()
    } else {
        if (browser.ios) {
            window.webkit.messageHandlers.openChargePage.postMessage(null)
        }
    }
};