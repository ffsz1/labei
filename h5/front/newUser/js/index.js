var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function(obj) {
    return typeof obj;
} : function(obj) {
    return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj;
};
var browser = checkVersion();
console.log(browser)
console.log(222)
var info = {};
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

// appFun(函数名字，传递的参数,function(data){
//     data 为客户端返回的数据
// })

// 或者没有传递参数的
appFun('getUid', function(data) {
    info.uid = data
    appFun('getTicket', function(data) {
        info.ticket = data
        initPage()
    })
})



// info.uid = 10176;
// info.ticket = "eyJhbGciOiJIUzI1NiJ9.eyJ0aWNrZXRfdHlwZSI6bnVsbCwidWlkIjoxMDE3NiwidGlja2V0X2lkIjoiM2IyNjUyYTAtNjhmNS00NjNjLWJjYzgtMWFiMzQ1NWFkOTFlIiwiZXhwIjozNjAwLCJjbGllbnRfaWQiOiJlcmJhbi1jbGllbnQifQ.jCWp6kF0KzEAGDD0LiE8JX-xaTWUP3DoTfPNnbfaj8o";

function initPage() {

    getStatus();


}
// initPage();

function getStatus() { //Map集合
    // 金币:box1：0.未达到领取, 1.可领取 , 2.已领取
    // 头饰:box2：0.未达到领取, 1.可领取 , 2.已领取
    // 座驾:box3:0.未达到领取, 1.可领取 , 2.已领取
    // $('.main .item .light').hide()
    // $('.main .item .shade').show()
    $.ajax({
        type: "get",
        url: allUrl() + "/public/charge/getList",
        data: {
            uid: info.uid,
            ticket: info.ticket
        },
        success: function(res) {
            console.log(res)
            if (res.code === 200) {
                var data = res.data;
                // console.log(data)
                setStatus($('.item1'), data['box1'])
                setStatus($('.item2'), data['box2'])
                setStatus($('.item3'), data['box3'])
            }

        },
        error: function() {
            console.log('getStatus Error')
        }
    });
}

function get(index) {

    $.ajax({
        type: "post",
        url: allUrl() + "/public/charge/receiveActivityItem",
        data: {
            uid: info.uid,
            ticket: info.ticket,
            itemId: index //1.金币 2.头饰 3.座驾
        },
        success: function(res) {
            console.log(1111111)
            if (res.code === 200) {
                $toast.show({
                    text: '领取成功',
                    time: 2000
                })
                setTimeout(function() {
                    initPage() //refresh the status
                }, 1000);

            } else {
                // console.log(res.message)
                var text = res.message.split(':')[1]
                $toast.show({
                    text: text,
                    time: 2000
                })
            }

        },
        error: function(res) {
            console.log('get Error')
            $toast.show({
                text: res.message,
                time: 2000
            })
        }
    });
}

function setStatus($obj, type) {
    type = parseInt(type);
    // console.log(type)
    if (type === 0) {
        $obj.find('.shade').show();
        $obj.find('.light').hide();
    } else if (type === 1) {
        $obj.find('.light').show();
        $obj.find('.shade').hide();

    } else if (type === 2) {
        $obj.find('.btn').html('已领取');
        $obj.find('.shade').show();
        $obj.find('.light').hide();
    }
}

/**
 * toast 
 * 2019/03/27 copy from 曾智勇
 */
$('body').append('<div class="toast">');
var $toast = {
    show: function(obj) {
        $('.toast').text(obj.text).show();
        setTimeout(function() {
            $('.toast').hide();
        }, obj.time)
    }
};


// $toast.show({
//     text: 'ji111',
//     time:2000
// })

$('.main .item .shade .alert-btn').on('click', function() {
    $toast.show({
        text: 'ji111',
        time: 2000
    })
})