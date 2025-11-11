console.log(location.href);
var browser = checkVersion();
var info = {};
// info.uid = 500198;
// info.ticket = "eyJhbGciOiJIUzI1NiJ9.eyJ0aWNrZXRfdHlwZSI6bnVsbCwidWlkIjo1MDAxOTgsInRpY2tldF9pZCI6ImM2MDBkMmQzLTJhZWEtNDM5Ni04NTY3LTgyMjMwOGMwNTIwNyIsImV4cCI6MzYwMCwiY2xpZW50X2lkIjoiZXJiYW4tY2xpZW50In0.kpCoAls1R34tPCQyWhqT8K2Q2NrLDs6zXknq28G7Xzw";
// init();
window.getMessage = function getMessage(key, value) {
    info[key] = value;
    if (info.uid && info.ticket) {
        init();
    }
};

if (browser.ios && window.webkit) {
    window.webkit.messageHandlers.getUid.postMessage(null);
    window.webkit.messageHandlers.getTicket.postMessage(null);

} else if (browser.android) {
    info.uid = parseInt(window.androidJsObj.getUid());
    info.ticket = window.androidJsObj.getTicket();
    if (info.uid && info.ticket) {
        init();
    }
}

function init() {
    $.ajax({
        type: 'GET',
        url: allUrl() + '/purseHotRoom/list',
        data: {
            uid: info.uid,
            ticket: info.ticket
        },
        success: function success(res) {
            if (res.code === 200) {
                var html = '<span><p>房间ID</p><p>购买日期</p><p>购买时间</p></span>';
                res.data.forEach(function(item) {
                    html += '<span><p>' + item.roomNo + '</p><p>' + item.date + '</p><p>' + item.hour + '</p></span>';
                });
                $('.content').html(html);
            } else {
                $toast.show({
                    text: res.message,
                    time: 3000
                })
            }
        }
    });
}