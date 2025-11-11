var weekStarGiftVO = '',
    listType = 1,
    globalGiftId = '',
    pageNum = 1,
    giftArray = [];

// 分享
var share = {
    title: '会上瘾的拉贝星球！', // 分享标题
    link: allUrl() + '/front/download/download.html', // 分享链接
    imgUrl: allUrl() + '/home/images/logo.png',
    desc: '拉贝星球交友速配处CP，你想要的我们都有。' // 分享描述
};

function shareInfo() {
    var info = {
        title: '会上瘾的拉贝星球！', // 分享标题
        showUrl: allUrl() + '/front/download/download.html',
        imgUrl: allUrl() + '/home/images/logo.png',
        desc: '拉贝星球交友速配处CP，你想要的我们都有。' // 分享描述
    };
    return JSON.stringify(info);
}

// UA
var browser = checkVersion();
// 存取UID和Ticket的对象
var info = {};

if (browser.ios && window.webkit) {
    window.webkit.messageHandlers.getUid.postMessage(null);
    window.webkit.messageHandlers.getTicket.postMessage(null);
} else if (browser.android) {
    info.uid = parseInt(window.androidJsObj.getUid());
    info.ticket = window.androidJsObj.getTicket();
    window.androidJsObj.showShareButton(false);
    getAjax();
}

// IOS返回UID和Ticket
var getMessage = function getMessage(key, value) {
    info[key] = value;
    if (info.uid && info.ticket) {
        getAjax();
    }
};

// 打开活动规则
$('.rule-block').on('touchend', function () {
    $('.pop-up, .rule').css('display', 'block');
});

// 打开历史
$('.history-block').click(function () {
    $('.pop-up, .history').css('display', 'block');
});

// 关闭活动规则 | 关闭历史周星
$('.close').on('touchend', function () {
    $('.pop-up, .rule, .history, .dialog').css('display', 'none');
});

// 导航切换
$('.btn-group>img').click(function () {
    $(this).attr('src', './img/' + $(this).attr('alt') + '_selected.png').removeClass('half').siblings().attr('src', './img/' + $(this).siblings().attr('alt') + '.png').addClass('half');
    if ($(this).attr('alt') == 'rich') {
        $("img[alt='heart']").attr("src", './img/gold.png');
        listType = 2;
    } else {
        $("img[alt='heart']").attr("src", './img/heart.png');
        listType = 1;
    }
    showList(globalGiftId, listType);
});

// 查看更多
$('.more>img').click(function () {
    $('.list').css('overflow', 'auto')
    $('.more').hide();
});

// 跳个人主页
function goToInfo(id) {
    if (browser.ios) {
        window.webkit.messageHandlers.openPersonPage.postMessage(id);
    } else if (browser.android) {
        if (androidJsObj && (typeof androidJsObj === 'undefined' ? 'undefined' : _typeof(androidJsObj)) === 'object') {
            window.androidJsObj.openPersonPage(id);
        }
    }
}

// 格式化Total字段
function formatTotal(val) {
    return val == null ? 0 : val >= 10000 ? (val / 10000).toFixed(1) + 'w' : val
}

// 格式化排名
function formatRank(rank) {
    return rank == null ? 0 : rank >= 100 ? 99 : rank;
}

// 获取本周周星礼物
function getAjax() {
    $.ajax({
        url: allUrl() + '/week/star/getWeekStarGift',
        type: 'GET',
        data: {
            ticket: info.ticket,
            uid: info.uid
        },
        success: function success(res) {
            if (res.code == 200) {
                // 本周周星礼物
                weekStarGiftVO = res.data.weekStarGiftVO;
                if (weekStarGiftVO.length > 0) {
                    const html = `
                        <div class="gift-item" style="margin-left: .075rem;" onclick="getStarList(${weekStarGiftVO[0].giftId})">
                            <img src="${weekStarGiftVO[0].picUrl}" alt="gift">
                            <p>${weekStarGiftVO[0].giftName}</p>
                            <p class="font-purple">${weekStarGiftVO[0].goldPrice}金币</p>
                        </div>
                        <div class="gift-item" onclick="getStarList(${weekStarGiftVO[1].giftId})">
                            <img src="${weekStarGiftVO[1].picUrl}" alt="gift">
                            <p>${weekStarGiftVO[1].giftName}</p>
                            <p class="font-purple">${weekStarGiftVO[1].goldPrice}金币</p>
                        </div>
                        <div class="gift-item" style="margin-right: .1rem;" onclick="getStarList(${weekStarGiftVO[2].giftId})">
                            <img src="${weekStarGiftVO[2].picUrl}" alt="gift">
                            <p>${weekStarGiftVO[2].giftName}</p>
                            <p class="font-purple">${weekStarGiftVO[2].goldPrice}金币</p>
                        </div>`;
                    $('.gifts').append(html);
                    giftArray = [weekStarGiftVO[0].giftId, weekStarGiftVO[1].giftId, weekStarGiftVO[2].giftId];
                    globalGiftId = weekStarGiftVO[0].giftId;
                    showList(weekStarGiftVO[0].giftId, listType);
                } else {
                    $('.pop-up, .dialog').css('display', 'block');
                }
            } else {
                $('.rank-content').css('display', 'none');
                $('.notice').css('display', 'unset');
            }
        }
    });
}

// 点击礼物获取相应的周星榜
function getStarList(giftId) {
    globalGiftId = giftId;
    showList(giftId, listType);
}

// 获取本周周星列表
function showList(giftId, type) {
    $.ajax({
        url: allUrl() + '/week/star/getStartList',
        type: 'GET',
        data: {
            ticket: info.ticket,
            uid: info.uid,
            giftId: giftId,
            type: type
        },
        success: function success(res) {
            if (res.code == 200) {
                $('.top').html("");
                $('.list').html("");
                $('.mine').html("");
                const weekStarList = res.data.weekStartVO;
                const myRank = res.data.myWeekStartVO;
                if (weekStarList == "") {
                    $('.rank-content').css('display', 'none');
                    $('.notice').css('display', 'unset');
                } else {
                    console.log(weekStarList)
                    console.log(weekStarList.length)
                    renderTopList(weekStarList);
                    if (weekStarList.length > 3) {
                        renderList(weekStarList);
                    }
                    if (weekStarList.length > 9) {
                        $('.more').css('display', 'block');
                    }
                    setMyInfo(myRank);
                }
            } else {
                $('.rank-content').css('display', 'none');
                $('.notice').css('display', 'unset');
            }
        }
    });
}

// 渲染周星榜前三名
function renderTopList(weekStarList) {
    let html = "";
    if (weekStarList.length >= 3) {
        html += `
            <div class="one">
                <div class="img-box" onclick="goToInfo(${weekStarList[0].uid})">
                    <img src="${weekStarList[0].avatar}" alt="head">
                </div>
                <p>${weekStarList[0].nick}</p>
                <p>ID:${weekStarList[0].erbanNo}</p>
                <p>
                    <img src="./img/heart.png" alt="heart">
                    <span>${formatTotal(weekStarList[0].total)}</span>
                </p>
            </div>
            <div class="two">
                <div class="img-box" onclick="goToInfo(${weekStarList[1].uid})">
                    <img src="${weekStarList[1].avatar}" alt="head">
                </div>
                <p>${weekStarList[1].nick}</p>
                <p>ID:${weekStarList[1].erbanNo}</p>
                <p>
                    <img src="./img/heart.png" alt="heart">
                    <span>${formatTotal(weekStarList[1].total)}</span>
                </p>
            </div>
            <div class="three">
                <div class="img-box" onclick="goToInfo(${weekStarList[2].uid})">
                    <img src="${weekStarList[2].avatar}" alt="head">
                </div>
                <p>${weekStarList[2].nick}</p>
                <p>ID:${weekStarList[2].erbanNo}</p>
                <p>
                    <img src="./img/heart.png" alt="heart">
                    <span>${formatTotal(weekStarList[2].total)}</span>
                </p>
            </div>`;
    } else if (weekStarList.length >= 2) {
        html += `
            <div class="one">
                <div class="img-box" onclick="goToInfo(${weekStarList[0].uid})">
                    <img src="${weekStarList[0].avatar}" alt="head">
                </div>
                <p>${weekStarList[0].nick}</p>
                <p>ID:${weekStarList[0].erbanNo}</p>
                <p>
                    <img src="./img/heart.png" alt="heart">
                    <span>${formatTotal(weekStarList[0].total)}</span>
                </p>
            </div>
            <div class="two">
                <div class="img-box" onclick="goToInfo(${weekStarList[1].uid})">
                    <img src="${weekStarList[1].avatar}" alt="head">
                </div>
                <p>${weekStarList[1].nick}</p>
                <p>ID:${weekStarList[1].erbanNo}</p>
                <p>
                    <img src="./img/heart.png" alt="heart">
                    <span>${formatTotal(weekStarList[1].total)}</span>
                </p>
            </div>`;
    } else if (weekStarList.length >= 1) {
        html += `
            <div class="two">
                <div class="img-box" onclick="goToInfo(${weekStarList[0].uid})">
                    <img src="${weekStarList[0].avatar}" alt="head">
                </div>
                <p>${weekStarList[0].nick}</p>
                <p>ID:${weekStarList[0].erbanNo}</p>
                <p>
                    <img src="./img/heart.png" alt="heart">
                    <span>${formatTotal(weekStarList[0].total)}</span>
                </p>
            </div>`;
    }
    $('.top').append(html);
}

// 渲染本周排行列表
function renderList(data) {
    let htmlSnippets = "";
    for (let i = 3; i < data.length; i++) {
        htmlSnippets += `<div class="list-item">
            <div class="rank-number">
                <span>${formatRank(data[i].rank)}</span>
            </div>
            <div class="header" onclick="goToInfo(${data[i].uid})">
                <img src="${data[i].avatar}" alt="head">
            </div>
            <div class="info">
                <p>${data[i].nick}</p>
                <p>ID:${data[i].erbanNo}</p>
            </div>
            <div class="score">
                <img src="./img/heart.png" alt="heart">
                <span>${formatTotal(data[i].total)}</span>
            </div>
        </div>`;
    }
    $('.list').append(htmlSnippets);
}

// 设置我的排名信息
function setMyInfo(data) {
    const snippets = `<div class="rank-number">
            <span>${formatRank(data.rank)}</span>
        </div>
        <div class="header" onclick="goToInfo(${data.uid})">
            <img src="${data.avatar}" alt="head">
        </div>
        <div class="info">
            <p>${data.nick}</p>
            <p>ID:${data.erbanNo}</p>
        </div>
        <div class="score">
            <img src="./img/heart.png" alt="heart">
            <span>${formatTotal(data.total)}</span>
        </div>`;
    $('.mine').append(snippets);
}

// 获取历史周星
function getHistoryList() {
    $.ajax({
        url: allUrl() + '/week/star/getHistoryWeekStartRank',
        type: 'GET',
        data: {
            ticket: info.ticket,
            uid: info.uid,
            pageNum: pageNum,
            pageSize: 2
        },
        success: function success(res) {
            if (res.code == 200) {
                renderHistory(res.data);
            } else {
                $('.dialog').find('.word').text('暂无数据~');
                $('.dialog>p:last-child').css('display', 'block');
                $('.pop-up, .dialog').css('display', 'block');
            }
        }
    });
}

// 渲染历史周星
function renderHistory(data) {
    const html = `
        <div class="history-number number-1">
            <span>第${data[0].weekNum}期</span>
        </div>
        <div class="history-list list-1">
            <div class="history-item">
                <img src="${data[0].historyWeekRichList[0].avatar}" alt="head">
                <p>${data[0].historyWeekRichList[0].nick}</p>
            </div>
            <div class="history-item">
                <img src="${data[0].historyWeekRichList[1].avatar}" alt="head">
                <p>${data[0].historyWeekRichList[1].nick}</p>
            </div>
            <div class="history-item">
                <img src="${data[0].historyWeekRichList[2].avatar}" alt="head">
                <p>${data[0].historyWeekRichList[2].nick}</p>
            </div>
        </div>
        <div class="history-list list-2">
            <div class="history-item">
                <img src="${data[0].historyWeekCharmList[0].avatar}" alt="head">
                <p>${data[0].historyWeekCharmList[0].nick}</p>
            </div>
            <div class="history-item">
                <img src="${data[0].historyWeekCharmList[1].avatar}" alt="head">
                <p>${data[0].historyWeekCharmList[1].nick}</p>
            </div>
            <div class="history-item">
                <img src="${data[0].historyWeekCharmList[2].avatar}" alt="head">
                <p>${data[0].historyWeekCharmList[2].nick}</p>
            </div>
        </div>`;
    if (data[1]) {
        html += `<div class="history-number number-2">
            <span>${data[1].weekNum}</span>
        </div>
        <div class="history-list list-3">
            <div class="history-item">
                <img src="${data[1].historyWeekRichList[0].avatar}" alt="head">
                <p>${data[1].historyWeekRichList[0].nick}</p>
            </div>
            <div class="history-item">
                <img src="${data[1].historyWeekRichList[1].avatar}" alt="head">
                <p>${data[1].historyWeekRichList[1].nick}</p>
            </div>
            <div class="history-item">
                <img src="${data[1].historyWeekRichList[2].avatar}" alt="head">
                <p>${data[1].historyWeekRichList[2].nick}</p>
            </div>
        </div>
        <div class="history-list list-4">
            <div class="history-item">
                <img src="${data[1].historyWeekCharmList[0].avatar}" alt="head">
                <p>${data[1].historyWeekCharmList[0].nick}</p>
            </div>
            <div class="history-item">
                <img src="${data[1].historyWeekCharmList[1].avatar}" alt="head">
                <p>${data[1].historyWeekCharmList[1].nick}</p>
            </div>
            <div class="history-item">
                <img src="${data[1].historyWeekCharmList[2].avatar}" alt="head">
                <p>${data[1].historyWeekCharmList[2].nick}</p>
            </div>
        </div>`;
    }
    html += `<div class="pager">
        <div class="btn-cover" id="prev">
            <a href="javascript:;">上一页</a>
        </div>
        <div class="btn-cover" id="next">
            <a href="javascript:;">下一页</a>
        </div>
    </div>`;
    $('.history-content').append(html);
}

$('#prev').on('touchend', function() {
    pageNum++;
    $('#prev').css('display', 'block');
    $('#next').css('display', 'none');
    getHistoryList();
});

$('#next').on('touchend', function() {
    pageNum--;
    $('#prev').css('display', 'none');
    $('#next').css('display', 'block');
    getHistoryList();
});
