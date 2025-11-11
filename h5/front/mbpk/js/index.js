var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function(obj) {
    return typeof obj;
} : function(obj) {
    return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj;
};
var share = {
    title: '点点大PK', // 分享标题
    link: allUrl() + '/front/download/download.html', // 分享链接
    imgUrl: allUrl() + '/home/images/logo.png',
    desc: '点点大PK，多数派赢大量萌币!' // 分享描述
};

function shareInfo() {
    var _url = allUrl() + '/front/download/download.html';
    var info = {
        title: '点点大PK', // 分享标题
        showUrl: _url,
        imgUrl: allUrl() + '/home/images/logo.png',
        desc: '点点大PK，多数派赢大量萌币!' // 分享描述
    };
    return JSON.stringify(info);
}
var browser = checkVersion();
var info = {};

// var androidJsObj = null;
// info.uid = 10247;
// info.ticket = "eyJhbGciOiJIUzI1NiJ9.eyJ0aWNrZXRfdHlwZSI6bnVsbCwidWlkIjoxMDI0NywidGlja2V0X2lkIjoiNjY1YTY5YjMtODYxMC00NmFhLWI0YWYtODM0ZTdhZDY4NmMwIiwiZXhwIjozNjAwLCJjbGllbnRfaWQiOiJlcmJhbi1jbGllbnQifQ.Xk9QXUpAeLpjkzd9u2fjLiQlPNLIMIpTFKtiWRZIDj0"
// getAjax()

if (browser.ios && window.webkit) {
    window.webkit.messageHandlers.getUid.postMessage(null);
    window.webkit.messageHandlers.getTicket.postMessage(null);
} else if (browser.android) {
    if (
        androidJsObj &&
        (typeof androidJsObj === "undefined" ?
            "undefined" :
            _typeof(androidJsObj)) === "object"
    ) {
        info.uid = parseInt(window.androidJsObj.getUid());
        info.ticket = window.androidJsObj.getTicket();
        window.androidJsObj.showShareButton(true)
        getAjax();
    }
}
var getMessage = function getMessage(key, value) {
    info[key] = value;
    if (info.uid && info.ticket) {
        getAjax();
    }
};

// $(".container").css("display", 'flex');
// 分享及获取参数end
// 获取信息
function getAjax() {
    $.ajax({
            url: allUrl() + '/mcoin/pk/getPkInfo',
            data: {
                uid: info.uid,
                ticket: info.ticket
            },
            success: function(res) {
                console.log(res);
                if (res.code != 200) {
                    // $toast.show({
                    //     text: '活动尚未开启，敬请期待',
                    //     time: 10000
                    // })
                    // console.log('进去else');
                    $('.popnone').show()
                } else {
                    $(".container").css("display", 'flex');
                    $(".js_term").text(res.data.term);
                    $(".js_blueAnswer").text(res.data.blueAnswer);
                    $(".js_redAnswer").text(res.data.redAnswer);
                    $(".js_title").text(res.data.title);
                    $(".js_redPolls").text(res.data.redPolls);
                    $(".js_bluePolls").text(res.data.bluePolls);
                    $(".js_carveUpMcoinNum").text(res.data.carveUpMcoinNum);
                    var h = (new Date(res.data.lotteryTime).getHours()) < 10 ? "0" + (new Date(res.data.lotteryTime).getHours()) : (new Date(res.data.lotteryTime).getHours());
                    var min = (new Date(res.data.lotteryTime).getMinutes()) < 10 ? "0" + (new Date(res.data.lotteryTime).getMinutes()) : (new Date(res.data.lotteryTime).getMinutes());
                    var date = (new Date(res.data.lotteryTime).getMonth() + 1) + "月" + (new Date(res.data.lotteryTime).getDate()) + "日" + h + ":" + min;
                    $(".js_lotteryTime").text(date);
                    $(".js_redPic").css("background-image", "url(" + res.data.redPic + ")");
                    $(".js_bluePic").css("background-image", "url(" + res.data.bluePic + ")");
                }
            },
            error: function(err) {
                console.log('111');
                console.log('错误', err);
            }
        })
        // $toast.show({
        //     text: '活动尚未开启，敬请期待',
        //     time: 10000
        // })
}
// getAjax();
var CLICKTAG1 = 0;
$(".js_redAnswer").on("click", function() {
    if (CLICKTAG1 == 0) {
        CLICKTAG1 == 1;
        $.ajax({
            url: allUrl() + '/mcoin/pk/supportPk',
            data: {
                uid: info.uid,
                ticket: info.ticket,
                supportType: 1
            },
            type: 'POST',
            success: function(res) {
                console.log(res);
                if (res.code != 200) {
                    $toast.show({
                        text: res.message,
                        time: 3000
                    })
                } else {
                    $toast.show({
                        text: '投票成功！',
                        time: 3000
                    })
                    $('.task_b .aa2').css({ 'opacity': 1 });
                    $(".js_carveUpMcoinNum").text(parseInt($(".js_carveUpMcoinNum").text()) + 10);
                    var str = $(".js_redPolls").text();
                    str = str.replace(/[^0-9]/ig, "");
                    var str2 = parseInt(str.substring(1, 2)) + 1;
                    str2 = (str2 >= 10 ? 0 : str2);
                    var str1 = (str2 == 0 ? parseInt(str.substring(0, 1)) + 1 : parseInt(str.substring(0, 1)))
                    str1 = (str1 >= 10 ? 0 : str1);
                    $(".js_redPolls").text("***" + str1 + str2);
                }
                CLICKTAG1 == 0;
            }
        })
    } else {
        $toast.show({
            text: '正在投票...',
            time: 3000
        })
    }
});
var CLICKTAG2 = 0;
$(".js_blueAnswer").on("click", function() {
    if (CLICKTAG2 == 0) {
        CLICKTAG2 == 1;
        $.ajax({
            url: allUrl() + '/mcoin/pk/supportPk',
            data: {
                uid: info.uid,
                ticket: info.ticket,
                supportType: 2
            },
            type: 'POST',
            success: function(res) {
                console.log(res);
                if (res.code != 200) {
                    $toast.show({
                        text: res.message,
                        time: 3000
                    })
                } else {
                    $toast.show({
                        text: '投票成功！',
                        time: 3000
                    })
                    $('.task_g .aa2').css({ 'opacity': 1 });
                    $(".js_carveUpMcoinNum").text(parseInt($(".js_carveUpMcoinNum").text()) + 10);
                    var str = $(".js_bluePolls").text();
                    str = str.replace(/[^0-9]/ig, "");
                    var str2 = parseInt(str.substring(1, 2)) + 1;
                    str2 = (str2 >= 10 ? 0 : str2);
                    var str1 = (str2 == 0 ? parseInt(str.substring(0, 1)) + 1 : parseInt(str.substring(0, 1)))
                    str1 = (str1 >= 10 ? 0 : str1);
                    $(".js_bluePolls").text("***" + str1 + str2);
                }
                CLICKTAG2 == 0;
            }
        })
    } else {
        $toast.show({
            text: '正在投票...',
            time: 3000
        })
    }
});
// var step=sessionStorage.getItem("step");
// if(step==2){
//     $("body").attr("class","step2");
// }
function closePop(ele, cb) {
    $(ele).hide();
    typeof cb == 'function' && cb();
}

function openPop(ele, cb) {
    $(ele).show();
    typeof cb == 'function' && cb();
}
$(".pop_close").on("click", function() {
    closePop('.pop');
})
$(".js_rule").on("click", function() {
        openPop('.pop');
    })
    // $(".js_detail").on("click", function() {
    //     $("body").attr("class", "step2");
    //     // sessionStorage.setItem("step", "2");
    // })
$(".js_zj").on("click", function() {
    location.href = "./zj.html"
})
$(".task_right").on("click", function() {
    location.href = "./review.html"
})
$(".js_yq").on("click", function() {
    if (browser.android) {
        window.androidJsObj.openSharePage('');
    } else if (browser.ios) {
        window.webkit.messageHandlers.openSharePage.postMessage(null);
    }
})
$(".js_mbzx").on("click", function() {
        if (browser.ios) {
            window.webkit.messageHandlers.closeWin.postMessage(null);
        } else if (browser.android) {
            window.androidJsObj.closeWin();
        }
    })
    // $toast.show({
    //             text: '活动尚未开启，敬请期待！',
    //             time: 3000
    //         })