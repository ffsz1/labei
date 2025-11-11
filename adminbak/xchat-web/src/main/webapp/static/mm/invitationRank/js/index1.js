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
setTimeout(()=>{
    $.ajax({
        // url: allUrl() + '/statpacket/rank',
        url: 'http://beta.47huyu.cn/statpacket/rank',
        data:{
            // uid:info.uid
            uid:100076
        },
        success:res=>{
            if(res.code === 200){
                $('.list-header-avatar').attr('src',res.data.me.avatar);
                if(res.data.me.seqNo >= 1000){
                    res.data.me.seqNo = '1000+' 
                }
                $('.list-header').html(`
                    <span>
                        <p class="list-header-title">排名</p>
                        <p>${res.data.me.seqNo}</p>
                    </span>
                    <span>
                        <p></p>
                        <p class="text-hidden list-header-name">${res.data.me.nick}</p>
                    </span>
                    <span>
                        <p class="list-header-title">奖励金</p>
                        <p class="FF3B3B">${res.data.me.packetNum}元</p>
                    </span>
                    <i class="list-header-bottom list-header-bottom1"></i>
                    <i class="list-header-bottom list-header-bottom2"></i>
                `)
                let html = '';
                res.data.rankList.forEach((item,index) => {
                    if(index <3){
                        html += `
                        <li>
                            <span class="list-rank-member">
                                <img src="images/no${index + 1}.png" class="rank-img">
                                <img src="${item.avatar}" class="rank-member-avatar">
                                <p class="text-hidden">${item.nick}</p>
                            </span>
                            <span>
                                <p>${item.packetNum}元</p>
                            </span>
                        </li>
                        `
                    }else{
                       html += `
                        <li>
                            <span class="list-rank-member">
                                <text class="rank-member-no">${index + 1}</text>
                                <img src="${item.avatar}" class="rank-member-avatar">
                                <p class="text-hidden">${item.nick}</p>
                            </span>
                            <span>
                                <p>${item.packetNum}元</p>
                            </span>
                        </li>
                       ` 
                    }
                });
                $('.list-content').html(html)
            }
        }
    })
    $('#btn-share').click(()=>{
        if (browser.app) {
            if (browser.android) {
                window.androidJsObj.openSharePage();
            } else if (browser.ios) {
                window.webkit.messageHandlers.openSharePage.postMessage(null);
            }
        }
    })
},50)
