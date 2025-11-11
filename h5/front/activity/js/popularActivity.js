var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function (obj) {
    return typeof obj;
} : function (obj) {
    return obj && typeof Symbol === "function" && obj.varructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj;
};

const browser = checkVersion();
const info = {};

$("img.lazy").lazyload();

// 打开活动规则弹框
$('.activity-rule').click(function () {
    $("#pop-up-rule").stop(true, true).animate({
        height: "100%"
    }, 1000);
});

// 关闭活动规则弹框
$('.close-rule').click(function () {
    $("#pop-up-rule").stop(true, true).animate({
        height: "0"
    }, 1000);
});

// 打开上周榜单弹框
$('.last-week-list').click(function () {
    getLastWeekRank();
    $("#last-week-rank").stop(true, true).animate({
        height: "100%"
    }, 500);
});

// 关闭上周榜单弹框
$('.close-week').click(function () {
    $("#last-week-rank").stop(true, true).animate({
        height: "0"
    }, 500);
});

// 打开TA的星推官弹框
function starPush(id) {
    getStarPush(id);
    $("#pop-up-star").stop(true, true).animate({
        height: "100%"
    }, 500);
}

// 关闭TA的星推官弹框
$('.close-star').click(function () {
    $("#pop-up-star").stop(true, true).animate({
        height: "0"
    }, 500);
});

$('.popular-activity .rank .item .info>img').click(function() {
    openRoom();
});

const swiperH = new Swiper('.swiper-container-h');

$('.list img').click(function (e) {
    const className = $(e.target).attr('class');
    const index = $(this).index();
    if (className.indexOf('girl') >= 0) {
        $('.girls-list').attr('src', './images/girl_list_selected.png');
        $('.boys-list').attr('src', './images/boy_list.png');
    } else if (className.indexOf('boy') >= 0) {
        $('.girls-list').attr('src', './images/girl_list.png');
        $('.boys-list').attr('src', './images/boy_list_selected.png');
    }
    swiperH.slideTo(index, 1000, false);
});

swiperH.on('slideChange', function () {
    if (this.activeIndex == 0) {
        $('.girls-list').attr('src', './images/girl_list_selected.png');
        $('.boys-list').attr('src', './images/boy_list.png');
    } else if (this.activeIndex == 1) {
        $('.girls-list').attr('src', './images/girl_list.png');
        $('.boys-list').attr('src', './images/boy_list_selected.png');
    }
});

function getMessage(key, value) {
    info[key] = value;
    console.log(key, value, 'getMessage');
}

setTimeout(function () {
    if (browser.ios && window.webkit) {
        window.webkit.messageHandlers.getUid.postMessage(null);
        window.webkit.messageHandlers.getTicket.postMessage(null);
    } else if (browser.android) {
        if (androidJsObj && (typeof androidJsObj === 'undefined' ? 'undefined' : _typeof(androidJsObj)) ===
            'object') {
            info.uid = parseInt(window.androidJsObj.getUid());
            info.ticket = window.androidJsObj.getTicket();
            console.log(["info", info]);
        }
    }
    getList();
    getMyRank();
}, 10);

// 获取本周男神女神榜方法
function getList() {
    const type = 2;
    const url = '/popularity/list/getTop20List';
    const header = '';
    const data = {};
    dispatchRequest(url, type, header, data);
}

// 获取我的排名方法
function getMyRank() {
    const type = 2;
    const url = '/popularity/list/getMyRank';
    const header = '';
    const data = {
        uid: info.uid, // 用户ID
    };
    dispatchRequest(url, type, header, data);
}

// 获取某个用户的星推官
function getStarPush(id) {
    const type = 2;
    const url = '/popularity/list/getUserRecommend';
    const header = '';
    const data = {
        userId: id, // 用户ID
    };
    dispatchRequest(url, type, header, data);
}

// 获取上周排名方法
function getLastWeekRank() {
    const type = 2;
    const url = '/popularity/list/getLastWeekList';
    const header = '';
    const data = {};
    dispatchRequest(url, type, header, data);
}

function openRoom() {
    // const type = 2;
    // const url = '/openRoom';
    // const header = '';
    // const data = {
    //     uid: info.uid
    // };
    // dispatchRequest(url, type, header, data);
}

// 调用原生方法发送请求
function dispatchRequest(requestURL, method, requestHeader, params) {
    const type = method;
    const url = requestURL;
    const header = requestHeader;
    const data = params;
    if (browser.ios) {
        const obj = {
            requestMethod: type,
            urlController: url,
            headerMapString: header,
            paramMapString: data,
        };
        window.webkit.messageHandlers.httpRequest.postMessage(JSON.stringify(obj));
        // window.webkit.messageHandlers.httpRequest.postMessage(type,url,header,JSON.stringify(data));JSON.stringify(obj)
    } else if (browser.android) {
        if (androidJsObj && (typeof androidJsObj === 'undefined' ? 'undefined' : _typeof(androidJsObj)) === 'object') {
            window.androidJsObj.httpRequest(type, url, header, JSON.stringify(data));
        }
    }
}

// 回调响应事件
var onHttpResponse = function (e, item) {
    console.log(e, '接口返回', item);
    let body = null;
    if (browser.ios) {
        body = item;
        if (body.urlController == "/popularity/list/getTop20List") {
            const responseData = JSON.parse(body.bodyString);
            console.log(responseData, 'bodyString');
            if (!body.isRequestError) {
                console.log(responseData.girls, 'girls')
                $('.girl-rank').html('');
                $('.boy-rank').html('');
                const girls = generateSnippets(responseData.girls, 'girls');
                const boys = generateSnippets(responseData.boys, 'boys');
                $('.girl-rank').append(girls);
                $('.boy-rank').append(boys);
            } else {
                console.log(responseData, responseData.message, 'bodyString.message');
                $('.toast').text(responseData.message).show();
                setTimeout(function () {
                    $('.toast').hide();
                }, 1200);
            }
        } else if (body.urlController == "/popularity/list/getMyRank") {
            const responseData = JSON.parse(body.bodyString);
            console.log(responseData, 'bodyString');
            if (!body.isRequestError) {
                $('#avatar').attr('src', responseData.avatar);
                $('#nick').text(responseData.nick);
                $('#rank').text(responseData.rank > 99 ? '99+' : responseData.rank == 0 ? "暂无排名" : responseData.rank);
                $('#receipt').text(responseData.receiptVotes >= 1000 ? `X${(number / 1000).toFixed(1)}k` : `X${responseData.receiptVotes}`);
                $('#send').text(responseData.sendVotes >= 1000 ? `X${(number / 1000).toFixed(1)}k` : `X${responseData.sendVotes}`);
            } else {
                $('.toast').text(responseData.message).show();
                setTimeout(function () {
                    $('.toast').hide();
                }, 1200);
            }
        } else if (body.urlController == "/popularity/list/getUserRecommend") {
            const response = body.bodyString;
            console.log(response, 'bodyString');
            if (!body.isRequestError) {
                const responseData = JSON.parse(body.bodyString);
                if (responseData) {
                    for (let index = 0; index < responseData.length; index++) {
                        const element = responseData[index];
                        if (index == 0) {
                            $('.first .head').attr('src', element.avatar);
                            $('.first #name').text(element.nick);
                            $('.first #vote').text(`已投${element.sendVotes}票`);
                        } else if (index == 1) {
                            $('.second .head').attr('src', element.avatar);
                            $('.second #name').text(element.nick);
                            $('.second #vote').text(`已投${element.sendVotes}票`);
                        } else if (index == 2) {
                            $('.third .head').attr('src', element.avatar);
                            $('.third #name').text(element.nick);
                            $('.third #vote').text(`已投${element.sendVotes}票`);
                        }
                    }
                } else {
                    $('.toast').text(responseData.message).show();
                    setTimeout(function () {
                        $('.toast').hide();
                    }, 1200);
                }
            } else {
                $('.toast').text('获取星推官数据失败!').show();
                setTimeout(function () {
                    $('.toast').hide();
                }, 1200);
            }
        } else if (body.urlController == "/popularity/list/getLastWeekList") {
            const responseData = JSON.parse(body.bodyString);
            console.log(responseData, 'bodyString');
            if (!body.isRequestError) {
                $('.rank-girl .lists').html('');
                $('.rank-boy .lists').html('');
                const girls = generateWeekRankSnippets(responseData.girls, 'girls');
                const boys = generateWeekRankSnippets(responseData.boys, 'boys');
                $('.rank-girl .lists').append(girls);
                $('.rank-boy .lists').append(boys);
            } else {
                $('.toast').text(responseData.message).show();
                setTimeout(function () {
                    $('.toast').hide();
                }, 1200);
            }
        }
    } else {
        body = JSON.parse(JSON.stringify(e));
        if (body.urlController == "/popularity/list/getTop20List") {
            const responseData = JSON.parse(body.bodyString);
            console.log(responseData, 'bodyString');
            if (!body.isRequestError) {
                if (responseData.code == 200) {
                    $('.girl-rank').html('');
                    $('.boy-rank').html('');
                    const girls = generateSnippets(responseData.data.girls, 'girls');
                    const boys = generateSnippets(responseData.data.boys, 'boys');
                    console.log(responseData.data.girls)
                    console.log(responseData.data.boys)
                    console.log(girls)
                    console.log(boys)
                    $('.girl-rank').append(girls);
                    $('.boy-rank').append(boys);
                } else {
                    console.log(responseData, responseData.message, 'bodyString.message');
                    $('.toast').text(responseData.message).show();
                    setTimeout(function () {
                        $('.toast').hide();
                    }, 1200);
                }
            } else {
                $('.toast').text('获取男神女神榜数据失败!').show();
                setTimeout(function () {
                    $('.toast').hide();
                }, 1200);
            }
        } else if (body.urlController == "/popularity/list/getMyRank") {
            if (!body.isRequestError) {
                const responseData = JSON.parse(body.bodyString);
                console.log(responseData, 'bodyString');
                if (responseData.code == 200) {
                    $('#avatar').attr('src', responseData.data.avatar);
                    $('#nick').text(responseData.data.nick);
                    $('#rank').text(responseData.data.rank > 99 ? '99+' : responseData.data.rank == 0 ? "暂无排名" : responseData.data.rank);
                    $('#receipt').text(responseData.data.receiptVotes >= 1000 ? `X${(number / 1000).toFixed(1)}k` : `X${responseData.data.receiptVotes}`);
                    $('#send').text(responseData.data.sendVotes >= 1000 ? `X${(number / 1000).toFixed(1)}k` : `X${responseData.data.sendVotes}`);
                } else {
                    $('.toast').text(responseData.message).show();
                    setTimeout(function () {
                        $('.toast').hide();
                    }, 1200);
                }
            } else {
                $('.toast').text('获取我的排行榜数据失败!').show();
                setTimeout(function () {
                    $('.toast').hide();
                }, 1200);
            }
        } else if (body.urlController == "/popularity/list/getUserRecommend") {
            if (!body.isRequestError) {
                const responseData = JSON.parse(body.bodyString);
                console.log(body, 'getUserRecommend');
                if (responseData.code == 200) {
                    for (let index = 0; index < responseData.data.length; index++) {
                        const element = responseData.data[index];
                        if (index == 0) {
                            $('.first .head').attr('src', element.avatar);
                            $('.first #name').text(element.nick);
                            $('.first #vote').text(`已投${element.sendVotes}票`);
                        } else if (index == 1) {
                            $('.second .head').attr('src', element.avatar);
                            $('.second #name').text(element.nick);
                            $('.second #vote').text(`已投${element.sendVotes}票`);
                        } else if (index == 2) {
                            $('.third .head').attr('src', element.avatar);
                            $('.third #name').text(element.nick);
                            $('.third #vote').text(`已投${element.sendVotes}票`);
                        }
                    }
                } else {
                    $('.toast').text(responseData.message).show();
                    setTimeout(function () {
                        $('.toast').hide();
                    }, 1200);
                }
            } else {
                $('.toast').text('获取星推官数据失败!').show();
                setTimeout(function () {
                    $('.toast').hide();
                }, 1200);
            }
        } else if (body.urlController == "/popularity/list/getLastWeekList") {
            if (!body.isRequestError) {
                const responseData = JSON.parse(body.bodyString);
                if (responseData.code == 200) {
                    $('.rank-girl .lists').html('');
                    $('.rank-boy .lists').html('');
                    const girls = generateWeekRankSnippets(responseData.data.girls, 'girls');
                    const boys = generateWeekRankSnippets(responseData.data.boys, 'boys');
                    $('.rank-girl .lists').append(girls);
                    $('.rank-boy .lists').append(boys);
                } else {
                    $('.toast').text(responseData.message).show();
                    setTimeout(function () {
                        $('.toast').hide();
                    }, 1200);
                }
            } else {
                $('.toast').text('获取上周榜单数据失败!').show();
                setTimeout(function () {
                    $('.toast').hide();
                }, 1200);
            }
        }
    }
};

// 榜单下拉滚动加载
// $('.view').scroll(function () {
//     var $this = $(this);
//     totalheight = parseFloat($this.height()) + parseFloat($this.scrollTop());
//     if ($('.swiper-container').height().toFixed(0) == totalheight) {
//         generateSnippets($this)
//     }
// });

// 生成HTML片段
function generateSnippets(item, sex) {
    let snippets = '';
    item.forEach((element, index) => {
        snippets += `<div class="item">
                        <div class="title">
                            ${getRank(index + 1)}
                        </div>
                        <div class="info">
                            <img src="${element.avatar}" alt="head" height="40" />
                            <div class="details">
                                <p>
                                    ${sex == 'girls' ? `<img src="./images/girl.png" alt="girl" width="15" />` : `<img src="./images/boy.png" alt="boy" width="15" />`}
                                    <span>${element.nick}</span>
                                </p>
                                <a href="javascript:;" class="star-push" onclick="starPush(${element.receiptId})">TA的星推官</a>
                            </div>
                        </div>
                        <div class="count">
                            <img src="./images/popular_ticket.png" alt="ticket" width="50" />
                            <span>X${element.receiptVotes >= 1000 ? (element.receiptVotes / 1000).toFixed(1) + 'k' : element.receiptVotes}</span>
                        </div>
                    </div>`;
    });
    return snippets;
}

function generateWeekRankSnippets(item, sex) {
    let snippets = '';
    item.forEach((element, index) => {
        snippets += `<div class="rank-item one">
                        ${getRank(index + 1)}
                        <img src="${element.avatar}" alt="head" class="head" height="40" />
                        <div class="rank-info">
                            <p>
                                <span class="info">
                                    ${sex == 'girls' ? '<img src="./images/girl.png" alt="girl" height="15">' : '<img src="./images/boy.png" alt="boy" height="15">'}
                                    <span>${element.nick}</span>
                                </span>
                                <span class="votes">
                                    <img src="./images/popular_ticket.png" alt="popular_ticket" style="height: 4vh;">
                                    <span>${element.receiptVotes >= 1000 ? (element.receiptVotes / 1000).toFixed(1) + 'k' : element.receiptVotes}票</span>
                                </span>
                            </p>
                            <p>
                                <img src="./images/chief_star.png" alt="chief_star" width="60">
                                <span>${element.sendNick}</span>
                                <span>已投${element.sendVotes >= 1000 ? (element.receiptVotes / 1000).toFixed(1) + 'k' : element.sendVotes}票</span>
                            </p>
                        </div>
                    </div>`;
    });
    return snippets;
}

// 获取排行榜的图标
function getRank(rank) {
    if (rank == 1) {
        return `<img src="./images/first.png" alt="first" width="45" height="45" />`;
    } else if (rank == 2) {
        return `<img src="./images/second.png" alt="second" width="45" height="45" />`;
    } else if (rank == 3) {
        return `<img src="./images/third.png" alt="third" width="45" height="45" />`;
    } else {
        return `<div>${rank}</div>`;
    }
}