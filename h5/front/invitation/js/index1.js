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

function formatDate(timestamp) {
    var date = new Date(timestamp);
    Y = date.getFullYear() + '年';
    M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '月';
    D = date.getDate() + '日';
    h = date.getHours() + ':';
    m = date.getMinutes() + ':';
    s = date.getSeconds();
    if (s < 10) {
        s = '0' + s;
    }
    var time = [Y + M + D, h + m + s];
    return time;
}

setTimeout(() => {
    $.ajax({
        // url: allUrl() + '/statpacket/invitedetail',
        url: 'http://beta.hulelive.com/statpacket/invitedetail',
        data: {
            // uid: info.uid
            uid: 100076
        },
        success: res => {
            console.log(res)
            if (res.code === 200) {
                $('#todayRegisterCount').html(res.data.inviteDetail.todayRegisterCount);
                $('#totalRegisterCount').html(res.data.inviteDetail.totalRegisterCount);
                $('#packetNum').html('￥' + res.data.inviteDetail.packetNum);
                let html = '';
                res.data.inviteList.forEach(item => {
                    item.createTime = formatDate(item.createTime);
                    html += `
                        <li>
                            <span>
                                <span class="list-member">
                                    <p>你邀请的*</p>
                                    <p class="text-hidden">${item.nick}</p>
                                    <p>*注册成功</p>
                                </span>
                                <span class="list-time">
                                    <p>${item.createTime[0]}</p>
                                    <p>${item.createTime[1]}</p>
                                </span>
                            </span>
                            <span>+${item.packetNum}元</span>
                        </li>
                    `
                });
                $('.list').html(html)
            }
        }
    })
    $('#btn-share').click(() => {
        if (browser.app) {
            if (browser.android) {
                window.androidJsObj.openSharePage();
            } else if (browser.ios) {
                window.webkit.messageHandlers.openSharePage.postMessage(null);
            }
        }
    })
}, 50)